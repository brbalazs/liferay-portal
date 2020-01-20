/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.headless.commerce.delivery.cart.client.dto.v1_0;

import com.liferay.headless.commerce.delivery.cart.client.function.UnsafeSupplier;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.PriceSerDes;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class Price {

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setCurrency(
		UnsafeSupplier<String, Exception> currencyUnsafeSupplier) {

		try {
			currency = currencyUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String currency;

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public void setDiscount(
		UnsafeSupplier<Double, Exception> discountUnsafeSupplier) {

		try {
			discount = discountUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double discount;

	public String getDiscountFormatted() {
		return discountFormatted;
	}

	public void setDiscountFormatted(String discountFormatted) {
		this.discountFormatted = discountFormatted;
	}

	public void setDiscountFormatted(
		UnsafeSupplier<String, Exception> discountFormattedUnsafeSupplier) {

		try {
			discountFormatted = discountFormattedUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String discountFormatted;

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public void setFinalPrice(
		UnsafeSupplier<Double, Exception> finalPriceUnsafeSupplier) {

		try {
			finalPrice = finalPriceUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double finalPrice;

	public String getFinalPriceFormatted() {
		return finalPriceFormatted;
	}

	public void setFinalPriceFormatted(String finalPriceFormatted) {
		this.finalPriceFormatted = finalPriceFormatted;
	}

	public void setFinalPriceFormatted(
		UnsafeSupplier<String, Exception> finalPriceFormattedUnsafeSupplier) {

		try {
			finalPriceFormatted = finalPriceFormattedUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String finalPriceFormatted;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setPrice(
		UnsafeSupplier<Double, Exception> priceUnsafeSupplier) {

		try {
			price = priceUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double price;

	public String getPriceFormatted() {
		return priceFormatted;
	}

	public void setPriceFormatted(String priceFormatted) {
		this.priceFormatted = priceFormatted;
	}

	public void setPriceFormatted(
		UnsafeSupplier<String, Exception> priceFormattedUnsafeSupplier) {

		try {
			priceFormatted = priceFormattedUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String priceFormatted;

	public Double getPromoPrice() {
		return promoPrice;
	}

	public void setPromoPrice(Double promoPrice) {
		this.promoPrice = promoPrice;
	}

	public void setPromoPrice(
		UnsafeSupplier<Double, Exception> promoPriceUnsafeSupplier) {

		try {
			promoPrice = promoPriceUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double promoPrice;

	public String getPromoPriceFormatted() {
		return promoPriceFormatted;
	}

	public void setPromoPriceFormatted(String promoPriceFormatted) {
		this.promoPriceFormatted = promoPriceFormatted;
	}

	public void setPromoPriceFormatted(
		UnsafeSupplier<String, Exception> promoPriceFormattedUnsafeSupplier) {

		try {
			promoPriceFormatted = promoPriceFormattedUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String promoPriceFormatted;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Price)) {
			return false;
		}

		Price price = (Price)object;

		return Objects.equals(toString(), price.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return PriceSerDes.toJSON(this);
	}

}