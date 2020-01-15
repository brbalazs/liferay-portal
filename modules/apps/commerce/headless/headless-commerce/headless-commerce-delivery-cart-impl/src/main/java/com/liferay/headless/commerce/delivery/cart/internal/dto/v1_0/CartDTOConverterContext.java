package com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0;

import com.liferay.headless.commerce.core.dto.v1_0.converter.DefaultDTOConverterContext;

import java.util.Locale;

public class CartDTOConverterContext extends DefaultDTOConverterContext {

	public CartDTOConverterContext(
		Locale locale, long resourcePrimKey, boolean useFullEntity) {

		super(locale, resourcePrimKey);

		_useFullEntity = useFullEntity;
	}

	public boolean isUseFullEntity() {
		return _useFullEntity;
	}

	private final boolean _useFullEntity;

}