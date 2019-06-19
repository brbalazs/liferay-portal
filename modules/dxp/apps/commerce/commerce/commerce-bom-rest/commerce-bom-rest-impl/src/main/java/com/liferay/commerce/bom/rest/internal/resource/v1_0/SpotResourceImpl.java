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

package com.liferay.commerce.bom.rest.internal.resource.v1_0;

import com.liferay.commerce.bom.rest.dto.v1_0.Spot;
import com.liferay.commerce.bom.rest.resource.v1_0.SpotResource;

import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/spot.properties",
	scope = ServiceScope.PROTOTYPE, service = SpotResource.class
)
public class SpotResourceImpl extends BaseSpotResourceImpl {

	@Override
	public Response deleteAreaIdSpot(Long id, Long spotId) throws Exception {
		return super.deleteAreaIdSpot(id, spotId);
	}

	@Override
	public Spot postAreaIdSpot(Long id, Spot spot) throws Exception {
		return super.postAreaIdSpot(id, spot);
	}

	@Override
	public Response putAreaIdSpot(Long id, Long spotId, Spot spot)
		throws Exception {

		return super.putAreaIdSpot(id, spotId, spot);
	}

}