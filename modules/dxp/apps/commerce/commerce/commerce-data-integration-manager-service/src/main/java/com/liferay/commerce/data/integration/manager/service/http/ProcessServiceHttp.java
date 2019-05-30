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

package com.liferay.commerce.data.integration.manager.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.data.integration.manager.service.ProcessServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * <code>ProcessServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
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
 * @author Marco Leo
 * @see ProcessServiceSoap
 * @generated
 */
@ProviderType
public class ProcessServiceHttp {

	public static com.liferay.commerce.data.integration.manager.model.Process
			addProcess(
				HttpPrincipal httpPrincipal,
				com.liferay.commerce.data.integration.manager.model.Process
					process,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "addProcess",
				_addProcessParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, process, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.commerce.data.integration.manager.model.Process)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			addProcess(
				HttpPrincipal httpPrincipal, String name, String className,
				String processType, String version, String contextProperties,
				long contextPropertiesFileEntryId, long srcArchiveFileEntryId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "addProcess",
				_addProcessParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, name, className, processType, version,
				contextProperties, contextPropertiesFileEntryId,
				srcArchiveFileEntryId, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.commerce.data.integration.manager.model.Process)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
		create(HttpPrincipal httpPrincipal) {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "create", _createParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(methodKey);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.commerce.data.integration.manager.model.Process)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			deleteProcess(
				HttpPrincipal httpPrincipal, long userId, long processId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "deleteProcess",
				_deleteProcessParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, userId, processId, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.commerce.data.integration.manager.model.Process)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			getProcess(HttpPrincipal httpPrincipal, long userId, long processId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "getProcess",
				_getProcessParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, userId, processId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.commerce.data.integration.manager.model.Process)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List
		<com.liferay.commerce.data.integration.manager.model.Process>
				getProcessesByGroupId(
					HttpPrincipal httpPrincipal, long userId, long groupId,
					int start, int end)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "getProcessesByGroupId",
				_getProcessesByGroupIdParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, userId, groupId, start, end);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (java.util.List
				<com.liferay.commerce.data.integration.manager.model.Process>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getProcessesByGroupIdCount(
			HttpPrincipal httpPrincipal, long userId, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "getProcessesByGroupIdCount",
				_getProcessesByGroupIdCountParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, userId, groupId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			updateProcess(
				HttpPrincipal httpPrincipal, long processId, String name,
				String className, String processType, String version,
				String contextProperties, long contextPropertiesFileEntryId,
				long srcArchiveFileEntryId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "updateProcess",
				_updateProcessParameterTypes7);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, processId, name, className, processType, version,
				contextProperties, contextPropertiesFileEntryId,
				srcArchiveFileEntryId, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.commerce.data.integration.manager.model.Process)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			updateProcess(
				HttpPrincipal httpPrincipal,
				com.liferay.commerce.data.integration.manager.model.Process
					process,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ProcessServiceUtil.class, "updateProcess",
				_updateProcessParameterTypes8);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, process, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.commerce.data.integration.manager.model.Process)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(ProcessServiceHttp.class);

	private static final Class<?>[] _addProcessParameterTypes0 = new Class[] {
		com.liferay.commerce.data.integration.manager.model.Process.class,
		com.liferay.portal.kernel.service.ServiceContext.class
	};
	private static final Class<?>[] _addProcessParameterTypes1 = new Class[] {
		String.class, String.class, String.class, String.class, String.class,
		long.class, long.class,
		com.liferay.portal.kernel.service.ServiceContext.class
	};
	private static final Class<?>[] _createParameterTypes2 = new Class[] {};
	private static final Class<?>[] _deleteProcessParameterTypes3 =
		new Class[] {
			long.class, long.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _getProcessParameterTypes4 = new Class[] {
		long.class, long.class
	};
	private static final Class<?>[] _getProcessesByGroupIdParameterTypes5 =
		new Class[] {long.class, long.class, int.class, int.class};
	private static final Class<?>[] _getProcessesByGroupIdCountParameterTypes6 =
		new Class[] {long.class, long.class};
	private static final Class<?>[] _updateProcessParameterTypes7 =
		new Class[] {
			long.class, String.class, String.class, String.class, String.class,
			String.class, long.class, long.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _updateProcessParameterTypes8 =
		new Class[] {
			com.liferay.commerce.data.integration.manager.model.Process.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};

}