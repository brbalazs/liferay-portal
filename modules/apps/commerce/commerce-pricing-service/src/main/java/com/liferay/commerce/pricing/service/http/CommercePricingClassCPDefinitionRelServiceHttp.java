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

package com.liferay.commerce.pricing.service.http;

import com.liferay.commerce.pricing.service.CommercePricingClassCPDefinitionRelServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * <code>CommercePricingClassCPDefinitionRelServiceUtil</code> service
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
 * @author Riccardo Alberti
 * @see CommercePricingClassCPDefinitionRelServiceSoap
 * @generated
 */
public class CommercePricingClassCPDefinitionRelServiceHttp {

	public static
		com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel
				addCommercePricingClassCPDefinitionRel(
					HttpPrincipal httpPrincipal, long commercePricingClassId,
					long cpDefinitionId,
					com.liferay.portal.kernel.service.ServiceContext
						serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"addCommercePricingClassCPDefinitionRel",
				_addCommercePricingClassCPDefinitionRelParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId, cpDefinitionId,
				serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.pricing.model.
				CommercePricingClassCPDefinitionRel)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static int countByCommercePricingClassId(
			HttpPrincipal httpPrincipal, long commercePricingClassId,
			String name, String languageId)
		throws com.liferay.portal.kernel.security.auth.PrincipalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"countByCommercePricingClassId",
				_countByCommercePricingClassIdParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId, name, languageId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.security.auth.
							PrincipalException) {

					throw (com.liferay.portal.kernel.security.auth.
						PrincipalException)exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel>
				searchByCommercePricingClassId(
					HttpPrincipal httpPrincipal, long commercePricingClassId,
					String name, String languageId, int start, int end)
			throws com.liferay.portal.kernel.security.auth.PrincipalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"searchByCommercePricingClassId",
				_searchByCommercePricingClassIdParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId, name, languageId, start,
				end);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.security.auth.
							PrincipalException) {

					throw (com.liferay.portal.kernel.security.auth.
						PrincipalException)exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.commerce.pricing.model.
					CommercePricingClassCPDefinitionRel>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static
		com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel
				deleteCommercePricingClassCPDefinitionRel(
					HttpPrincipal httpPrincipal,
					com.liferay.commerce.pricing.model.
						CommercePricingClassCPDefinitionRel
							commercePricingClassCPDefinitionRel)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"deleteCommercePricingClassCPDefinitionRel",
				_deleteCommercePricingClassCPDefinitionRelParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassCPDefinitionRel);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.pricing.model.
				CommercePricingClassCPDefinitionRel)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static
		com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel
				deleteCommercePricingClassCPDefinitionRel(
					HttpPrincipal httpPrincipal,
					long commercePricingClassCPDefinitionRelId)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"deleteCommercePricingClassCPDefinitionRel",
				_deleteCommercePricingClassCPDefinitionRelParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassCPDefinitionRelId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.pricing.model.
				CommercePricingClassCPDefinitionRel)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static
		com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel
				fetchCommercePricingClassCPDefinitionRel(
					HttpPrincipal httpPrincipal, long commercePricingClassId,
					long cpDefinitionId)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"fetchCommercePricingClassCPDefinitionRel",
				_fetchCommercePricingClassCPDefinitionRelParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId, cpDefinitionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.pricing.model.
				CommercePricingClassCPDefinitionRel)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static
		com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel
				getCommercePricingClassCPDefinitionRel(
					HttpPrincipal httpPrincipal,
					long commercePricingClassCPDefinitionRelId)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"getCommercePricingClassCPDefinitionRel",
				_getCommercePricingClassCPDefinitionRelParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassCPDefinitionRelId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.pricing.model.
				CommercePricingClassCPDefinitionRel)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel>
				getCommercePricingClassCPDefinitionRelByClassId(
					HttpPrincipal httpPrincipal, long commercePricingClassId)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"getCommercePricingClassCPDefinitionRelByClassId",
				_getCommercePricingClassCPDefinitionRelByClassIdParameterTypes7);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.commerce.pricing.model.
					CommercePricingClassCPDefinitionRel>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel>
				getCommercePricingClassCPDefinitionRels(
					HttpPrincipal httpPrincipal, long commercePricingClassId,
					int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.commerce.pricing.model.
							CommercePricingClassCPDefinitionRel>
								orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"getCommercePricingClassCPDefinitionRels",
				_getCommercePricingClassCPDefinitionRelsParameterTypes8);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId, start, end,
				orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.commerce.pricing.model.
					CommercePricingClassCPDefinitionRel>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static int getCommercePricingClassCPDefinitionRelsCount(
			HttpPrincipal httpPrincipal, long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"getCommercePricingClassCPDefinitionRelsCount",
				_getCommercePricingClassCPDefinitionRelsCountParameterTypes9);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static long[] getCPDefinitionIds(
			HttpPrincipal httpPrincipal, long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommercePricingClassCPDefinitionRelServiceUtil.class,
				"getCPDefinitionIds", _getCPDefinitionIdsParameterTypes10);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commercePricingClassId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (long[])returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		CommercePricingClassCPDefinitionRelServiceHttp.class);

	private static final Class<?>[]
		_addCommercePricingClassCPDefinitionRelParameterTypes0 = new Class[] {
			long.class, long.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[]
		_countByCommercePricingClassIdParameterTypes1 = new Class[] {
			long.class, String.class, String.class
		};
	private static final Class<?>[]
		_searchByCommercePricingClassIdParameterTypes2 = new Class[] {
			long.class, String.class, String.class, int.class, int.class
		};
	private static final Class<?>[]
		_deleteCommercePricingClassCPDefinitionRelParameterTypes3 =
			new Class[] {
				com.liferay.commerce.pricing.model.
					CommercePricingClassCPDefinitionRel.class
			};
	private static final Class<?>[]
		_deleteCommercePricingClassCPDefinitionRelParameterTypes4 =
			new Class[] {long.class};
	private static final Class<?>[]
		_fetchCommercePricingClassCPDefinitionRelParameterTypes5 = new Class[] {
			long.class, long.class
		};
	private static final Class<?>[]
		_getCommercePricingClassCPDefinitionRelParameterTypes6 = new Class[] {
			long.class
		};
	private static final Class<?>[]
		_getCommercePricingClassCPDefinitionRelByClassIdParameterTypes7 =
			new Class[] {long.class};
	private static final Class<?>[]
		_getCommercePricingClassCPDefinitionRelsParameterTypes8 = new Class[] {
			long.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[]
		_getCommercePricingClassCPDefinitionRelsCountParameterTypes9 =
			new Class[] {long.class};
	private static final Class<?>[] _getCPDefinitionIdsParameterTypes10 =
		new Class[] {long.class};

}