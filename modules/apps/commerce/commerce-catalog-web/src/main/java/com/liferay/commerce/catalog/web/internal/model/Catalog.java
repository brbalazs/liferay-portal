package com.liferay.commerce.catalog.web.internal.model;

/**
 * @author Gianmarco Brunialti Masera
 */
public class Catalog {

	public Catalog(
		long catalogId, String name, String defaultLanguageId,
		String currencyCode) {

		_name = name;
		_catalogId = catalogId;
		_currencyCode = currencyCode;
		_defaultLanguageId = defaultLanguageId;
	}

	public long getCatalogId() {
		return _catalogId;
	}

	public String getName() {
		return _name;
	}

	public String getCurrencyCode() {
		return _currencyCode;
	}

	public String getDefaultLanguageId() {
		return _defaultLanguageId;
	}

	private final long _catalogId;
	private final String _name;
	private final String _currencyCode;
	private final String _defaultLanguageId;

}