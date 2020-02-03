package com.liferay.commerce.catalog.web.internal.model;

/**
 * @author Gianmarco Brunialti Masera
 */
public class AccountGroup {

	public AccountGroup(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	private final String _name;

}