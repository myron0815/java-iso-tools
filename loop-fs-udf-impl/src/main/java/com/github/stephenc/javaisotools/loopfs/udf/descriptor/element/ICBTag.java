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
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidICBTag;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidLBAddr;

/**
 * The ICB Tag (ECMA-167 4/14.6)
 */
public class ICBTag {
//	struct icbtag { /* ECMA 167 4/14.6 */
//		Uint32 PriorRecordedNumberofDirectEntries;
//		Uint16 StrategyType;
//		byte StrategyParameter[2];
//		Uint16 MaximumNumberofEntries;
//		byte Reserved;
//		Uint8 FileType;
//		Lb_addr ParentICBLocation;
//		Uint16 Flags;
//	}

	public Long priorEntryNum;
	public Integer strategyType;
	public byte[] strategyParam;
	public Integer maxEntries;
	// "Reserved" field skipped
	public Integer fileType;
	public LBAddr parentICBLocation;
	public Integer flags;

	// the minimum length of an ICB tag (bytes)
	public static final int LENGTH = 20;

	/**
	 * Constructor
	 *
	 * @param bytes byte array contains a raw logical block address descriptor at
	 *              the beginning
	 * @throws InvalidICBTag
	 */
	public ICBTag(byte[] bytes) throws InvalidICBTag {
		this.deserialize(bytes);
	}

	/**
	 * deserialize bytes of a raw logical block address descriptor
	 *
	 * @param bytes byte array contains a raw logical block address descriptor at
	 *              the beginning
	 * @throws InvalidICBTag
	 */
	public void deserialize(byte[] bytes) throws InvalidICBTag {
		if (bytes.length < LENGTH) {
			throw new InvalidICBTag("ICB tag too short");
		}

		byte[] fragment;

		this.priorEntryNum = UDFUtil.getUInt32(bytes, 0);
		this.strategyType = UDFUtil.getUInt16(bytes, 4);

		// OSTA-UDF 2.3.5.1
		// Strategy 4096 is not supported as well.
		if (this.strategyType != 4) {
			throw new InvalidICBTag("Unsupported ICB strategy: " + this.strategyType);
		}

		this.strategyParam = UDFUtil.getBytes(bytes, 6, 2);
		this.maxEntries = UDFUtil.getUInt16(bytes, 8);
		// 1 byte reserved
		this.fileType = UDFUtil.getUInt8(bytes, 11);

		fragment = UDFUtil.getBytes(bytes, 12, 6);
		try {
			this.parentICBLocation = new LBAddr(fragment);
		} catch (InvalidLBAddr ex) {
			throw new InvalidICBTag(ex.getMessage());
		}

		this.flags = UDFUtil.getUInt16(bytes, 18);
	}

	@Override
	public String toString() {
		return "ICBTag [priorEntryNum=" + priorEntryNum + ", strategyType=" + strategyType + ", strategyParam="
				+ new String(strategyParam, StandardCharsets.UTF_8).trim() + ", maxEntries=" + maxEntries
				+ ", fileType=" + fileType + ", parentICBLocation=" + parentICBLocation + ", flags=" + flags + "]";
	}
}
