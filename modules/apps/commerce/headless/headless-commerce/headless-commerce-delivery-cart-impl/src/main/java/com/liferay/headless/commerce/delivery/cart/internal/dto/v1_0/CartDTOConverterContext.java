package com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0;

import com.liferay.headless.commerce.core.dto.v1_0.converter.DefaultDTOConverterContext;

import java.util.Locale;

public class CartDTOConverterContext extends DefaultDTOConverterContext {

	public CartDTOConverterContext(
		Locale locale, long resourcePrimKey, long channelSiteGroupId) {

		super(locale, resourcePrimKey);

		_channelSiteGroupId = channelSiteGroupId;
	}

	public long getChannelSiteGroupId() {
		return _channelSiteGroupId;
	}

	private final long _channelSiteGroupId;

}