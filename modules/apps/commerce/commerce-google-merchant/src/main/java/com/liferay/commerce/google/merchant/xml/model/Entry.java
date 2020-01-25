package com.liferay.commerce.google.merchant.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Kayleen Lim
 *
 * Represents Google Merchant Center Entry in a Feed
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "entry")
@XmlType(
	propOrder = {
		"_id", "_title", "_description", "_link", "_image_link", "_condition",
		"_availability", "_price", "_shipping"
	}
)
public class Entry {

	public void setAvailability(String availability) {
		_availability = availability;
	}

	public void setCondition(String condition) {
		_condition = condition;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setImage_link(String image_link) {
		_image_link = image_link;
	}

	public void setLink(String link) {
		_link = link;
	}

	public void setPrice(String price) {
		_price = price;
	}

	public void setShipping(Shipping shipping) {
		_shipping = shipping;
	}

	public void setTitle(String title) {
		_title = title;
	}

	@XmlElement(
		name = "availability", namespace = "http://base.google.com/ns/1.0"
	)
	private String _availability;

	@XmlElement(name = "condition", namespace = "http://base.google.com/ns/1.0")
	private String _condition;

	@XmlElement(
		name = "description", namespace = "http://base.google.com/ns/1.0"
	)
	private String _description;

	@XmlElement(name = "id", namespace = "http://base.google.com/ns/1.0")
	private String _id;

	@XmlElement(
		name = "image_link", namespace = "http://base.google.com/ns/1.0"
	)
	private String _image_link;

	@XmlElement(name = "link", namespace = "http://base.google.com/ns/1.0")
	private String _link;

	@XmlElement(name = "price", namespace = "http://base.google.com/ns/1.0")
	private String _price;

	@XmlElement(name = "shipping", namespace = "http://base.google.com/ns/1.0")
	private Shipping _shipping;

	@XmlElement(name = "title", namespace = "http://base.google.com/ns/1.0")
	private String _title;

}