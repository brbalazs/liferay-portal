/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.poller.comet;

/**
 * @author Edward Han
 * @author Brian Wing Shun Chan
 */
public abstract class BaseCometSession implements CometSession {

	@Override
	public void close() throws CometException {
		try {
			doClose();

			_cometResponse.close();
		}
		catch (CometException ce) {
			throw ce;
		}
		catch (Exception e) {
			throw new CometException(e);
		}
	}

	@Override
	public CometRequest getCometRequest() {
		return _cometRequest;
	}

	@Override
	public CometResponse getCometResponse() {
		return _cometResponse;
	}

	@Override
	public String getSessionId() {
		return _sessionId;
	}

	@Override
	public void setCometRequest(CometRequest cometRequest) {
		_cometRequest = cometRequest;
	}

	@Override
	public void setCometResponse(CometResponse cometResponse) {
		_cometResponse = cometResponse;
	}

	@Override
	public void setSessionId(String sessionId) {
		_sessionId = sessionId;
	}

	protected abstract void doClose() throws Exception;

	private CometRequest _cometRequest;
	private CometResponse _cometResponse;
	private String _sessionId;

}