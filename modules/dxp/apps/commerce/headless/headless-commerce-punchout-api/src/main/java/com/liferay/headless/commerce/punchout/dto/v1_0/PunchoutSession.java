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

package com.liferay.headless.commerce.punchout.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jaclyn Ong
 * @generated
 */
@Generated("")
@GraphQLName("PunchoutSession")
@JsonFilter("Liferay.Vulcan")
@Schema(
	requiredProperties = {
		"buyerAccountReferenceCode", "buyerGroup", "punchoutReturnURL",
		"punchoutSessionType", "buyerUser", "cart"
	}
)
@XmlRootElement(name = "PunchoutSession")
public class PunchoutSession {

	public static PunchoutSession toDTO(String json) {
		return ObjectMapperUtil.readValue(PunchoutSession.class, json);
	}

	@Schema
	public String getBuyerAccountReferenceCode() {
		return buyerAccountReferenceCode;
	}

	public void setBuyerAccountReferenceCode(String buyerAccountReferenceCode) {
		this.buyerAccountReferenceCode = buyerAccountReferenceCode;
	}

	@JsonIgnore
	public void setBuyerAccountReferenceCode(
		UnsafeSupplier<String, Exception>
			buyerAccountReferenceCodeUnsafeSupplier) {

		try {
			buyerAccountReferenceCode =
				buyerAccountReferenceCodeUnsafeSupplier.get();
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
	protected String buyerAccountReferenceCode;

	@Schema
	@Valid
	public Group getBuyerGroup() {
		return buyerGroup;
	}

	public void setBuyerGroup(Group buyerGroup) {
		this.buyerGroup = buyerGroup;
	}

	@JsonIgnore
	public void setBuyerGroup(
		UnsafeSupplier<Group, Exception> buyerGroupUnsafeSupplier) {

		try {
			buyerGroup = buyerGroupUnsafeSupplier.get();
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
	@NotNull
	protected Group buyerGroup;

	@Schema
	@Valid
	public Organization getBuyerOrganization() {
		return buyerOrganization;
	}

	public void setBuyerOrganization(Organization buyerOrganization) {
		this.buyerOrganization = buyerOrganization;
	}

	@JsonIgnore
	public void setBuyerOrganization(
		UnsafeSupplier<Organization, Exception>
			buyerOrganizationUnsafeSupplier) {

		try {
			buyerOrganization = buyerOrganizationUnsafeSupplier.get();
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
	protected Organization buyerOrganization;

	@Schema
	@Valid
	public User getBuyerUser() {
		return buyerUser;
	}

	public void setBuyerUser(User buyerUser) {
		this.buyerUser = buyerUser;
	}

	@JsonIgnore
	public void setBuyerUser(
		UnsafeSupplier<User, Exception> buyerUserUnsafeSupplier) {

		try {
			buyerUser = buyerUserUnsafeSupplier.get();
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
	@NotNull
	protected User buyerUser;

	@Schema
	@Valid
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@JsonIgnore
	public void setCart(UnsafeSupplier<Cart, Exception> cartUnsafeSupplier) {
		try {
			cart = cartUnsafeSupplier.get();
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
	@NotNull
	protected Cart cart;

	@Schema
	public String getPunchoutReturnURL() {
		return punchoutReturnURL;
	}

	public void setPunchoutReturnURL(String punchoutReturnURL) {
		this.punchoutReturnURL = punchoutReturnURL;
	}

	@JsonIgnore
	public void setPunchoutReturnURL(
		UnsafeSupplier<String, Exception> punchoutReturnURLUnsafeSupplier) {

		try {
			punchoutReturnURL = punchoutReturnURLUnsafeSupplier.get();
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
	protected String punchoutReturnURL;

	@Schema
	public String getPunchoutSessionType() {
		return punchoutSessionType;
	}

	public void setPunchoutSessionType(String punchoutSessionType) {
		this.punchoutSessionType = punchoutSessionType;
	}

	@JsonIgnore
	public void setPunchoutSessionType(
		UnsafeSupplier<String, Exception> punchoutSessionTypeUnsafeSupplier) {

		try {
			punchoutSessionType = punchoutSessionTypeUnsafeSupplier.get();
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
	protected String punchoutSessionType;

	@Schema
	public String getPunchoutStartURL() {
		return punchoutStartURL;
	}

	public void setPunchoutStartURL(String punchoutStartURL) {
		this.punchoutStartURL = punchoutStartURL;
	}

	@JsonIgnore
	public void setPunchoutStartURL(
		UnsafeSupplier<String, Exception> punchoutStartURLUnsafeSupplier) {

		try {
			punchoutStartURL = punchoutStartURLUnsafeSupplier.get();
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
	protected String punchoutStartURL;

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
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (buyerAccountReferenceCode != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerAccountReferenceCode\": ");

			sb.append("\"");

			sb.append(_escape(buyerAccountReferenceCode));

			sb.append("\"");
		}

		if (buyerGroup != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerGroup\": ");

			sb.append(String.valueOf(buyerGroup));
		}

		if (buyerOrganization != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerOrganization\": ");

			sb.append(String.valueOf(buyerOrganization));
		}

		if (buyerUser != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerUser\": ");

			sb.append(String.valueOf(buyerUser));
		}

		if (cart != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"cart\": ");

			sb.append(String.valueOf(cart));
		}

		if (punchoutReturnURL != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"punchoutReturnURL\": ");

			sb.append("\"");

			sb.append(_escape(punchoutReturnURL));

			sb.append("\"");
		}

		if (punchoutSessionType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"punchoutSessionType\": ");

			sb.append("\"");

			sb.append(_escape(punchoutSessionType));

			sb.append("\"");
		}

		if (punchoutStartURL != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"punchoutStartURL\": ");

			sb.append("\"");

			sb.append(_escape(punchoutStartURL));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		defaultValue = "com.liferay.headless.commerce.punchout.dto.v1_0.PunchoutSession",
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