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
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DString;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.CharSpec;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.Timestamp;


/**
 * The Primary Volume Descriptor (ECMA-167 3/10.1)
 */
public class PrimaryVolumeDescriptor extends UDFDescriptor
{
	public Long volumeDescriptorSeq;
	public Long primaryVolumeDescriptorNum;
	public DString volumeIdentifier;
	public Integer volumeSeq;
	public Integer maxVolumeSeq;
	public Integer interchangeLevel;
	public Integer maxInterchangeLevel;
	public Long charsetList;
	public Long maxCharsetList;
	public DString volumeSetIdentifier;
	public CharSpec descriptorCharset;
	public CharSpec explanatoryCharset;
	public ExtentAD volumeAbstract;
	public ExtentAD volumeCopyrightNotice;
	public RegId appIdentifier;
	public Timestamp recordDateTime;
	public RegId implIdentifier;
	public byte[] implUse;
	public Long predecessorVDSeq;
	public Integer flags;

	// length, beginning position, ending position of these fields above
	public final int LEN_VD_SEQ = 4;
	public final int LEN_PRIM_VD_NUM = 4;
	public final int LEN_VOL_ID = 32;
	public final int LEN_VOL_SEQ = 2;
	public final int LEN_MAX_VOL_SEQ = 2;
	public final int LEN_INTERCH_LVL = 2;
	public final int LEN_MAX_INTERCH_LVL = 2;
	public final int LEN_CHARSET_LIST = 4;
	public final int LEN_MAX_CHARSET_LIST = 4;
	public final int LEN_VOL_SET_ID = 128;
	public final int LEN_DESCRIPTOR_CHAR = 64;
	public final int LEN_EXPLAN_CHAR = 64;
	public final int LEN_VOL_ABSTRACT = 8;
	public final int LEN_VOL_COPYRIGHT = 8;
	public final int LEN_APP_ID = 32;
	public final int LEN_RECORD_DT = 12;
	public final int LEN_IMPL_ID = 32;
	public final int LEN_IMPL_USE = 64;
	public final int LEN_PRE_VD_SEQ = 4;
	public final int LEN_FLAGS = 2;
	public final int LEN_RESERVED = 22;

	public final int BP_VD_SEQ = 16;
	public final int BP_PRIM_VD_NUM = 20;
	public final int BP_VOL_ID = 24;
	public final int BP_VOL_SEQ = 56;
	public final int BP_MAX_VOL_SEQ = 58;
	public final int BP_INTERCH_LVL = 60;
	public final int BP_MAX_INTERCH_LVL = 62;
	public final int BP_CHARSET_LIST = 64;
	public final int BP_MAX_CHARSET_LIST = 68;
	public final int BP_VOL_SET_ID = 72;
	public final int BP_DESCRIPTOR_CHAR = 200;
	public final int BP_EXPLAN_CHAR = 264;
	public final int BP_VOL_ABSTRACT = 328;
	public final int BP_VOL_COPYRIGHT = 336;
	public final int BP_APP_ID = 344;
	public final int BP_RECORD_DT = 376;
	public final int BP_IMPL_ID = 388;
	public final int BP_IMPL_USE = 420;
	public final int BP_PRE_VD_SEQ = 484;
	public final int BP_FLAGS = 488;
	public final int BP_RESERVED = 490;

