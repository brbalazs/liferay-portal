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

package com.liferay.osb.faro.web.internal.context;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = GroupInfoContextProvider.class)
@Provider
public class GroupInfoContextProvider implements ContextProvider<GroupInfo> {

	@Override
	public GroupInfo createContext(Message message) {
		HttpServletRequest httpServletRequest =
			(HttpServletRequest)message.getContextualProperty("HTTP.REQUEST");

		String authorization = httpServletRequest.getHeader("Authorization");

		if (authorization == null) {
			throw new IllegalStateException(
				"Authorization Header is not available");
		}

		try {
			OAuth2Authorization userOAuth2Authorization =
				_getUserOAuth2AuthorizationsByAccessToken(
					authorization.substring(7));

			ExpandoBridge expandoBridge =
				userOAuth2Authorization.getExpandoBridge();

			return new GroupInfo(
				(long)expandoBridge.getAttribute("groupId", false));
		}
		catch (PortalException portalException) {
			throw new IllegalStateException(portalException);
		}
	}

	private OAuth2Authorization _getUserOAuth2AuthorizationsByAccessToken(
			String accessToken)
		throws PortalException {

		List<OAuth2Authorization> userOAuth2Authorizations =
			_oAuth2AuthorizationService.getUserOAuth2Authorizations(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Stream<OAuth2Authorization> stream = userOAuth2Authorizations.stream();

		Optional<OAuth2Authorization> oAuth2AuthorizationOptional =
			stream.filter(
				oAuth2Authorization -> Objects.equals(
					oAuth2Authorization.getAccessTokenContent(), accessToken)
			).findFirst();

		return oAuth2AuthorizationOptional.orElseThrow(
			() -> new IllegalStateException(
				"Unable to fetch the OAuth2Authorization with access token " +
					accessToken));
	}

	@Reference
	private OAuth2AuthorizationService _oAuth2AuthorizationService;

}