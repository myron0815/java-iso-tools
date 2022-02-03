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
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The regid block (ECMA-167 1/7.4)
 *
 * Not implemented and not in use
 */
public class RegId {
	public int Flags; // Uint8
	public byte Identifier[]; // char[23]
	public byte IdentifierSuffix[]; // char[8]

	public static final int LENGTH = 32;

	public RegId(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("RegId allocation descriptor too short");
		}

		this.Flags = UDFUtil.getUInt8(bytes, 0);
		Identifier = UDFUtil.getBytes(bytes, 1, 23);
		IdentifierSuffix = UDFUtil.getBytes(bytes, 24, 31);
	}
	
	public String getId() {
		return new String(Identifier);
	}

	@Override
	public String toString() {
		return "RegId [Flags=" + Flags + ", Identifier=" + new String(Identifier, StandardCharsets.UTF_8).trim() + ", IdentifierSuffix=" + new String(IdentifierSuffix, StandardCharsets.UTF_8).trim() + "]";
	}

}