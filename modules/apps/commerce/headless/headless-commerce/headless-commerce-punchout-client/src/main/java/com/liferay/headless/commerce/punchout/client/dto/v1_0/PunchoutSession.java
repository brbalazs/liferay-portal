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
import com.liferay.headless.commerce.punchout.client.serdes.v1_0.PunchoutSessionSerDes;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Jaclyn Ong
 * @generated
 */
@Generated("")
public class PunchoutSession implements Cloneable {

	public static PunchoutSession toDTO(String json) {
		return PunchoutSessionSerDes.toDTO(json);
	}

	public String getBuyerAccountReferenceCode() {
		return buyerAccountReferenceCode;
	}

	public void setBuyerAccountReferenceCode(String buyerAccountReferenceCode) {
		this.buyerAccountReferenceCode = buyerAccountReferenceCode;
	}

	public void setBuyerAccountReferenceCode(
		UnsafeSupplier<String, Exception>
			buyerAccountReferenceCodeUnsafeSupplier) {

		try {
			buyerAccountReferenceCode =
				buyerAccountReferenceCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String buyerAccountReferenceCode;

	public Group getBuyerGroup() {
		return buyerGroup;
	}

	public void setBuyerGroup(Group buyerGroup) {
		this.buyerGroup = buyerGroup;
	}

	public void setBuyerGroup(
		UnsafeSupplier<Group, Exception> buyerGroupUnsafeSupplier) {

		try {
			buyerGroup = buyerGroupUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Group buyerGroup;

	public Organization getBuyerOrganization() {
		return buyerOrganization;
	}

	public void setBuyerOrganization(Organization buyerOrganization) {
		this.buyerOrganization = buyerOrganization;
	}

	public void setBuyerOrganization(
		UnsafeSupplier<Organization, Exception>
			buyerOrganizationUnsafeSupplier) {

		try {
			buyerOrganization = buyerOrganizationUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Organization buyerOrganization;

	public User getBuyerUser() {
		return buyerUser;
	}

	public void setBuyerUser(User buyerUser) {
		this.buyerUser = buyerUser;
	}

	public void setBuyerUser(
		UnsafeSupplier<User, Exception> buyerUserUnsafeSupplier) {

		try {
			buyerUser = buyerUserUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected User buyerUser;

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public void setCart(UnsafeSupplier<Cart, Exception> cartUnsafeSupplier) {
		try {
			cart = cartUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Cart cart;

	public String getPunchoutReturnURL() {
		return punchoutReturnURL;
	}

	public void setPunchoutReturnURL(String punchoutReturnURL) {
		this.punchoutReturnURL = punchoutReturnURL;
	}

	public void setPunchoutReturnURL(
		UnsafeSupplier<String, Exception> punchoutReturnURLUnsafeSupplier) {

		try {
			punchoutReturnURL = punchoutReturnURLUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String punchoutReturnURL;

	public String getPunchoutSessionType() {
		return punchoutSessionType;
	}

	public void setPunchoutSessionType(String punchoutSessionType) {
		this.punchoutSessionType = punchoutSessionType;
	}

	public void setPunchoutSessionType(
		UnsafeSupplier<String, Exception> punchoutSessionTypeUnsafeSupplier) {

		try {
			punchoutSessionType = punchoutSessionTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String punchoutSessionType;

	public String getPunchoutStartURL() {
		return punchoutStartURL;
	}

	public void setPunchoutStartURL(String punchoutStartURL) {
		this.punchoutStartURL = punchoutStartURL;
	}

	public void setPunchoutStartURL(
		UnsafeSupplier<String, Exception> punchoutStartURLUnsafeSupplier) {

		try {
			punchoutStartURL = punchoutStartURLUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String punchoutStartURL;

	@Override
	public PunchoutSession clone() throws CloneNotSupportedException {
		return (PunchoutSession)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof PunchoutSession)) {
			return false;
		}

		PunchoutSession punchoutSession = (PunchoutSession)object;

		return Objects.equals(toString(), punchoutSession.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return PunchoutSessionSerDes.toJSON(this);
	}

}