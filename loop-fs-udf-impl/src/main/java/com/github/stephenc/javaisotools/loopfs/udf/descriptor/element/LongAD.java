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

import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidLongAD;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidLBAddr;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LBAddr;


/**
 * The Long Allocation Descriptor (ECMA-167 4/14.14.2.1)
 */
public class LongAD
{
	// attributes in a long allocation descriptor (ECMA-167 figure 4/43)
	public Long length;
	public LBAddr location;
	public byte[] implUse;

	// length, beginning position, ending position of these fields above
	public final int LEN_LENGTH = 4;
	public final int LEN_LOCATION = 6;
	public final int LEN_IMPL_USE = 6;

	public final int BP_LENGTH = 0;
	public final int BP_LOCATION = 4;
	public final int BP_IMPL_USE = 10;

	public final int EP_LOCATION = BP_LOCATION + LEN_LENGTH;
	public final int EP_IMPL_USE = BP_IMPL_USE + LEN_IMPL_USE;

	// the minimum length of a long allocation descriptor (bytes)
	public final int MINIMUM_LENGTH = 16;

	/**
	 * Constructor
	 *
	 * @param bytes byte array contains a raw long
	 *              allocation descriptor at the beginning
	 * @throws InvalidLongAD
	 */
	public LongAD(byte[] bytes) throws InvalidLongAD {
		this.deserialize(bytes);
	}

	/**
	 * deserialize bytes of a raw long allocation descriptor
	 *
	 * @param bytes byte array contains a raw long
	 *              allocation descriptor at the beginning
	 * @throws InvalidLongAD
	 */
	public void deserialize(byte[] bytes) throws InvalidLongAD {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidLongAD("long allocation descriptor too short");
		}

		this.length = UDFUtil.getUInt32(bytes, BP_LENGTH);

		byte[] lbAddrBytes = UDFUtil.getBytes(bytes, BP_LOCATION, LEN_LOCATION);

		try {
			this.location = new LBAddr(lbAddrBytes);
		} catch (InvalidLBAddr ex) {
			throw new InvalidLongAD(ex.getMessage());
		}

		this.implUse = UDFUtil.getBytes(bytes, BP_IMPL_USE, LEN_IMPL_USE);
	}
}
