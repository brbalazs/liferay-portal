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

package com.liferay.headless.commerce.delivery.catalog.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
@GraphQLName("Availability")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "Availability")
public class Availability {

	@Schema
	public String getAvailabilityLabel() {
		return availabilityLabel;
	}

	public void setAvailabilityLabel(String availabilityLabel) {
		this.availabilityLabel = availabilityLabel;
	}

	@JsonIgnore
	public void setAvailabilityLabel(
		UnsafeSupplier<String, Exception> availabilityLabelUnsafeSupplier) {

		try {
			availabilityLabel = availabilityLabelUnsafeSupplier.get();
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
	protected String availabilityLabel;

	@Schema
	public Integer getAvailabilityNumber() {
		return availabilityNumber;
	}

	public void setAvailabilityNumber(Integer availabilityNumber) {
		this.availabilityNumber = availabilityNumber;
	}

	@JsonIgnore
	public void setAvailabilityNumber(
		UnsafeSupplier<Integer, Exception> availabilityNumberUnsafeSupplier) {

		try {
			availabilityNumber = availabilityNumberUnsafeSupplier.get();
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
	protected Integer availabilityNumber;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Availability)) {
			return false;
		}

		Availability availability = (Availability)object;

		return Objects.equals(toString(), availability.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (availabilityLabel != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"availabilityLabel\": ");

			sb.append("\"");

			sb.append(_escape(availabilityLabel));

			sb.append("\"");
		}

		if (availabilityNumber != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"availabilityNumber\": ");

			sb.append(availabilityNumber);
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		defaultValue = "com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Availability",
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