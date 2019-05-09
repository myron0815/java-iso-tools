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

package com.github.stephenc.javaisotools.loopfs.udf.descriptor.element;

import java.util.List;
import java.util.ArrayList;

import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptorTag;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.Constants;


/**
 * The descriptor tag (ECMA-167 3/7.2)
 */
public class DescriptorTag
{
	private static List<Integer> D_TYPES;
	static {
		D_TYPES = new ArrayList<Integer>();
		D_TYPES.add(Constants.D_TYPE_PRIMARY_VOLUME);
		D_TYPES.add(Constants.D_TYPE_ANCHOR_POINTER);
		D_TYPES.add(Constants.D_TYPE_POINTER);
		D_TYPES.add(Constants.D_TYPE_IMPL_USE);
		D_TYPES.add(Constants.D_TYPE_PARTITION);
		D_TYPES.add(Constants.D_TYPE_LOGICAL_VOLUME);
		D_TYPES.add(Constants.D_TYPE_UNALLOCATED_SPACE);
		D_TYPES.add(Constants.D_TYPE_TERMINATING);
		D_TYPES.add(Constants.D_TYPE_LOGICAL_VOLUME_INTEGRITY);
		D_TYPES.add(Constants.D_TYPE_FILE_SET);
		D_TYPES.add(Constants.D_TYPE_FILE_IDENTIFIER);
		D_TYPES.add(Constants.D_TYPE_ALLOCATION_EXTENT);
		D_TYPES.add(Constants.D_TYPE_INDIRECT_ENTRY);
		D_TYPES.add(Constants.D_TYPE_TERMINAL_ENTRY);
		D_TYPES.add(Constants.D_TYPE_FILE_ENTRY);
		D_TYPES.add(Constants.D_TYPE_EXTENDED_ATTRIBUTE_HEADER);
		D_TYPES.add(Constants.D_TYPE_UNALLOCATED_SPACE_ENTRY);
		D_TYPES.add(Constants.D_TYPE_SPACE_BITMAP_DESCRIPTOR);
		D_TYPES.add(Constants.D_TYPE_PARTITION_INTEGRITY_ENTRY);
		D_TYPES.add(Constants.D_TYPE_EXTENDED_FILE_ENTRY);
	}

	// attributes in a descriptor tag (ECMA-167 figure 3/2)
	public Integer identifier;
	public Integer version;
	public Integer checksum;
	// Reserved field skipped
	public Integer serialNumber;
	public Integer crc;
	public Integer crcLength;
	public Long location;

	// length, beginning position of these fields above
	public final int LEN_IDENTIFIER = 2;
	public final int LEN_VERSION = 2;
	public final int LEN_CHECKSUM = 1;
	public final int LEN_RESERVED = 1;
	public final int LEN_SERIAL_NUMBER = 2;
	public final int LEN_CRC = 2;
	public final int LEN_CRC_LENGTH = 2;
	public final int LEN_LOCATION = 4;

	public final int BP_IDENTIFIER = 0;
	public final int BP_VERSION = 2;
	public final int BP_CHECKSUM = 4;
	public final int BP_RESERVED = 5;
	public final int BP_SERIAL_NUMBER = 6;
	public final int BP_CRC = 8;
	public final int BP_CRC_LENGTH = 10;
	public final int BP_LOCATION = 12;

	// the minimum length of descriptor tag (bytes)
	public final int MINIMUM_LENGTH = 16;

	/**
	 * Constructor
	 *
	 * @param bytes byte array that contains a raw descriptor tag at the beginning
	 * @throws InvalidDescriptorTag
	 */
	public DescriptorTag(byte[] bytes) throws InvalidDescriptorTag {
		this.deserialize(bytes);
	}

	/**
	 * deserialize bytes of a raw descriptor
	 *
	 * @param bytes byte array that contains a raw descriptor tag at the beginning
	 * @throws InvalidDescriptorTag
	 */
	public void deserialize(byte[] bytes) throws InvalidDescriptorTag {
		if (bytes.length < MINIMUM_LENGTH){
			throw new InvalidDescriptorTag("descriptor tag too short");
		}

		this.identifier = UDFUtil.getUInt16(bytes, BP_IDENTIFIER);

		if (! D_TYPES.contains(this.identifier)) {
			throw new InvalidDescriptorTag(
				"Unknown tag identifier: " + this.identifier
			);
		}


		this.version = UDFUtil.getUInt16(bytes, BP_VERSION);
		this.checksum = UDFUtil.getUInt8(bytes, BP_CHECKSUM);
		// Reserved (uint8) field skipped
		this.serialNumber = UDFUtil.getUInt16(bytes, BP_SERIAL_NUMBER);
		this.crc = UDFUtil.getUInt16(bytes, BP_CRC);
		this.crcLength = UDFUtil.getUInt16(bytes, BP_CRC_LENGTH);
		this.location = UDFUtil.getUInt32(bytes, BP_LOCATION);
	}
}
