/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Map;

/**
 * @author Leslie Wong
 */
public abstract class VisitorBehaviorMetric {

	public VisitorBehaviorMetric() {
	}

	public VisitorBehaviorMetric(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public long getAnonymousVisitors() {
		return getVisitors() - getKnownVisitors();
	}

	public long getKnownVisitors() {
		if (_knownVisitors == null) {
			return 0;
		}

		return _knownVisitors.longValue();
	}

	public long getSessions() {
		if (_sessions == null) {
			return 0;
		}

		return _sessions.longValue();
	}

	public double getSessionsPerVisitor() {
		if ((_sessions != null) && (_visitors != null) &&
			!_visitors.equals(BigDecimal.ZERO)) {

			BigDecimal sessionsPerVisitor = _sessions.divide(
				_visitors, 2, RoundingMode.HALF_UP);

			return sessionsPerVisitor.doubleValue();
		}

		return 0;
	}

	public long getVisitors() {
		if (_visitors == null) {
			return 0;
		}

		return _visitors.longValue();
	}

	public Boolean isPrevious() {
		return _previous;
	}

	public void setKnownVisitors(BigDecimal knownVisitors) {
		_knownVisitors = knownVisitors;
	}

	public void setPrevious(Boolean previous) {
		_previous = previous;
	}

	public void setSessions(BigDecimal sessions) {
		_sessions = sessions;
	}

	public void setVisitors(BigDecimal visitors) {
		_visitors = visitors;
	}

	private BigDecimal _knownVisitors;
	private Boolean _previous;
	private BigDecimal _sessions;
	private BigDecimal _visitors;

}