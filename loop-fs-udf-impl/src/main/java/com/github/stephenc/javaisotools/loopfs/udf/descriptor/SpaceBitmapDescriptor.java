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
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Space Bitmap Descriptor (ECMA 167 4/14.12)<br>
 * A Space Bitmap descriptor specifies a bit for every logical block in the
 * partition
 */
public class SpaceBitmapDescriptor extends UDFDescriptor {

//	struct SpaceBitmap { /* ECMA 167 4/14.12 */
//		struct Tag DescriptorTag;
//		Uint32 NumberOfBits;
//		Uint32 NumberOfBytes;
//		byte Bitmap[];
//	}

	public Long numberOfBits;
	public Long numberOfBytes;
	public byte[] bitmap;

	// minimum length (excl bitmap)
	public final int MINIMUM_LENGTH = 24;

	public SpaceBitmapDescriptor() {
		super();
	}

	public SpaceBitmapDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_SPACE_BITMAP_DESCRIPTOR;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("Space Bitmap descriptor too short");
		}
		this.deserializeTag(bytes);

		this.numberOfBits = getUInt32(bytes);
		this.numberOfBytes = getUInt32(bytes);
		this.bitmap = getBytes(bytes, this.numberOfBytes.intValue());
	}
}
