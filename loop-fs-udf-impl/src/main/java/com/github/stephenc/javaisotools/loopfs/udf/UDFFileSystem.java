/*
 * Copyright (c) 2019. Mr.Indescribable (https://github.com/Mr-indescribable).
 * Copyright (c) 2010. Stephen Connolly.
 * Copyright (c) 2006-2007. loopy project (http://loopy.sourceforge.net).
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.github.stephenc.javaisotools.loopfs.udf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.github.stephenc.javaisotools.loopfs.spi.AbstractBlockFileSystem;
import com.github.stephenc.javaisotools.loopfs.spi.SeekableInput;
import com.github.stephenc.javaisotools.loopfs.spi.SeekableInputFile;
import com.github.stephenc.javaisotools.loopfs.spi.VolumeDescriptorSet;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.AnchorDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.UnsupportedStandard;


public class UDFFileSystem extends AbstractBlockFileSystem<UDFFileEntry>
{
	private AnchorDescriptor anchor;

	public UDFFileSystem(
		File file,
		boolean readOnly
	) throws IOException {
		this(
			new SeekableInputFile(file),
			readOnly,
			Constants.DEFAULT_BLOCK_SIZE
		);
	}

	/**
	 * Constructor.
	 *
	 * In AbstractBlockFileSystem.loadVolumeDescriptors method, descriptors
	 * will be read sequentially from the first block after reserved blocks.
	 * However, volume descriptors in UDF shall be read from the block
	 * which the Anchor pointed out. And the Anchor is actually the first block
	 * after those reserved blocks (basically).
	 *
	 * For this, AbstractBlockFileSystem.reservedBlocks must be
	 * modifiable. Otherwise, we will need to modify the Constructor of
	 * AbstractBlockFileSystem class.
	 */
	public UDFFileSystem(
			SeekableInput file,
			boolean readOnly,
			Integer sectorSize
	) throws IOException {
		super(
			file,
			readOnly,
			sectorSize,
			Constants.RESERVED_SECTORS
		);

		try {
			this.verifyStandard();
		} catch (UnsupportedStandard ex) {
			throw new IOException("Unsupported standard");
		}
		try {
			this.loadAnchor();
		} catch (InvalidDescriptor ex) {
			throw new IOException("Invalid Anchor Volume Pointer Descriptor");
		}
		this.setReservedBlocks(
			this.anchor.mainVolumeExtent.location.intValue()
		);
	}

	public InputStream getInputStream(UDFFileEntry entry) {
		ensureOpen();
		return new UDFEntryInputStream(entry, this);
	}

	public int readBytes(
			long startPos,
			byte[] buffer,
			int offset,
			int len
	) throws IOException {
		return readData(startPos, buffer, offset, len);
	}

	public int readFileContent(
		UDFFileEntry entry,
		long entryOffset,
		byte[] buffer,
		int bufferOffset,
		int len
	) throws IOException {
		if (entry.isDirectory()) {
			throw new IOException("Entry is a directory");
		}

		int bs = Constants.DEFAULT_BLOCK_SIZE;

		int read = 0;
		long offset = 0L;
		long processed = 0L; // bytes has been processed
		long endPosition = len + entryOffset;

		// Here we merge fragments of the file into one piece,
		// and compare entryOffset argument with each fragment.
		//
		//  offset                entryOffset       len + entryOffset
		//    |                        |                   |
		//    +-----------------+------+----------+--------+------+
		//    |     frag 0      |     frag 1      |    frag 2     |
		//
		//
		//                   offset  entryOffset    len + entryOffset
		//                      |      |                   |
		//    +-----------------+------+----------+--------+------+
		//    |     frag 0      |     frag 1      |    frag 2     |
		//
		//
		//                         entryOffset       len + entryOffset
		//                             |                   |
		//    +-----------------+------+----------+--------+------+
		//    |     frag 0      |     frag 1      |    frag 2     |
		//                                     offset
		//
		//
		//                         entryOffset       len + entryOffset
		//                             |                   |
		//    +-----------------+------+----------+--------+------+
		//    |     frag 0      |     frag 1      |    frag 2     |
		//                                                      offset
		//
		//
		//    Pseudo Code of reading:
		//        byte[] result;
		//
		//        offset = 0
		//        offset += frag0.length
		//
		//        offset += frag1.length
		//        of = entryOffset - frag0.length
		//        ln = offset - entryOffset
		//        result += read(of, ln)
		//
		//        offset += frag2.length
		//        of = 0
		//        ln = len + entryOffset - (frag0.length + frag1.length)
		//        result += read(of, ln)
		for (ExtentAD ead : entry.getADs()) {
      if (ead.length == 0) {
        continue;
      }
			long fragLen = ead.length;
			long fragPos = this.getPDStartPos() + ead.location;
			offset += fragLen;

			if (offset <= entryOffset) {
				processed += fragLen;
				continue;
			}

			long off;
			long ln;

			if (processed < entryOffset) {
				off = entryOffset - processed;
			} else {
				off = 0L;
			}

			if (offset < endPosition) {
        ln = offset - entryOffset;
			} else {
				ln = endPosition - Math.max(processed, entryOffset);
			}

			if (ln == 0L) {
				break;  // End Position reached
			}

			long startPos = bs * fragPos + off;
			int extentRead = readData(startPos, buffer, bufferOffset, (int)ln);
			processed += fragLen;

			if (extentRead < ln){
				read += extentRead;
				break;
			} else {
				read += ln;
				bufferOffset += ln;
			}

			if (read == len) {
				break;
			} else if (read > len) {
				// is this possible?
				throw new IOException(
					"byte array has been read is longer than expected"
				);
			}
		}

		return read;
	}

	protected Iterator<UDFFileEntry> iterator(UDFFileEntry root) {
		UDFVolumeDescriptorSet ds = this.getUDFDescriptorSet();
		UDFFileEntry rootEntry = (UDFFileEntry) ds.getRootEntry();
		return new UDFEntryIterator(this, rootEntry);
	}

	protected VolumeDescriptorSet<UDFFileEntry> createVolumeDescriptorSet() {
		return new UDFVolumeDescriptorSet(this);
	}

	/**
	 * Load the anchor descriptor.
	 *
	 * TODO:
	 *     Read Anchor from block N and N - 256 (OSTA-UDF 2.2.3)
	 */
	private void loadAnchor() throws IOException, InvalidDescriptor {
		byte[] buffer = new byte[Constants.DEFAULT_BLOCK_SIZE];
		readBlock(Constants.ANCHOR_SECTOR_NUMBER, buffer);
		this.anchor = new AnchorDescriptor(buffer);
	}

	public AnchorDescriptor getAnchor() {
		return this.anchor;
	}

	public UDFVolumeDescriptorSet getUDFDescriptorSet() {
		return (UDFVolumeDescriptorSet) this.getVolumeDescriptorSet();
	}

	/**
	 * Gets the starting location of the partition
	 */
	public long getPDStartPos() {
		UDFVolumeDescriptorSet ds = this.getUDFDescriptorSet();
		return ds.getPDStartPos();
	}

	/**
	 * Check if the standard in volume structure descriptors has been supported.
	 *
	 * TODO:
	 *     Implement it.
	 *
	 * @throws UnsupportedStandard
	 */
	private void verifyStandard() throws UnsupportedStandard {

	}
}
