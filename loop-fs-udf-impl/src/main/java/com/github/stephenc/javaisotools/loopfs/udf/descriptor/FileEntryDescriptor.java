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

import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;
import java.io.IOException;

import com.github.stephenc.javaisotools.loopfs.udf.Constants;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.UDFFileSystem;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.UDFDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileIdentifierDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ICBTag;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.ExtentAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.Timestamp;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.LongAD;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.RegId;


/**
 * The File Entry Descriptor (ECMA-167 4/14.9)
 */
public class FileEntryDescriptor extends UDFDescriptor
{
	public ICBTag icbTag;
	public Long uid;
	public Long gid;
	public Long permissions;
	public Integer fileLinks;
	public Integer recordFormat;
	public Integer recordDisplayAttrs;
	public Long recordLength;
	public BigInteger infoLength;
	public BigInteger lbRecorded;
	public Timestamp accessDT;
	public Timestamp modifiDT;
	public Timestamp attributeDT;
	public Long checkpoint;
	public LongAD extAttrICB;
	public RegId implIdentifier;
	public BigInteger uniqueId;
	public Long extAttrLength;
	public Long allocDescriptorLength;
	public byte[] extAttrs;
	public List<ExtentAD> allocDescriptors = new ArrayList<ExtentAD>();

	// This field is not one of these attributes in the specification.
	// This one is a shortcut used by the implementation.
	//
	// If the entry is not a directory, then the length of fids shall be 1.
	public List<FileIdentifierDescriptor> fids =
			new ArrayList<FileIdentifierDescriptor>();

	// length, beginning position, ending position of these fields above
	public final int LEN_ICB_TAG = 20;
	public final int LEN_UID = 4;
	public final int LEN_GID = 4;
	public final int LEN_PERM = 4;
	public final int LEN_F_LINK_CNT = 2;
	public final int LEN_RCD_FMT = 1;
	public final int LEN_RCD_DISP_ATTRS = 1;
	public final int LEN_RCD_LEN = 4;
	public final int LEN_INFO_LEN = 8;
	public final int LEN_LB_RECORDED = 8;
	public final int LEN_ACCESS_DT = 12;
	public final int LEN_MODIFI_DT = 12;
	public final int LEN_ATTR_DT = 12;
	public final int LEN_CHECKPOINT = 4;
	public final int LEN_EXT_ATTR_ICB = 16;
	public final int LEN_IMPL_ID = 32;
	public final int LEN_UNIQUE_ID = 8;
	public final int LEN_EXT_ATTR_LEN = 4;
	public final int LEN_ALLOC_DESC_LEN = 4;

	public final int BP_ICB_TAG = 16;
	public final int BP_UID = 36;
	public final int BP_GID = 40;
	public final int BP_PERM = 44;
	public final int BP_F_LINK_CNT = 48;
	public final int BP_RCD_FMT = 50;
	public final int BP_RCD_DISP_ATTRS = 51;
	public final int BP_RCD_LEN = 52;
	public final int BP_INFO_LEN = 56;
	public final int BP_LB_RECORDED = 64;
	public final int BP_ACCESS_DT = 72;
	public final int BP_MODIFI_DT = 84;
	public final int BP_ATTR_DT = 96;
	public final int BP_CHECKPOINT = 108;
	public final int BP_EXT_ATTR_ICB = 112;
	public final int BP_IMPL_ID = 128;
	public final int BP_UNIQUE_ID = 160;
	public final int BP_EXT_ATTR_LEN = 168;
	public final int BP_ALLOC_DESC_LEN = 172;
	public final int BP_EXT_ATTRS = 176;

