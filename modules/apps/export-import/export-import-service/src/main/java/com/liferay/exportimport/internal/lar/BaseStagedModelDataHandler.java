/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.lar;

import com.liferay.portal.kernel.model.StagedModel;

/**
 * @author     Máté Thurzó
 * @deprecated As of Judson (7.1.x)
 */
@Deprecated
public abstract class BaseStagedModelDataHandler<T extends StagedModel>
	extends com.liferay.exportimport.data.handler.base.
				BaseStagedModelDataHandler<T> {
}