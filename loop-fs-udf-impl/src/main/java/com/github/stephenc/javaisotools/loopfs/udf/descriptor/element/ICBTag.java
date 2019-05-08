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

import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidICBTag;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidLBAddr;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LBAddr;


/**
 * The ICB Tag (ECMA-167 4/14.6)
 */
public class ICBTag
{
	public Long priorEntryNum;
	public Integer strategyType;
	public byte[] strategyParam;
	public Integer maxEntries;
	// "Reserved" field skipped
	public Integer fileType;
	public LBAddr parentICBLocation;
	public Integer flags;

	// length, beginning position, ending position of these fields above
	public final int LEN_PRI_ENT_NUM = 4;
	public final int LEN_STG_TYPE = 2;
	public final int LEN_STG_PARAM = 2;
	public final int LEN_MAX_ENTRIES = 2;
	public final int LEN_RESERVED = 1;
	public final int LEN_FILE_TYPE = 1;
	public final int LEN_P_ICB_LOC = 6;
	public final int LEN_FLAGS = 2;

	public final int BP_PRI_ENT_NUM = 0;
	public final int BP_STG_TYPE = 4;
	public final int BP_STG_PARAM = 6;
	public final int BP_MAX_ENTRIES = 8;
	public final int BP_RESERVED = 10;
	public final int BP_FILE_TYPE = 11;
	public final int BP_P_ICB_LOC = 12;
	public final int BP_FLAGS = 18;

	public final int EP_PRI_ENT_NUM = BP_PRI_ENT_NUM + LEN_PRI_ENT_NUM;
	public final int EP_STG_TYPE = BP_STG_TYPE + LEN_STG_TYPE;
	public final int EP_STG_PARAM = BP_STG_PARAM + LEN_STG_PARAM;
	public final int EP_MAX_ENTRIES = BP_MAX_ENTRIES + LEN_MAX_ENTRIES;
	public final int EP_RESERVED = BP_RESERVED + LEN_RESERVED;
	public final int EP_FILE_TYPE = BP_FILE_TYPE + LEN_FILE_TYPE;
	public final int EP_P_ICB_LOC = BP_P_ICB_LOC + LEN_P_ICB_LOC;
	public final int EP_FLAGS = BP_FLAGS + LEN_FLAGS;

	// the minimum length of an ICB tag (bytes)
	public final int MINIMUM_LENGTH = 20;

	/**
	 * Constructor
	 *
	 * @param bytes byte array contains a raw logical
	 *              block address descriptor at the beginning
	 * @throws InvalidICBTag
	 */
	public ICBTag(byte[] bytes) throws InvalidICBTag {
		this.deserialize(bytes);
	}

	/**
	 * deserialize bytes of a raw logical block address descriptor
	 *
	 * @param bytes byte array contains a raw logical
	 *              block address descriptor at the beginning
	 * @throws InvalidICBTag
	 */
	public void deserialize(byte[] bytes) throws InvalidICBTag {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidICBTag("ICB tag too short");
		}

		byte[] fragment;

		this.priorEntryNum = UDFUtil.getUInt32(bytes, BP_PRI_ENT_NUM);
		this.strategyType = UDFUtil.getUInt16(bytes, BP_STG_TYPE);
		this.strategyParam = UDFUtil.getBytes(bytes, BP_STG_PARAM, LEN_STG_PARAM);
		this.maxEntries = UDFUtil.getUInt16(bytes, BP_MAX_ENTRIES);
		this.fileType = UDFUtil.getUInt8(bytes, BP_FILE_TYPE);

		fragment = UDFUtil.getBytes(bytes, BP_P_ICB_LOC, LEN_P_ICB_LOC);
		try {
			this.parentICBLocation = new LBAddr(fragment);
		} catch (InvalidLBAddr ex) {
			throw new InvalidICBTag(ex.getMessage());
		}

		this.flags = UDFUtil.getUInt16(bytes, BP_FLAGS);
	}
}
