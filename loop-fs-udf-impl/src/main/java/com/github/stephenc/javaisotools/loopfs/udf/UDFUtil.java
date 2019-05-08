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

package com.github.stephenc.javaisotools.loopfs.udf;

import java.util.Arrays;
import java.math.BigInteger;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import com.github.stephenc.javaisotools.loopfs.util.LittleEndian;


public class UDFUtil
{
	/**
	 * Gets an unsigned 8-bit value
	 */
	public static int getUInt8(byte[] block, int pos) {
		return LittleEndian.getUInt8(block, pos);
	}

	/**
	 * Gets an unsigned 16-bit value
	 */
	public static int getUInt16(byte[] block, int pos) {
		return LittleEndian.getUInt16(block, pos);
	}

	/**
	 * Gets an unsigned 32-bit value
	 */
	public static long getUInt32(byte[] block, int pos) {
		return LittleEndian.getUInt32(block, pos);
	}

	/**
	 * Gets an unsigned 64-bit value
	 */
	public static BigInteger getUInt64(byte[] block, int pos) {
		byte[] fragment = getBytes(block, pos, 8);

		// Well, BigInteger didn't provide a parameter of byte order
		// in it's constructor.
		ArrayUtils.reverse(fragment);

		return new BigInteger(fragment);
	}

	/**
	 * Gets a fragment from a byte array
	 */
	public static byte[] getBytes(byte[] bytes, int pos, int length) {
		return Arrays.copyOfRange(bytes, pos, pos + length);
	}

	/**
	 * Gets all remaining bytes in a byte array after the specified position
	 * (byte a the specified position included)
	 */
	public static byte[] getRemainingBytes(byte[] bytes, int pos) {
		return Arrays.copyOfRange(bytes, pos, bytes.length);
	}
}