	public final int EP_VD_SEQ = BP_VD_SEQ + LEN_VD_SEQ;
	public final int EP_PRIM_VD_NUM = BP_PRIM_VD_NUM + LEN_PRIM_VD_NUM;
	public final int EP_VOL_ID = BP_VOL_ID + LEN_VOL_ID;
	public final int EP_VOL_SEQ = BP_VOL_SEQ + LEN_VOL_SEQ;
	public final int EP_MAX_VOL_SEQ = BP_MAX_VOL_SEQ + LEN_MAX_VOL_SEQ;
	public final int EP_INTERCH_LVL = BP_INTERCH_LVL + LEN_INTERCH_LVL;
	public final int EP_MAX_INTERCH_LVL = BP_MAX_INTERCH_LVL + LEN_MAX_INTERCH_LVL;
	public final int EP_CHARSET_LIST = BP_CHARSET_LIST + LEN_CHARSET_LIST;
	public final int EP_MAX_CHARSET_LIST = BP_MAX_CHARSET_LIST + LEN_MAX_CHARSET_LIST;
	public final int EP_VOL_SET_ID = BP_VOL_SET_ID + LEN_VOL_SET_ID;
	public final int EP_DESCRIPTOR_CHAR = BP_DESCRIPTOR_CHAR + LEN_DESCRIPTOR_CHAR;
	public final int EP_EXPLAN_CHAR = BP_EXPLAN_CHAR + LEN_EXPLAN_CHAR;
	public final int EP_VOL_ABSTRACT = BP_VOL_ABSTRACT + LEN_VOL_ABSTRACT;
	public final int EP_VOL_COPYRIGHT = BP_VOL_COPYRIGHT + LEN_VOL_COPYRIGHT;
	public final int EP_APP_ID = BP_APP_ID + LEN_APP_ID;
	public final int EP_RECORD_DT = BP_RECORD_DT + LEN_RECORD_DT;
	public final int EP_IMPL_ID = BP_IMPL_ID + LEN_IMPL_ID;
	public final int EP_IMPL_USE = BP_IMPL_USE + LEN_IMPL_USE;
	public final int EP_PRE_VD_SEQ = BP_PRE_VD_SEQ + LEN_PRE_VD_SEQ;
	public final int EP_FLAGS = BP_FLAGS + LEN_FLAGS;
	public final int EP_RESERVED = BP_RESERVED + LEN_RESERVED;

	// minimum length of a primary volume descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

	public PrimaryVolumeDescriptor() {
		super();
	}

	public PrimaryVolumeDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_PRIMARY_VOLUME;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("Primary volume descriptor too short");
		}

		byte[] fragment;

		this.deserializeTag(bytes);

		this.volumeDescriptorSeq = UDFUtil.getUInt32(bytes, BP_VD_SEQ);
		this.primaryVolumeDescriptorNum = UDFUtil.getUInt32(bytes, BP_PRIM_VD_NUM);

		fragment = UDFUtil.getBytes(bytes, BP_VOL_ID, LEN_VOL_ID);
		this.volumeIdentifier = new DString(fragment);

		this.volumeSeq = UDFUtil.getUInt16(bytes, BP_VOL_SEQ);
		this.maxVolumeSeq = UDFUtil.getUInt16(bytes, BP_MAX_VOL_SEQ);
		this.interchangeLevel = UDFUtil.getUInt16(bytes, BP_INTERCH_LVL);
		this.maxInterchangeLevel = UDFUtil.getUInt16(bytes, BP_MAX_INTERCH_LVL);
		this.charsetList = UDFUtil.getUInt32(bytes, BP_CHARSET_LIST);
		this.maxCharsetList = UDFUtil.getUInt32(bytes, BP_MAX_CHARSET_LIST);

		fragment = UDFUtil.getBytes(bytes, BP_VOL_SET_ID, LEN_VOL_SET_ID);
		this.volumeSetIdentifier = new DString(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_DESCRIPTOR_CHAR, LEN_DESCRIPTOR_CHAR);
		this.descriptorCharset = new CharSpec(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_EXPLAN_CHAR, LEN_EXPLAN_CHAR);
		this.explanatoryCharset = new CharSpec(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_VOL_ABSTRACT, LEN_VOL_ABSTRACT);
		this.volumeAbstract = new ExtentAD(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_VOL_COPYRIGHT, LEN_VOL_COPYRIGHT);
		this.volumeCopyrightNotice = new ExtentAD(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_APP_ID, LEN_APP_ID);
		this.appIdentifier = new RegId(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_RECORD_DT, LEN_RECORD_DT);
		this.recordDateTime = new Timestamp(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_IMPL_ID, LEN_IMPL_ID);
		this.implIdentifier = new RegId(fragment);

		this.implUse = UDFUtil.getBytes(bytes, BP_IMPL_USE, LEN_IMPL_USE);
		this.predecessorVDSeq = UDFUtil.getUInt32(bytes, BP_PRE_VD_SEQ);
		this.flags = UDFUtil.getUInt16(bytes, BP_FLAGS);
	}
}
