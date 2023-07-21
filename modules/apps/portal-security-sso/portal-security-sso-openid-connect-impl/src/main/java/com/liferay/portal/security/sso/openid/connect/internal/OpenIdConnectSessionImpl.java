/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect.internal;

import com.liferay.portal.security.sso.openid.connect.OpenIdConnectFlowState;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectSession;

import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.openid.connect.sdk.Nonce;

import java.io.Serializable;

import net.minidev.json.JSONObject;

/**
 * @author Edward C. Han
 */
public class OpenIdConnectSessionImpl
	implements OpenIdConnectSession, Serializable {

	public OpenIdConnectSessionImpl(
		String openIdProviderName, Nonce nonce, State state) {

		_openIdProviderName = openIdProviderName;
		_nonce = nonce;
		_state = state;
	}

	public AccessToken getAccessToken() {
		return _accessToken;
	}

	@Override
	public String getAccessTokenValue() {
		return _accessToken.getValue();
	}

	@Override
	public long getLoginTime() {
		return _loginTime;
	}

	@Override
	public long getLoginUserId() {
		return _loginUserId;
	}

	public Nonce getNonce() {
		return _nonce;
	}

	@Override
	public String getNonceValue() {
		return _nonce.getValue();
	}

	@Override
	public OpenIdConnectFlowState getOpenIdConnectFlowState() {
		return _openIdConnectFlowState;
	}

	@Override
	public String getOpenIdProviderName() {
		return _openIdProviderName;
	}

	public RefreshToken getRefreshToken() {
		return _refreshToken;
	}

	@Override
	public String getRefreshTokenValue() {
		return _refreshToken.getValue();
	}

	public State getState() {
		return _state;
	}

	@Override
	public String getStateValue() {
		return _state.getValue();
	}

	public JSONObject getUserInfoJSONObject() {
		return _userInfoJSONObject;
	}

	public void setAccessToken(AccessToken accessToken) {
		_accessToken = accessToken;
	}

	public void setLoginTime(long loginTime) {
		_loginTime = loginTime;
	}

	public void setLoginUserId(long loginUserId) {
		_loginUserId = loginUserId;
	}

	@Override
	public void setOpenIdConnectFlowState(
		OpenIdConnectFlowState openIdConnectFlowState) {

		_openIdConnectFlowState = openIdConnectFlowState;
	}

	public void setRefreshToken(RefreshToken refreshToken) {
		_refreshToken = refreshToken;
	}

	public void setUserInfoJSONObject(JSONObject userInfoJSONObject) {
		_userInfoJSONObject = userInfoJSONObject;
	}

	private AccessToken _accessToken;
	private long _loginTime;
	private long _loginUserId;
	private final Nonce _nonce;
	private OpenIdConnectFlowState _openIdConnectFlowState =
		OpenIdConnectFlowState.INITIALIZED;
	private final String _openIdProviderName;
	private RefreshToken _refreshToken;
	private final State _state;
	private JSONObject _userInfoJSONObject;

}