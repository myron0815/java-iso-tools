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

package com.github.stephenc.javaisotools.loopfs.udf.descriptor;

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Extended Attribute Header Descriptor (ECMA 167 4/14.10.1)
 */
public class ExtendedAttributeHeaderDescriptor extends UDFDescriptor {

//	struct ExtendedAttributeHeaderDescriptor { /* ECMA 167 4/14.10.1 */
//		struct tag DescriptorTag;
//		Uint32 ImplementationAttributesLocation;
//		Uint32 ApplicationAttributesLocation;
//	}

	public long implementationAttributesLocation;
	public long applicationAttributesLocation;

	// minimum length of an unallocated space descriptor (field "Reserved" included)
	public static final int LENGTH = 24;

	public ExtendedAttributeHeaderDescriptor() {
		super();
	}

	public ExtendedAttributeHeaderDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_EXTENDED_ATTRIBUTE_HEADER;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Extended Attribute Header Descriptor too short");
		}
		this.deserializeTag(bytes);

		this.implementationAttributesLocation = getUInt32(bytes);
		this.applicationAttributesLocation = getUInt32(bytes);
	}
}
