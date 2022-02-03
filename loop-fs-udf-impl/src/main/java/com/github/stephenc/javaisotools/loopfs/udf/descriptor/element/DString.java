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


/**
 * DString in OSTA Compressed Unicode format
 */
public class DString
{
	public byte[] bytes;

	public DString(byte[] bytes) {
		this.bytes = bytes;
	}

	private String dcharToString (byte[] dchar) {
		if (dchar.length == 1){
			return new String(dchar, StandardCharsets.UTF_8);
		} else {
			return new String(dchar, StandardCharsets.UTF_16);
		}
	}

	public String toString() {
		if (this.bytes.length <= 1){
			return "";
		}

		Integer charLen;
		Integer compressionID = UDFUtil.getUInt8(this.bytes, 0);

		if (compressionID == 8) { 
			charLen = 1;
		} else if (compressionID == 16) {
			charLen = 2;
		} else {
//			throw new RuntimeException(
//				"Unknown dstring compression ID: " + compressionID.toString()
//			);
//			nah, just return something... it's not critical!
			return "Unknown dstring compression ID: " + compressionID.toString();
		}

		byte[] data = UDFUtil.getRemainingBytes(this.bytes, 1);

		String result = "";
		Integer charOffset = 0;

		while (charOffset < data.length) {
			byte[] dcharBytes = UDFUtil.getBytes(data, charOffset, charLen);
			String chr = this.dcharToString(dcharBytes);

			charOffset += charLen;
			result += chr;
		}

		return result;
	}
}
