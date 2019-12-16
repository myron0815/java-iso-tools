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

package com.github.stephenc.javaisotools.loopfs.iso9660;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Iterator;

import com.github.stephenc.javaisotools.loopfs.spi.AbstractBlockFileSystem;
import com.github.stephenc.javaisotools.loopfs.spi.SeekableInput;
import com.github.stephenc.javaisotools.loopfs.spi.SeekableInputFile;
import com.github.stephenc.javaisotools.loopfs.spi.VolumeDescriptorSet;
import com.github.stephenc.javaisotools.loopfs.iso9660.susp.SuspField;
import com.github.stephenc.javaisotools.loopfs.iso9660.susp.SuspFieldSP;

public class Iso9660FileSystem extends AbstractBlockFileSystem<Iso9660FileEntry> {

    // Whether we need to read the System Use field
    private boolean enableSUSPScan;

    // A result cache of this.suspUsed method
    private Boolean suspUsedCache = null;

    public Iso9660FileSystem(File file, boolean readOnly) throws IOException {
        this(new SeekableInputFile(file), readOnly, false);
    }

    public Iso9660FileSystem(File file, boolean readOnly, boolean enableSUSPScan) throws IOException {
        this(new SeekableInputFile(file), readOnly, enableSUSPScan);
    }

    public Iso9660FileSystem(SeekableInput seekable, boolean readOnly) throws IOException {
        this(seekable, readOnly, false);
    }

    public Iso9660FileSystem(
            SeekableInput seekable,
            boolean readOnly,
            boolean enableSUSPScan
    ) throws IOException {
        super(seekable, readOnly, Constants.DEFAULT_BLOCK_SIZE, Constants.RESERVED_SECTORS);

        this.enableSUSPScan = enableSUSPScan;
    }

    public String getEncoding() {
        return ((Iso9660VolumeDescriptorSet) getVolumeDescriptorSet()).getEncoding();
    }

    public InputStream getInputStream(Iso9660FileEntry entry) {
        ensureOpen();
        return new EntryInputStream(entry, this);
    }

    byte[] getBytes(Iso9660FileEntry entry) throws IOException {
        int size = (int) entry.getSize();

        byte[] buf = new byte[size];

        readBytes(entry, 0, buf, 0, size);

        return buf;
    }

    public int readBytes(Iso9660FileEntry entry, long entryOffset, byte[] buffer, int bufferOffset, int len)
            throws IOException {
        long startPos = (entry.getStartBlock() * Constants.DEFAULT_BLOCK_SIZE) + entryOffset;
        return readData(startPos, buffer, bufferOffset, len);
    }

    protected Iterator<Iso9660FileEntry> iterator(Iso9660FileEntry _) {
        // We must choose the root entry inside this method.
        // Otherwise, we cannot ensure which root entry it is.
        Iso9660VolumeDescriptorSet descSet =
                (Iso9660VolumeDescriptorSet) getVolumeDescriptorSet();

        Iso9660FileEntry rootEntry = descSet.getPrimaryRootEntry();

        if (this.suspUsed()) {
            return new EntryIterator(this, rootEntry);
        } else {
            if (descSet.hasSupplementary()) {
                Iso9660FileEntry supRootEntry = descSet.getSupRootEntry();
                return new EntryIterator(this, supRootEntry);
            } else {
                return new EntryIterator(this, rootEntry);
            }
        }

    }

    protected VolumeDescriptorSet<Iso9660FileEntry> createVolumeDescriptorSet() {
        return new Iso9660VolumeDescriptorSet(this);
    }

    /**
     * Determines whether the current disc has used SUSP
     */
    private boolean suspEnabled() {
        Iso9660VolumeDescriptorSet descSet =
                (Iso9660VolumeDescriptorSet) getVolumeDescriptorSet();
        Iso9660FileEntry root = descSet.getPrimaryRootEntry();

        if (root == null) {
            throw new RuntimeException("Root entry has not been loaded yet");
        }

        List<SuspField> suspFields = root.getSuspFields();
        if (suspFields.toArray().length < 1) {
            return false;
        }

        SuspField firstField = suspFields.get(0);
        if (firstField.getId() != Constants.SU_FIELD_ID_SP){
            return false;
        }

        SuspFieldSP spField = (SuspFieldSP) firstField;
        return spField.suspEnabled();
    }

    /**
     * Wraps suspEnabled method and caches the result
     */
    public Boolean suspUsed() {
        if (! this.enableSUSPScan) {
            return false;
        }

        if (this.suspUsedCache != null) {
            return this.suspUsedCache;
        }

        this.suspUsedCache = this.suspEnabled();
        return this.suspUsedCache;
    }
}
