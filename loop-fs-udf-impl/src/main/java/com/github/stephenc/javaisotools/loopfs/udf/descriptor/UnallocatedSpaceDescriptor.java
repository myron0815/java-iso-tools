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

import java.util.ArrayList;
import java.util.List;

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Unallocated SpaceDescriptor (ECMA-167 3/10.8)
 */
public class UnallocatedSpaceDescriptor extends UDFDescriptor {

//	struct UnallocatedSpaceDesc { /* ECMA 167 3/10.8 */
//		struct tag DescriptorTag;
//		Uint32 VolumeDescriptorSequenceNumber;
//		Uint32 NumberofAllocationDescriptors;
//		extent_ad AllocationDescriptors[];
//	}

	public Long volumeDescriptorSequenceNumber;
	public Long numberofAllocationDescriptors;
	public List<ExtentAD> allocationDescriptors;

	// minimum length of an unallocated space descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

	public UnallocatedSpaceDescriptor() {
		super();
	}

	public UnallocatedSpaceDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_UNALLOCATED_SPACE;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("unallocated space descriptor too short");
		}
		this.deserializeTag(bytes);

		this.volumeDescriptorSequenceNumber = getUInt32(bytes);
		this.numberofAllocationDescriptors = getUInt32(bytes);

		this.allocationDescriptors = new ArrayList<ExtentAD>();
		int allocCnt = this.numberofAllocationDescriptors.intValue();
		while (allocCnt > 0) {
			ExtentAD extend = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));
			if (extend.length > 0) {
				this.allocationDescriptors.add(extend);
			}
			allocCnt--;
		}
	}
}
