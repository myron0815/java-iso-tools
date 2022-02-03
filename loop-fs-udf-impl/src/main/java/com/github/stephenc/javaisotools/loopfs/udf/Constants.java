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

package com.github.stephenc.javaisotools.loopfs.udf;

public interface Constants {

	/**
	 * ISO sector size.
	 * This does not include the 288 bytes reserved for synchronization, header,
	 * and EC on CD-ROMs because this information is not used in .iso files.
	 */
	int DEFAULT_BLOCK_SIZE = 2 * 1024;

	/**
	 * The number of reserved sectors at the beginning of the file.
	 *
	 * This position is for volume structure descriptors. But in UDF,
	 * We cannot find the root entry of the file system from here.
	 * So, blocks in this position are used in verification only.
	 */
	int RESERVED_SECTORS = 16;

	/**
	 * The number of reserved bytes at the beginning of the file.
	 */
	int RESERVED_BYTES = RESERVED_SECTORS * DEFAULT_BLOCK_SIZE;

	/**
	 * Default character encoding.
	 */
	String DEFAULT_ENCODING = "US-ASCII";

	/**
	 * The position of anchor sector.
	 */
	int ANCHOR_SECTOR_NUMBER = 256;

	/**
	 * The divisor used in the modulo operation to calculate
	 * the length of padding in file identifier descriptor.
	 */
	int FID_PADDING_DIVISOR = 4;

	/**
	 * Descriptor types (ECMA-167 3/7.2 and 4/7.2.1)
	 *
	 * Though ECMA-167 classified these descriptors, but we can still put
	 * them in a single category, there is no confliction between them.
	 */
	int D_TYPE_PRIMARY_VOLUME = 1;
	int D_TYPE_ANCHOR_POINTER = 2;
	int D_TYPE_VOLUME_DESCRIPTOR_POINTER = 3;
	int D_TYPE_IMPL_USE = 4;
	int D_TYPE_PARTITION = 5;
	int D_TYPE_LOGICAL_VOLUME = 6;
	int D_TYPE_UNALLOCATED_SPACE = 7;
	int D_TYPE_TERMINATING = 8;
	int D_TYPE_LOGICAL_VOLUME_INTEGRITY = 9;
	
	int D_TYPE_VIRTUAL_ALLOCATION_TABLE = 248;
	int D_TYPE_REAL_TIME_FILE = 249;
	int D_TYPE_METADATA_FILE = 250;
	int D_TYPE_METADATA_FILE_MIRROR = 251;
	int D_TYPE_METADATA_BITMAP_FILE = 252;
	// 253 shall not be used
	// 254 shall not be used
	// 255 shall not be used
	int D_TYPE_FILE_SET = 256;
	int D_TYPE_FILE_IDENTIFIER = 257;
	int D_TYPE_ALLOCATION_EXTENT = 258;
	int D_TYPE_INDIRECT_ENTRY = 259;
	int D_TYPE_TERMINAL_ENTRY = 260;
	int D_TYPE_FILE_ENTRY = 261;
	int D_TYPE_EXTENDED_ATTRIBUTE_HEADER = 262;
	int D_TYPE_UNALLOCATED_SPACE_ENTRY = 263;
	int D_TYPE_SPACE_BITMAP_DESCRIPTOR = 264;
	int D_TYPE_PARTITION_INTEGRITY_ENTRY = 265;
	int D_TYPE_EXTENDED_FILE_ENTRY = 266;

	/**
	 * File entry types in ICB Tag (ECMA-167 4/14.6.6)
	 */
	int FE_TYPE_NOT_SPECIFIED = 0;
	int FE_TYPE_UNALLOCATED_SPACE = 1;
	int FE_TYPE_PARTITION_INTEGRITY = 2;
	int FE_TYPE_INDIRECT = 3;
	int FE_TYPE_DIRECTORY = 4;
	int FE_TYPE_FILE_BLOCKS = 5;
	int FE_TYPE_BLOCK_DEVICE = 6;
	int FE_TYPE_CHAR_DEVICE = 7;
	int FE_TYPE_EXT_ATTRS = 8;
	int FE_TYPE_FIFO = 9;
	int FE_TYPE_C_ISSOCK = 10;
	int FE_TYPE_TERMINAL = 11;
	int FE_TYPE_LINK = 12;
	int FE_TYPE_STREAM_DIR = 13;
}
