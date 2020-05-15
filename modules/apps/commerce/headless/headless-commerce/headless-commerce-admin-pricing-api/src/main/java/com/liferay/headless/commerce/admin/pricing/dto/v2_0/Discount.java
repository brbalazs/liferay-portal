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

package com.liferay.headless.commerce.admin.pricing.dto.v2_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
@GraphQLName("Discount")
@JsonFilter("Liferay.Vulcan")
@Schema(requiredProperties = {"limitationType", "target", "title"})
@XmlRootElement(name = "Discount")
public class Discount {

	@Schema
	@Valid
	public DiscountAccountGroup[] getAccountGroups() {
		return accountGroups;
	}

	public void setAccountGroups(DiscountAccountGroup[] accountGroups) {
		this.accountGroups = accountGroups;
	}

	@JsonIgnore
	public void setAccountGroups(
		UnsafeSupplier<DiscountAccountGroup[], Exception>
			accountGroupsUnsafeSupplier) {

		try {
			accountGroups = accountGroupsUnsafeSupplier.get();
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
	protected DiscountAccountGroup[] accountGroups;

	@Schema
	@Valid
	public DiscountAccount[] getAccounts() {
		return accounts;
	}

	public void setAccounts(DiscountAccount[] accounts) {
		this.accounts = accounts;
	}

	@JsonIgnore
	public void setAccounts(
		UnsafeSupplier<DiscountAccount[], Exception> accountsUnsafeSupplier) {

		try {
			accounts = accountsUnsafeSupplier.get();
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
	protected DiscountAccount[] accounts;

	@Schema
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@JsonIgnore
	public void setActive(
		UnsafeSupplier<Boolean, Exception> activeUnsafeSupplier) {

		try {
			active = activeUnsafeSupplier.get();
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
	protected Boolean active;

	@Schema
	@Valid
	public DiscountCategory[] getCategories() {
		return categories;
	}

	public void setCategories(DiscountCategory[] categories) {
		this.categories = categories;
	}

	@JsonIgnore
	public void setCategories(
		UnsafeSupplier<DiscountCategory[], Exception>
			categoriesUnsafeSupplier) {

		try {
			categories = categoriesUnsafeSupplier.get();
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
	protected DiscountCategory[] categories;

	@Schema
	@Valid
	public DiscountChannel[] getChannels() {
		return channels;
	}

	public void setChannels(DiscountChannel[] channels) {
		this.channels = channels;
	}

	@JsonIgnore
	public void setChannels(
		UnsafeSupplier<DiscountChannel[], Exception> channelsUnsafeSupplier) {

		try {
			channels = channelsUnsafeSupplier.get();
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
	protected DiscountChannel[] channels;

	@Schema
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	@JsonIgnore
	public void setCouponCode(
		UnsafeSupplier<String, Exception> couponCodeUnsafeSupplier) {

		try {
			couponCode = couponCodeUnsafeSupplier.get();
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
	protected String couponCode;

	@Schema
	@Valid
	public Map<String, ?> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, ?> customFields) {
		this.customFields = customFields;
	}

	@JsonIgnore
	public void setCustomFields(
		UnsafeSupplier<Map<String, ?>, Exception> customFieldsUnsafeSupplier) {

		try {
			customFields = customFieldsUnsafeSupplier.get();
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
	protected Map<String, ?> customFields;

	@Schema
	public Date getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(Date displayDate) {
		this.displayDate = displayDate;
	}

	@JsonIgnore
	public void setDisplayDate(
		UnsafeSupplier<Date, Exception> displayDateUnsafeSupplier) {

		try {
			displayDate = displayDateUnsafeSupplier.get();
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
	protected Date displayDate;

	@Schema
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@JsonIgnore
	public void setExpirationDate(
		UnsafeSupplier<Date, Exception> expirationDateUnsafeSupplier) {

		try {
			expirationDate = expirationDateUnsafeSupplier.get();
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
	protected Date expirationDate;

	@Schema
	public String getExternalReferenceCode() {
		return externalReferenceCode;
	}

	public void setExternalReferenceCode(String externalReferenceCode) {
		this.externalReferenceCode = externalReferenceCode;
	}

	@JsonIgnore
	public void setExternalReferenceCode(
		UnsafeSupplier<String, Exception> externalReferenceCodeUnsafeSupplier) {

		try {
			externalReferenceCode = externalReferenceCodeUnsafeSupplier.get();
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
	protected String externalReferenceCode;

	@DecimalMin("0")
	@Schema
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
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
	protected Long id;

	@Schema
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@JsonIgnore
	public void setLevel(
		UnsafeSupplier<String, Exception> levelUnsafeSupplier) {

		try {
			level = levelUnsafeSupplier.get();
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
	protected String level;

	@DecimalMin("0")
	@Schema
	public Integer getLimitationTimes() {
		return limitationTimes;
	}

	public void setLimitationTimes(Integer limitationTimes) {
		this.limitationTimes = limitationTimes;
	}

	@JsonIgnore
	public void setLimitationTimes(
		UnsafeSupplier<Integer, Exception> limitationTimesUnsafeSupplier) {

		try {
			limitationTimes = limitationTimesUnsafeSupplier.get();
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
	protected Integer limitationTimes;

	@Schema
	public String getLimitationType() {
		return limitationType;
	}

	public void setLimitationType(String limitationType) {
		this.limitationType = limitationType;
	}

	@JsonIgnore
	public void setLimitationType(
		UnsafeSupplier<String, Exception> limitationTypeUnsafeSupplier) {

		try {
			limitationType = limitationTypeUnsafeSupplier.get();
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
	@NotEmpty
	protected String limitationType;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getMaximumDiscountAmount() {
		return maximumDiscountAmount;
	}

	public void setMaximumDiscountAmount(BigDecimal maximumDiscountAmount) {
		this.maximumDiscountAmount = maximumDiscountAmount;
	}

	@JsonIgnore
	public void setMaximumDiscountAmount(
		UnsafeSupplier<BigDecimal, Exception>
			maximumDiscountAmountUnsafeSupplier) {

		try {
			maximumDiscountAmount = maximumDiscountAmountUnsafeSupplier.get();
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
	protected BigDecimal maximumDiscountAmount;

	@Schema
	public Boolean getNeverExpire() {
		return neverExpire;
	}

	public void setNeverExpire(Boolean neverExpire) {
		this.neverExpire = neverExpire;
	}

	@JsonIgnore
	public void setNeverExpire(
		UnsafeSupplier<Boolean, Exception> neverExpireUnsafeSupplier) {

		try {
			neverExpire = neverExpireUnsafeSupplier.get();
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
	protected Boolean neverExpire;

	@DecimalMin("0")
	@Schema
	public Integer getNumberOfUse() {
		return numberOfUse;
	}

	public void setNumberOfUse(Integer numberOfUse) {
		this.numberOfUse = numberOfUse;
	}

	@JsonIgnore
	public void setNumberOfUse(
		UnsafeSupplier<Integer, Exception> numberOfUseUnsafeSupplier) {

		try {
			numberOfUse = numberOfUseUnsafeSupplier.get();
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
	protected Integer numberOfUse;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getPercentageLevel1() {
		return percentageLevel1;
	}

	public void setPercentageLevel1(BigDecimal percentageLevel1) {
		this.percentageLevel1 = percentageLevel1;
	}

	@JsonIgnore
	public void setPercentageLevel1(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel1UnsafeSupplier) {

		try {
			percentageLevel1 = percentageLevel1UnsafeSupplier.get();
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
	protected BigDecimal percentageLevel1;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getPercentageLevel2() {
		return percentageLevel2;
	}

	public void setPercentageLevel2(BigDecimal percentageLevel2) {
		this.percentageLevel2 = percentageLevel2;
	}

	@JsonIgnore
	public void setPercentageLevel2(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel2UnsafeSupplier) {

		try {
			percentageLevel2 = percentageLevel2UnsafeSupplier.get();
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
	protected BigDecimal percentageLevel2;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getPercentageLevel3() {
		return percentageLevel3;
	}

	public void setPercentageLevel3(BigDecimal percentageLevel3) {
		this.percentageLevel3 = percentageLevel3;
	}

	@JsonIgnore
	public void setPercentageLevel3(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel3UnsafeSupplier) {

		try {
			percentageLevel3 = percentageLevel3UnsafeSupplier.get();
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
	protected BigDecimal percentageLevel3;

	@DecimalMin("0")
	@Schema
	@Valid
	public BigDecimal getPercentageLevel4() {
		return percentageLevel4;
	}

	public void setPercentageLevel4(BigDecimal percentageLevel4) {
		this.percentageLevel4 = percentageLevel4;
	}

	@JsonIgnore
	public void setPercentageLevel4(
		UnsafeSupplier<BigDecimal, Exception> percentageLevel4UnsafeSupplier) {

		try {
			percentageLevel4 = percentageLevel4UnsafeSupplier.get();
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
	protected BigDecimal percentageLevel4;

	@Schema
	@Valid
	public DiscountProductGroup[] getProductGroups() {
		return productGroups;
	}

	public void setProductGroups(DiscountProductGroup[] productGroups) {
		this.productGroups = productGroups;
	}

	@JsonIgnore
	public void setProductGroups(
		UnsafeSupplier<DiscountProductGroup[], Exception>
			productGroupsUnsafeSupplier) {

		try {
			productGroups = productGroupsUnsafeSupplier.get();
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
	protected DiscountProductGroup[] productGroups;

	@Schema
	@Valid
	public DiscountProduct[] getProducts() {
		return products;
	}

	public void setProducts(DiscountProduct[] products) {
		this.products = products;
	}

	@JsonIgnore
	public void setProducts(
		UnsafeSupplier<DiscountProduct[], Exception> productsUnsafeSupplier) {

		try {
			products = productsUnsafeSupplier.get();
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
	protected DiscountProduct[] products;

	@Schema
	@Valid
	public DiscountRule[] getRules() {
		return rules;
	}

	public void setRules(DiscountRule[] rules) {
		this.rules = rules;
	}

	@JsonIgnore
	public void setRules(
		UnsafeSupplier<DiscountRule[], Exception> rulesUnsafeSupplier) {

		try {
			rules = rulesUnsafeSupplier.get();
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
	protected DiscountRule[] rules;

	@Schema
	public Boolean getRulesConjuntion() {
		return rulesConjuntion;
	}

	public void setRulesConjuntion(Boolean rulesConjuntion) {
		this.rulesConjuntion = rulesConjuntion;
	}

	@JsonIgnore
	public void setRulesConjuntion(
		UnsafeSupplier<Boolean, Exception> rulesConjuntionUnsafeSupplier) {

		try {
			rulesConjuntion = rulesConjuntionUnsafeSupplier.get();
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
	protected Boolean rulesConjuntion;

	@Schema
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@JsonIgnore
	public void setTarget(
		UnsafeSupplier<String, Exception> targetUnsafeSupplier) {

		try {
			target = targetUnsafeSupplier.get();
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
	@NotEmpty
	protected String target;

	@Schema
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	public void setTitle(
		UnsafeSupplier<String, Exception> titleUnsafeSupplier) {

		try {
			title = titleUnsafeSupplier.get();
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
	@NotEmpty
	protected String title;

	@Schema
	public Boolean getUseCouponCode() {
		return useCouponCode;
	}

	public void setUseCouponCode(Boolean useCouponCode) {
		this.useCouponCode = useCouponCode;
	}

	@JsonIgnore
	public void setUseCouponCode(
		UnsafeSupplier<Boolean, Exception> useCouponCodeUnsafeSupplier) {

		try {
			useCouponCode = useCouponCodeUnsafeSupplier.get();
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
	protected Boolean useCouponCode;

	@Schema
	public Boolean getUsePercentage() {
		return usePercentage;
	}

	public void setUsePercentage(Boolean usePercentage) {
		this.usePercentage = usePercentage;
	}

	@JsonIgnore
	public void setUsePercentage(
		UnsafeSupplier<Boolean, Exception> usePercentageUnsafeSupplier) {

		try {
			usePercentage = usePercentageUnsafeSupplier.get();
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
	protected Boolean usePercentage;

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
		StringBundler sb = new StringBundler();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

		if (accountGroups != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountGroups\": ");

			sb.append("[");

			for (int i = 0; i < accountGroups.length; i++) {
				sb.append(String.valueOf(accountGroups[i]));

				if ((i + 1) < accountGroups.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (accounts != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accounts\": ");

			sb.append("[");

			for (int i = 0; i < accounts.length; i++) {
				sb.append(String.valueOf(accounts[i]));

				if ((i + 1) < accounts.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (active != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"active\": ");

			sb.append(active);
		}

		if (categories != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"categories\": ");

			sb.append("[");

			for (int i = 0; i < categories.length; i++) {
				sb.append(String.valueOf(categories[i]));

				if ((i + 1) < categories.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (channels != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"channels\": ");

			sb.append("[");

			for (int i = 0; i < channels.length; i++) {
				sb.append(String.valueOf(channels[i]));

				if ((i + 1) < channels.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (couponCode != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"couponCode\": ");

			sb.append("\"");

			sb.append(_escape(couponCode));

			sb.append("\"");
		}

		if (customFields != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"customFields\": ");

			sb.append(_toJSON(customFields));
		}

		if (displayDate != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"displayDate\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(displayDate));

			sb.append("\"");
		}

		if (expirationDate != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"expirationDate\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(expirationDate));

			sb.append("\"");
		}

		if (externalReferenceCode != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"externalReferenceCode\": ");

			sb.append("\"");

			sb.append(_escape(externalReferenceCode));

			sb.append("\"");
		}

		if (id != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(id);
		}

		if (level != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"level\": ");

			sb.append("\"");

			sb.append(_escape(level));

			sb.append("\"");
		}

		if (limitationTimes != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"limitationTimes\": ");

			sb.append(limitationTimes);
		}

		if (limitationType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"limitationType\": ");

			sb.append("\"");

			sb.append(_escape(limitationType));

			sb.append("\"");
		}

		if (maximumDiscountAmount != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maximumDiscountAmount\": ");

			sb.append(maximumDiscountAmount);
		}

		if (neverExpire != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"neverExpire\": ");

			sb.append(neverExpire);
		}

		if (numberOfUse != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"numberOfUse\": ");

			sb.append(numberOfUse);
		}

		if (percentageLevel1 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"percentageLevel1\": ");

			sb.append(percentageLevel1);
		}

		if (percentageLevel2 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"percentageLevel2\": ");

			sb.append(percentageLevel2);
		}

		if (percentageLevel3 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"percentageLevel3\": ");

			sb.append(percentageLevel3);
		}

		if (percentageLevel4 != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"percentageLevel4\": ");

			sb.append(percentageLevel4);
		}

		if (productGroups != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"productGroups\": ");

			sb.append("[");

			for (int i = 0; i < productGroups.length; i++) {
				sb.append(String.valueOf(productGroups[i]));

				if ((i + 1) < productGroups.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (products != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"products\": ");

			sb.append("[");

			for (int i = 0; i < products.length; i++) {
				sb.append(String.valueOf(products[i]));

				if ((i + 1) < products.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (rules != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"rules\": ");

			sb.append("[");

			for (int i = 0; i < rules.length; i++) {
				sb.append(String.valueOf(rules[i]));

				if ((i + 1) < rules.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (rulesConjuntion != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"rulesConjuntion\": ");

			sb.append(rulesConjuntion);
		}

		if (target != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"target\": ");

			sb.append("\"");

			sb.append(_escape(target));

			sb.append("\"");
		}

		if (title != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"title\": ");

			sb.append("\"");

			sb.append(_escape(title));

			sb.append("\"");
		}

		if (useCouponCode != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"useCouponCode\": ");

			sb.append(useCouponCode);
		}

		if (usePercentage != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"usePercentage\": ");

			sb.append(usePercentage);
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		defaultValue = "com.liferay.headless.commerce.admin.pricing.dto.v2_0.Discount",
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

			Object value = entry.getValue();

			Class<?> clazz = value.getClass();

			if (clazz.isArray()) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(value);
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}