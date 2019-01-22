/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.theme.minium.impl.internal.product.model;

/**
 * @author Alessio Antonio Rendina
 */
public class PriceModel {

	public PriceModel(String price) {
		_price = price;
	}

	public String getDiscount() {
		return _discount;
	}

	public String getPrice() {
		return _price;
	}

	public String getPromoPrice() {
		return _promoPrice;
	}

	public void setDiscount(String discount) {
		_discount = discount;
	}

	public void setPrice(String price) {
		_price = price;
	}

	public void setPromoPrice(String promoPrice) {
		_promoPrice = promoPrice;
	}

	private String _discount;
	private String _price;
	private String _promoPrice;

}