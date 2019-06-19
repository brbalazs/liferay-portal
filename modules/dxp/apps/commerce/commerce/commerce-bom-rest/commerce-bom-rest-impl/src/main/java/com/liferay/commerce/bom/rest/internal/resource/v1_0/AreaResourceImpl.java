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

import com.liferay.commerce.bom.rest.dto.v1_0.Area;
import com.liferay.commerce.bom.rest.resource.v1_0.AreaResource;
import com.liferay.portal.vulcan.pagination.Pagination;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/area.properties",
	scope = ServiceScope.PROTOTYPE, service = AreaResource.class
)
public class AreaResourceImpl extends BaseAreaResourceImpl {

	@Override
	public Area getArea(Long id, Pagination pagination) throws Exception {
		return super.getArea(id, pagination);
	}

}