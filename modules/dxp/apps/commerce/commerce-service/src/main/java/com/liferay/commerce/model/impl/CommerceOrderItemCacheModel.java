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

package com.liferay.commerce.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.model.CommerceOrderItem;

import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.math.BigDecimal;

import java.util.Date;

/**
 * The cache model class for representing CommerceOrderItem in entity cache.
 *
 * @author Alessio Antonio Rendina
 * @see CommerceOrderItem
 * @generated
 */
@ProviderType
public class CommerceOrderItemCacheModel implements CacheModel<CommerceOrderItem>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CommerceOrderItemCacheModel)) {
			return false;
		}

		CommerceOrderItemCacheModel commerceOrderItemCacheModel = (CommerceOrderItemCacheModel)obj;

		if (commerceOrderItemId == commerceOrderItemCacheModel.commerceOrderItemId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, commerceOrderItemId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(43);

		sb.append("{commerceOrderItemId=");
		sb.append(commerceOrderItemId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", commerceOrderId=");
		sb.append(commerceOrderId);
		sb.append(", CPInstanceId=");
		sb.append(CPInstanceId);
		sb.append(", quantity=");
		sb.append(quantity);
		sb.append(", shippedQuantity=");
		sb.append(shippedQuantity);
		sb.append(", json=");
		sb.append(json);
		sb.append(", name=");
		sb.append(name);
		sb.append(", sku=");
		sb.append(sku);
		sb.append(", unitPrice=");
		sb.append(unitPrice);
		sb.append(", discountAmount=");
		sb.append(discountAmount);
		sb.append(", finalPrice=");
		sb.append(finalPrice);
		sb.append(", discountPersentageLeve1=");
		sb.append(discountPersentageLeve1);
		sb.append(", discountPersentageLeve2=");
		sb.append(discountPersentageLeve2);
		sb.append(", discountPersentageLeve3=");
		sb.append(discountPersentageLeve3);
		sb.append(", discountPersentageLeve4=");
		sb.append(discountPersentageLeve4);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public CommerceOrderItem toEntityModel() {
		CommerceOrderItemImpl commerceOrderItemImpl = new CommerceOrderItemImpl();

		commerceOrderItemImpl.setCommerceOrderItemId(commerceOrderItemId);
		commerceOrderItemImpl.setGroupId(groupId);
		commerceOrderItemImpl.setCompanyId(companyId);
		commerceOrderItemImpl.setUserId(userId);

		if (userName == null) {
			commerceOrderItemImpl.setUserName("");
		}
		else {
			commerceOrderItemImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			commerceOrderItemImpl.setCreateDate(null);
		}
		else {
			commerceOrderItemImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			commerceOrderItemImpl.setModifiedDate(null);
		}
		else {
			commerceOrderItemImpl.setModifiedDate(new Date(modifiedDate));
		}

		commerceOrderItemImpl.setCommerceOrderId(commerceOrderId);
		commerceOrderItemImpl.setCPInstanceId(CPInstanceId);
		commerceOrderItemImpl.setQuantity(quantity);
		commerceOrderItemImpl.setShippedQuantity(shippedQuantity);

		if (json == null) {
			commerceOrderItemImpl.setJson("");
		}
		else {
			commerceOrderItemImpl.setJson(json);
		}

		if (name == null) {
			commerceOrderItemImpl.setName("");
		}
		else {
			commerceOrderItemImpl.setName(name);
		}

		if (sku == null) {
			commerceOrderItemImpl.setSku("");
		}
		else {
			commerceOrderItemImpl.setSku(sku);
		}

		commerceOrderItemImpl.setUnitPrice(unitPrice);
		commerceOrderItemImpl.setDiscountAmount(discountAmount);
		commerceOrderItemImpl.setFinalPrice(finalPrice);
		commerceOrderItemImpl.setDiscountPersentageLeve1(discountPersentageLeve1);
		commerceOrderItemImpl.setDiscountPersentageLeve2(discountPersentageLeve2);
		commerceOrderItemImpl.setDiscountPersentageLeve3(discountPersentageLeve3);
		commerceOrderItemImpl.setDiscountPersentageLeve4(discountPersentageLeve4);

		commerceOrderItemImpl.resetOriginalValues();

		return commerceOrderItemImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {
		commerceOrderItemId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		commerceOrderId = objectInput.readLong();

		CPInstanceId = objectInput.readLong();

		quantity = objectInput.readInt();

		shippedQuantity = objectInput.readInt();
		json = objectInput.readUTF();
		name = objectInput.readUTF();
		sku = objectInput.readUTF();
		unitPrice = (BigDecimal)objectInput.readObject();
		discountAmount = (BigDecimal)objectInput.readObject();
		finalPrice = (BigDecimal)objectInput.readObject();
		discountPersentageLeve1 = (BigDecimal)objectInput.readObject();
		discountPersentageLeve2 = (BigDecimal)objectInput.readObject();
		discountPersentageLeve3 = (BigDecimal)objectInput.readObject();
		discountPersentageLeve4 = (BigDecimal)objectInput.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(commerceOrderItemId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(commerceOrderId);

		objectOutput.writeLong(CPInstanceId);

		objectOutput.writeInt(quantity);

		objectOutput.writeInt(shippedQuantity);

		if (json == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(json);
		}

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (sku == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(sku);
		}

		objectOutput.writeObject(unitPrice);
		objectOutput.writeObject(discountAmount);
		objectOutput.writeObject(finalPrice);
		objectOutput.writeObject(discountPersentageLeve1);
		objectOutput.writeObject(discountPersentageLeve2);
		objectOutput.writeObject(discountPersentageLeve3);
		objectOutput.writeObject(discountPersentageLeve4);
	}

	public long commerceOrderItemId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long commerceOrderId;
	public long CPInstanceId;
	public int quantity;
	public int shippedQuantity;
	public String json;
	public String name;
	public String sku;
	public BigDecimal unitPrice;
	public BigDecimal discountAmount;
	public BigDecimal finalPrice;
	public BigDecimal discountPersentageLeve1;
	public BigDecimal discountPersentageLeve2;
	public BigDecimal discountPersentageLeve3;
	public BigDecimal discountPersentageLeve4;
}