	public final int EP_ICB_TAG = BP_ICB_TAG + LEN_ICB_TAG;
	public final int EP_UID = BP_UID + LEN_UID;
	public final int EP_GID = BP_GID + LEN_GID;
	public final int EP_PERM = BP_PERM + LEN_PERM;
	public final int EP_F_LINK_CNT = BP_F_LINK_CNT + LEN_F_LINK_CNT;
	public final int EP_RCD_FMT = BP_RCD_FMT + LEN_RCD_FMT;
	public final int EP_RCD_DISP_ATTRS = BP_RCD_DISP_ATTRS + LEN_RCD_DISP_ATTRS;
	public final int EP_RCD_LEN = BP_RCD_LEN + LEN_RCD_LEN;
	public final int EP_INFO_LEN = BP_INFO_LEN + LEN_INFO_LEN;
	public final int EP_LB_RECORDED = BP_LB_RECORDED + LEN_LB_RECORDED;
	public final int EP_ACCESS_DT = BP_ACCESS_DT + LEN_ACCESS_DT;
	public final int EP_MODIFI_DT = BP_MODIFI_DT + LEN_MODIFI_DT;
	public final int EP_ATTR_DT = BP_ATTR_DT + LEN_ATTR_DT;
	public final int EP_CHECKPOINT = BP_CHECKPOINT + LEN_CHECKPOINT;
	public final int EP_EXT_ATTR_ICB = BP_EXT_ATTR_ICB + LEN_EXT_ATTR_ICB;
	public final int EP_IMPL_ID = BP_IMPL_ID + LEN_IMPL_ID;
	public final int EP_UNIQUE_ID = BP_UNIQUE_ID + LEN_UNIQUE_ID;
	public final int EP_EXT_ATTR_LEN = BP_EXT_ATTR_LEN + LEN_EXT_ATTR_LEN;
	public final int EP_ALLOC_DESC_LEN = BP_ALLOC_DESC_LEN + LEN_ALLOC_DESC_LEN;

	// minimum length of a file entry descriptor (field "Reserved" included)
	public final int MINIMUM_LENGTH = 512;

	public FileEntryDescriptor() {
		super();
	}

	public FileEntryDescriptor(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
		this.verifyTagIdentifier();
	}

	@Override
	public int getExpectedTagIdentifier() {
		return Constants.D_TYPE_FILE_ENTRY;
	}

