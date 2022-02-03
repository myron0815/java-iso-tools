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

public class PartitionMap {

	public int partitionMapType; // Uint8
	public int partitionMapLength; // Uint8

	public static final int MINUMUM_LENGTH = 6;

	public PartitionMap(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINUMUM_LENGTH) {
			throw new InvalidDescriptor("Partition map type 1too short");
		}

		this.partitionMapType = UDFUtil.getUInt8(bytes, 0);
		this.partitionMapLength = UDFUtil.getUInt8(bytes, 1);
	}

	@Override
	public String toString() {
		return "PartitionMap [partitionMapType=" + partitionMapType + ", partitionMapLength=" + partitionMapLength
				+ "]";
	}
}
