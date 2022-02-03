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

package com.github.stephenc.javaisotools.loopfs.udf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.stephenc.javaisotools.loopfs.api.FileEntry;
import com.github.stephenc.javaisotools.loopfs.spi.VolumeDescriptorSet;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.ExtendedFileEntryDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileEntryDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileSetDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.ImplementationUseDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.LogicalVolumeDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.LogicalVolumeIntegrityDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.PartitionDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.PrimaryVolumeDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.TerminatingDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.UnallocatedSpaceDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DescriptorTag;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

public class UDFVolumeDescriptorSet implements VolumeDescriptorSet {
	private static final Log log = LogFactory.getLog(UDFVolumeDescriptorSet.class);

	private UDFFileSystem fs;

	public PrimaryVolumeDescriptor pvd;
	public LogicalVolumeDescriptor lvd;
	public LogicalVolumeIntegrityDescriptor lvid;
	public PartitionDescriptor pd;
	public ImplementationUseDescriptor impl;
	public UnallocatedSpaceDescriptor un;
	public FileSetDescriptor rootFSD;
	public FileEntryDescriptor rootEntry;

	private long fsdLocation = 0L;
	private int invalidCnt = 0;

	public UDFVolumeDescriptorSet(UDFFileSystem fs) {
		this.fs = fs;
	}

	public boolean deserialize(byte[] bytes) throws IOException {

		Integer type = UDFUtil.getUInt16(bytes, 0);
		boolean terminated = false;

		try {
			switch (type) {
			case Constants.D_TYPE_PRIMARY_VOLUME:
				log.debug("Found primary volume descriptor.");
				this.pvd = new PrimaryVolumeDescriptor(bytes);
				break;
			case Constants.D_TYPE_ANCHOR_POINTER:
				log.debug("Found anchor pointer.");
				break;
			case Constants.D_TYPE_VOLUME_DESCRIPTOR_POINTER:
				log.debug("Found volume descriptorpointer.");
				break;
			case Constants.D_TYPE_IMPL_USE:
				log.debug("Found implementation use descriptor.");
				impl = new ImplementationUseDescriptor(bytes);
				break;
			case Constants.D_TYPE_PARTITION:
				log.debug("Found partition descriptor.");
				this.pd = new PartitionDescriptor(bytes);
				break;
			case Constants.D_TYPE_LOGICAL_VOLUME:
				log.debug("Found logical volume descriptor");
				this.lvd = new LogicalVolumeDescriptor(bytes);
				log.debug("Disc identifier: " + lvd.logicalVolumeIdentifier.toString().replace("\0.*$", "").trim());
				break;
			case Constants.D_TYPE_UNALLOCATED_SPACE:
				log.debug("Found unallocated space descriptor.");
				un = new UnallocatedSpaceDescriptor(bytes);
				break;
			case Constants.D_TYPE_TERMINATING:
				log.debug("Found terminating descriptor.");
				TerminatingDescriptor t = new TerminatingDescriptor(bytes);
				terminated = true;
				tracePartition();
				break;
			case Constants.D_TYPE_LOGICAL_VOLUME_INTEGRITY:
				log.debug("Found logical volume integrity descriptor.");
				lvid = new LogicalVolumeIntegrityDescriptor(bytes);
				break;
			default:
				log.debug("Found unknown volume descriptor with type: " + type);
				invalidCnt++;
				if (invalidCnt > 1000) {
					// got an ISO9660 image, which could be opened by UDF
					// anchor block 256 available, and a few PVDs...
					// unfortunately, nothing else, and it hung up w/o throwing an error.
					// so if we have 1000 unknown blocks, better step out...
					terminated = true;
					log.warn("Too many unknown descriptors - giving up!");
				}
			}
		} catch (InvalidDescriptor ex) {
			throw new IOException(ex.getMessage());
		}

		return terminated;
	}

