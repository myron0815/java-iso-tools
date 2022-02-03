/*
 * Copyright (c) 2019. Mr.Indescribable (https://github.com/Mr-indescribable).
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

package com.github.stephenc.javaisotools.loopfs.udf;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.stephenc.javaisotools.loopfs.udf.descriptor.AnchorDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.ExtendedFileEntryDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileEntryDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileIdentifierDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileSetDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.ImplementationUseDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.LogicalVolumeDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.LogicalVolumeIntegrityDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.PartitionDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.PrimaryVolumeDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.UnallocatedSpaceDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DescriptorTag;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * Tests the UDF implementation.
 */
public class UDFTest {

	private static Properties testProperties;
	private static String discPath;
	private static File discFile;

	@BeforeClass
	public static void loadConfiguration() throws Exception {
		testProperties = new Properties();
		InputStream is = null;
		is = UDFTest.class.getClassLoader().getResourceAsStream("test.properties");
		testProperties.load(is);
		is.close();

		discPath = testProperties.getProperty("source-image");
		discFile = new File(discPath);
	}

	@Test
	public void dumpSingleBlock() throws Exception {
		UDFFileSystem img = new UDFFileSystem(discFile, true);
		byte[] buffer = new byte[Constants.DEFAULT_BLOCK_SIZE];

		img.readBlock(257, buffer);

		int type = UDFUtil.getUInt16(buffer, 0);
		idToStr(type, buffer);
		img.close();
	}

	@Test
	public void dumpStruc() throws Exception {
		UDFFileSystem img = new UDFFileSystem(discFile, true);
		for (int i = 0; i <= img.maxBlock; i++) {
			byte[] buffer = new byte[Constants.DEFAULT_BLOCK_SIZE];
			img.readBlock(i, buffer);
			int type = UDFUtil.getUInt16(buffer, 0);
			if (DescriptorTag.D_TYPES.contains(type)) {
				System.out.println("Block: " + StringUtils.leftPad(String.valueOf(i), 5) + "     TagID: "
						+ StringUtils.leftPad(String.valueOf(type), 3) + "   " + idToStr(type, buffer));
			}
		}
		img.close();
	}

	// Helper for output, and for breakpoints :)
	private String idToStr(int id, byte[] buffer) throws InvalidDescriptor {
		switch (id) {
		case Constants.D_TYPE_PRIMARY_VOLUME:
			PrimaryVolumeDescriptor pvd = new PrimaryVolumeDescriptor(buffer);
			return "Primary Volume Descriptor";
		case Constants.D_TYPE_ANCHOR_POINTER:
			AnchorDescriptor avdp = new AnchorDescriptor(buffer);
			return "Anchor Pointer";
		case Constants.D_TYPE_VOLUME_DESCRIPTOR_POINTER:
			break;
		case Constants.D_TYPE_IMPL_USE:
			ImplementationUseDescriptor impl = new ImplementationUseDescriptor(buffer);
			return "Implementation Use Descriptor";
		case Constants.D_TYPE_PARTITION:
			PartitionDescriptor pd = new PartitionDescriptor(buffer);
			return "Partition Descriptor";
		case Constants.D_TYPE_LOGICAL_VOLUME:
			LogicalVolumeDescriptor lvd = new LogicalVolumeDescriptor(buffer);
			return "Logical Volume Descriptor";
		case Constants.D_TYPE_UNALLOCATED_SPACE:
			UnallocatedSpaceDescriptor un = new UnallocatedSpaceDescriptor(buffer);
			return "Unallocated Space Descriptor";
		case Constants.D_TYPE_TERMINATING:
			return "Terminator";
		case Constants.D_TYPE_LOGICAL_VOLUME_INTEGRITY:
			LogicalVolumeIntegrityDescriptor lvid = new LogicalVolumeIntegrityDescriptor(buffer);
			return "Logical Volume Integrity Descriptor";

		case Constants.D_TYPE_VIRTUAL_ALLOCATION_TABLE:
			break;
		case Constants.D_TYPE_REAL_TIME_FILE:
			break;
		case Constants.D_TYPE_METADATA_FILE:
			break;
		case Constants.D_TYPE_METADATA_FILE_MIRROR:
			break;
		case Constants.D_TYPE_METADATA_BITMAP_FILE:
			break;
		case Constants.D_TYPE_FILE_SET:
			FileSetDescriptor fsd = new FileSetDescriptor(buffer);
			return "File Set Descriptor";
		case Constants.D_TYPE_FILE_IDENTIFIER:
			FileIdentifierDescriptor fid = new FileIdentifierDescriptor(buffer);
			return "File Identifier Descriptor";
		case Constants.D_TYPE_ALLOCATION_EXTENT:
			break;
		case Constants.D_TYPE_INDIRECT_ENTRY:
			break;
		case Constants.D_TYPE_TERMINAL_ENTRY:
			break;
		case Constants.D_TYPE_FILE_ENTRY:
			FileEntryDescriptor fed = new FileEntryDescriptor(buffer);
			return ""; // 90% of all entries are that
		case Constants.D_TYPE_EXTENDED_ATTRIBUTE_HEADER:
			break;
		case Constants.D_TYPE_UNALLOCATED_SPACE_ENTRY:
			break;
		case Constants.D_TYPE_SPACE_BITMAP_DESCRIPTOR:
			break;
		case Constants.D_TYPE_PARTITION_INTEGRITY_ENTRY:
			break;
		case Constants.D_TYPE_EXTENDED_FILE_ENTRY:
			ExtendedFileEntryDescriptor efed = new ExtendedFileEntryDescriptor(buffer);
			return "Extended File Entry Descriptor";
		default:
			break;
		}
		return "";
	}

	@Test
	public void listFiles() throws Exception {
		UDFFileSystem img = new UDFFileSystem(discFile, true);
		for (UDFFileEntry fe : img) {
			listFiles(fe);
		}
		img.close();
	}

	private void listFiles(UDFFileEntry fe) throws Exception {
		if (fe.isDirectory()) {
			for (UDFFileEntry child : fe.getFiles()) {
				listFiles(child);
			}
		} else {
			System.out.println(fe.getPath());
		}
	}

	@Test
	public void smokes() throws Exception {
		UDFFileSystem img = new UDFFileSystem(discFile, true);
		runCheck(img);
		img.close();
	}

	/**
	 * File structure of the image for test:
	 *
	 * / └── a.txt (17733 bytes)
	 */
	private void runCheck(UDFFileSystem img) throws Exception {
		for (UDFFileEntry fe : img) {
			if (fe.isDirectory()) {
				Assert.assertEquals(fe.getName(), "/");
				Assert.assertEquals(fe.getPath(), "/");
			} else {
				InputStream inStream = img.getInputStream(fe);
				String content = IOUtils.toString(inStream, StandardCharsets.UTF_8.toString());

				Assert.assertEquals(fe.getPath(), File.separator + "a.txt");
				Assert.assertEquals(fe.getName(), "a.txt");
				Assert.assertEquals(content.length(), 17733);
			}
		}
	}
}
