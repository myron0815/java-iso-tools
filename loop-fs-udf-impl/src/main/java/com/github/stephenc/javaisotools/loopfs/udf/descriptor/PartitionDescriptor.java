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

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.PartitionHeader;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Partition Descriptor (ECMA-167 3/10.5)
 */
public class PartitionDescriptor extends UDFDescriptor {
//	struct PartitionDescriptor { /* ECMA 167 3/10.5 */
//		struct tag DescriptorTag;
//		Uint32 VolumeDescriptorSequenceNumber;
//		Uint16 PartitionFlags;
//		Uint16 PartitionNumber;
//		struct EntityID PartitionContents;
//		byte PartitionContentsUse[128];
//		Uint32 AccessType;
//		Uint32 PartitionStartingLocation;
//		Uint32 PartitionLength;
//		struct EntityID ImplementationIdentifier;
//		byte ImplementationUse[128];
//		byte Reserved[156];
//	}

	public Long volumeDescriptorSeq;
	public Integer flags;
	public Integer partitionNumber;
	public RegId contents;
	public PartitionHeader contentsUse;
	public Long accessType;
	public Long startingLocation;
	public Long length;
	public RegId implIdentifier;
	public byte[] implUse;

	// minimum length of a partition descriptor (field "Reserved" included)
	public static final int LENGTH = 512;

	public PartitionDescriptor() {
		super();
	}

	public PartitionDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_PARTITION;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Partition descriptor too short");
		}
		this.deserializeTag(bytes);

		this.volumeDescriptorSeq = getUInt32(bytes);
		this.flags = getUInt16(bytes);
		this.partitionNumber = getUInt16(bytes);
		this.contents = new RegId(getBytes(bytes, RegId.LENGTH));
		this.contentsUse = new PartitionHeader(getBytes(bytes, PartitionHeader.LENGTH));
		this.accessType = getUInt32(bytes);
		this.startingLocation = getUInt32(bytes);
		this.length = getUInt32(bytes);
		this.implIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));
		this.implUse = getBytes(bytes, 128);
		
		currentPos = LENGTH; // set to end
	}
}
