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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class implements a new XML parser based on the built-in Java 8 XML libraries. It must
 * replace the original JDOM implementation.
 * 
 * @author Fabio Jun Takada Chino
 * @since 20210728
 */
public class AFTxXMLParser {

	/**
	 * Creates a very permissive document builder.
	 * 
	 * @return The new document builder.
	 * @throws ParserConfigurationException If the configuration is not accepted by the parser.
	 */
	protected static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		factory.setNamespaceAware(false);
		return factory.newDocumentBuilder();		
	}
	
	/**
	 * Parses the file list XML. This parser will be used by
	 * br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClient.getFileList(String, String, String).
	 * 
	 * @param in The input stream.
	 * @return The list of files.
	 * @throws IOException In case of IO errors.
	 * @throws SAXException In case of XML parsing errors.
	 */
	public static List<String> parseFileList(InputStream in) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilder builder = createDocumentBuilder();
		Document document = builder.parse(in);
		Element root = document.getDocumentElement();
		if (!root.getNodeName().equals("availableFiles")) {
			throw new SAXException("The root is not 'availableFiles'.");
		}
		NodeList children = root.getElementsByTagName("file");		
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i< children.getLength(); i++) {
			Node child = children.item(i);
			list.add(child.getTextContent());
		}
		return list;
	}
	
	/**
	 * Parses the log list XML. This parser will be used by
	 * br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClient.getLogsList(String, String).
	 * 
	 * @param in The input stream.
	 * @return The list of files.
	 * @throws IOException In case of IO errors.
	 * @throws SAXException In case of XML parsing errors.
	 */	
	public static List<String> parseLogList(InputStream in) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilder builder = createDocumentBuilder();
		Document document = builder.parse(in);
		Element root = document.getDocumentElement();
		if (!root.getNodeName().equals("ul")) {
			throw new SAXException("The root is not 'availableFiles'.");
		}
		NodeList children = root.getElementsByTagName("li");		
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i< children.getLength(); i++) {
			Node child = children.item(i);
			list.add(child.getTextContent().trim());
		}
		return list;
	}
}
