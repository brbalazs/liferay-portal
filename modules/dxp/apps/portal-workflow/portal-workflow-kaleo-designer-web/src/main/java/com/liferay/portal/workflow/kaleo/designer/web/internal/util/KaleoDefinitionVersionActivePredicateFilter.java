/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.designer.web.internal.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.workflow.kaleo.designer.web.internal.constants.KaleoDefinitionVersionConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion;

/**
 * @author Inácio Nery
 */
public class KaleoDefinitionVersionActivePredicateFilter
	implements PredicateFilter<KaleoDefinitionVersion> {

	public KaleoDefinitionVersionActivePredicateFilter(int status) {
		_status = status;
	}

	@Override
	public boolean filter(KaleoDefinitionVersion kaleoDefinitionVersion) {
		if (_status == KaleoDefinitionVersionConstants.STATUS_ALL) {
			return true;
		}

		try {
			KaleoDefinition kaleoDefinition =
				kaleoDefinitionVersion.getKaleoDefinition();

			if (_status == KaleoDefinitionVersionConstants.STATUS_PUBLISHED) {
				return kaleoDefinition.isActive();
			}

			return !kaleoDefinition.isActive();
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			if (_status == KaleoDefinitionVersionConstants.STATUS_PUBLISHED) {
				return false;
			}

			return true;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoDefinitionVersionActivePredicateFilter.class);

	private final int _status;

}