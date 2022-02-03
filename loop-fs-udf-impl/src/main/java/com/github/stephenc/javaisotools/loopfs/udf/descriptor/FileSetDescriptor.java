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
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.CharSpec;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DString;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LongAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.Timestamp;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The File Set Descriptor (ECMA-167 4/14.1)
 */
public class FileSetDescriptor extends UDFDescriptor {
//	struct FileSetDescriptor { /* ECMA 167 4/14.1 */
//		struct tag DescriptorTag;
//		struct timestamp RecordingDateandTime;
//		Uint16 InterchangeLevel;
//		Uint16 MaximumInterchangeLevel;
//		Uint32 CharacterSetList;
//		Uint32 MaximumCharacterSetList;
//		Uint32 FileSetNumber;
//		Uint32 FileSetDescriptorNumber;
//		struct charspec LogicalVolumeIdentifierCharacterSet;
//		dstring LogicalVolumeIdentifier[128];
//		struct charspec FileSetCharacterSet;
//		dstring FileSetIdentifier[32];
//		dstring CopyrightFileIdentifier[32];
//		dstring AbstractFileIdentifier[32];
//		struct long_ad RootDirectoryICB;
//		struct EntityID DomainIdentifier;
//		struct long_ad NextExtent;
//		struct long_ad SystemStreamDirectoryICB;
//		byte Reserved[32];
//	}

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

	// minimum length of a file set descriptor (field "Reserved" included)
	public static final int LENGTH = 512;

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
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("File set descriptor too short");
		}
		this.deserializeTag(bytes);

		this.recordDateTime = new Timestamp(getBytes(bytes, Timestamp.LENGTH));
		this.interchangeLevel = getUInt16(bytes);
		this.maxInterchangeLevel = getUInt16(bytes);
		this.charsetList = getUInt32(bytes);
		this.maxCharsetList = getUInt32(bytes);
		this.fileSetNum = getUInt32(bytes);
		this.fileSetDescriptorNum = getUInt32(bytes);
		this.lvIdentifierCharset = new CharSpec(getBytes(bytes, CharSpec.LENGTH));
		this.lvIdentifier = new DString(getBytes(bytes, 128));
		this.fileSetCharset = new CharSpec(getBytes(bytes, CharSpec.LENGTH));
		this.fileSetIdentifier = new DString(getBytes(bytes, 32));
		this.copyrightFileId = new DString(getBytes(bytes, 32));
		this.abstractFileId = new DString(getBytes(bytes, 32));
		this.rootICB = new LongAD(getBytes(bytes, LongAD.LENGTH));
		this.domainIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));
		this.nextExtent = new LongAD(getBytes(bytes, LongAD.LENGTH));
		this.sysStreamDirICB = new LongAD(getBytes(bytes, LongAD.LENGTH));
		
		currentPos = LENGTH; // set to end
	}
}
