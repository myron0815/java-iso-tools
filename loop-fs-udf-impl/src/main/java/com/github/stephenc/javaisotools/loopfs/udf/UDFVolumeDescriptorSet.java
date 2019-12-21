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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.stephenc.javaisotools.loopfs.api.FileEntry;
import com.github.stephenc.javaisotools.loopfs.spi.VolumeDescriptorSet;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.ExtendedFileEntryDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileEntryDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.FileSetDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.PartitionDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.PrimaryVolumeDescriptor;
import com.github.stephenc.javaisotools.loopfs.udf.descriptor.element.DescriptorTag;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

public class UDFVolumeDescriptorSet implements VolumeDescriptorSet {
  private static final Log       log = LogFactory.getLog(UDFVolumeDescriptorSet.class);

  private UDFFileSystem          fs;

  public PrimaryVolumeDescriptor pvd;
  public PartitionDescriptor     pd;
  public FileSetDescriptor       rootFSD;
  public FileEntryDescriptor     rootEntry;

  public UDFVolumeDescriptorSet(UDFFileSystem fs) {
    this.fs = fs;
  }

  public boolean deserialize(byte[] bytes) throws IOException {
    Integer type = UDFUtil.getUInt16(bytes, 0);
    boolean terminated = false;

    try {
      switch (type) {
        case Constants.D_TYPE_PRIMARY_VOLUME:
          this.pvd = new PrimaryVolumeDescriptor(bytes);
          log.debug("Found primary volume descriptor.");
          break;
        case Constants.D_TYPE_IMPL_USE:
          log.debug("Found implementation use descriptor.");
          break;
        case Constants.D_TYPE_PARTITION:
          this.pd = new PartitionDescriptor(bytes);
          log.debug("Found partition descriptor.");

          this.tracePartition();
          break;
        case Constants.D_TYPE_LOGICAL_VOLUME:
          log.debug("Found logical volume descriptor.");
          break;
        case Constants.D_TYPE_UNALLOCATED_SPACE:
          log.debug("Found unallocated space descriptor.");
          break;
        case Constants.D_TYPE_TERMINATING:
          terminated = true;
          log.debug("Found terminating descriptor.");
          break;
        default:
          log.debug("Found unknown volume descriptor with type: " + type);
      }
    }
    catch (InvalidDescriptor ex) {
      throw new IOException(ex.getMessage());
    }

    return terminated;
  }

  /**
   * Read file set descriptors at the beginning of a partition.
   *
   * Currently, this method reads the first file set descriptor only.
   */
  private void tracePartition() throws IOException, InvalidDescriptor {
    byte[] buffer = new byte[Constants.DEFAULT_BLOCK_SIZE];
    DescriptorTag tag;

    // seek the first fsd
    int pos = 0;
    while (this.fs.readBlock(this.pd.startingLocation + pos, buffer)) {
      try {
        tag = new DescriptorTag(buffer);
        if (tag.identifier == Constants.D_TYPE_FILE_SET) {
          System.out.println("found at " + pos);
          break;
        }
      }
      catch (Exception e) {
      }
      pos++;
    }
    this.rootFSD = new FileSetDescriptor(buffer);

    // adopt partition start pos
    this.pd.startingLocation += pos;

    // find the first file entry
    this.fs.readBlock(this.getPDStartPos() + this.rootFSD.rootICB.location.blockNumber, buffer);

    tag = new DescriptorTag(buffer);
    if (tag.identifier == Constants.D_TYPE_FILE_ENTRY) {
      this.rootEntry = new FileEntryDescriptor(buffer);
    }
    else if (tag.identifier == Constants.D_TYPE_EXTENDED_FILE_ENTRY) {
      this.rootEntry = new ExtendedFileEntryDescriptor(buffer);
    }
  }

  public FileEntry getRootEntry() {
    return new UDFFileEntry(this.fs, this.rootEntry, null, null, true);
  }

  /**
   * Gets the starting location of the partition
   */
  public long getPDStartPos() {
    return this.pd.startingLocation;
  }
}
