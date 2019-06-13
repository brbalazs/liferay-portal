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

package com.liferay.commerce.bom.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.bom.service.CommerceBOMEntryServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * {@link CommerceBOMEntryServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * {@link HttpPrincipal} parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Luca Pellizzon
 * @see CommerceBOMEntryServiceSoap
 * @see HttpPrincipal
 * @see CommerceBOMEntryServiceUtil
 * @generated
 */
@ProviderType
public class CommerceBOMEntryServiceHttp {
	public static com.liferay.commerce.bom.model.CommerceBOMEntry addCommerceBOMEntry(
		HttpPrincipal httpPrincipal, long userId, int number,
		String cpInstanceUuid, long cProductId, long commerceBOMDefinitionId,
		double positionX, double positionY, double radius)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(CommerceBOMEntryServiceUtil.class,
					"addCommerceBOMEntry", _addCommerceBOMEntryParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					number, cpInstanceUuid, cProductId,
					commerceBOMDefinitionId, positionX, positionY, radius);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.commerce.bom.model.CommerceBOMEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.bom.model.CommerceBOMEntry updateCommerceBOMEntry(
		HttpPrincipal httpPrincipal, long commerceBOMEntryId, int number,
		String cpInstanceUuid, long cProductId, long commerceBOMDefinitionId,
		double positionX, double positionY, double radius)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(CommerceBOMEntryServiceUtil.class,
					"updateCommerceBOMEntry",
					_updateCommerceBOMEntryParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					commerceBOMEntryId, number, cpInstanceUuid, cProductId,
					commerceBOMDefinitionId, positionX, positionY, radius);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.commerce.bom.model.CommerceBOMEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(CommerceBOMEntryServiceHttp.class);
	private static final Class<?>[] _addCommerceBOMEntryParameterTypes0 = new Class[] {
			long.class, int.class, String.class, long.class, long.class,
			double.class, double.class, double.class
		};
	private static final Class<?>[] _updateCommerceBOMEntryParameterTypes1 = new Class[] {
			long.class, int.class, String.class, long.class, long.class,
			double.class, double.class, double.class
		};
}