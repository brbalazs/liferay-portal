package com.liferay.commerce.catalog.web.internal.model;

/**
 * @author Gianmarco Brunialti Masera
 */
public class Catalog {

	public Catalog(
		long catalogId, String catalogName, String defaultLanguageId,
		String currencyCode) {

		_catalogName = catalogName;
		_catalogId = catalogId;
		_currencyCode = currencyCode;
		_defaultLanguageId = defaultLanguageId;
	}

	public long getCatalogId() {
		return _catalogId;
	}

	public String getCatalogName() {
		return _catalogName;
	}

	public String getCurrencyCode() {
		return _currencyCode;
	}

	public String getDefaultLanguageId() {
		return _defaultLanguageId;
	}

	private final long _catalogId;
	private final String _catalogName;
	private final String _currencyCode;
	private final String _defaultLanguageId;

}