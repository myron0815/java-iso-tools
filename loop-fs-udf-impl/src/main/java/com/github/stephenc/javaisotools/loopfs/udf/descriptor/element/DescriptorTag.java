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
	public static List<Integer> D_TYPES;
	static {
		D_TYPES = new ArrayList<Integer>();
		D_TYPES.add(Constants.D_TYPE_PRIMARY_VOLUME);
		D_TYPES.add(Constants.D_TYPE_ANCHOR_POINTER);
		D_TYPES.add(Constants.D_TYPE_VOLUME_DESCRIPTOR_POINTER);
		D_TYPES.add(Constants.D_TYPE_IMPL_USE);
		D_TYPES.add(Constants.D_TYPE_PARTITION);
		D_TYPES.add(Constants.D_TYPE_LOGICAL_VOLUME);
		D_TYPES.add(Constants.D_TYPE_UNALLOCATED_SPACE);
		D_TYPES.add(Constants.D_TYPE_TERMINATING);
		D_TYPES.add(Constants.D_TYPE_LOGICAL_VOLUME_INTEGRITY);
		
		D_TYPES.add(Constants.D_TYPE_VIRTUAL_ALLOCATION_TABLE);
		D_TYPES.add(Constants.D_TYPE_REAL_TIME_FILE);
		D_TYPES.add(Constants.D_TYPE_METADATA_FILE);
		D_TYPES.add(Constants.D_TYPE_METADATA_FILE_MIRROR);
		D_TYPES.add(Constants.D_TYPE_METADATA_BITMAP_FILE);
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

//	struct tag { /* ECMA 167 3/7.2 */
//		Uint16 TagIdentifier;
//		Uint16 DescriptorVersion;
//		Uint8 TagChecksum;
//		byte Reserved;
//		Uint16 TagSerialNumber;
//		Uint16 DescriptorCRC;
//		Uint16 DescriptorCRCLength;
//		Uint32 TagLocation;
//	}
	
	// attributes in a descriptor tag (ECMA-167 figure 3/2)
	public Integer identifier;
	public Integer version;
	public Integer checksum;
	// Reserved field skipped
	public Integer serialNumber;
	public Integer crc;
	public Integer crcLength;
	public Long location;

	// the minimum length of descriptor tag (bytes)
	public static final int LENGTH = 16;

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
		if (bytes.length < LENGTH){
			throw new InvalidDescriptorTag("descriptor tag too short");
		}

		this.identifier = UDFUtil.getUInt16(bytes, 0);

		if (! D_TYPES.contains(this.identifier)) {
			throw new InvalidDescriptorTag(
				"Unknown tag identifier: " + this.identifier
			);
		}

		this.version = UDFUtil.getUInt16(bytes, 2);
		this.checksum = UDFUtil.getUInt8(bytes, 4);
		// Reserved (uint8) field skipped
		this.serialNumber = UDFUtil.getUInt16(bytes, 6);
		this.crc = UDFUtil.getUInt16(bytes, 8);
		this.crcLength = UDFUtil.getUInt16(bytes, 10);
		this.location = UDFUtil.getUInt32(bytes, 12);
	}

	@Override
	public String toString() {
		return "DescriptorTag [identifier=" + identifier + ", version=" + version + ", checksum=" + checksum
				+ ", serialNumber=" + serialNumber + ", crc=" + crc + ", crcLength=" + crcLength + ", location="
				+ location + "]";
	}
}
