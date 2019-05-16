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

package com.github.stephenc.javaisotools.loopfs.iso9660.susp;

import java.io.IOException;

import com.github.stephenc.javaisotools.loopfs.iso9660.Util;
import com.github.stephenc.javaisotools.loopfs.iso9660.susp.exceptions.InvalidSuspField;
import com.github.stephenc.javaisotools.loopfs.iso9660.susp.exceptions.UnknownSuspFieldId;


public abstract class SuspField
{
	/**
	 * According to SUSP 4.1, we have 4 fields with the same meanning
	 * in all System Use fields.
	 *
	 * And the first two fields have been merged into one single field "id".
	 *
	 * +----------+----------+------------+-------------+
	 * |   SIG1   |   SIG2   |   LENGTH   |   VERSION   |
	 * |   BP1    |   BP2    |    BP3     |     BP4     |
	 * +----------+----------+------------+-------------+
	 * |         id          |   length   |   version   |
	 * +---------------------+------------+-------------+
	 */
	public int id;
	public int length;
	public int version;

	/**
	 * Extracts data from a given block of bytes.
	 *
	 * @param block bytes to deserialize
	 * @param bp position of first byte of the SU block
	 *
	 * @throws InvalidSuspField
	 */
	public abstract void deserialize(byte[] block, int bp) throws InvalidSuspField;

	/**
	 * Returns the expected value of the id of current SUSP field
	 */
	public abstract int getExpectedId();

	/**
	 * Deserializes the first 4 bytes in the block.
	 */
	public void deserializeHeader(byte[] block, int bp) throws InvalidSuspField {
		if (block.length < 4) {
			throw new InvalidSuspField("SUSP field shall contain at least 4 bytes");
		}

		this.id = Util.getUInt16BE(block, bp);
		this.length = Util.getUInt8(block, bp + 2);
		this.version = Util.getUInt8(block, bp + 3);
	}

	/**
	 * Checks if the id matchs the current SUSP field class
	 */
	public void verifyId() throws UnknownSuspFieldId {
		int id = this.getId();
		int expected = this.getExpectedId();

		if (id != expected) {
			String hexId = Integer.toHexString(id).toUpperCase();
			throw new UnknownSuspFieldId("Unexpected SUSP field id: 0x" + hexId);
		}
	}

	public int getId() {
		return this.id;
	}

	public int getLength() {
		return this.length;
	}

	public int getVersion() {
		return this.version;
	}

	public static int extractFieldId(byte[] block, int bp) {
		return Util.getUInt16BE(block, bp);
	}
}
