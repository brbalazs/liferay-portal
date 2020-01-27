package com.liferay.commerce.google.merchant.internal.xml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Kayleen Lim
 *
 * Represents Google Merchant Center Feed
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "feed")
@XmlType(propOrder = {"_xmlns", "_title", "_link", "_updated", "_entries"})
public class Feed {

	public void addEntry(Entry entry) {
		_entries.add(entry);
	}

	public void setEntries(List<Entry> entries) {
		_entries = entries;
	}

	public void setLink(Link link) {
		_link = link;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setUpdated(String updated) {
		_updated = updated;
	}

	@XmlAttribute(name = "xmlns")
	private static final String _xmlns = "http://www.w3.org/2005/Atom";

	@XmlElement(name = "entry")
	private List<Entry> _entries = new ArrayList<>();

	@XmlElement(name = "link", required = true)
	private Link _link;

	@XmlElement(name = "title", required = true)
	private String _title;

	@XmlElement(name = "updated", required = true)
	private String _updated;

}