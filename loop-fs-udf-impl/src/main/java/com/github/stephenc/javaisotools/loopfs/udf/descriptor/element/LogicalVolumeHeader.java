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

import java.math.BigInteger;

import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * LogicalVolumeHeaderDesc { ECMA 167 4/14.15
 *
 * Not implemented and not in use
 */
public class LogicalVolumeHeader {

//	struct LogicalVolumeHeaderDesc { /* ECMA 167 4/14.15 */
//		Uint64 UniqueID,
//		bytes Reserved[24]
//	}

	public BigInteger uniqueId;
	public byte reserved[];

	public static final int LENGTH = 32;

	public LogicalVolumeHeader(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("RegId allocation descriptor too short");
		}

		uniqueId = UDFUtil.getUInt64(bytes, 0);
		reserved = UDFUtil.getBytes(bytes, 8, 24);
	}

	@Override
	public String toString() {
		return "LogicalVolumeHeader [uniqueId=" + uniqueId + ", reserved=" + reserved + "]";
	}
}
