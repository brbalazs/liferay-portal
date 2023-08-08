/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.AsahMarker;
import com.liferay.osb.asah.common.repository.AsahMarkerRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class AsahMarkerDog {

	public AsahMarker addAsahMarker(AsahMarker asahMarker) {
		if (!asahMarker.isNew()) {
			throw new IllegalArgumentException("Unable to add Asah Marker");
		}

		return _asahMarkerRepository.save(asahMarker);
	}

	public void deleteAsahMarker(String asahMarkerId) {
		_asahMarkerRepository.deleteById(asahMarkerId);
	}

	public void deleteAsahMarkers(List<String> asahMarkerIds) {
		for (String asahMarkerId : asahMarkerIds) {
			_asahMarkerRepository.deleteById(asahMarkerId);
		}
	}

	public AsahMarker fetchAsahMarker(String asahMarkerId) {
		Optional<AsahMarker> asahMarkerOptional =
			_asahMarkerRepository.findById(asahMarkerId);

		AsahMarker asahMarker = asahMarkerOptional.orElse(null);

		if (asahMarker != null) {
			asahMarker.setIsNew(Boolean.FALSE);
		}

		return asahMarker;
	}

	public AsahMarker getAsahMarker(String asahMarkerId) {
		AsahMarker asahMarker = fetchAsahMarker(asahMarkerId);

		if (asahMarker == null) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no Asah Marker with ID " + asahMarkerId);
		}

		return asahMarker;
	}

	public AsahMarker updateAsahMarker(AsahMarker asahMarker) {
		if (asahMarker.isNew()) {
			throw new IllegalArgumentException("Unable to update Asah Marker");
		}

		return _asahMarkerRepository.save(asahMarker);
	}

	@Autowired
	private AsahMarkerRepository _asahMarkerRepository;

}