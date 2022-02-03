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
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.CharSpec;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DString;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LongAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.PartitionMap;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.PartitionMapMetadata;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.PartitionMapSparable;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.PartitionMapType1;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.PartitionMapType2;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The Logical Volume Descriptor (ECMA-167 3/10.6)
 */
public class LogicalVolumeDescriptor extends UDFDescriptor {

//	struct LogicalVolumeDescriptor { /* ECMA 167 3/10.6 */
//		struct tag DescriptorTag;
//		Uint32 VolumeDescriptorSequenceNumber;
//		struct charspec DescriptorCharacterSet;
//		dstring LogicalVolumeIdentifier[128];
//		Uint32 LogicalBlockSize,
//		struct EntityID DomainIdentifier;
//		byte LogicalVolumeContentsUse[16];
//		Uint32 MapTableLength;
//		Uint32 NumberofPartitionMaps;
//		struct EntityID ImplementationIdentifier;
//		byte ImplementationUse[128];
//		extent_ad IntegritySequenceExtent,
//		byte PartitionMaps[];
//	}

	public Long volumeDescriptorSequenceNumber;
	/**
	 * Interpreted as specifying the character set allowed in the
	 * LogicalVolumeIdentifier field. <br>
	 * <br>
	 * Shall be set to indicate support for CS0 as defined in 2.1.2
	 */
	public CharSpec descriptorCharacterSet;
	public DString logicalVolumeIdentifier;
	/**
	 * Interpreted as specifying the Logical Block Size for the logical volume
	 * identified by this Logical Volume Descriptor.<br>
	 * <br>
	 * This field shall be set to the largest logical sector size encountered
	 * amongst all the partitions on media that constitute the logical volume
	 * identified by this Logical Volume Descriptor. Since UDF requires that all
	 * Volumes within a Volume Set have the same logical sector size, the Logical
	 * Block Size will be the same as the logical sector size of the Volume.
	 */
	public Long logicalBlockSize;
	/**
	 * Interpreted as specifying a domain specifying rules on the use of, and
	 * restrictions on, certain fields in the descriptors. If this field is all zero
	 * then it is ignored, otherwise the Entity Identifier rules are followed. <br>
	 * <br>
	 * NOTE 1: If the field does not contain “*OSTA UDF Compliant” then an
	 * implementation may deny the user access to the logical volume.<br>
	 * <br>
	 * This field shall indicate that the contents of this logical volume conforms
	 * to the domain defined in this document, therefore the Domain Identifier ID
	 * value shall be set to: "*OSTA UDF Compliant" As described in the section on
	 * Entity Identifier the Identifier Suffix field of this EntityID shall contain
	 * the revision of this document for which the contents of the Logical Volume is
	 * compatible. For more information on the proper handling of this field see
	 * section 2.1.5. <br>
	 * <br>
	 * NOTE 2: The Identifier Suffix field of this EntityID contains
	 * SoftWriteProtect and HardWriteProtect flags. Refer to 2.1.5.3.
	 */
	public RegId domainIdentifier;
	/**
	 * This field contains the extent location of the File Set Descriptor. This is
	 * described in 4/3.1 of ECMA 167 as follows:<br>
	 * <br>
	 * “If the volume is recorded according to Part 3, th e extent in which the
	 * first File Set Descriptor Sequence of the logical volume is recorded shall be
	 * identified by a long_ad (4/14.14.2) recorded in the Logical Volume Contents
	 * Us e field (see 3/10.6.7) of the Logical Volume Descriptor describing the
	 * logical volume in which the File Set Descriptors are recorded.”<br>
	 * <br>
	 * This field can be used to find the File Set Descriptor, and from the File Set
	 * Descriptor the root directory can be found.
	 * 
	 */
	public LongAD logicalVolumeContentsUse;
	/**
	 * this field shall specify the length of the Partition Maps field in bytes
	 */
	public Long mapTableLength;
	/**
	 * This field shall specify the number of Partition Maps recorded in the Partition Maps field
	 */
	public Long numberofPartitionMaps;
	/**
	 * For more information on the proper handling of this field see section 2.1.5.
	 */
	public RegId implementationIdentifier;
	public byte[] implementationUse;
	/**
	 * A value in this field is required for the Logical Volume Integrity
	 * Descriptor. For Rewriteable or Overwriteable media this shall be set to a
	 * minimum of 8K bytes.<br>
	 * <br>
	 * <b>WARNING:</b> For WORM media this field should be set to an extent of some
	 * substantial length. Once the WORM volume on which the Logical Volume
	 * Integrity Descriptor resides is full a new volume must be added to the volume
	 * set since the Logical Volume Integrity Descriptor must reside on the same
	 * volume as the prevailing Logical Volume Descriptor.
	 */
	public ExtentAD integritySequenceExtent;
	/**
	 * For the purpose of interchange, Partition Maps shall be limited to Partition
	 * Map type 1, except type 2 maps as described in this document (2.2.8, 2.2.9
	 * and 2.2.10)
	 */
	public List<PartitionMap> partitionMaps;

	// minimum length of a logical volume descriptor
	public final int MINIMUM_LENGTH = 512;

	public LogicalVolumeDescriptor() {
		super();
	}

	public LogicalVolumeDescriptor(byte[] bytes) throws InvalidDescriptor {
		super(bytes);
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_LOGICAL_VOLUME;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("Logical volume descriptor too short");
		}
		this.deserializeTag(bytes);

		this.volumeDescriptorSequenceNumber = getUInt32(bytes);
		this.descriptorCharacterSet = new CharSpec(getBytes(bytes, CharSpec.LENGTH));
		this.logicalVolumeIdentifier = new DString(getBytes(bytes, 128));
		this.logicalBlockSize = getUInt32(bytes);
		this.domainIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));
		this.logicalVolumeContentsUse = new LongAD(getBytes(bytes, LongAD.LENGTH));
		this.mapTableLength = getUInt32(bytes); // bytes
		this.numberofPartitionMaps = getUInt32(bytes); // number
		this.implementationIdentifier = new RegId(getBytes(bytes, RegId.LENGTH));
		this.implementationUse = getBytes(bytes, 128);
		this.integritySequenceExtent = new ExtentAD(getBytes(bytes, ExtentAD.LENGTH));

		this.partitionMaps = new ArrayList<PartitionMap>();
		
		for (int i = 0; i < numberofPartitionMaps; i++) {
			int type = bytes[currentPos];
			if (type == 1) {
				PartitionMapType1 pm1 = new PartitionMapType1(getBytes(bytes, PartitionMapType1.LENGTH));
				partitionMaps.add(pm1);
			} else if (type == 2) {
				byte[] frag = getBytes(bytes, PartitionMapType2.LENGTH);
				PartitionMapType2 pm2 = new PartitionMapType2(frag);
				if (pm2.partitionTypeIdentifier.getId().equals("*UDF Sparable Partition")) {
					partitionMaps.add(new PartitionMapSparable(frag));
				} else if (pm2.partitionTypeIdentifier.getId().equals("*UDF Metadata Partition")) {
					partitionMaps.add(new PartitionMapMetadata(frag));
				}
			} else {
				new InvalidDescriptor("Unknown partition map type " + type);
			}
		}
	}
}