	/**
	 * Read file set descriptors at the beginning of a partition.
	 *
	 * Currently, this method reads the first file set descriptor only.
	 */
	private void tracePartition() throws IOException, InvalidDescriptor {
		byte[] buffer = new byte[Constants.DEFAULT_BLOCK_SIZE];
		DescriptorTag tag;

		// LVD tells us where the LVID is (which usually comes AFTER the terminator desc
		if (lvd.integritySequenceExtent.length > 0) {
			this.fs.readBlock(lvd.integritySequenceExtent.location, buffer);
			lvid = new LogicalVolumeIntegrityDescriptor(buffer);
		}

		// Anchor tells us the 2*16 blocks of the disc structure
		// so we get the PD

		// Multiple File Sets are only allowed on WORM media.
		// The default File Set shall be the one with the highest FileSetNumber.
		// meh - scanning whole media not possible - how to find the correct one?!?

		// pd startpos(abs block) + pd length (block) is our partition (eg 9)
		//

		// then the PD tells us where the FSD is:
		fsdLocation = this.pd.startingLocation;
		this.fs.readBlock(fsdLocation, buffer);
		int type = UDFUtil.getUInt16(buffer, 0);

		// we expect a FSD, but if it is a FED, the extend tells us the FSD - don't
		// ask...
		if (type == Constants.D_TYPE_FILE_ENTRY) {
			FileEntryDescriptor fed = new FileEntryDescriptor(buffer);
			if (fed.allocDescriptorLength > 0) {
				int fidFsdLoc = fed.allocDescriptors.get(0).location.intValue();
				fsdLocation = fsdLocation + fidFsdLoc;
				this.fs.readBlock(fsdLocation, buffer);
				type = UDFUtil.getUInt16(buffer, 0);
			}
		}

		// ...but not always! sometimes the LVD tells us the offset of the PD
		if (type != Constants.D_TYPE_FILE_SET) {
			fsdLocation = this.pd.startingLocation + this.lvd.logicalVolumeContentsUse.location.blockNumber;
			this.fs.readBlock(fsdLocation, buffer);
			type = UDFUtil.getUInt16(buffer, 0);
		}

		// ...but not always! sometimes it is right after the anchor!
		if (type != Constants.D_TYPE_FILE_SET) {
			fsdLocation = Constants.ANCHOR_SECTOR_NUMBER + 1;
			this.fs.readBlock(fsdLocation, buffer);
			type = UDFUtil.getUInt16(buffer, 0);
		}

		// ...but not always! sometimes it is another anchor position away!
		if (type != Constants.D_TYPE_FILE_SET) {
			fsdLocation = this.pd.startingLocation + this.fs.getAnchor().mainVolumeExtent.location;
			this.fs.readBlock(fsdLocation, buffer);
			type = UDFUtil.getUInt16(buffer, 0);
		}

		// BruteForce: still not found? check first 300 blocks...
		if (type != Constants.D_TYPE_FILE_SET) {
			List<Integer> blockIds = new ArrayList<Integer>(300);
			fsdLocation = 32;
			while (type != Constants.D_TYPE_FILE_SET && fsdLocation < 300) {
				this.fs.readBlock(++fsdLocation, buffer);
				type = UDFUtil.getUInt16(buffer, 0);
				blockIds.add(type);
			}
			log.debug("IDs of block 32-300: " + blockIds);
		}

		this.rootFSD = new FileSetDescriptor(buffer);
		log.debug("Found FSD at block " + fsdLocation);

		// The FSD tells us where the (relative) root file entry is
		if (this.rootFSD.rootICB.length > 0) {
			this.fs.readBlock(fsdLocation + this.rootFSD.rootICB.location.blockNumber, buffer);
			tag = new DescriptorTag(buffer);
			if (tag.identifier == Constants.D_TYPE_FILE_ENTRY) {
				this.rootEntry = new FileEntryDescriptor(buffer);
			} else if (tag.identifier == Constants.D_TYPE_EXTENDED_FILE_ENTRY) {
				this.rootEntry = new ExtendedFileEntryDescriptor(buffer);
			}
		}
		if (this.rootFSD.sysStreamDirICB.length > 0) {
			this.fs.readBlock(fsdLocation + this.rootFSD.rootICB.location.blockNumber, buffer);
			tag = new DescriptorTag(buffer);
			if (tag.identifier == Constants.D_TYPE_FILE_ENTRY) {
				this.rootEntry = new FileEntryDescriptor(buffer);
			} else if (tag.identifier == Constants.D_TYPE_EXTENDED_FILE_ENTRY) {
				this.rootEntry = new ExtendedFileEntryDescriptor(buffer);
			}
		}
	}

	public FileEntry getRootEntry() {
		return new UDFFileEntry(this.fs, this.rootEntry, null, null, true);
	}

	/**
	 * Gets the partition starting position (PD)
	 */
	public long getPDStartPos() {
		return this.pd.startingLocation;
	}

	/**
	 * Gets the starting position (FSD), where all blocks are relative of
	 */
	public long getFSDloc() {
		return fsdLocation;
	}
}
