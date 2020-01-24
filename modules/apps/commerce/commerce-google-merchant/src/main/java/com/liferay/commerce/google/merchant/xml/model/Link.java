package com.liferay.commerce.google.merchant.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Kayleen Lim
 *
 * Represents Link, a required attribute of Atom 1.0 XML
 *
 * Example: <code><link rel="self" href="http://www.example.com"/></code>
 */
@XmlRootElement(name = "link")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
	propOrder={
		"_rel", "_href"
	}
)
public class Link {
	public Link() {}

	public Link(String href) {
		_href = href;
	}

	public void setHref(String href) {
		_href = href;
	}

	@XmlAttribute(name = "href")
	private String _href;

	@XmlAttribute(name = "rel")
	private String _rel = "self";

}