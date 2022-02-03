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

import java.nio.charset.StandardCharsets;

import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidLBAddr;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidLongAD;

/**
 * The Long Allocation Descriptor (ECMA-167 4/14.14.2.1)
 */
public class LongAD {

//	struct long_ad { /* ECMA 167 4/14.14.2 */
//		Uint32 ExtentLength;
//		Lb_addr ExtentLocation;
//		byte ImplementationUse[6];
//	}

	public Long length;
	public LBAddr location;
	public byte[] implUse;

	// the minimum length of a long allocation descriptor (bytes)
	public static final int LENGTH = 16;

	/**
	 * Constructor
	 *
	 * @param bytes byte array contains a raw long allocation descriptor at the
	 *              beginning
	 * @throws InvalidLongAD
	 */
	public LongAD(byte[] bytes) throws InvalidLongAD {
		this.deserialize(bytes);
	}

	/**
	 * deserialize bytes of a raw long allocation descriptor
	 *
	 * @param bytes byte array contains a raw long allocation descriptor at the
	 *              beginning
	 * @throws InvalidLongAD
	 */
	public void deserialize(byte[] bytes) throws InvalidLongAD {
		if (bytes.length < LENGTH) {
			throw new InvalidLongAD("long allocation descriptor too short");
		}

		this.length = UDFUtil.getUInt32(bytes, 0);
		byte[] lbAddrBytes = UDFUtil.getBytes(bytes, 4, LBAddr.LENGTH);
		try {
			this.location = new LBAddr(lbAddrBytes);
		} catch (InvalidLBAddr ex) {
			throw new InvalidLongAD(ex.getMessage());
		}

		this.implUse = UDFUtil.getBytes(bytes, 10, 6);
	}

	@Override
	public String toString() {
		return "LongAD [length=" + length + ", location=" + location + ", implUse="
				+ new String(implUse, StandardCharsets.UTF_8).trim() + "]";
	}
}
