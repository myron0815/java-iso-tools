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

package com.github.stephenc.javaisotools.loopfs.udf;

import java.io.IOException;
import java.io.InputStream;

import com.github.stephenc.javaisotools.loopfs.udf.UDFFileEntry;
import com.github.stephenc.javaisotools.loopfs.udf.UDFFileSystem;


/**
 * InputStream that reads a UDFFileEntry's data.
 *
 * Copied 95% of code from loopfs.iso9660.EntryInputStream
 */
class UDFEntryInputStream extends InputStream {

	// entry within the file system
	private UDFFileEntry entry;

	// the parent file system
	private UDFFileSystem fs;

	// current position within entry data
	private long pos;

	// number of remaining bytes within entry
	private long rem;

	public UDFEntryInputStream(final UDFFileEntry entry, final UDFFileSystem fs){
		this.fs = fs;
		this.entry = entry;
		this.pos = 0L;
		this.rem = this.entry.getSize();
	}

	public int read(
		final byte b[],
		final int off,
		final int len
	) throws IOException {
		ensureOpen();

		if (this.rem <= 0) {
			return -1;
		}
		if (len <= 0) {
			return 0;
		}

		int toRead = len;

		if (toRead > this.rem) {
			toRead = (int) this.rem;
		}

		int read;

		synchronized (this.fs) {
			if (this.fs.isClosed()) {
				throw new IOException("ISO file closed.");
			}

			read = this.fs.readFileContent(this.entry, this.pos, b, off, toRead);
		}

		if (read > 0) {
			this.pos += read;
			this.rem -= read;
		}

		return read;
	}

	public int read() throws IOException {
		ensureOpen();

		final byte[] b = new byte[1];

		if (read(b, 0, 1) == 1) {
			return b[0] & 0xff;
		} else {
			return -1;
		}
	}

	public long skip(final long n) {
		ensureOpen();

		final int len = (n > this.rem) ? (int) this.rem : (int) n;

		this.pos += len;
		this.rem -= len;

		if (this.rem <= 0) {
			close();
		}

		return len;
	}

	public int available() {
		return Math.max((int)this.rem, 0);
	}

	public long size() {
		ensureOpen();

		return this.entry.getSize();
	}

	public void close() {
		this.rem = 0;
		this.entry = null;
		this.fs = null;
	}

	private void ensureOpen() {
		if (null == this.entry) {
			throw new IllegalStateException("stream has been closed");
		}
	}
}
