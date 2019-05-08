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

import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;


/**
 * The Extent Allocation Descriptor (ECMA-167 3/7.1)
 */
public class ExtentAD
{
	// attributes in a extent allocation descriptor (ECMA-167 figure 3/1)
	public Long length;
	public Long location;

	// length, beginning position of these fields above
	public final int LEN_LENGTH = 4;
	public final int LEN_LOCATION = 4;

	public final int BP_LENGTH = 0;
	public final int BP_LOCATION = 4;

	// the minimum length of a extent allocation descriptor (bytes)
	public final int MINIMUM_LENGTH = 8;

	/**
	 * Constructor
	 *
	 * @param bytes byte array contains a raw extent
	 *              allocation descriptor at the beginning
	 * @throws InvalidExtentAD
	 */
	public ExtentAD(byte[] bytes) throws InvalidExtentAD {
		this.deserialize(bytes);
	}

	/**
	 * deserialize bytes of a raw extent allocation descriptor
	 *
	 * @param bytes byte array contains a raw extent
	 *              allocation descriptor at the beginning
	 * @throws InvalidExtentAD
	 */
	public void deserialize(byte[] bytes) throws InvalidExtentAD {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidExtentAD("extent allocation descriptor too short");
		}

		this.length = UDFUtil.getUInt32(bytes, BP_LENGTH);
		this.location = UDFUtil.getUInt32(bytes, BP_LOCATION);
	}
}
