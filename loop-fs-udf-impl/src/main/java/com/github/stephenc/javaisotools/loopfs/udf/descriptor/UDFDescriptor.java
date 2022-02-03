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

import java.math.BigInteger;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DescriptorTag;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;
import com.github.stephenc.javaisotools.loopfs.util.LittleEndian;


public abstract class UDFDescriptor
{
	public DescriptorTag tag;

	// length of descriptor tags
	public final int TAG_LENGTH = 16;
	public int currentPos = TAG_LENGTH;

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
	
	// ************************************************************
	// Helper methods, to not always specify position & length ;)
	// ************************************************************

	/**
	 * Gets an unsigned 8-bit value
	 */
	public int getUInt8(byte[] block) {
		int i = LittleEndian.getUInt8(block, currentPos); 
		currentPos += 1;
		return  i;
	}

	/**
	 * Gets an unsigned 16-bit value
	 */
	public int getUInt16(byte[] block) {
		int i = LittleEndian.getUInt16(block, currentPos); 
		currentPos += 2;
		return  i;
	}

	/**
	 * Gets an unsigned 32-bit value
	 */
	public long getUInt32(byte[] block) {
		long i = LittleEndian.getUInt32(block, currentPos); 
		currentPos += 4;
		return  i;
	}

	/**
	 * Gets an unsigned 64-bit value
	 */
	public BigInteger getUInt64(byte[] block) {
		byte[] fragment = getBytes(block, 8);

		// Well, BigInteger didn't provide a parameter of byte order
		// in it's constructor.
		ArrayUtils.reverse(fragment);

		return new BigInteger(fragment);
	}

	/**
	 * Gets a fragment from a byte array
	 */
	public byte[] getBytes(byte[] bytes, int length) {
		byte[] b = Arrays.copyOfRange(bytes, currentPos, currentPos + length);
		currentPos += length;
		return b;
	}
}
