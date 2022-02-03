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
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.Timestamp;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The LogicalVolumeIntegrityDesc block (ECMA-167 1/7.4)
 *
 * Not implemented and not in use
 */
public class LogicalVolumeIntegrityDescriptor extends UDFDescriptor {

//	struct LogicalVolumeIntegrityDesc { /* ECMA 167 3/10.10 */
//		struct tag DescriptorTag,
//		Timestamp RecordingDateAndTime,
//		Uint32 IntegrityType,
//		struct extent_ad NextIntegrityExtent,
//		byte LogicalVolumeContentsUse[32],
//		Uint32 NumberOfPartitions,
//		Uint32 LengthOfImplementationUse, /* = L_IU */
//		Uint32 FreeSpaceTable[],
//		Uint32 SizeTable[],
//		byte ImplementationUse[]
//	}

	Timestamp recordingDateAndTime;
	Long integrityType;
	ExtentAD nextIntegrityExtent;
	byte[] logicalVolumeContentsUse;
	Long numberOfPartitions;
	Long lengthOfImplementationUse;
	byte[] freeSpaceTable;
	byte[] sizeTable;

//	ImplementationUse format 2.2.6.4
//	RBP Length Name Contents
//	0 32 ImplementationID EntityID
//	32 4 Number of Files Uint32
//	36 4 Number of Directories Uint32
//	40 2 Minimum UDF Read Revision Uint16
//	42 2 Minimum UDF Write Revision Uint16
//	44 2 Maximum UDF Write Revision Uint16
//	46 L_IU - 46 Implementation Use byte

	RegId implementationId;
	Long numberOfFiles;
	Long numberOfDirectories;
	Integer minReadVersion;
	Integer minWriteVersion;
	Integer maxWriteersion;
	byte[] implementationUse;

	public static final int MINUMUM_LENGTH = 80;

	public LogicalVolumeIntegrityDescriptor() {
		super();
	}

	public LogicalVolumeIntegrityDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_LOGICAL_VOLUME_INTEGRITY;
	}

	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINUMUM_LENGTH) {
			throw new InvalidDescriptor("LogicalVolumeIntegrityDescriptor descriptor too short");
		}
		this.deserializeTag(bytes);

		recordingDateAndTime = new Timestamp(getBytes(bytes, Timestamp.LENGTH));
		integrityType = getUInt32(bytes);
		nextIntegrityExtent = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));
		logicalVolumeContentsUse = getBytes(bytes, 32);
		numberOfPartitions = getUInt32(bytes);
		lengthOfImplementationUse = getUInt32(bytes);

		freeSpaceTable = getBytes(bytes, numberOfPartitions.intValue() * 4);
		sizeTable = getBytes(bytes, numberOfPartitions.intValue() * 4);

		// LVID impl use [46]
		implementationId = new RegId(getBytes(bytes, RegId.LENGTH));
		numberOfFiles = getUInt32(bytes);
		numberOfDirectories = getUInt32(bytes);
		minReadVersion = getUInt16(bytes);
		minWriteVersion = getUInt16(bytes);
		maxWriteersion = getUInt16(bytes);
		// or, if longer, read further another impl use
		implementationUse = getBytes(bytes, lengthOfImplementationUse.intValue() - 46);
	}
}
