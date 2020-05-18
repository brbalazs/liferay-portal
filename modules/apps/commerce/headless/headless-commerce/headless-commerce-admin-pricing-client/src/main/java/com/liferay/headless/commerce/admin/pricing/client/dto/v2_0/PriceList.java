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

package com.liferay.headless.commerce.admin.pricing.client.dto.v2_0;

import com.liferay.headless.commerce.admin.pricing.client.function.UnsafeSupplier;
import com.liferay.headless.commerce.admin.pricing.client.serdes.v2_0.PriceListSerDes;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public class PriceList implements Cloneable {

	public static enum Type {

		PRICE_LIST("price-list"), PROMOTION("promotion"), CONTRACT("contract");

		public static Type create(String value) {
			for (Type type : values()) {
				if (Objects.equals(type.getValue(), value)) {
					return type;
				}
			}

			return null;
		}

		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private Type(String value) {
			_value = value;
		}

		private final String _value;

	}

	public PriceListAccountGroup[] getAccountGroups() {
		return accountGroups;
	}

	public void setAccountGroups(PriceListAccountGroup[] accountGroups) {
		this.accountGroups = accountGroups;
	}

	public void setAccountGroups(
		UnsafeSupplier<PriceListAccountGroup[], Exception>
			accountGroupsUnsafeSupplier) {

		try {
			accountGroups = accountGroupsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PriceListAccountGroup[] accountGroups;

	public PriceListAccount[] getAccounts() {
		return accounts;
	}

	public void setAccounts(PriceListAccount[] accounts) {
		this.accounts = accounts;
	}

	public void setAccounts(
		UnsafeSupplier<PriceListAccount[], Exception> accountsUnsafeSupplier) {

		try {
			accounts = accountsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PriceListAccount[] accounts;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setActive(
		UnsafeSupplier<Boolean, Exception> activeUnsafeSupplier) {

		try {
			active = activeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean active;

	public Boolean getCatalogBasePriceList() {
		return catalogBasePriceList;
	}

	public void setCatalogBasePriceList(Boolean catalogBasePriceList) {
		this.catalogBasePriceList = catalogBasePriceList;
	}

	public void setCatalogBasePriceList(
		UnsafeSupplier<Boolean, Exception> catalogBasePriceListUnsafeSupplier) {

		try {
			catalogBasePriceList = catalogBasePriceListUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean catalogBasePriceList;

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public void setCatalogId(
		UnsafeSupplier<Long, Exception> catalogIdUnsafeSupplier) {

		try {
			catalogId = catalogIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long catalogId;

	public PriceListChannel[] getChannels() {
		return channels;
	}

	public void setChannels(PriceListChannel[] channels) {
		this.channels = channels;
	}

	public void setChannels(
		UnsafeSupplier<PriceListChannel[], Exception> channelsUnsafeSupplier) {

		try {
			channels = channelsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PriceListChannel[] channels;

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setCurrencyCode(
		UnsafeSupplier<String, Exception> currencyCodeUnsafeSupplier) {

		try {
			currencyCode = currencyCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String currencyCode;

	public Map<String, ?> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, ?> customFields) {
		this.customFields = customFields;
	}

	public void setCustomFields(
		UnsafeSupplier<Map<String, ?>, Exception> customFieldsUnsafeSupplier) {

		try {
			customFields = customFieldsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, ?> customFields;

	public PriceListDiscount[] getDiscounts() {
		return discounts;
	}

	public void setDiscounts(PriceListDiscount[] discounts) {
		this.discounts = discounts;
	}

	public void setDiscounts(
		UnsafeSupplier<PriceListDiscount[], Exception>
			discountsUnsafeSupplier) {

		try {
			discounts = discountsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PriceListDiscount[] discounts;

	public Date getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(Date displayDate) {
		this.displayDate = displayDate;
	}

	public void setDisplayDate(
		UnsafeSupplier<Date, Exception> displayDateUnsafeSupplier) {

		try {
			displayDate = displayDateUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Date displayDate;

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setExpirationDate(
		UnsafeSupplier<Date, Exception> expirationDateUnsafeSupplier) {

		try {
			expirationDate = expirationDateUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Date expirationDate;

	public String getExternalReferenceCode() {
		return externalReferenceCode;
	}

	public void setExternalReferenceCode(String externalReferenceCode) {
		this.externalReferenceCode = externalReferenceCode;
	}

	public void setExternalReferenceCode(
		UnsafeSupplier<String, Exception> externalReferenceCodeUnsafeSupplier) {

		try {
			externalReferenceCode = externalReferenceCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String externalReferenceCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String name;

	public Boolean getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(Boolean netPrice) {
		this.netPrice = netPrice;
	}

	public void setNetPrice(
		UnsafeSupplier<Boolean, Exception> netPriceUnsafeSupplier) {

		try {
			netPrice = netPriceUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean netPrice;

	public Boolean getNeverExpire() {
		return neverExpire;
	}

	public void setNeverExpire(Boolean neverExpire) {
		this.neverExpire = neverExpire;
	}

	public void setNeverExpire(
		UnsafeSupplier<Boolean, Exception> neverExpireUnsafeSupplier) {

		try {
			neverExpire = neverExpireUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean neverExpire;

	public PriceEntry[] getPriceEntries() {
		return priceEntries;
	}

	public void setPriceEntries(PriceEntry[] priceEntries) {
		this.priceEntries = priceEntries;
	}

	public void setPriceEntries(
		UnsafeSupplier<PriceEntry[], Exception> priceEntriesUnsafeSupplier) {

		try {
			priceEntries = priceEntriesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PriceEntry[] priceEntries;

	public Double getPriority() {
		return priority;
	}

	public void setPriority(Double priority) {
		this.priority = priority;
	}

	public void setPriority(
		UnsafeSupplier<Double, Exception> priorityUnsafeSupplier) {

		try {
			priority = priorityUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double priority;

	public Type getType() {
		return type;
	}

	public String getTypeAsString() {
		if (type == null) {
			return null;
		}

		return type.toString();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setType(UnsafeSupplier<Type, Exception> typeUnsafeSupplier) {
		try {
			type = typeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Type type;

	@Override
	public PriceList clone() throws CloneNotSupportedException {
		return (PriceList)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof PriceList)) {
			return false;
		}

		PriceList priceList = (PriceList)object;

		return Objects.equals(toString(), priceList.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return PriceListSerDes.toJSON(this);
	}

}