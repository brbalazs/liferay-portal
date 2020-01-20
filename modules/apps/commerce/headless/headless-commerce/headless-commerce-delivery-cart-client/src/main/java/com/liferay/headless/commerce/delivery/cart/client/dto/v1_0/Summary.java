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
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.SummarySerDes;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class Summary {

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

	public String getShippingDiscountValue() {
		return shippingDiscountValue;
	}

	public void setShippingDiscountValue(String shippingDiscountValue) {
		this.shippingDiscountValue = shippingDiscountValue;
	}

	public void setShippingDiscountValue(
		UnsafeSupplier<String, Exception> shippingDiscountValueUnsafeSupplier) {

		try {
			shippingDiscountValue = shippingDiscountValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String shippingDiscountValue;

	public String getShippingValue() {
		return shippingValue;
	}

	public void setShippingValue(String shippingValue) {
		this.shippingValue = shippingValue;
	}

	public void setShippingValue(
		UnsafeSupplier<String, Exception> shippingValueUnsafeSupplier) {

		try {
			shippingValue = shippingValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String shippingValue;

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public void setSubtotal(
		UnsafeSupplier<String, Exception> subtotalUnsafeSupplier) {

		try {
			subtotal = subtotalUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String subtotal;

	public String getSubtotalDiscountValue() {
		return subtotalDiscountValue;
	}

	public void setSubtotalDiscountValue(String subtotalDiscountValue) {
		this.subtotalDiscountValue = subtotalDiscountValue;
	}

	public void setSubtotalDiscountValue(
		UnsafeSupplier<String, Exception> subtotalDiscountValueUnsafeSupplier) {

		try {
			subtotalDiscountValue = subtotalDiscountValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String subtotalDiscountValue;

	public String getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(String taxValue) {
		this.taxValue = taxValue;
	}

	public void setTaxValue(
		UnsafeSupplier<String, Exception> taxValueUnsafeSupplier) {

		try {
			taxValue = taxValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String taxValue;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public void setTotal(
		UnsafeSupplier<String, Exception> totalUnsafeSupplier) {

		try {
			total = totalUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String total;

	public String getTotalDiscountValue() {
		return totalDiscountValue;
	}

	public void setTotalDiscountValue(String totalDiscountValue) {
		this.totalDiscountValue = totalDiscountValue;
	}

	public void setTotalDiscountValue(
		UnsafeSupplier<String, Exception> totalDiscountValueUnsafeSupplier) {

		try {
			totalDiscountValue = totalDiscountValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String totalDiscountValue;

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