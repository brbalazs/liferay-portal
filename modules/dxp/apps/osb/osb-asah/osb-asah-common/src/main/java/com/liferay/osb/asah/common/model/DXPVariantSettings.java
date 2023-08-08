/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Marcellus Tavares
 */
public class DXPVariantSettings {

	public DXPVariantSettings() {
	}

	public DXPVariantSettings(
		boolean control, String dxpVariantId, Double trafficSplit) {

		_control = control;
		_dxpVariantId = dxpVariantId;
		_trafficSplit = trafficSplit;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DXPVariantSettings)) {
			return false;
		}

		DXPVariantSettings dxpVariantSettings = (DXPVariantSettings)obj;

		if (Objects.equals(_control, dxpVariantSettings._control) &&
			Objects.equals(_dxpVariantId, dxpVariantSettings._dxpVariantId) &&
			Objects.equals(_trafficSplit, dxpVariantSettings._trafficSplit)) {

			return true;
		}

		return false;
	}

	@JsonProperty("dxpVariantId")
	@NotBlank
	public String getDXPVariantId() {
		return _dxpVariantId;
	}

	@Max(100)
	@Min(0)
	public Double getTrafficSplit() {
		return _trafficSplit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_control, _dxpVariantId, _trafficSplit);
	}

	public boolean isControl() {
		return _control;
	}

	public void setControl(boolean control) {
		_control = control;
	}

	public void setDXPVariantId(String dxpVariantId) {
		_dxpVariantId = dxpVariantId;
	}

	public void setTrafficSplit(Double trafficSplit) {
		_trafficSplit = trafficSplit;
	}

	private boolean _control;
	private String _dxpVariantId;
	private Double _trafficSplit;

}