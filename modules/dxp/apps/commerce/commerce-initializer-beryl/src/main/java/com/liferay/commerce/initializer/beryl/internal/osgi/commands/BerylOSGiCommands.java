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

package com.liferay.commerce.initializer.beryl.internal.osgi.commands;

import com.liferay.commerce.initializer.beryl.internal.BerylLayoutsInitializer;
import com.liferay.commerce.initializer.beryl.internal.BerylSampleForecastsInitializer;
import com.liferay.commerce.initializer.beryl.internal.BerylSampleOrdersInitializer;
import com.liferay.commerce.initializer.beryl.internal.BerylThemePortletSettingsInitializer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=initBerylLayouts",
		"osgi.command.function=initBerylSampleForecasts",
		"osgi.command.function=initBerylSampleOrders",
		"osgi.command.function=initBerylThemePortletSettings",
		"osgi.command.scope=commerce"
	},
	service = BerylOSGiCommands.class
)
public class BerylOSGiCommands {

	public void initBerylLayouts(final long groupId) throws Throwable {
		TransactionInvokerUtil.invoke(
			_transactionConfig,
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					ServiceContext serviceContext = _getServiceContext(groupId);

					_berylLayoutsInitializer.initialize(serviceContext);

					return null;
				}

			});
	}

	public void initBerylSampleForecasts(final long groupId) throws Throwable {
		TransactionInvokerUtil.invoke(
			_transactionConfig,
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					_berylSampleForecastsInitializer.initialize(groupId);

					return null;
				}

			});
	}

	public void initBerylSampleOrders(final long groupId) throws Throwable {
		TransactionInvokerUtil.invoke(
			_transactionConfig,
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					_berylSampleOrdersInitializer.initialize(groupId);

					return null;
				}

			});
	}

	public void initBerylThemePortletSettings(final long groupId)
		throws Throwable {

		TransactionInvokerUtil.invoke(
			_transactionConfig,
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					ServiceContext serviceContext = _getServiceContext(groupId);

					_berylThemePortletSettingsInitializer.initialize(
						serviceContext);

					return null;
				}

			});
	}

	private ServiceContext _getServiceContext(long groupId)
		throws PortalException {

		Group group = _groupLocalService.getGroup(groupId);

		User user = _userLocalService.getUser(group.getCreatorUserId());

		Locale locale = LocaleUtil.getSiteDefault();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(group.getCompanyId());
		serviceContext.setLanguageId(LanguageUtil.getLanguageId(locale));
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setUserId(user.getUserId());
		serviceContext.setTimeZone(user.getTimeZone());

		return serviceContext;
	}

	private static final TransactionConfig _transactionConfig;

	static {
		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRES_NEW);
		builder.setRollbackForClasses(Exception.class);

		_transactionConfig = builder.build();
	}

	@Reference
	private BerylLayoutsInitializer _berylLayoutsInitializer;

	@Reference
	private BerylSampleForecastsInitializer _berylSampleForecastsInitializer;

	@Reference
	private BerylSampleOrdersInitializer _berylSampleOrdersInitializer;

	@Reference
	private BerylThemePortletSettingsInitializer
		_berylThemePortletSettingsInitializer;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private UserLocalService _userLocalService;

}