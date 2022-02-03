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

import java.util.List;

import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

public class PartitionMapSparable extends PartitionMapType2 {

	public Integer packetLength;
	public Integer numberOfSparingTables; // N_ST [1-4]
	// public byte[] reserved2; // #00 bytes [1]
	public Long sizeOfEachSparingTable;
	public List<Long> locationsOfSparingTables; // 4 * N_ST
	// padding

	public static final int LENGTH = PartitionMapType2.LENGTH;

	public PartitionMapSparable(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Partition map type 2 too short");
		}

		packetLength = UDFUtil.getUInt16(bytes, 40);
		numberOfSparingTables = UDFUtil.getUInt8(bytes, 42);
		// reserved [1]
		sizeOfEachSparingTable = UDFUtil.getUInt32(bytes, 44);
		int pos = 0;
		for (int i = 0; i < numberOfSparingTables; i++) {
			pos = 48 + i * 4; // pos 48 + 4 bytes per Long
			locationsOfSparingTables.add(UDFUtil.getUInt32(bytes, pos));
		}
		// padding (48 + 4 * N_ST; length: 16 - 4 * N_ST)
	}

	@Override
	public String toString() {
		return "PartitionMapSparable [packetLength=" + packetLength + ", numberOfSparingTables=" + numberOfSparingTables
				+ ", sizeOfEachSparingTable=" + sizeOfEachSparingTable + ", locationsOfSparingTables="
				+ locationsOfSparingTables + ", " + super.toString() + "]";
	}
}
