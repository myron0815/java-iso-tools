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
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.Timestamp;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Primary Volume Descriptor (ECMA-167 3/10.1)
 */
public class PrimaryVolumeDescriptor extends UDFDescriptor {

//	struct PrimaryVolumeDescriptor { /* ECMA 167 3/10.1 */
//		struct tag DescriptorTag;
//		Uint32 VolumeDescriptorSequenceNumber;
//		Uint32 PrimaryVolumeDescriptorNumber;
//		dstring VolumeIdentifier[32];
//		Uint16 VolumeSequenceNumber;
//		Uint16 MaximumVolumeSequenceNumber;
//		Uint16 InterchangeLevel;
//		Uint16 MaximumInterchangeLevel;
//		Uint32 CharacterSetList;
//		Uint32 MaximumCharacterSetList;
//		dstring VolumeSetIdentifier[128];
//		struct charspec DescriptorCharacterSet;
//		struct charspec ExplanatoryCharacterSet;
//		struct extent_ad VolumeAbstract;
//		struct extent_ad VolumeCopyrightNotice;
//		struct EntityID ApplicationIdentifier;
//		struct timestamp RecordingDateandTime;
//		struct EntityID ImplementationIdentifier;
//		byte ImplementationUse[64];
//		Uint32 PredecessorVolumeDescriptorSequenceLocation;
//		Uint16 Flags;
//		byte Reserved[22];
//	}

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

	// minimum length of a primary volume descriptor (field "Reserved" included)
	public static final int LENGTH = 512;

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
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Primary volume descriptor too short");
		}
		this.deserializeTag(bytes);

		this.volumeDescriptorSeq = getUInt32(bytes);
		this.primaryVolumeDescriptorNum = getUInt32(bytes);
		this.volumeIdentifier = new DString(getBytes(bytes, 32));
		this.volumeSeq = getUInt16(bytes);
		this.maxVolumeSeq = getUInt16(bytes);
		this.interchangeLevel = getUInt16(bytes);
		this.maxInterchangeLevel = getUInt16(bytes);
		this.charsetList = getUInt32(bytes);
		this.maxCharsetList = getUInt32(bytes);
		this.volumeSetIdentifier = new DString(getBytes(bytes, 128));
		this.descriptorCharset = new CharSpec(getBytes(bytes, CharSpec.LENGTH));
		this.explanatoryCharset = new CharSpec(getBytes(bytes, CharSpec.LENGTH));
		this.volumeAbstract = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));
		this.volumeCopyrightNotice = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));
		this.appIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));
		this.recordDateTime = new Timestamp(getBytes(bytes, Timestamp.LENGTH));
		this.implIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));
		this.implUse = getBytes(bytes, 64);
		this.predecessorVDSeq = getUInt32(bytes);
		this.flags = getUInt16(bytes);

		currentPos = LENGTH; // set to end
	}
}
