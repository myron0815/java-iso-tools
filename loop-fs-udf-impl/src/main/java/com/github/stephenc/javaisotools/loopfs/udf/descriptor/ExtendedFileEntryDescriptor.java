/*
 * Copyright (c) 2022. Myron Boyle (https://github.com/myron0815/)
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

package com.github.stephenc.javaisotools.loopfs.udf.descriptor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.UDFFileSystem;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtendedAttribute;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ICBTag;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LongAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.Timestamp;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The File Entry Descriptor (ECMA-167 4/14.9)
 */
public class ExtendedFileEntryDescriptor extends FileEntryDescriptor {
	public BigInteger objectSize;
	public Timestamp creatiDT;
	public LongAD streamDirICB;

	// minimum length of a file entry descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

	public ExtendedFileEntryDescriptor() {
		super();
	}

	public ExtendedFileEntryDescriptor(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
		this.verifyTagIdentifier();
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_EXTENDED_FILE_ENTRY;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("File entry descriptor too short");
		}
		this.deserializeTag(bytes);

		this.icbTag = new ICBTag(getBytes(bytes, ICBTag.LENGTH));
		this.uid = getUInt32(bytes);
		this.gid = getUInt32(bytes);
		this.permissions = getUInt32(bytes);
		this.fileLinks = getUInt16(bytes);
		this.recordFormat = getUInt8(bytes);
		this.recordDisplayAttrs = getUInt8(bytes);
		this.recordLength = getUInt32(bytes);
		this.infoLength = getUInt64(bytes);
		this.objectSize = getUInt64(bytes);
		this.lbRecorded = getUInt64(bytes);
		this.accessDT = new Timestamp(getBytes(bytes, Timestamp.LENGTH));
		this.modifiDT = new Timestamp(getBytes(bytes, Timestamp.LENGTH));
		this.creatiDT = new Timestamp(getBytes(bytes, Timestamp.LENGTH));
		this.attributeDT = new Timestamp(getBytes(bytes, Timestamp.LENGTH));
		this.checkpoint = getUInt32(bytes);
		getUInt32(bytes); // reserved
		this.extAttrICB = new LongAD(getBytes(bytes, LongAD.LENGTH));
		this.streamDirICB = new LongAD(getBytes(bytes, LongAD.LENGTH));
		this.implIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));
		this.uniqueId = getUInt64(bytes);
		this.extAttrLength = getUInt32(bytes);
		this.allocDescriptorLength = getUInt32(bytes);

		// According to OSTA-UDF 2.3.6 NOTE 1, the file entry descriptor
		// shall not exceed the size of one logical block.
		// So, this.extAttrLength must less than (2048 - BP_EXT_ATTRS) and it's
		// safe to convert it into an Integer, otherwise, it's invalid.
		Integer maxExtAttrLen = Constants.DEFAULT_BLOCK_SIZE - currentPos;
		if (this.extAttrLength > maxExtAttrLen) {
			throw new InvalidDescriptor("Extended attributes too long");
		}
		// TODO: ignore them for now
		// extendedAttributes = new ExtendedAttributeDescriptor(getBytes(bytes, extAttrLength.intValue()));
		extendedAttributes = getBytes(bytes, extAttrLength.intValue());

		// Same as extended attributes (OSTA-UDF 2.3.6 NOTE 1)
		Integer maxAllocLen = Constants.DEFAULT_BLOCK_SIZE - currentPos;
		if (this.allocDescriptorLength > maxAllocLen) {
			throw new InvalidDescriptor("Allocation descriptors too long");
		}
		// According to ECMA-167 4/14.9.22, there is a sequence of ADs
		int len = 0;
		this.allocDescriptors = new ArrayList<ExtentAD>();
		while (len < this.allocDescriptorLength) {
			ExtentAD extend = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));
			len = len + ExtentAD.LENGTH;
			if (extend.length > 0) {
				this.allocDescriptors.add(extend);
			}
		}
	}

	/**
	 * Load FileIdentifierDescriptors (File Identifiers) in current ICB
	 *
	 * @param fs the UDFFileSystem object to read
	 */
	public void loadChildren(UDFFileSystem fs) throws IOException {
		FileIdentifierDescriptor fid;
		int bs = Constants.DEFAULT_BLOCK_SIZE;

		for (ExtentAD ead : this.allocDescriptors) {
			// uint32 meets no long type support again ¯\_(ツ)_/¯
			int bufferLength = ead.length.intValue();
			if (bufferLength == 0) {
				continue;
			}

			byte[] buffer = new byte[bufferLength];

			long absStartPos = fs.getFSDloc() + ead.location;
			int bytesRead = fs.readBytes(absStartPos * bs, buffer, 0, // bufferOffset
					bufferLength);

			if (bytesRead != ead.length) {
				throw new IOException("Failed to read " + ead.length + " bytes at the beginning of sector "
						+ absStartPos + ". Actually read " + bytesRead + " bytes.");
			}

			int offset = 0;
			byte[] nextFragment = buffer;

			// The first FileIdentifierDescriptor belongs to the directory and
			// we should skip the first FileIdentifierDescriptor.
			// (OSTA-UDF 2.3.4 NOTE-1)
			boolean first = true;
			boolean noMoreData = false;

			while (true) {
				try {
					fid = new FileIdentifierDescriptor(nextFragment);
				} catch (InvalidDescriptor ex) {
					throw new IOException(ex.getMessage());
				}

				offset += fid.getConsumption();

				// Consumption of fid has included the padding, so it could
				// exceed the bufferLength.
				if (offset >= bufferLength) {
					noMoreData = true;
				} else {
					nextFragment = UDFUtil.getRemainingBytes(buffer, offset);
				}

				if (first) {
					first = false;
					continue;
				} else {
					this.fids.add(fid);
				}

				// If nextFragment is another file identifier descriptor,
				// then it must starts with 0x0101 and contains 40 bytes at least
				if (!noMoreData && nextFragment.length > 40 && UDFUtil.getUInt16(nextFragment, 0) == 0x0101) {
					// this looks much better than using logical not
					continue;
				} else {
					break;
				}
			}
		}
	}
}
