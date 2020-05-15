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
import com.liferay.headless.commerce.admin.pricing.client.serdes.v2_0.DiscountSerDes;

import java.math.BigDecimal;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public class Discount implements Cloneable {

	public DiscountAccountGroup[] getAccountGroups() {
		return accountGroups;
	}

	public void setAccountGroups(DiscountAccountGroup[] accountGroups) {
		this.accountGroups = accountGroups;
	}

	public void setAccountGroups(
		UnsafeSupplier<DiscountAccountGroup[], Exception>
			accountGroupsUnsafeSupplier) {

		try {
			accountGroups = accountGroupsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DiscountAccountGroup[] accountGroups;

	public DiscountAccount[] getAccounts() {
		return accounts;
	}

	public void setAccounts(DiscountAccount[] accounts) {
		this.accounts = accounts;
	}

	public void setAccounts(
		UnsafeSupplier<DiscountAccount[], Exception> accountsUnsafeSupplier) {

		try {
			accounts = accountsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DiscountAccount[] accounts;

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

	public DiscountCategory[] getCategories() {
		return categories;
	}

	public void setCategories(DiscountCategory[] categories) {
		this.categories = categories;
	}

	public void setCategories(
		UnsafeSupplier<DiscountCategory[], Exception>
			categoriesUnsafeSupplier) {

		try {
			categories = categoriesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DiscountCategory[] categories;

	public DiscountChannel[] getChannels() {
		return channels;
	}

	public void setChannels(DiscountChannel[] channels) {
		this.channels = channels;
	}

	public void setChannels(
		UnsafeSupplier<DiscountChannel[], Exception> channelsUnsafeSupplier) {

		try {
			channels = channelsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DiscountChannel[] channels;

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public void setCouponCode(
		UnsafeSupplier<String, Exception> couponCodeUnsafeSupplier) {

		try {
			couponCode = couponCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String couponCode;

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setLevel(
		UnsafeSupplier<String, Exception> levelUnsafeSupplier) {

		try {
			level = levelUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String level;

	public Integer getLimitationTimes() {
		return limitationTimes;
	}

	public void setLimitationTimes(Integer limitationTimes) {
		this.limitationTimes = limitationTimes;
	}

	public void setLimitationTimes(
		UnsafeSupplier<Integer, Exception> limitationTimesUnsafeSupplier) {

		try {
			limitationTimes = limitationTimesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer limitationTimes;

	public String getLimitationType() {
		return limitationType;
	}

	public void setLimitationType(String limitationType) {
		this.limitationType = limitationType;
	}

	public void setLimitationType(
		UnsafeSupplier<String, Exception> limitationTypeUnsafeSupplier) {

		try {
			limitationType = limitationTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String limitationType;

	public BigDecimal getMaximumDiscountAmount() {
		return maximumDiscountAmount;
	}

	public void setMaximumDiscountAmount(BigDecimal maximumDiscountAmount) {
		this.maximumDiscountAmount = maximumDiscountAmount;
	}

	public void setMaximumDiscountAmount(
		UnsafeSupplier<BigDecimal, Exception>
			maximumDiscountAmountUnsafeSupplier) {

		try {
			maximumDiscountAmount = maximumDiscountAmountUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal maximumDiscountAmount;

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

	public Integer getNumberOfUse() {
		return numberOfUse;
	}

	public void setNumberOfUse(Integer numberOfUse) {
		this.numberOfUse = numberOfUse;
	}

	public void setNumberOfUse(
		UnsafeSupplier<Integer, Exception> numberOfUseUnsafeSupplier) {

		try {
			numberOfUse = numberOfUseUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer numberOfUse;

	public BigDecimal getPercentageLevel1() {
		return percentageLevel1;
	}

	public void setPercentageLevel1(BigDecimal percentageLevel1) {
		this.percentageLevel1 = percentageLevel1;
	}

	public void setPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel1UnsafeSupplier) {

		try {
			percentageLevel1 = percentageLevel1UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal percentageLevel1;

	public BigDecimal getPercentageLevel2() {
		return percentageLevel2;
	}

	public void setPercentageLevel2(BigDecimal percentageLevel2) {
		this.percentageLevel2 = percentageLevel2;
	}

	public void setPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel2UnsafeSupplier) {

		try {
			percentageLevel2 = percentageLevel2UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal percentageLevel2;

	public BigDecimal getPercentageLevel3() {
		return percentageLevel3;
	}

	public void setPercentageLevel3(BigDecimal percentageLevel3) {
		this.percentageLevel3 = percentageLevel3;
	}

	public void setPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel3UnsafeSupplier) {

		try {
			percentageLevel3 = percentageLevel3UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal percentageLevel3;

	public BigDecimal getPercentageLevel4() {
		return percentageLevel4;
	}

	public void setPercentageLevel4(BigDecimal percentageLevel4) {
		this.percentageLevel4 = percentageLevel4;
	}

	public void setPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel4UnsafeSupplier) {

		try {
			percentageLevel4 = percentageLevel4UnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BigDecimal percentageLevel4;

	public DiscountProductGroup[] getProductGroups() {
		return productGroups;
	}

	public void setProductGroups(DiscountProductGroup[] productGroups) {
		this.productGroups = productGroups;
	}

	public void setProductGroups(
		UnsafeSupplier<DiscountProductGroup[], Exception>
			productGroupsUnsafeSupplier) {

		try {
			productGroups = productGroupsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DiscountProductGroup[] productGroups;

	public DiscountProduct[] getProducts() {
		return products;
	}

	public void setProducts(DiscountProduct[] products) {
		this.products = products;
	}

	public void setProducts(
		UnsafeSupplier<DiscountProduct[], Exception> productsUnsafeSupplier) {

		try {
			products = productsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DiscountProduct[] products;

	public DiscountRule[] getRules() {
		return rules;
	}

	public void setRules(DiscountRule[] rules) {
		this.rules = rules;
	}

	public void setRules(
		UnsafeSupplier<DiscountRule[], Exception> rulesUnsafeSupplier) {

		try {
			rules = rulesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DiscountRule[] rules;

	public Boolean getRulesConjuntion() {
		return rulesConjuntion;
	}

	public void setRulesConjuntion(Boolean rulesConjuntion) {
		this.rulesConjuntion = rulesConjuntion;
	}

	public void setRulesConjuntion(
		UnsafeSupplier<Boolean, Exception> rulesConjuntionUnsafeSupplier) {

		try {
			rulesConjuntion = rulesConjuntionUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean rulesConjuntion;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setTarget(
		UnsafeSupplier<String, Exception> targetUnsafeSupplier) {

		try {
			target = targetUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String target;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitle(
		UnsafeSupplier<String, Exception> titleUnsafeSupplier) {

		try {
			title = titleUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String title;

	public Boolean getUseCouponCode() {
		return useCouponCode;
	}

	public void setUseCouponCode(Boolean useCouponCode) {
		this.useCouponCode = useCouponCode;
	}

	public void setUseCouponCode(
		UnsafeSupplier<Boolean, Exception> useCouponCodeUnsafeSupplier) {

		try {
			useCouponCode = useCouponCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean useCouponCode;

	public Boolean getUsePercentage() {
		return usePercentage;
	}

	public void setUsePercentage(Boolean usePercentage) {
		this.usePercentage = usePercentage;
	}

	public void setUsePercentage(
		UnsafeSupplier<Boolean, Exception> usePercentageUnsafeSupplier) {

		try {
			usePercentage = usePercentageUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean usePercentage;

	@Override
	public Discount clone() throws CloneNotSupportedException {
		return (Discount)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Discount)) {
			return false;
		}

		Discount discount = (Discount)object;

		return Objects.equals(toString(), discount.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return DiscountSerDes.toJSON(this);
	}

}