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

package com.liferay.headless.commerce.punchout.client.dto.v1_0;

import com.liferay.headless.commerce.punchout.client.function.UnsafeSupplier;
import com.liferay.headless.commerce.punchout.client.serdes.v1_0.PriceSerDes;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Jaclyn Ong
 * @generated
 */
@Generated("")
public class Price implements Cloneable {

	public static Price toDTO(String json) {
		return PriceSerDes.toDTO(json);
	}

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

	public String getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public void setDiscountPercentage(
		UnsafeSupplier<String, Exception> discountPercentageUnsafeSupplier) {

		try {
			discountPercentage = discountPercentageUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String discountPercentage;

	public Double getDiscountPercentageLevel1() {
		return discountPercentageLevel1;
	}

	public void setDiscountPercentageLevel1(Double discountPercentageLevel1) {
		this.discountPercentageLevel1 = discountPercentageLevel1;
	}

	public void setDiscountPercentageLevel1(
		UnsafeSupplier<Double, Exception>
			discountPercentageLevel1UnsafeSupplier) {

		try {
			discountPercentageLevel1 =
				discountPercentageLevel1UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double discountPercentageLevel1;

	public Double getDiscountPercentageLevel2() {
		return discountPercentageLevel2;
	}

	public void setDiscountPercentageLevel2(Double discountPercentageLevel2) {
		this.discountPercentageLevel2 = discountPercentageLevel2;
	}

	public void setDiscountPercentageLevel2(
		UnsafeSupplier<Double, Exception>
			discountPercentageLevel2UnsafeSupplier) {

		try {
			discountPercentageLevel2 =
				discountPercentageLevel2UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double discountPercentageLevel2;

	public Double getDiscountPercentageLevel3() {
		return discountPercentageLevel3;
	}

	public void setDiscountPercentageLevel3(Double discountPercentageLevel3) {
		this.discountPercentageLevel3 = discountPercentageLevel3;
	}

	public void setDiscountPercentageLevel3(
		UnsafeSupplier<Double, Exception>
			discountPercentageLevel3UnsafeSupplier) {

		try {
			discountPercentageLevel3 =
				discountPercentageLevel3UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double discountPercentageLevel3;

	public Double getDiscountPercentageLevel4() {
		return discountPercentageLevel4;
	}

	public void setDiscountPercentageLevel4(Double discountPercentageLevel4) {
		this.discountPercentageLevel4 = discountPercentageLevel4;
	}

	public void setDiscountPercentageLevel4(
		UnsafeSupplier<Double, Exception>
			discountPercentageLevel4UnsafeSupplier) {

		try {
			discountPercentageLevel4 =
				discountPercentageLevel4UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double discountPercentageLevel4;

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

	@Override
	public Price clone() throws CloneNotSupportedException {
		return (Price)super.clone();
	}

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