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
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LongAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DString;


/**
 * The File Identifier Descriptor (ECMA-167 4/14.4)
 */
public class FileIdentifierDescriptor extends UDFDescriptor
{
	public Integer version;
	public Integer characteristics;
	public Integer fileIdLength;
	public LongAD icb;
	public Integer implUseLength;
	public byte[] implUse;
	public DString fileId;

	// number of bytes that this file identifier descriptor consumes
	protected Integer consumption;

	// length, beginning position, ending position of these fields above
	public final int LEN_VERSION = 2;
	public final int LEN_CHARACTERISTICS = 1;
	public final int LEN_LEN_FID = 1;
	public final int LEN_ICB = 16;
	public final int LEN_LEN_IMPL_USE = 2;

	public final int BP_VERSION = 16;
	public final int BP_CHARACTERISTICS = 18;
	public final int BP_LEN_FID = 19;
	public final int BP_ICB = 20;
	public final int BP_LEN_IMPL_USE = 36;
	public final int BP_IMPL_USE = 38;

	public final int EP_VERSION = BP_VERSION + LEN_VERSION;
	public final int EP_CHARACTERISTICS = BP_CHARACTERISTICS + LEN_CHARACTERISTICS;
	public final int EP_LEN_FID = BP_LEN_FID + LEN_LEN_FID;
	public final int EP_ICB = BP_ICB + LEN_ICB;
	public final int EP_LEN_IMPL_USE = BP_LEN_IMPL_USE + LEN_LEN_IMPL_USE;

	// minimum length of a file identifier descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

	public FileIdentifierDescriptor() {
		super();
	}

	public FileIdentifierDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_FILE_IDENTIFIER;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("File identifier descriptor too short");
		}

		byte[] fragment;

		this.deserializeTag(bytes);

		this.version = UDFUtil.getUInt16(bytes, BP_VERSION);
		this.characteristics = UDFUtil.getUInt8(bytes, BP_CHARACTERISTICS);
		this.fileIdLength = UDFUtil.getUInt8(bytes, BP_LEN_FID);

		fragment = UDFUtil.getBytes(bytes, BP_ICB, LEN_ICB);
		this.icb = new LongAD(fragment);

		this.implUseLength = UDFUtil.getUInt16(bytes, BP_LEN_IMPL_USE);
		this.implUse = UDFUtil.getBytes(bytes, BP_IMPL_USE, this.implUseLength);

		Integer bpFileIdentifier = BP_IMPL_USE + this.implUseLength;
		fragment = UDFUtil.getBytes(bytes, bpFileIdentifier, this.fileIdLength);
		this.fileId = new DString(fragment);

		Integer bytesConsumed = bpFileIdentifier + this.fileIdLength;
		
		Integer divisor = Constants.FID_PADDING_DIVISOR;
		Integer paddingLength = divisor - (bytesConsumed % divisor);
		this.consumption = bytesConsumed + paddingLength;
	}

	/**
	 * Get number of bytes that the current file identifier descriptor consumes
	 * (padding field included).
	 */
	public Integer getConsumption() {
		return this.consumption;
	}
}