	@Override
	public void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < MINIMUM_LENGTH) {
			throw new InvalidDescriptor("File entry descriptor too short");
		}

		byte[] fragment;

		this.deserializeTag(bytes);

		fragment = UDFUtil.getBytes(bytes, BP_ICB_TAG, LEN_ICB_TAG);
		this.icbTag = new ICBTag(fragment);

		this.uid = UDFUtil.getUInt32(bytes, BP_UID);
		this.gid = UDFUtil.getUInt32(bytes, BP_GID);
		this.permissions = UDFUtil.getUInt32(bytes, BP_PERM);
		this.fileLinks = UDFUtil.getUInt16(bytes, BP_F_LINK_CNT);
		this.recordFormat = UDFUtil.getUInt8(bytes, BP_RCD_FMT);
		this.recordDisplayAttrs = UDFUtil.getUInt8(bytes, BP_RCD_DISP_ATTRS);
		this.recordLength = UDFUtil.getUInt32(bytes, BP_RCD_LEN);
		this.infoLength = UDFUtil.getUInt64(bytes, BP_INFO_LEN);
		this.lbRecorded = UDFUtil.getUInt64(bytes, BP_LB_RECORDED);

		fragment = UDFUtil.getBytes(bytes, BP_ACCESS_DT, LEN_ACCESS_DT);
		this.accessDT = new Timestamp(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_MODIFI_DT, LEN_MODIFI_DT);
		this.modifiDT = new Timestamp(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_ATTR_DT, LEN_ATTR_DT);
		this.attributeDT = new Timestamp(fragment);

		checkpoint = UDFUtil.getUInt32(bytes, BP_CHECKPOINT);

		fragment = UDFUtil.getBytes(bytes, BP_EXT_ATTR_ICB, LEN_EXT_ATTR_ICB);
		this.extAttrICB = new LongAD(fragment);

		fragment = UDFUtil.getBytes(bytes, BP_IMPL_ID, LEN_IMPL_ID);
		this.implIdentifier = new RegId(fragment);

		this.uniqueId = UDFUtil.getUInt64(bytes, BP_IMPL_ID);
		this.extAttrLength = UDFUtil.getUInt32(bytes, BP_EXT_ATTR_LEN);
		this.allocDescriptorLength = UDFUtil.getUInt32(bytes, BP_ALLOC_DESC_LEN);

		// According to OSTA-UDF 2.3.6 NOTE 1, the file entry descriptor
		// shall not exceed the size of one logical block.
		// So, this.extAttrLength must less than (2048 - BP_EXT_ATTRS) and it's
		// safe to convert it into an Integer, otherwise, it's invalid.
		Integer maxExtAttrLen = Constants.DEFAULT_BLOCK_SIZE - BP_EXT_ATTRS;
		if (this.extAttrLength > maxExtAttrLen) {
			throw new InvalidDescriptor("Extended attributes too long");
		}

		Integer intExtAttrLength = this.extAttrLength.intValue();
		this.extAttrs = UDFUtil.getBytes(bytes, BP_EXT_ATTRS, intExtAttrLength);

		// Same as extended attributes (OSTA-UDF 2.3.6 NOTE 1)
		Integer bpAllocDescriptors = BP_EXT_ATTRS + intExtAttrLength;
		Integer maxAllocLen = Constants.DEFAULT_BLOCK_SIZE - bpAllocDescriptors;
		if (this.allocDescriptorLength > maxAllocLen) {
			throw new InvalidDescriptor("Allocation descriptors too long");
		}

		Integer intAllocDescLen = this.allocDescriptorLength.intValue();
		byte[] bytesAllocDescriptors = UDFUtil.getBytes(
			bytes, bpAllocDescriptors, intAllocDescLen
		);

		// According to ECMA-167 4/14.9.22, there is a sequence of ADs
		int allocOffset = 0;
		while (allocOffset + 8 <= intAllocDescLen) {
			fragment = UDFUtil.getBytes(bytesAllocDescriptors, allocOffset, 8);
			this.allocDescriptors.add( new ExtentAD(fragment) );
			allocOffset += 8;
		}
	}

	/**
	 * Load FileIdentifierDescriptors (File Identifiers) in current ICB
	 *
	 * @param fs the UDFFileSystem object to read
	 */
	public void loadChildren(UDFFileSystem fs) throws IOException {
		byte[] buffer = new byte[Constants.DEFAULT_BLOCK_SIZE];
		FileIdentifierDescriptor fid;

		for (ExtentAD ead : this.allocDescriptors) {
			fs.readBlock(
				fs.getPDStartPos() + ead.location,
				buffer
			);

			int offset = 0;
			byte[] nextFragment = buffer;

			// The first FileIdentifierDescriptor belongs to the directory and
			// we should skip the fisrt FileIdentifierDescriptor.
			// (OSTA-UDF 2.3.4 NOTE-1)
			boolean first = true;

			while (true) {
				try {
					fid = new FileIdentifierDescriptor(nextFragment);
				} catch (InvalidDescriptor ex) {
					throw new IOException(ex.getMessage());
				}

				offset += fid.getConsumption();
				nextFragment = UDFUtil.getRemainingBytes(buffer, offset);

				if (first) {
					first = false;
					continue;
				} else {
					this.fids.add(fid);
				}

				// If nextFragment is another file identifier descriptor,
				// then it must starts with 0x0101 and contains 40 bytes at least
				if (
					nextFragment.length > 40 &&
					UDFUtil.getUInt16(nextFragment, 0) == 0x0101
				){
					// this looks much better than using logical not
					continue;
				} else {
					break;
				}
			}
		}
	}
}
