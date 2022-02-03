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

import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Extended Attribute (ECMA-167 3/7.1)
 */
public class ExtendedAttributeInformationTimes extends ExtendedAttribute {

//	0 4 Attribute Type Uint32 (1/7.1.5) = 3
//	4 1 Attribute Subtype Uint8 (1/7.1.1) = 1
//	5 3 Reserved #00 bytes
//	8 4 Attribute Length Uint32 (1/7.1.5)
//
//	12 4 Data Length(=D_L) Uint32 (1/7.1.5)
//	16 4 Information Time Existence Uint32 (1/7.1.5)
//	20 D_L Information Times bytes

	public Long dataLength;
	public Long informationTimeExistence;
	public byte[] informationTimes;

	public static final int MINIMUM_LENGTH = 12;

	public ExtendedAttributeInformationTimes(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		dataLength = UDFUtil.getUInt32(bytes, 12);
		informationTimeExistence = UDFUtil.getUInt32(bytes, 16);
		informationTimes = UDFUtil.getBytes(bytes, 20, dataLength.intValue());
	}

}
