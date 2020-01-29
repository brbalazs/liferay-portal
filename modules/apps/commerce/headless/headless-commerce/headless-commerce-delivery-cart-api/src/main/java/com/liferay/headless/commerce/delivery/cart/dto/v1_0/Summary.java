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

package com.liferay.headless.commerce.delivery.cart.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
@GraphQLName("Summary")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "Summary")
public class Summary {

	@Schema
	public Integer getItemsQuantity() {
		return itemsQuantity;
	}

	public void setItemsQuantity(Integer itemsQuantity) {
		this.itemsQuantity = itemsQuantity;
	}

	@JsonIgnore
	public void setItemsQuantity(
		UnsafeSupplier<Integer, Exception> itemsQuantityUnsafeSupplier) {

		try {
			itemsQuantity = itemsQuantityUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer itemsQuantity;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getShippingDiscountPercentageLevel1() {
		return shippingDiscountPercentageLevel1;
	}

	public void setShippingDiscountPercentageLevel1(
		BigDecimal shippingDiscountPercentageLevel1) {

		this.shippingDiscountPercentageLevel1 =
			shippingDiscountPercentageLevel1;
	}

	@JsonIgnore
	public void setShippingDiscountPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel1UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel1 =
				shippingDiscountPercentageLevel1UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal shippingDiscountPercentageLevel1;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getShippingDiscountPercentageLevel2() {
		return shippingDiscountPercentageLevel2;
	}

	public void setShippingDiscountPercentageLevel2(
		BigDecimal shippingDiscountPercentageLevel2) {

		this.shippingDiscountPercentageLevel2 =
			shippingDiscountPercentageLevel2;
	}

	@JsonIgnore
	public void setShippingDiscountPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel2UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel2 =
				shippingDiscountPercentageLevel2UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal shippingDiscountPercentageLevel2;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getShippingDiscountPercentageLevel3() {
		return shippingDiscountPercentageLevel3;
	}

	public void setShippingDiscountPercentageLevel3(
		BigDecimal shippingDiscountPercentageLevel3) {

		this.shippingDiscountPercentageLevel3 =
			shippingDiscountPercentageLevel3;
	}

	@JsonIgnore
	public void setShippingDiscountPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel3UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel3 =
				shippingDiscountPercentageLevel3UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal shippingDiscountPercentageLevel3;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getShippingDiscountPercentageLevel4() {
		return shippingDiscountPercentageLevel4;
	}

	public void setShippingDiscountPercentageLevel4(
		BigDecimal shippingDiscountPercentageLevel4) {

		this.shippingDiscountPercentageLevel4 =
			shippingDiscountPercentageLevel4;
	}

	@JsonIgnore
	public void setShippingDiscountPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception>
			shippingDiscountPercentageLevel4UnsafeSupplier) {

		try {
			shippingDiscountPercentageLevel4 =
				shippingDiscountPercentageLevel4UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal shippingDiscountPercentageLevel4;

	@Schema
	public String getShippingDiscountValue() {
		return shippingDiscountValue;
	}

	public void setShippingDiscountValue(String shippingDiscountValue) {
		this.shippingDiscountValue = shippingDiscountValue;
	}

	@JsonIgnore
	public void setShippingDiscountValue(
		UnsafeSupplier<String, Exception> shippingDiscountValueUnsafeSupplier) {

		try {
			shippingDiscountValue = shippingDiscountValueUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String shippingDiscountValue;

	@Schema
	public String getShippingValue() {
		return shippingValue;
	}

	public void setShippingValue(String shippingValue) {
		this.shippingValue = shippingValue;
	}

	@JsonIgnore
	public void setShippingValue(
		UnsafeSupplier<String, Exception> shippingValueUnsafeSupplier) {

		try {
			shippingValue = shippingValueUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String shippingValue;

	@Schema
	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	@JsonIgnore
	public void setSubtotal(
		UnsafeSupplier<String, Exception> subtotalUnsafeSupplier) {

		try {
			subtotal = subtotalUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String subtotal;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getSubtotalDiscountPercentageLevel1() {
		return subtotalDiscountPercentageLevel1;
	}

	public void setSubtotalDiscountPercentageLevel1(
		BigDecimal subtotalDiscountPercentageLevel1) {

		this.subtotalDiscountPercentageLevel1 =
			subtotalDiscountPercentageLevel1;
	}

	@JsonIgnore
	public void setSubtotalDiscountPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel1UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel1 =
				subtotalDiscountPercentageLevel1UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal subtotalDiscountPercentageLevel1;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getSubtotalDiscountPercentageLevel2() {
		return subtotalDiscountPercentageLevel2;
	}

	public void setSubtotalDiscountPercentageLevel2(
		BigDecimal subtotalDiscountPercentageLevel2) {

		this.subtotalDiscountPercentageLevel2 =
			subtotalDiscountPercentageLevel2;
	}

	@JsonIgnore
	public void setSubtotalDiscountPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel2UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel2 =
				subtotalDiscountPercentageLevel2UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal subtotalDiscountPercentageLevel2;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getSubtotalDiscountPercentageLevel3() {
		return subtotalDiscountPercentageLevel3;
	}

	public void setSubtotalDiscountPercentageLevel3(
		BigDecimal subtotalDiscountPercentageLevel3) {

		this.subtotalDiscountPercentageLevel3 =
			subtotalDiscountPercentageLevel3;
	}

	@JsonIgnore
	public void setSubtotalDiscountPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel3UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel3 =
				subtotalDiscountPercentageLevel3UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal subtotalDiscountPercentageLevel3;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getSubtotalDiscountPercentageLevel4() {
		return subtotalDiscountPercentageLevel4;
	}

	public void setSubtotalDiscountPercentageLevel4(
		BigDecimal subtotalDiscountPercentageLevel4) {

		this.subtotalDiscountPercentageLevel4 =
			subtotalDiscountPercentageLevel4;
	}

	@JsonIgnore
	public void setSubtotalDiscountPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception>
			subtotalDiscountPercentageLevel4UnsafeSupplier) {

		try {
			subtotalDiscountPercentageLevel4 =
				subtotalDiscountPercentageLevel4UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal subtotalDiscountPercentageLevel4;

	@Schema
	public String getSubtotalDiscountValue() {
		return subtotalDiscountValue;
	}

	public void setSubtotalDiscountValue(String subtotalDiscountValue) {
		this.subtotalDiscountValue = subtotalDiscountValue;
	}

	@JsonIgnore
	public void setSubtotalDiscountValue(
		UnsafeSupplier<String, Exception> subtotalDiscountValueUnsafeSupplier) {

		try {
			subtotalDiscountValue = subtotalDiscountValueUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String subtotalDiscountValue;

	@Schema
	public String getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(String taxValue) {
		this.taxValue = taxValue;
	}

	@JsonIgnore
	public void setTaxValue(
		UnsafeSupplier<String, Exception> taxValueUnsafeSupplier) {

		try {
			taxValue = taxValueUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String taxValue;

	@Schema
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@JsonIgnore
	public void setTotal(
		UnsafeSupplier<String, Exception> totalUnsafeSupplier) {

		try {
			total = totalUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String total;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getTotalDiscountPercentageLevel1() {
		return totalDiscountPercentageLevel1;
	}

	public void setTotalDiscountPercentageLevel1(
		BigDecimal totalDiscountPercentageLevel1) {

		this.totalDiscountPercentageLevel1 = totalDiscountPercentageLevel1;
	}

	@JsonIgnore
	public void setTotalDiscountPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel1UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel1 =
				totalDiscountPercentageLevel1UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal totalDiscountPercentageLevel1;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getTotalDiscountPercentageLevel2() {
		return totalDiscountPercentageLevel2;
	}

	public void setTotalDiscountPercentageLevel2(
		BigDecimal totalDiscountPercentageLevel2) {

		this.totalDiscountPercentageLevel2 = totalDiscountPercentageLevel2;
	}

	@JsonIgnore
	public void setTotalDiscountPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel2UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel2 =
				totalDiscountPercentageLevel2UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal totalDiscountPercentageLevel2;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getTotalDiscountPercentageLevel3() {
		return totalDiscountPercentageLevel3;
	}

	public void setTotalDiscountPercentageLevel3(
		BigDecimal totalDiscountPercentageLevel3) {

		this.totalDiscountPercentageLevel3 = totalDiscountPercentageLevel3;
	}

	@JsonIgnore
	public void setTotalDiscountPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel3UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel3 =
				totalDiscountPercentageLevel3UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal totalDiscountPercentageLevel3;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getTotalDiscountPercentageLevel4() {
		return totalDiscountPercentageLevel4;
	}

	public void setTotalDiscountPercentageLevel4(
		BigDecimal totalDiscountPercentageLevel4) {

		this.totalDiscountPercentageLevel4 = totalDiscountPercentageLevel4;
	}

	@JsonIgnore
	public void setTotalDiscountPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception>
			totalDiscountPercentageLevel4UnsafeSupplier) {

		try {
			totalDiscountPercentageLevel4 =
				totalDiscountPercentageLevel4UnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected BigDecimal totalDiscountPercentageLevel4;

	@Schema
	public String getTotalDiscountValue() {
		return totalDiscountValue;
	}

	public void setTotalDiscountValue(String totalDiscountValue) {
		this.totalDiscountValue = totalDiscountValue;
	}

	@JsonIgnore
	public void setTotalDiscountValue(
		UnsafeSupplier<String, Exception> totalDiscountValueUnsafeSupplier) {

		try {
			totalDiscountValue = totalDiscountValueUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
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
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (itemsQuantity != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"itemsQuantity\": ");

			sb.append(itemsQuantity);
		}

		if (shippingDiscountPercentageLevel1 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel1\": ");

			sb.append(shippingDiscountPercentageLevel1);
		}

		if (shippingDiscountPercentageLevel2 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel2\": ");

			sb.append(shippingDiscountPercentageLevel2);
		}

		if (shippingDiscountPercentageLevel3 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel3\": ");

			sb.append(shippingDiscountPercentageLevel3);
		}

		if (shippingDiscountPercentageLevel4 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel4\": ");

			sb.append(shippingDiscountPercentageLevel4);
		}

		if (shippingDiscountValue != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountValue\": ");

			sb.append("\"");

			sb.append(_escape(shippingDiscountValue));

			sb.append("\"");
		}

		if (shippingValue != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingValue\": ");

			sb.append("\"");

			sb.append(_escape(shippingValue));

			sb.append("\"");
		}

		if (subtotal != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotal\": ");

			sb.append("\"");

			sb.append(_escape(subtotal));

			sb.append("\"");
		}

		if (subtotalDiscountPercentageLevel1 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel1\": ");

			sb.append(subtotalDiscountPercentageLevel1);
		}

		if (subtotalDiscountPercentageLevel2 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel2\": ");

			sb.append(subtotalDiscountPercentageLevel2);
		}

		if (subtotalDiscountPercentageLevel3 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel3\": ");

			sb.append(subtotalDiscountPercentageLevel3);
		}

		if (subtotalDiscountPercentageLevel4 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel4\": ");

			sb.append(subtotalDiscountPercentageLevel4);
		}

		if (subtotalDiscountValue != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountValue\": ");

			sb.append("\"");

			sb.append(_escape(subtotalDiscountValue));

			sb.append("\"");
		}

		if (taxValue != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"taxValue\": ");

			sb.append("\"");

			sb.append(_escape(taxValue));

			sb.append("\"");
		}

		if (total != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"total\": ");

			sb.append("\"");

			sb.append(_escape(total));

			sb.append("\"");
		}

		if (totalDiscountPercentageLevel1 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel1\": ");

			sb.append(totalDiscountPercentageLevel1);
		}

		if (totalDiscountPercentageLevel2 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel2\": ");

			sb.append(totalDiscountPercentageLevel2);
		}

		if (totalDiscountPercentageLevel3 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel3\": ");

			sb.append(totalDiscountPercentageLevel3);
		}

		if (totalDiscountPercentageLevel4 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel4\": ");

			sb.append(totalDiscountPercentageLevel4);
		}

		if (totalDiscountValue != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountValue\": ");

			sb.append("\"");

			sb.append(_escape(totalDiscountValue));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		defaultValue = "com.liferay.headless.commerce.delivery.cart.dto.v1_0.Summary",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		return string.replaceAll("\"", "\\\\\"");
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\":");
			sb.append("\"");
			sb.append(entry.getValue());
			sb.append("\"");

			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}