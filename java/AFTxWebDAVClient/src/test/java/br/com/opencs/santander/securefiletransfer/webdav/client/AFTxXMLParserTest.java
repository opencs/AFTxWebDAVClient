/*
 * BSD 3-Clause License
 * 
 * Copyright (c) 2007-2021, Open Communications Security
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.opencs.santander.securefiletransfer.webdav.client;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

public class AFTxXMLParserTest {
	
	
	private static final byte [] SAMPLE_FILE_LIST_XML = 
			("<availableFiles>\n"
			+ "    <file>file1</file>\n"
			+ "    <file>file2</file>\n"
			+ "    <file>file3</file>\n"
			+ "    <file>filen</file>\n"
			+ "</availableFiles>\n").getBytes(Charset.forName("utf-8"));
	
	protected static List<String> parseFileListCompat(InputStream in) throws Exception {
		// TODO Remove it in the future
		ArrayList<String> list = new ArrayList<>(); 
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List<Element> aftFiles = root.getChildren();
		for (Element aftFile : aftFiles) {
			list.add(aftFile.getText());
		}
		return list;
	}

	@Test
	public void testParseFileListCompat() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(SAMPLE_FILE_LIST_XML);
		
		List<String> ret = parseFileListCompat(in);
		assertEquals(4, ret.size());
		assertEquals(ret.get(0), "file1");
		assertEquals(ret.get(1), "file2");
		assertEquals(ret.get(2), "file3");
		assertEquals(ret.get(3), "filen");
	}

	
	@Test
	public void testParseFileList() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(SAMPLE_FILE_LIST_XML);
		
		List<String> ret = AFTxXMLParser.parseFileList(in);
		assertEquals(4, ret.size());
		assertEquals(4, ret.size());
		assertEquals(ret.get(0), "file1");
		assertEquals(ret.get(1), "file2");
		assertEquals(ret.get(2), "file3");
		assertEquals(ret.get(3), "filen");		
	}

	
	private static final byte [] SAMPLE_LOG_LIST_XML = 
			("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<ul>\n"
			+ "  <li>\n"
			+ "    <a href=\"/log/log-name1.xml\">log-name1.xml</a>\n"
			+ "  </li>\n"
			+ "  <li>\n"
			+ "    <a href=\"/log/log-name2.xml\">log-name2.xml</a>\n"
			+ "  </li>\n"
			+ "  <li>\n"
			+ "    <a href=\"/log/log-name3.xml\">log-name3.xml</a>\n"
			+ "  </li>\n"
			+ "</ul>").getBytes(Charset.forName("utf-8"));
	
	
	protected static List<String> parseLogListCompat(InputStream in) throws Exception {
		// TODO Remove it in the future
		ArrayList<String> list = new ArrayList<>(); 
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List<Element> logFiles = root.getChildren();
		for (Element logFile : logFiles) {
			list.add(logFile.getChildText("a"));
		}
		return list;
	}
	
	@Test
	public void testparseLogListCompat() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(SAMPLE_LOG_LIST_XML);
		
		List<String> ret = parseLogListCompat(in);
		assertEquals(3, ret.size());
		assertEquals(ret.get(0), "log-name1.xml");
		assertEquals(ret.get(1), "log-name2.xml");
		assertEquals(ret.get(2), "log-name3.xml");
	}


	@Test
	public void testParseLogList() throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(SAMPLE_LOG_LIST_XML);
		
		List<String> ret = AFTxXMLParser.parseLogList(in);
		assertEquals(3, ret.size());
		assertEquals(ret.get(0), "log-name1.xml");
		assertEquals(ret.get(1), "log-name2.xml");
		assertEquals(ret.get(2), "log-name3.xml");
	}

}
