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

package com.github.stephenc.javaisotools.loopfs.udf.descriptor.element;

import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The regid block (ECMA-167 1/7.4)
 *
 * Not implemented and not in use
 */
public class PartitionHeader {

//	struct PartitionHeaderDescriptor { /* ECMA 167 4/14.3 */
//		struct short_ad UnallocatedSpaceTable;
//		struct short_ad UnallocatedSpaceBitmap;
//		struct short_ad PartitionIntegrityTable;
//		struct short_ad FreedSpaceTable;
//		struct short_ad FreedSpaceBitmap;
//		byte Reserved[88];
//	}

  ExtentAD unallocatedSpaceTable;
  ExtentAD unallocatedSpaceBitmap;
  ExtentAD partitionIntegrityTable;
  ExtentAD freedSpaceTable;
  ExtentAD freedSpaceBitmap;

	public static final int LENGTH = 128;

	public PartitionHeader(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
	}

	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Partition header allocation descriptor too short");
		}

		this.unallocatedSpaceTable = new ExtentAD(UDFUtil.getBytes(bytes, 0, ExtentAD.LENGTH));
		this.unallocatedSpaceBitmap = new ExtentAD(UDFUtil.getBytes(bytes, 8, ExtentAD.LENGTH));
		this.partitionIntegrityTable = new ExtentAD(UDFUtil.getBytes(bytes, 16, ExtentAD.LENGTH));
		this.freedSpaceTable = new ExtentAD(UDFUtil.getBytes(bytes, 24, ExtentAD.LENGTH));
		this.freedSpaceBitmap = new ExtentAD(UDFUtil.getBytes(bytes, 32, ExtentAD.LENGTH));
	}
}
