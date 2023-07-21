/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.archived.modules.upgrade.internal;

import com.liferay.message.boards.service.MBThreadLocalService;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.ImageLocalService;
import com.liferay.portal.kernel.service.ReleaseLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	configurationPid = "com.liferay.archived.modules.upgrade.internal.ArchivedModulesUpgradeConfiguration",
	immediate = true, service = UpgradeStepRegistrator.class
)
public class ArchivedModulesUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		try {
			if (_archivedModulesUpgradeConfiguration.removeChatModuleData()) {
				Release release = _releaseLocalService.fetchRelease(
					"com.liferay.chat.service");

				if (release != null) {
					UpgradeChat upgradeChat = new UpgradeChat();

					upgradeChat.upgrade();

					CacheRegistryUtil.clear();
				}
			}

			if (_archivedModulesUpgradeConfiguration.
					removeMailReaderModuleData()) {

				Release release = _releaseLocalService.fetchRelease(
					"com.liferay.mail.reader.service");

				if (release != null) {
					UpgradeMailReader upgradeMailReader =
						new UpgradeMailReader();

					upgradeMailReader.upgrade();

					CacheRegistryUtil.clear();
				}
			}

			if (_archivedModulesUpgradeConfiguration.
					removeShoppingModuleData()) {

				Release release = _releaseLocalService.fetchRelease(
					"com.liferay.shopping.service");

				if (release != null) {
					UpgradeShopping upgradeShopping = new UpgradeShopping(
						_imageLocalService);

					upgradeShopping.upgrade();

					CacheRegistryUtil.clear();
				}
			}

			if (_archivedModulesUpgradeConfiguration.
					removePrivateMessagingModuleData()) {

				Release release = _releaseLocalService.fetchRelease(
					"com.liferay.social.privatemessaging.service");

				if (release != null) {
					UpgradePrivateMessaging upgradePrivateMessaging =
						new UpgradePrivateMessaging(_mbThreadLocalService);

					upgradePrivateMessaging.upgrade();

					CacheRegistryUtil.clear();
				}
			}

			if (_archivedModulesUpgradeConfiguration.
					removeTwitterModuleData()) {

				Release release = _releaseLocalService.fetchRelease(
					"com.liferay.twitter.service");

				if (release != null) {
					UpgradeTwitter upgradeTwitter = new UpgradeTwitter();

					upgradeTwitter.upgrade();

					CacheRegistryUtil.clear();
				}
			}
		}
		catch (UpgradeException ue) {
			ReflectionUtil.throwException(ue);
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_archivedModulesUpgradeConfiguration =
			ConfigurableUtil.createConfigurable(
				ArchivedModulesUpgradeConfiguration.class, properties);
	}

	private ArchivedModulesUpgradeConfiguration
		_archivedModulesUpgradeConfiguration;

	@Reference
	private ImageLocalService _imageLocalService;

	@Reference
	private MBThreadLocalService _mbThreadLocalService;

	@Reference
	private ReleaseLocalService _releaseLocalService;

}