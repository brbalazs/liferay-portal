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
import com.liferay.headless.commerce.punchout.client.serdes.v1_0.SummarySerDes;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Jaclyn Ong
 * @generated
 */
@Generated("")
public class Summary implements Cloneable {

	public static Summary toDTO(String json) {
		return SummarySerDes.toDTO(json);
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

	public Integer getItemsQuantity() {
		return itemsQuantity;
	}

	public void setItemsQuantity(Integer itemsQuantity) {
		this.itemsQuantity = itemsQuantity;
	}

	public void setItemsQuantity(
		UnsafeSupplier<Integer, Exception> itemsQuantityUnsafeSupplier) {

		try {
			itemsQuantity = itemsQuantityUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer itemsQuantity;

	public String[] getShippingDiscountPercentages() {
		return shippingDiscountPercentages;
	}

	public void setShippingDiscountPercentages(
		String[] shippingDiscountPercentages) {

		this.shippingDiscountPercentages = shippingDiscountPercentages;
	}

	public void setShippingDiscountPercentages(
		UnsafeSupplier<String[], Exception>
			shippingDiscountPercentagesUnsafeSupplier) {

		try {
			shippingDiscountPercentages =
				shippingDiscountPercentagesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] shippingDiscountPercentages;

	public Double getShippingDiscountValue() {
		return shippingDiscountValue;
	}

	public void setShippingDiscountValue(Double shippingDiscountValue) {
		this.shippingDiscountValue = shippingDiscountValue;
	}

	public void setShippingDiscountValue(
		UnsafeSupplier<Double, Exception> shippingDiscountValueUnsafeSupplier) {

		try {
			shippingDiscountValue = shippingDiscountValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double shippingDiscountValue;

	public Double getShippingValue() {
		return shippingValue;
	}

	public void setShippingValue(Double shippingValue) {
		this.shippingValue = shippingValue;
	}

	public void setShippingValue(
		UnsafeSupplier<Double, Exception> shippingValueUnsafeSupplier) {

		try {
			shippingValue = shippingValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double shippingValue;

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public void setSubtotal(
		UnsafeSupplier<Double, Exception> subtotalUnsafeSupplier) {

		try {
			subtotal = subtotalUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double subtotal;

	public String[] getSubtotalDiscountPercentages() {
		return subtotalDiscountPercentages;
	}

	public void setSubtotalDiscountPercentages(
		String[] subtotalDiscountPercentages) {

		this.subtotalDiscountPercentages = subtotalDiscountPercentages;
	}

	public void setSubtotalDiscountPercentages(
		UnsafeSupplier<String[], Exception>
			subtotalDiscountPercentagesUnsafeSupplier) {

		try {
			subtotalDiscountPercentages =
				subtotalDiscountPercentagesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] subtotalDiscountPercentages;

	public Double getSubtotalDiscountValue() {
		return subtotalDiscountValue;
	}

	public void setSubtotalDiscountValue(Double subtotalDiscountValue) {
		this.subtotalDiscountValue = subtotalDiscountValue;
	}

	public void setSubtotalDiscountValue(
		UnsafeSupplier<Double, Exception> subtotalDiscountValueUnsafeSupplier) {

		try {
			subtotalDiscountValue = subtotalDiscountValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double subtotalDiscountValue;

	public Double getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(Double taxValue) {
		this.taxValue = taxValue;
	}

	public void setTaxValue(
		UnsafeSupplier<Double, Exception> taxValueUnsafeSupplier) {

		try {
			taxValue = taxValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double taxValue;

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public void setTotal(
		UnsafeSupplier<Double, Exception> totalUnsafeSupplier) {

		try {
			total = totalUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double total;

	public String[] getTotalDiscountPercentages() {
		return totalDiscountPercentages;
	}

	public void setTotalDiscountPercentages(String[] totalDiscountPercentages) {
		this.totalDiscountPercentages = totalDiscountPercentages;
	}

	public void setTotalDiscountPercentages(
		UnsafeSupplier<String[], Exception>
			totalDiscountPercentagesUnsafeSupplier) {

		try {
			totalDiscountPercentages =
				totalDiscountPercentagesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] totalDiscountPercentages;

	public Double getTotalDiscountValue() {
		return totalDiscountValue;
	}

	public void setTotalDiscountValue(Double totalDiscountValue) {
		this.totalDiscountValue = totalDiscountValue;
	}

	public void setTotalDiscountValue(
		UnsafeSupplier<Double, Exception> totalDiscountValueUnsafeSupplier) {

		try {
			totalDiscountValue = totalDiscountValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double totalDiscountValue;

	@Override
	public Summary clone() throws CloneNotSupportedException {
		return (Summary)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Summary)) {
			return false;
		}

		Summary summary = (Summary)object;

		return Objects.equals(toString(), summary.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return SummarySerDes.toJSON(this);
	}

}