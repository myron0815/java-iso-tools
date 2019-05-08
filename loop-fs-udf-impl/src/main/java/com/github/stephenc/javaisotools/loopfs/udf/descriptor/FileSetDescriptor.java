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

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.UDFDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DString;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.CharSpec;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.Timestamp;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LongAD;


/**
 * The File Set Descriptor (ECMA-167 4/14.1)
 */
public class FileSetDescriptor extends UDFDescriptor
{
	public Timestamp recordDateTime;
	public Integer interchangeLevel;
	public Integer maxInterchangeLevel;
	public Long charsetList;
	public Long maxCharsetList;
	public Long fileSetNum;
	public Long fileSetDescriptorNum;
	public CharSpec lvIdentifierCharset;
	public DString lvIdentifier;
	public CharSpec fileSetCharset;
	public DString fileSetIdentifier;
	public DString copyrightFileId;
	public DString abstractFileId;
	public LongAD rootICB;
	public RegId domainIdentifier;
	public LongAD nextExtent;
	public LongAD sysStreamDirICB;

	// length, beginning position, ending position of these fields above
	public final int LEN_RECORD_DT = 12;
	public final int LEN_INTERCH_LVL = 2;
	public final int LEN_MAX_INTERCH_LVL = 2;
	public final int LEN_CHARSET_LIST = 4;
	public final int LEN_MAX_CHARSET_LIST = 4;
	public final int LEN_FSET_NUM = 4;
	public final int LEN_FSET_DESC_NUM = 4;
	public final int LEN_LV_ID_CHARSET = 64;
	public final int LEN_LV_ID = 128;
	public final int LEN_FSET_CHARSET = 64;
	public final int LEN_FSET_ID = 32;
	public final int LEN_COPYRIGHT_FID = 32;
	public final int LEN_ABSTRACT_FID = 32;
	public final int LEN_ROOT_DIR_ICB = 16;
	public final int LEN_DOMAIN_ID = 32;
	public final int LEN_NEXT_EXTENT = 16;
	public final int LEN_STREAM_DIR_ICB = 16;
	public final int LEN_RESERVED = 32;

	public final int BP_RECORD_DT = 16;
	public final int BP_INTERCH_LVL = 28;
	public final int BP_MAX_INTERCH_LVL = 30;
	public final int BP_CHARSET_LIST = 32;
	public final int BP_MAX_CHARSET_LIST = 36;
	public final int BP_FSET_NUM = 40;
	public final int BP_FSET_DESC_NUM = 44;
	public final int BP_LV_ID_CHARSET = 48;
	public final int BP_LV_ID = 112;
	public final int BP_FSET_CHARSET = 240;
	public final int BP_FSET_ID = 304;
	public final int BP_COPYRIGHT_FID = 336;
	public final int BP_ABSTRACT_FID = 368;
	public final int BP_ROOT_DIR_ICB = 400;
	public final int BP_DOMAIN_ID = 416;
	public final int BP_NEXT_EXTENT = 448;
	public final int BP_STREAM_DIR_ICB = 464;
	public final int BP_RESERVED = 480;

	public final int EP_RECORD_DT = BP_RECORD_DT + LEN_RECORD_DT;
	public final int EP_INTERCH_LVL = BP_INTERCH_LVL + LEN_INTERCH_LVL;
	public final int EP_MAX_INTERCH_LVL = BP_MAX_INTERCH_LVL + LEN_MAX_INTERCH_LVL;
	public final int EP_CHARSET_LIST = BP_CHARSET_LIST + LEN_CHARSET_LIST;
	public final int EP_MAX_CHARSET_LIST = BP_MAX_CHARSET_LIST + LEN_MAX_CHARSET_LIST;
	public final int EP_FSET_NUM = BP_FSET_NUM + LEN_FSET_NUM;
	public final int EP_FSET_DESC_NUM = BP_FSET_DESC_NUM + LEN_FSET_DESC_NUM;
	public final int EP_LV_ID_CHARSET = BP_LV_ID_CHARSET + LEN_LV_ID_CHARSET;
	public final int EP_LV_ID = BP_LV_ID + LEN_LV_ID;
	public final int EP_FSET_CHARSET = BP_FSET_CHARSET + LEN_FSET_CHARSET;
	public final int EP_FSET_ID = BP_FSET_ID + LEN_FSET_ID;
	public final int EP_COPYRIGHT_FID = BP_COPYRIGHT_FID + LEN_COPYRIGHT_FID;
	public final int EP_ABSTRACT_FID = BP_ABSTRACT_FID + LEN_ABSTRACT_FID;
	public final int EP_ROOT_DIR_ICB = BP_ROOT_DIR_ICB + LEN_ROOT_DIR_ICB;
	public final int EP_DOMAIN_ID = BP_DOMAIN_ID + LEN_DOMAIN_ID;
	public final int EP_NEXT_EXTENT = BP_NEXT_EXTENT + LEN_NEXT_EXTENT;
	public final int EP_STREAM_DIR_ICB = BP_STREAM_DIR_ICB + LEN_STREAM_DIR_ICB;
	public final int EP_RESERVED = BP_RESERVED + LEN_RESERVED;

	// minimum length of a file set descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

	public FileSetDescriptor() {
		super();
	}

	public FileSetDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_FILE_SET;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("File set descriptor too short");
		}

		byte[] fragment;

		this.deserializeTag(bytes);

		fragment = UDFUtil.getBytes(bytes, BP_RECORD_DT, LEN_RECORD_DT);
		this.recordDateTime = new Timestamp(fragment);

		this.interchangeLevel = UDFUtil.getUInt16(bytes, BP_INTERCH_LVL);
		this.maxInterchangeLevel = UDFUtil.getUInt16(bytes, BP_MAX_INTERCH_LVL);
		this.charsetList = UDFUtil.getUInt32(bytes, BP_CHARSET_LIST);
		this.maxCharsetList = UDFUtil.getUInt32(bytes, BP_MAX_CHARSET_LIST);
		this.fileSetNum = UDFUtil.getUInt32(bytes, BP_FSET_NUM);
		this.fileSetDescriptorNum = UDFUtil.getUInt32(bytes, BP_FSET_DESC_NUM);

		fragment = UDFUtil.getBytes(bytes, BP_LV_ID_CHARSET, LEN_LV_ID_CHARSET);
		this.lvIdentifierCharset = new CharSpec(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_LV_ID, LEN_LV_ID);
		this.lvIdentifier = new DString(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_FSET_CHARSET, LEN_FSET_CHARSET);
		this.fileSetCharset = new CharSpec(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_FSET_ID, LEN_FSET_ID);
		this.fileSetIdentifier = new DString(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_COPYRIGHT_FID, LEN_COPYRIGHT_FID);
		this.copyrightFileId = new DString(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_ABSTRACT_FID, LEN_ABSTRACT_FID);
		this.abstractFileId = new DString(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_ROOT_DIR_ICB, LEN_ROOT_DIR_ICB);
		this.rootICB = new LongAD(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_DOMAIN_ID, LEN_DOMAIN_ID);
		this.domainIdentifier = new RegId(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_NEXT_EXTENT, LEN_NEXT_EXTENT);
		this.nextExtent = new LongAD(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_STREAM_DIR_ICB, LEN_STREAM_DIR_ICB);
		this.sysStreamDirICB = new LongAD(fragment);
	}
}
