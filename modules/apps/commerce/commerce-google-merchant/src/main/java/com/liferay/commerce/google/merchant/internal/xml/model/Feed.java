package com.liferay.commerce.google.merchant.internal.xml.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kayleen Lim
 *
 * Represents Google Merchant Center Feed
 *
 */
@JacksonXmlRootElement(localName = "feed")
@JsonPropertyOrder({"xmlns", "xmlns:g", "title", "link", "updated", "entries"})
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

	@JacksonXmlProperty(isAttribute = true, localName = "xmlns")
	private static final String _xmlns = "http://www.w3.org/2005/Atom";

	@JacksonXmlProperty(isAttribute = true, localName = "xmlns:g")
	private static final String _xmlnsg = "http://base.google.com/ns/1.0";

	@JacksonXmlProperty(localName = "entry")
	private List<Entry> _entries = new ArrayList<>();

	@JacksonXmlProperty(localName = "link")
	private Link _link;

	@JacksonXmlProperty(localName = "title")
	private String _title;

	@JacksonXmlProperty(localName = "updated")
	private String _updated;

}