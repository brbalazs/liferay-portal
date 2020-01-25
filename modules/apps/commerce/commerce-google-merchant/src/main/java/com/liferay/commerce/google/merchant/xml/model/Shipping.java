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
 * Represents Google Merchant Center Shipping attributes
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "shipping", namespace = "http://base.google.com/ns/1.0")
@XmlType(propOrder = {"_country", "_service", "_price"})
public class Shipping {

	public Shipping() {
	}

	public Shipping(String country, String service, String price) {
		_country = country;
		_service = service;
		_price = price;
	}

	public void setCountry(String country) {
		_country = country;
	}

	public void setPrice(String price) {
		_price = price;
	}

	public void setService(String service) {
		_service = service;
	}

	@XmlElement(name = "country", namespace = "http://base.google.com/ns/1.0")
	private String _country;

	@XmlElement(name = "price", namespace = "http://base.google.com/ns/1.0")
	private String _price;

	@XmlElement(name = "service", namespace = "http://base.google.com/ns/1.0")
	private String _service;

}