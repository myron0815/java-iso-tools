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
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Implementation Use Descriptor (ECMA-167 3/10.4)
 */
public class ImplementationUseDescriptor extends UDFDescriptor {

//	struct ImpUseVolumeDescriptor { /* ECMA 167 3/10.4 */
//		struct tag DescriptorTag;
//		Uint32 VolumeDescriptorSequenceNumber;
//		struct EntityID ImplementationIdentifier;
//		byte ImplementationUse[460];
//	}

	public long volumeDescriptorSequenceNumber;
	public RegId implementationIdentifier;

//	struct LVInformation {
//		struct charspec LVICharset,
//		dstring LogicalVolumeIdentifier[128],
//		dstring LVInfo1[36],
//		dstring LVInfo2[36],
//		dstring LVInfo3[36],
//		struct EntityID ImplementationID,
//		bytes ImplementationUse[128];
//	}

	public CharSpec LVICharset;
	public DString logicalVolumeIdentifier;
	public DString LVInfo1;
	public DString LVInfo2;
	public DString LVInfo3;
	public RegId implementationID;
	public byte[] implementationUse;

	// minimum length of an Implementation use descriptor (field "Reserved"
	// included)
	public static final int LENGTH = 512;

	public ImplementationUseDescriptor() {
		super();
	}

	public ImplementationUseDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_IMPL_USE;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Anchor descriptor too short");
		}
		this.deserializeTag(bytes);

		volumeDescriptorSequenceNumber = getUInt32(bytes);
		implementationIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));

		LVICharset = new CharSpec(getBytes(bytes, CharSpec.LENGTH));
		logicalVolumeIdentifier = new DString(getBytes(bytes, 128));
		LVInfo1 = new DString(getBytes(bytes, 36));
		LVInfo2 = new DString(getBytes(bytes, 36));
		LVInfo3 = new DString(getBytes(bytes, 36));
		implementationID = new RegId(getBytes(bytes, RegId.LENGTH));
		implementationUse = getBytes(bytes, 128);
	}
}
