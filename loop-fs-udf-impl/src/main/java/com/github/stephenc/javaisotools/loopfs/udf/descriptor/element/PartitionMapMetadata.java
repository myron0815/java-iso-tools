/*
 * Copyright (c) 2022. Myron Boyle (https://github.com/myron0815/)
 * Copyright (c) 2010. Stephen Connolly.
 * Copyright (c) 2006. Björn Stickler <bjoern@stickler.de>.
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.github.stephenc.javaisotools.loopfs.udf.descriptor.element;

import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

public class PartitionMapMetadata extends PartitionMapType2 {

	public Long metadataFileLocation;
	public Long metadataMirrorFileLocation;
	public Long metadataBitmapFileLocation;
	public Long allocationUnitSizeInBlocks;
	public Integer allignmentUnitSizeInBlocks;
	public Integer flags;
	// reserved [5]

	public static final int LENGTH = PartitionMapType2.LENGTH;

	public PartitionMapMetadata(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		metadataFileLocation = UDFUtil.getUInt32(bytes, 40);
		metadataMirrorFileLocation = UDFUtil.getUInt32(bytes, 44);
		metadataBitmapFileLocation = UDFUtil.getUInt32(bytes, 48);
		allocationUnitSizeInBlocks = UDFUtil.getUInt32(bytes, 52);
		allignmentUnitSizeInBlocks = UDFUtil.getUInt16(bytes, 56);
		flags = UDFUtil.getUInt8(bytes, 58);
	}

	@Override
	public String toString() {
		return "PartitionMapMetadata [metadataFileLocation=" + metadataFileLocation + ", metadataMirrorFileLocation="
				+ metadataMirrorFileLocation + ", metadataBitmapFileLocation=" + metadataBitmapFileLocation
				+ ", allocationUnitSizeInBlocks=" + allocationUnitSizeInBlocks + ", allignmentUnitSizeInBlocks="
				+ allignmentUnitSizeInBlocks + ", flags=" + flags + ", " + super.toString() + "]";
	}
}
