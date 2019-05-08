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


/**
 * The Anchor Volume Pointer Descriptor (ECMA-167 3/10.2)
 */
public class AnchorDescriptor extends UDFDescriptor
{
	public ExtentAD mainVolumeExtent;
	public ExtentAD reserveVolumeExtent;

	// length, beginning position, ending position of these fields above
	public final int LEN_MV_EXTENT = 8;
	public final int LEN_RV_EXTENT = 8;
	public final int LEN_RESERVED = 480;

	public final int BP_MV_EXTENT = 16;
	public final int BP_RV_EXTENT = 24;
	public final int BP_RESERVED = 32;

	public final int EP_MV_EXTENT = BP_MV_EXTENT + LEN_MV_EXTENT;
	public final int EP_RV_EXTENT = BP_RV_EXTENT + LEN_RV_EXTENT;

	// minimum length of an anchor descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

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
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("Anchor descriptor too short");
		}

		this.deserializeTag(bytes);

		byte[] mvBytes = UDFUtil.getBytes(bytes, BP_MV_EXTENT, LEN_MV_EXTENT);
		byte[] rvBytes = UDFUtil.getBytes(bytes, BP_RV_EXTENT, LEN_RV_EXTENT);

		this.mainVolumeExtent = new ExtentAD(mvBytes);
		this.reserveVolumeExtent = new ExtentAD(rvBytes);
	}
}
