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

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.stephenc.javaisotools.loopfs.udf.UDFFileEntry;
import com.github.stephenc.javaisotools.loopfs.udf.UDFFileSystem;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileEntryDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileIdentifierDescriptor;


/**
 * Iterator for UDFFileSystem, breadth-first as well.
 */
public class UDFEntryIterator implements Iterator<UDFFileEntry>
{
	private final UDFFileSystem fs;
	private final List<UDFFileEntry> queue;

	public UDFEntryIterator(final UDFFileSystem fs, final UDFFileEntry rootEntry) {
		this.fs = fs;
		this.queue = new LinkedList<>();
		if (rootEntry != null) {
			this.queue.add(rootEntry);
		}
	}

	public boolean hasNext() {
		return !this.queue.isEmpty();
	}

	public UDFFileEntry next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		UDFFileEntry entry = this.queue.remove(0);

		if ( entry.isDirectory() ) {
			try {
				entry.loadFiles();
				for ( UDFFileEntry fe : entry.getFiles() ) {
					this.queue.add(fe);
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		return entry;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
