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

import java.math.BigDecimal;

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

	public BigDecimal getShippingDiscountPercentageLevel1() {
		return shippingDiscountPercentageLevel1;
	}

	public void setShippingDiscountPercentageLevel1(
		BigDecimal shippingDiscountPercentageLevel1) {

		this.shippingDiscountPercentageLevel1 =
			shippingDiscountPercentageLevel1;
	}

	public void setShippingDiscountPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel1UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel1 =
				shippingDiscountPercentageLevel1UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal shippingDiscountPercentageLevel1;

	public BigDecimal getShippingDiscountPercentageLevel2() {
		return shippingDiscountPercentageLevel2;
	}

	public void setShippingDiscountPercentageLevel2(
		BigDecimal shippingDiscountPercentageLevel2) {

		this.shippingDiscountPercentageLevel2 =
			shippingDiscountPercentageLevel2;
	}

	public void setShippingDiscountPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel2UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel2 =
				shippingDiscountPercentageLevel2UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal shippingDiscountPercentageLevel2;

	public BigDecimal getShippingDiscountPercentageLevel3() {
		return shippingDiscountPercentageLevel3;
	}

	public void setShippingDiscountPercentageLevel3(
		BigDecimal shippingDiscountPercentageLevel3) {

		this.shippingDiscountPercentageLevel3 =
			shippingDiscountPercentageLevel3;
	}

	public void setShippingDiscountPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel3UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel3 =
				shippingDiscountPercentageLevel3UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal shippingDiscountPercentageLevel3;

	public BigDecimal getShippingDiscountPercentageLevel4() {
		return shippingDiscountPercentageLevel4;
	}

	public void setShippingDiscountPercentageLevel4(
		BigDecimal shippingDiscountPercentageLevel4) {

		this.shippingDiscountPercentageLevel4 =
			shippingDiscountPercentageLevel4;
	}

	public void setShippingDiscountPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel4UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel4 =
				shippingDiscountPercentageLevel4UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal shippingDiscountPercentageLevel4;

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

	public BigDecimal getSubtotalDiscountPercentageLevel1() {
		return subtotalDiscountPercentageLevel1;
	}

	public void setSubtotalDiscountPercentageLevel1(
		BigDecimal subtotalDiscountPercentageLevel1) {

		this.subtotalDiscountPercentageLevel1 =
			subtotalDiscountPercentageLevel1;
	}

	public void setSubtotalDiscountPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel1UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel1 =
				subtotalDiscountPercentageLevel1UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal subtotalDiscountPercentageLevel1;

	public BigDecimal getSubtotalDiscountPercentageLevel2() {
		return subtotalDiscountPercentageLevel2;
	}

	public void setSubtotalDiscountPercentageLevel2(
		BigDecimal subtotalDiscountPercentageLevel2) {

		this.subtotalDiscountPercentageLevel2 =
			subtotalDiscountPercentageLevel2;
	}

	public void setSubtotalDiscountPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel2UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel2 =
				subtotalDiscountPercentageLevel2UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal subtotalDiscountPercentageLevel2;

	public BigDecimal getSubtotalDiscountPercentageLevel3() {
		return subtotalDiscountPercentageLevel3;
	}

	public void setSubtotalDiscountPercentageLevel3(
		BigDecimal subtotalDiscountPercentageLevel3) {

		this.subtotalDiscountPercentageLevel3 =
			subtotalDiscountPercentageLevel3;
	}

	public void setSubtotalDiscountPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel3UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel3 =
				subtotalDiscountPercentageLevel3UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal subtotalDiscountPercentageLevel3;

	public BigDecimal getSubtotalDiscountPercentageLevel4() {
		return subtotalDiscountPercentageLevel4;
	}

	public void setSubtotalDiscountPercentageLevel4(
		BigDecimal subtotalDiscountPercentageLevel4) {

		this.subtotalDiscountPercentageLevel4 =
			subtotalDiscountPercentageLevel4;
	}

	public void setSubtotalDiscountPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel4UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel4 =
				subtotalDiscountPercentageLevel4UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal subtotalDiscountPercentageLevel4;

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

	public BigDecimal getTotalDiscountPercentageLevel1() {
		return totalDiscountPercentageLevel1;
	}

	public void setTotalDiscountPercentageLevel1(
		BigDecimal totalDiscountPercentageLevel1) {

		this.totalDiscountPercentageLevel1 = totalDiscountPercentageLevel1;
	}

	public void setTotalDiscountPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel1UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel1 =
				totalDiscountPercentageLevel1UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal totalDiscountPercentageLevel1;

	public BigDecimal getTotalDiscountPercentageLevel2() {
		return totalDiscountPercentageLevel2;
	}

	public void setTotalDiscountPercentageLevel2(
		BigDecimal totalDiscountPercentageLevel2) {

		this.totalDiscountPercentageLevel2 = totalDiscountPercentageLevel2;
	}

	public void setTotalDiscountPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel2UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel2 =
				totalDiscountPercentageLevel2UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal totalDiscountPercentageLevel2;

	public BigDecimal getTotalDiscountPercentageLevel3() {
		return totalDiscountPercentageLevel3;
	}

	public void setTotalDiscountPercentageLevel3(
		BigDecimal totalDiscountPercentageLevel3) {

		this.totalDiscountPercentageLevel3 = totalDiscountPercentageLevel3;
	}

	public void setTotalDiscountPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel3UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel3 =
				totalDiscountPercentageLevel3UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal totalDiscountPercentageLevel3;

	public BigDecimal getTotalDiscountPercentageLevel4() {
		return totalDiscountPercentageLevel4;
	}

	public void setTotalDiscountPercentageLevel4(
		BigDecimal totalDiscountPercentageLevel4) {

		this.totalDiscountPercentageLevel4 = totalDiscountPercentageLevel4;
	}

	public void setTotalDiscountPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel4UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel4 =
				totalDiscountPercentageLevel4UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal totalDiscountPercentageLevel4;

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