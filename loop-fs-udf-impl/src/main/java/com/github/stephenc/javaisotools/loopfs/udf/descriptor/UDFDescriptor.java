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

package com.github.stephenc.javaisotools.loopfs.udf.descriptor;

import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DescriptorTag;


public abstract class UDFDescriptor
{
	public DescriptorTag tag;

	// length of descriptor tags
	public final int TAG_LENGTH = 16;

	/**
	 * Constructor
	 */
	public UDFDescriptor() {}

	/**
	 * Constructor
	 *
	 * @param bytes byte array that contains a raw descriptor
	 * @throws InvalidDescriptor
	 */
	public UDFDescriptor(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
		this.verifyTagIdentifier();
	}

	/**
	 * @return returns the expected value of tag identifier in the descriptor tag
	 */
	public abstract int getExpectedTagIdentifier();

	/**
	 * deserialize a raw descriptor and extract informations
	 *
	 * @param bytes byte array that contains a raw descriptor
	 * @throws InvalidDescriptor
	 */
	public abstract void deserialize(byte[] bytes) throws InvalidDescriptor;

	/**
	 * Verify the tag identifier in the descriptor tag
	 *
	 * @throws InvalidDescriptor when verification not passed
	 */
	public void verifyTagIdentifier() throws InvalidDescriptor {
		Integer tagId = this.tag.identifier;
		if ( tagId != this.getExpectedTagIdentifier() ){
			throw new InvalidDescriptor(
				"Unexpected tag identifier: " + tagId.toString()
			);
		}
	}

	/**
	 * Exctract the descriptor tag from bytes of a row descriptor
	 *
	 * @param bytes byte array that contains a raw descriptor
	 * @throws InvalidDescriptor
	 */
	public void deserializeTag(byte[] bytes) throws InvalidDescriptor {
		this.tag = new DescriptorTag(bytes);
	}
}
