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

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DString;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LongAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The File Identifier Descriptor (ECMA-167 4/14.4)
 */
public class FileIdentifierDescriptor extends UDFDescriptor {

//	struct FileIdentifierDescriptor { /* ECMA 167 4/14.4 */
//		struct tag DescriptorTag;
//		Uint16 FileVersionNumber;
//		Uint8 FileCharacteristics;
//		Uint8 LengthofFileIdentifier;
//		struct long_ad ICB;
//		Uint16 LengthOfImplementationUse;
//		byte ImplementationUse[];
//		char FileIdentifier[];
//		byte Padding[];
//	}

	public Integer version;
	public Integer characteristics;
	public Integer fileIdLength;
	public LongAD icb;
	public Integer implUseLength;
	public RegId implUse;
	public DString fileId;

	// number of bytes that this file identifier descriptor consumes
	protected Integer consumption;

	// minimum length of a file identifier descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 40;

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
		this.deserializeTag(bytes);

		this.version = getUInt16(bytes);
		this.characteristics = getUInt8(bytes);
		this.fileIdLength = getUInt8(bytes);
		this.icb = new LongAD(getBytes(bytes, LongAD.LENGTH));
		this.implUseLength = getUInt16(bytes);
		if (this.implUseLength > 0) {
			this.implUse = new RegId(getBytes(bytes, this.implUseLength));
		}
		this.fileId = new DString(getBytes(bytes, this.fileIdLength));

		// Padding
		// alt: (4 * ((currentPos + 3) / 4)) - currentPos;
		if ((currentPos % Constants.FID_PADDING_DIVISOR) > 0) {
			int paddingLength = Constants.FID_PADDING_DIVISOR - (currentPos % Constants.FID_PADDING_DIVISOR);
			currentPos += paddingLength;
		}
		this.consumption = currentPos;
	}

	/**
	 * Get number of bytes that the current file identifier descriptor consumes
	 * (padding field included).
	 */
	public Integer getConsumption() {
		return this.consumption;
	}
}
