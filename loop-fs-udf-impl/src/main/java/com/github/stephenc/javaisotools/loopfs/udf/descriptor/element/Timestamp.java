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

package com.github.stephenc.javaisotools.loopfs.udf.descriptor.element;

import java.util.Date;
import java.util.GregorianCalendar;

import com.github.stephenc.javaisotools.loopfs.udf.UDFUtil;
import com.github.stephenc.javaisotools.loopfs.udf.exceptions.InvalidDescriptor;

/**
 * The timestamp block (ECMA-167 1/7.3)
 *
 * Not implemented and not in use
 */
public class Timestamp {
//	struct timestamp { /* ECMA 167 1/7.3 */
//		Uint16 TypeAndTimezone;
//		Int16 Year;
//		Uint8 Month;
//		Uint8 Day;
//		Uint8 Hour;
//		Uint8 Minute;
//		Uint8 Second;
//		Uint8 Centiseconds;
//		Uint8 HundredsofMicroseconds;
//		Uint8 Microseconds;
//	}		

	public Integer typeAndTimezone;
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	public int second;
	public int centiSeconds;
	public int hundredsofMicroseconds;
	public int microseconds;

	public static final int LENGTH = 12;

	public Timestamp(byte[] bytes) throws InvalidDescriptor {
		this.deserialize(bytes);
	}

	private void deserialize(byte[] bytes) throws InvalidDescriptor {
		if (bytes.length < LENGTH) {
			throw new InvalidDescriptor("Timezone allocation descriptor too short");
		}

		this.typeAndTimezone = UDFUtil.getUInt16(bytes, 0);
		this.year = UDFUtil.getUInt16(bytes, 2);
		this.month = UDFUtil.getUInt8(bytes, 4);
		this.day = UDFUtil.getUInt8(bytes, 5);
		this.hour = UDFUtil.getUInt8(bytes, 6);
		this.minute = UDFUtil.getUInt8(bytes, 7);
		this.second = UDFUtil.getUInt8(bytes, 8);
		this.centiSeconds = UDFUtil.getUInt8(bytes, 9);
		this.hundredsofMicroseconds = UDFUtil.getUInt8(bytes, 10);
		this.microseconds = UDFUtil.getUInt8(bytes, 11);
	}

	public Date toDate() {
		return new GregorianCalendar(year, month, day, hour, minute, second).getTime();
	}

	@Override
	public String toString() {
		return "Timestamp [typeAndTimezone=" + typeAndTimezone + ", year=" + year + ", month=" + month + ", day=" + day
				+ ", hour=" + hour + ", minute=" + minute + ", second=" + second + ", centiSeconds=" + centiSeconds
				+ ", hundredsofMicroseconds=" + hundredsofMicroseconds + ", microseconds=" + microseconds + "]";
	}

}
