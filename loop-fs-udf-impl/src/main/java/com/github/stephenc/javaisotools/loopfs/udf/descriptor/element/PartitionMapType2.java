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

public class PartitionMapType2 extends PartitionMap {

	// public byte[] reserved1; // #00 bytes [2]
	public RegId partitionTypeIdentifier;
	public int volumeSequenceNumber; // UInt16
	public int partitionNumber; // UInt16

	public static final int LENGTH = 64;

	public PartitionMapType2(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Partition map type 2 too short");
		}

		this.partitionTypeIdentifier = new RegId(UDFUtil.getBytes(bytes, 4, RegId.LENGTH));
		this.volumeSequenceNumber = UDFUtil.getUInt16(bytes, 36);
		this.partitionNumber = UDFUtil.getUInt16(bytes, 38);
	}

	@Override
	public String toString() {
		return "PartitionMapType2 [partitionTypeIdentifier=" + partitionTypeIdentifier + ", volumeSequenceNumber="
				+ volumeSequenceNumber + ", partitionNumber=" + partitionNumber + ", " + super.toString()
				+ "]";
	}

}
