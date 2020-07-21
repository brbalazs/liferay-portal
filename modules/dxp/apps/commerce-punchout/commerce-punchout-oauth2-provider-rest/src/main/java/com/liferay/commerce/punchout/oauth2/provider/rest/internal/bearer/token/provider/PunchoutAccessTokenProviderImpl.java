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

package com.liferay.commerce.punchout.oauth2.provider.rest.internal.bearer.token.provider;

import com.liferay.commerce.punchout.oauth2.provider.PunchoutAccessTokenProvider;
import com.liferay.commerce.punchout.oauth2.provider.model.PunchoutAccessToken;
import com.liferay.commerce.punchout.oauth2.provider.rest.internal.bearer.token.provider.configuration.PunchoutAccessTokenProviderConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	configurationPid = "com.liferay.commerce.punchout.oauth2.provider.rest.internal.bearer.token.provider.configuration.PunchoutAccessTokenProviderConfiguration",
	property = "timeout:Integer=15", service = PunchoutAccessTokenProvider.class
)
public class PunchoutAccessTokenProviderImpl
	implements PunchoutAccessTokenProvider {

	public PunchoutAccessToken generatePunchoutAccessToken(
		long groupId, long commerceAccountId, String currencyCode,
		String userEmailAddress, String commerceOrderUuid,
		HashMap<String, Object> punchoutSessionAttributes) {

		PunchoutAccessToken punchoutAccessToken = _generatePunchoutAccessToken(
			groupId, commerceAccountId, currencyCode, userEmailAddress,
			commerceOrderUuid, punchoutSessionAttributes);

		if (!_clusterMasterExecutor.isEnabled() ||
			_clusterMasterExecutor.isMaster()) {

			_putPunchoutAccessToken(punchoutAccessToken);
		}
		else {
			Future<?> future = _clusterMasterExecutor.executeOnMaster(
				new MethodHandler(
					_putPunchoutAccessTokenMethodKey, punchoutAccessToken));

			try {
				future.get(_timeout, TimeUnit.SECONDS);
			}
			catch (Exception e) {
				_log.error(
					"Timeout setting punchout access token to master node");
			}
		}

		return punchoutAccessToken;
	}

	public PunchoutAccessToken getPunchoutAccessToken(String token) {
		if (!_clusterMasterExecutor.isEnabled() ||
			_clusterMasterExecutor.isMaster()) {

			return _getPunchoutAccessToken(token);
		}

		Future<PunchoutAccessToken> future =
			_clusterMasterExecutor.executeOnMaster(
				new MethodHandler(_getPunchoutAccessTokenMethodKey, token));

		try {
			return future.get(_timeout, TimeUnit.SECONDS);
		}
		catch (Exception e) {
			_log.error(
				"Timeout getting punchout access token from master node");

			return null;
		}
	}

	public PunchoutAccessToken removePunchoutAccessToken(String token) {
		if (!_clusterMasterExecutor.isEnabled() ||
			_clusterMasterExecutor.isMaster()) {

			return _removePunchoutAccessToken(token);
		}

		Future<PunchoutAccessToken> future =
			_clusterMasterExecutor.executeOnMaster(
				new MethodHandler(_removePunchoutAccessTokenMethodKey, token));

		try {
			return future.get(_timeout, TimeUnit.SECONDS);
		}
		catch (Exception e) {
			_log.error(
				"Timeout removing punchout access token from master node");

			return null;
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_timeout = MapUtil.getInteger(properties, "timeout");

		_punchoutAccessTokenProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				PunchoutAccessTokenProviderConfiguration.class, properties);
	}

	private static void _cleanUp() {
		while (_punchoutAccessTokenDelayQueue.poll() != null);
	}

	private static void _putPunchoutAccessToken(
		PunchoutAccessToken punchoutAccessToken) {

		_cleanUp();

		_punchoutAccessTokenDelayQueue.add(
			new PunchoutAccessTokenDelayed(punchoutAccessToken));
	}

	private static PunchoutAccessToken _removePunchoutAccessToken(
		String token) {

		_cleanUp();

		AtomicReference<PunchoutAccessToken>
			punchoutAccessTokenAtomicReference = new AtomicReference<>();

		_punchoutAccessTokenDelayQueue.removeIf(
			punchoutAccessTokenDelayed -> {
				PunchoutAccessToken punchoutAccessToken =
					punchoutAccessTokenDelayed.getPunchoutAccessToken();

				byte[] tokenBytes = punchoutAccessToken.getToken();

				String tokenString = tokenBytes.toString();

				if (token.equals(tokenString)) {
					punchoutAccessTokenAtomicReference.compareAndSet(
						null, punchoutAccessToken);

					return true;
				}

				return false;
			});

		return punchoutAccessTokenAtomicReference.get();
	}

	private PunchoutAccessToken _generatePunchoutAccessToken(
		long groupId, long commerceAccountId, String currencyCode,
		String userEmailAddress, String commerceOrderUuid,
		HashMap<String, Object> punchoutSessionAttributes) {

		PunchoutAccessToken punchoutAccessToken = new PunchoutAccessToken();

		punchoutAccessToken.setGroupId(groupId);

		punchoutAccessToken.setCommerceAccountId(commerceAccountId);

		punchoutAccessToken.setCurrencyCode(currencyCode);

		punchoutAccessToken.setIssuedAt(System.currentTimeMillis());

		int expiresInSeconds =
			_punchoutAccessTokenProviderConfiguration.accessTokenExpiresIn();

		long expiresInMilliseconds = TimeUnit.MILLISECONDS.convert(
			expiresInSeconds, TimeUnit.SECONDS);

		punchoutAccessToken.setExpiresIn(expiresInMilliseconds);

		byte[] token = _generateSecureRandomBytes(
			_punchoutAccessTokenProviderConfiguration.accessTokenKeyByteSize());

		punchoutAccessToken.setToken(token);

		punchoutAccessToken.setUserEmailAddress(userEmailAddress);

		punchoutAccessToken.setCommerceOrderUuid(commerceOrderUuid);

		punchoutAccessToken.setPunchoutSessionAttributes(
			punchoutSessionAttributes);

		return punchoutAccessToken;
	}

	private byte[] _generateSecureRandomBytes(int size) {
		byte[] bytes = new byte[size];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = SecureRandomUtil.nextByte();
		}

		return bytes;
	}

	private PunchoutAccessToken _getPunchoutAccessToken(String token) {
		_cleanUp();

		for (PunchoutAccessTokenDelayed punchoutAccessTokenDelayed :
				_punchoutAccessTokenDelayQueue) {

			PunchoutAccessToken punchoutAccessToken =
				punchoutAccessTokenDelayed.getPunchoutAccessToken();

			byte[] tokenBytes = punchoutAccessToken.getToken();

			String tokenString = tokenBytes.toString();

			if (token.equals(tokenString)) {
				return punchoutAccessToken;
			}
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutAccessTokenProviderImpl.class);

	private static final MethodKey _getPunchoutAccessTokenMethodKey =
		new MethodKey(
			PunchoutAccessTokenProviderImpl.class, "_getPunchoutAccessToken",
			String.class);
	private static final DelayQueue<PunchoutAccessTokenDelayed>
		_punchoutAccessTokenDelayQueue = new DelayQueue<>();
	private static final MethodKey _putPunchoutAccessTokenMethodKey =
		new MethodKey(
			PunchoutAccessTokenProviderImpl.class, "_putPunchoutAccessToken",
			PunchoutAccessToken.class);
	private static final MethodKey _removePunchoutAccessTokenMethodKey =
		new MethodKey(
			PunchoutAccessTokenProviderImpl.class, "_removePunchoutAccessToken",
			String.class);

	@Reference
	private ClusterMasterExecutor _clusterMasterExecutor;

	private PunchoutAccessTokenProviderConfiguration
		_punchoutAccessTokenProviderConfiguration;
	private int _timeout;

	private static class PunchoutAccessTokenDelayed implements Delayed {

		public PunchoutAccessTokenDelayed(
			PunchoutAccessToken punchoutAccessToken) {

			_punchoutAccessToken = punchoutAccessToken;
		}

		@Override
		public int compareTo(Delayed delayed) {
			return _comparator.compare(
				this, (PunchoutAccessTokenDelayed)delayed);
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long expirationTime = _getExpirationTime();

			return unit.convert(
				expirationTime - System.currentTimeMillis(),
				TimeUnit.MILLISECONDS);
		}

		public PunchoutAccessToken getPunchoutAccessToken() {
			return _punchoutAccessToken;
		}

		private long _getExpirationTime() {
			return _punchoutAccessToken.getIssuedAt() +
				_punchoutAccessToken.getExpiresIn();
		}

		private static final Comparator<PunchoutAccessTokenDelayed>
			_comparator = Comparator.comparing(
				PunchoutAccessTokenDelayed::_getExpirationTime);

		private final PunchoutAccessToken _punchoutAccessToken;

	}

}