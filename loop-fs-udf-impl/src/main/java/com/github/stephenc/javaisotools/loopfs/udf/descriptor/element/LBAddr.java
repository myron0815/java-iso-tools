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

import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidLBAddr;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;


/**
 * The Logical Block Address descriptor (ECMA-167 4/7.1)
 */
public class LBAddr
{
	// attributes in a logical block address descriptor (ECMA-167 figure 4/1)
	public Long blockNumber;
	public Integer partitionReferenceNumber;

	// length, beginning position of these fields above
	public final int LEN_BLOCK_NUM = 4;
	public final int LEN_PARTITION_REF = 4;

	public final int BP_BLOCK_NUM = 0;
	public final int BP_PARTITION_REF = 4;

	// the minimum length of a logical block address descriptor (bytes)
	public final int MINIMUM_LENGTH = 6;

	/**
	 * Constructor
	 *
	 * @param bytes byte array contains a raw logical
	 *              block address descriptor at the beginning
	 * @throws InvalidLBAddr
	 */
	public LBAddr(byte[] bytes) throws InvalidLBAddr {
		this.deserialize(bytes);
	}

	/**
	 * deserialize bytes of a raw logical block address descriptor
	 *
	 * @param bytes byte array contains a raw logical
	 *              block address descriptor at the beginning
	 * @throws InvalidLBAddr
	 */
	public void deserialize(byte[] bytes) throws InvalidLBAddr {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidLBAddr("logical block address descriptor too short");
		}

		this.blockNumber = UDFUtil.getUInt32(bytes, BP_BLOCK_NUM);
		this.partitionReferenceNumber = UDFUtil.getUInt16(bytes, BP_PARTITION_REF);
	}
}
