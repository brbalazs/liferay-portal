/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.cluster;

import com.liferay.portal.internal.cluster.ClusterableAdvice;
import com.liferay.portal.internal.cluster.SPIClusterableAdvice;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.spring.aop.ChainableMethodAdviceInjector;
import com.liferay.portal.util.PropsValues;

/**
 * @author Shuyang Zhou
 */
public class ClusterableChainableMethodAdviceInjector
	extends ChainableMethodAdviceInjector {

	@Override
	public void inject() {
		setInjectCondition(PropsValues.CLUSTER_LINK_ENABLED);
		setNewChainableMethodAdvice(new ClusterableAdvice());

		super.inject();

		if (SPIUtil.isSPI()) {
			setInjectCondition(true);
			setNewChainableMethodAdvice(new SPIClusterableAdvice());

			super.inject();
		}
	}

}