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

import com.liferay.commerce.bom.service.CommerceBOMFolderServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * {@link CommerceBOMFolderServiceUtil} service utility. The
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
 * @see CommerceBOMFolderServiceSoap
 * @see HttpPrincipal
 * @see CommerceBOMFolderServiceUtil
 * @generated
 */
@ProviderType
public class CommerceBOMFolderServiceHttp {
	public static com.liferay.commerce.bom.model.CommerceBOMFolder addCommerceBOMFolder(
		HttpPrincipal httpPrincipal, long userId,
		long parentCommerceBOMFolderId, String name, long imageId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(CommerceBOMFolderServiceUtil.class,
					"addCommerceBOMFolder", _addCommerceBOMFolderParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					parentCommerceBOMFolderId, name, imageId);

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

			return (com.liferay.commerce.bom.model.CommerceBOMFolder)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.bom.model.CommerceBOMFolder getCommerceBOMFolder(
		HttpPrincipal httpPrincipal, long commerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(CommerceBOMFolderServiceUtil.class,
					"getCommerceBOMFolder", _getCommerceBOMFolderParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					commerceBOMFolderId);

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

			return (com.liferay.commerce.bom.model.CommerceBOMFolder)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.commerce.bom.model.CommerceBOMFolder> getCommerceBOMFolders(
		HttpPrincipal httpPrincipal, long companyId,
		long parentCommerceBOMFolderId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(CommerceBOMFolderServiceUtil.class,
					"getCommerceBOMFolders",
					_getCommerceBOMFoldersParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, parentCommerceBOMFolderId, start, end);

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

			return (java.util.List<com.liferay.commerce.bom.model.CommerceBOMFolder>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getCommerceBOMFoldersCount(HttpPrincipal httpPrincipal,
		long companyId, long parentCommerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(CommerceBOMFolderServiceUtil.class,
					"getCommerceBOMFoldersCount",
					_getCommerceBOMFoldersCountParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, parentCommerceBOMFolderId);

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

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.bom.model.CommerceBOMFolder updateCommerceBOMFolder(
		HttpPrincipal httpPrincipal, long commerceBOMFolderId, String name,
		long imageId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(CommerceBOMFolderServiceUtil.class,
					"updateCommerceBOMFolder",
					_updateCommerceBOMFolderParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					commerceBOMFolderId, name, imageId);

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

			return (com.liferay.commerce.bom.model.CommerceBOMFolder)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(CommerceBOMFolderServiceHttp.class);
	private static final Class<?>[] _addCommerceBOMFolderParameterTypes0 = new Class[] {
			long.class, long.class, String.class, long.class
		};
	private static final Class<?>[] _getCommerceBOMFolderParameterTypes1 = new Class[] {
			long.class
		};
	private static final Class<?>[] _getCommerceBOMFoldersParameterTypes2 = new Class[] {
			long.class, long.class, int.class, int.class
		};
	private static final Class<?>[] _getCommerceBOMFoldersCountParameterTypes3 = new Class[] {
			long.class, long.class
		};
	private static final Class<?>[] _updateCommerceBOMFolderParameterTypes4 = new Class[] {
			long.class, String.class, long.class
		};
}