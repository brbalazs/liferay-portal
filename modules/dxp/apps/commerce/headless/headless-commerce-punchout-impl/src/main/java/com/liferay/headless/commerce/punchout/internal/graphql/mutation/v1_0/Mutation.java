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

package com.liferay.headless.commerce.punchout.internal.graphql.mutation.v1_0;

import com.liferay.headless.commerce.punchout.dto.v1_0.PunchoutSession;
import com.liferay.headless.commerce.punchout.dto.v1_0.User;
import com.liferay.headless.commerce.punchout.resource.v1_0.PunchoutSessionResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Jaclyn Ong
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setPunchoutSessionResourceComponentServiceObjects(
		ComponentServiceObjects<PunchoutSessionResource>
			punchoutSessionResourceComponentServiceObjects) {

		_punchoutSessionResourceComponentServiceObjects =
			punchoutSessionResourceComponentServiceObjects;
	}

	@GraphQLField(description = "Create a punchout session.")
	public PunchoutSession createPunchoutSessionRequest(
			@GraphQLName("punchoutSession") PunchoutSession punchoutSession)
		throws Exception {

		return _applyComponentServiceObjects(
			_punchoutSessionResourceComponentServiceObjects,
			this::_populateResourceContext,
			punchoutSessionResource ->
				punchoutSessionResource.postPunchoutSessionRequest(
					punchoutSession));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			PunchoutSessionResource punchoutSessionResource)
		throws Exception {

		punchoutSessionResource.setContextAcceptLanguage(_acceptLanguage);
		punchoutSessionResource.setContextCompany(_company);
		punchoutSessionResource.setContextHttpServletRequest(
			_httpServletRequest);
		punchoutSessionResource.setContextHttpServletResponse(
			_httpServletResponse);
		punchoutSessionResource.setContextUriInfo(_uriInfo);
		punchoutSessionResource.setContextUser(_user);
	}

	private static ComponentServiceObjects<PunchoutSessionResource>
		_punchoutSessionResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private com.liferay.portal.kernel.model.User _user;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private UriInfo _uriInfo;

}