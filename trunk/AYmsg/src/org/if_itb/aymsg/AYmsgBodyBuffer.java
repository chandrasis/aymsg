/*
 * OpenYMSG, an implementation of the Yahoo Instant Messaging and Chat protocol.
 * Copyright (C) 2007 G. der Kinderen, Nimbuzz.com 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package org.if_itb.aymsg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This handy class hides most of the pain of building the Yahoo message body.
 * Each body consists of key/value pairs (or sometimes just keys) with each
 * field separated by the sequence 0xc080
 * 
 * Note: this class is NOT thread safe (although, to be honest, building a
 * single message body from more than one independent thread is surely asking
 * for trouble?!??)
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 * @author S.E. Morris
 */
public class AYmsgBodyBuffer {
	private final static int[] SEPARATOR = { 0xc0, 0x80 }; // Yahoo separator

	private final static String SEPARATOR_STRING = new String(new byte[] {
			(byte) 0xc0, (byte) 0x80 });
	private String charEncoding; // Character encoding

	protected ByteArrayOutputStream baos;

	public AYmsgBodyBuffer() {
		this.baos = new ByteArrayOutputStream(1024); // 1K initial size
		this.charEncoding = System.getProperty("openymsg.network.charEncoding",
				"UTF-8");
	}

	/**
	 * Add key/value pair to buffer. Note: this method is NOT thread safe.
	 * 
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public void addElement(String key, String value)
			throws UnsupportedEncodingException, IOException {
		this.addString(key);
		this.addString(value);
	}

	/**
	 * Add a string to the buffer, and terminate with separator. Note: this
	 * method is NOT thread safe.
	 * 
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public void addString(String s) throws UnsupportedEncodingException, IOException {
		this.baos.write(s.getBytes(this.charEncoding));
		this.baos.write(SEPARATOR[0]);
		this.baos.write(SEPARATOR[1]);
	}

	/**
	 * Return buffer as byte array. Note: this method is NOT thread safe.
	 */
	public synchronized byte[] getBuffer() {
		return this.baos.toByteArray();
	}

	/**
	 * Reset (clear) buffer
	 */
	public void reset() {
		this.baos.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new String(this.baos.toByteArray()).replace(SEPARATOR_STRING, " ");
	}
}
