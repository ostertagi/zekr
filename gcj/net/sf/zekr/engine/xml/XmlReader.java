/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Sep 10, 2004
 */

package net.sf.zekr.engine.xml;

//import gnu.xml.dom.DomDocumentBuilderFactory;
import org.apache.xerces.jaxp.*;

import net.sf.zekr.engine.log.Logger;

import java.io.File;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 */
public class XmlReader {
	private final static Logger logger = Logger.getLogger(XmlReader.class);

	private Document xmlDocument;
	private Node parentNode;

	/**
	 * Reads the given XML file (with absolute or relative path of <tt>filePath</tt>).
	 * 
	 * @param filePath the path of the file to be read
	 * @throws XmlReadException if any error encountered during XML loading/parsig.
	 */
	public XmlReader(String filePath) throws XmlReadException {
		this(new File(filePath));
	}

	/**
	 * Reads the given XML file.
	 * 
	 * @param file the file to be read
	 * @throws XmlReadException if any error encountered during XML loading/parsing.
	 */
	public XmlReader(File file) throws XmlReadException {
		try {
			//DocumentBuilderFactory documentBuilderFactory = new DomDocumentBuilderFactory();
			DocumentBuilderFactory documentBuilderFactory = new DocumentBuilderFactoryImpl();
			logger.debug("documentBuilderFactory impl is: " + documentBuilderFactory.getClass().getName());
			DocumentBuilder parser;
			parser = documentBuilderFactory.newDocumentBuilder();
			logger.debug("DocumentBuilder impl is: " + parser.getClass().getName());
			xmlDocument = parser.parse(file);

			parentNode = xmlDocument.getFirstChild();
			if (parentNode.getNodeType() == Node.COMMENT_NODE)
				parentNode = parentNode.getNextSibling();
		} catch (Exception e) {
			throw new XmlReadException("Error while loading XML: " + file + ": " + e.getMessage(), e);
		}
	}

	public Element getDocumentElement() {
		return xmlDocument.getDocumentElement();
	}

	/**
	 * @param nodeHierarchy A dot separated node hierarchy for specifying a node inside other nodes. For
	 *            example <code>"body.div"</code> means <code>div</code> which is inside <code>body</code>.
	 *            <code>nodeHierarchy</code> should not contain the parent node (<code>parentNode</code>),
	 *            and the hierarchy is started from parent children.
	 * @return the node with <code>nodeHierarchy</code> hierarchy, or <code>null</code> if it can not be
	 *         found.
	 */
	public NodeList getNodes(String nodeHierarchy) {
		NodeList list = new NodeList();
		NodeList tempList = new NodeList();

		Node node = parentNode;

		String nextToken;
		StringTokenizer tokenizer = new StringTokenizer(nodeHierarchy, ".");

		list = XmlUtils.getNodes(node, tokenizer.nextToken());

		while (tokenizer.hasMoreTokens() && list.size() > 0) {
			nextToken = tokenizer.nextToken();
			tempList.add(XmlUtils.getNodes(list, nextToken));
			list = new NodeList(tempList);
			tempList.deleteAll();
		}
		return list;
	}

	public Node getNode(String nodeHierarchy) {
		return getNodes(nodeHierarchy).item(0);
	}

	public Element getElement(String elementHierarchy) {
		return (Element) getNodes(elementHierarchy).item(0);
	}

	public Node getNodeByAttr(String nodeHierarchy, String attrName, String attrValue) {
		NodeList nodeList = getNodes(nodeHierarchy);
		return XmlUtils.getElementByNamedAttr(nodeList, nodeHierarchy, attrName, attrValue);
	}

	public Document getDocument() {
		return xmlDocument;
	}
}
