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
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Anchor Volume Pointer Descriptor (ECMA-167 3/10.2)
 */
public class AnchorDescriptor extends UDFDescriptor {

//	struct AnchorVolumeDescriptorPointer { /* ECMA 167 3/10.2 */
//		struct tag DescriptorTag;
//		struct extent_ad MainVolumeDescriptorSequenceExtent;
//		struct extent_ad ReserveVolumeDescriptorSequenceExtent;
//		byte Reserved[480];
//	}

	public ExtentAD mainVolumeExtent;
	public ExtentAD reserveVolumeExtent;

	// minimum length of an anchor descriptor (field "Reserved" included)
	public static final int LENGTH = 512;

	public AnchorDescriptor() {
		super();
	}

	public AnchorDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_ANCHOR_POINTER;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Anchor descriptor too short");
		}
		this.deserializeTag(bytes);

		this.mainVolumeExtent = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));
		this.reserveVolumeExtent = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));

		currentPos = LENGTH; // set to end
	}

	@Override
	public String toString() {
		return "AnchorDescriptor [mainVolumeExtent=" + mainVolumeExtent + ", reserveVolumeExtent=" + reserveVolumeExtent
				+ "]";
	}
}
