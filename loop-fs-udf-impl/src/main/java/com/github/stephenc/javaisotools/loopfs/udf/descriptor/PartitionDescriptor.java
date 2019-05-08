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

package com.github.stephenc.javaisotools.loopfs.udf.descriptor;

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.UDFDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;


/**
 * The Partition Descriptor (ECMA-167 3/10.5)
 */
public class PartitionDescriptor extends UDFDescriptor
{
	public Long volumeDescriptorSeq;
	public Integer flags;
	public Integer partitionNumber;
	public RegId contents;
	public byte[] contentsUse;
	public Long accessType;
	public Long startingLocation;
	public Long length;
	public RegId implIdentifier;
	public byte[] implUse;

	// length, beginning position, ending position of these fields above
	public final int LEN_VD_SEQ = 4;
	public final int LEN_FLAGS = 2;
	public final int LEN_PT_NUMBER = 2;
	public final int LEN_CONTENTS = 32;
	public final int LEN_CONTENTS_USE = 128;
	public final int LEN_ACCESS_TYPE = 4;
	public final int LEN_START_LOCATION = 4;
	public final int LEN_LENGTH = 4;
	public final int LEN_IMPL_ID = 32;
	public final int LEN_IMPL_USE = 128;
	public final int LEN_RESERVED = 156;

	public final int BP_VD_SEQ = 16;
	public final int BP_FLAGS = 20;
	public final int BP_PT_NUMBER = 22;
	public final int BP_CONTENTS = 24;
	public final int BP_CONTENTS_USE = 56;
	public final int BP_ACCESS_TYPE = 184;
	public final int BP_START_LOCATION = 188;
	public final int BP_LENGTH = 192;
	public final int BP_IMPL_ID = 196;
	public final int BP_IMPL_USE = 228;
	public final int BP_RESERVED = 356;

	public final int EP_VD_SEQ = BP_VD_SEQ + LEN_VD_SEQ;
	public final int EP_FLAGS = BP_FLAGS + LEN_FLAGS;
	public final int EP_PT_NUMBER = BP_PT_NUMBER + LEN_PT_NUMBER;
	public final int EP_CONTENTS = BP_CONTENTS + LEN_CONTENTS;
	public final int EP_CONTENTS_USE = BP_CONTENTS_USE + LEN_CONTENTS_USE;
	public final int EP_ACCESS_TYPE = BP_ACCESS_TYPE + LEN_ACCESS_TYPE;
	public final int EP_START_LOCATION = BP_START_LOCATION + LEN_START_LOCATION;
	public final int EP_LENGTH = BP_LENGTH + LEN_LENGTH;
	public final int EP_IMPL_ID = BP_IMPL_ID + LEN_IMPL_ID;
	public final int EP_IMPL_USE = BP_IMPL_USE + LEN_IMPL_USE;
	public final int EP_RESERVED = BP_RESERVED + LEN_RESERVED;

	// minimum length of a partition descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

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
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("Partition descriptor too short");
		}

		byte[] fragment;

		this.deserializeTag(bytes);

		this.volumeDescriptorSeq = UDFUtil.getUInt32(bytes, BP_VD_SEQ);
		this.flags = UDFUtil.getUInt16(bytes, BP_FLAGS);
		this.partitionNumber = UDFUtil.getUInt16(bytes, BP_PT_NUMBER);

		fragment = UDFUtil.getBytes(bytes, BP_CONTENTS, LEN_CONTENTS);
		this.contents = new RegId(fragment);

		this.contentsUse = UDFUtil.getBytes(
			bytes, BP_CONTENTS_USE, LEN_CONTENTS_USE
		);
		this.accessType = UDFUtil.getUInt32(bytes, BP_ACCESS_TYPE);
		this.startingLocation = UDFUtil.getUInt32(bytes, BP_START_LOCATION);
		this.length = UDFUtil.getUInt32(bytes, BP_LENGTH);

		fragment = UDFUtil.getBytes(bytes, BP_IMPL_ID, LEN_IMPL_ID);
		this.implIdentifier = new RegId(fragment);

		this.implUse = UDFUtil.getBytes(bytes, BP_IMPL_USE, LEN_IMPL_USE);
	}
}
