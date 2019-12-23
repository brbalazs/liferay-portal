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

package com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0;

import com.liferay.headless.commerce.delivery.catalog.client.function.UnsafeSupplier;
import com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0.AvailabilitySerDes;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class Availability {

	public String getAvailabilityLabel() {
		return availabilityLabel;
	}

	public void setAvailabilityLabel(String availabilityLabel) {
		this.availabilityLabel = availabilityLabel;
	}

	public void setAvailabilityLabel(
		UnsafeSupplier<String, Exception> availabilityLabelUnsafeSupplier) {

		try {
			availabilityLabel = availabilityLabelUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String availabilityLabel;

	public Integer getAvailabilityNumber() {
		return availabilityNumber;
	}

	public void setAvailabilityNumber(Integer availabilityNumber) {
		this.availabilityNumber = availabilityNumber;
	}

	public void setAvailabilityNumber(
		UnsafeSupplier<Integer, Exception> availabilityNumberUnsafeSupplier) {

		try {
			availabilityNumber = availabilityNumberUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

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
		return AvailabilitySerDes.toJSON(this);
	}

}