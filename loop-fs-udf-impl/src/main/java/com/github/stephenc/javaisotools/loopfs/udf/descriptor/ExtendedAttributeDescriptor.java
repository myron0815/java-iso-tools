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

import java.util.ArrayList;
import java.util.List;

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtendedAttribute;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Extended Attribute Header Descriptor (ECMA 167 4/14.10.1)
 */
public class ExtendedAttributeDescriptor extends UDFDescriptor {

//	HEADER
//	struct ExtendedAttributeHeaderDescriptor { /* ECMA 167 4/14.10.1 */
//		struct tag DescriptorTag;
//		Uint32 ImplementationAttributesLocation;
//		Uint32 ApplicationAttributesLocation;
//	}

//	DATA 1+
//	RBP Length Name Contents
//	0 4 Attribute Type Uint32 (1/7.1.5)
//	4 1 Attribute Subtype Uint8 (1/7.1.1)
//	5 3 Reserved #00 bytes
//	8 4 Attribute Length (=A_L) Uint32 (1/7.1.5)
//	12 A_L-12 Attribute Data bytes


	public long implementationAttributesLocation;
	public long applicationAttributesLocation;
	public List<ExtendedAttribute> extendedAttribs;

	// minimum length of an unallocated space descriptor (field "Reserved" included)
	public static final int LENGTH = 24;

	public ExtendedAttributeDescriptor() {
		super();
	}

	public ExtendedAttributeDescriptor(byte[] bytes) throws InvalidDescriptor {
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

		// header
		this.implementationAttributesLocation = getUInt32(bytes);
		this.applicationAttributesLocation = getUInt32(bytes);
		
		// and multiple attributes...
		extendedAttribs = new ArrayList<>();
		byte[] fragment = getBytes(bytes, bytes.length - ExtendedAttributeDescriptor.LENGTH);
		while (fragment.length > 0) {
			ExtendedAttribute ea = new ExtendedAttribute(bytes);
			// TODO: cast to correct impl!
			extendedAttribs.add(ea);
		}

	}
}
