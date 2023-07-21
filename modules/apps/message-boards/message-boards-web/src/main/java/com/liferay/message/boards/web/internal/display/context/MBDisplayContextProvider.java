/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.web.internal.display.context;

import com.liferay.message.boards.display.context.MBAdminListDisplayContext;
import com.liferay.message.boards.display.context.MBDisplayContextFactory;
import com.liferay.message.boards.display.context.MBHomeDisplayContext;
import com.liferay.message.boards.display.context.MBListDisplayContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Iván Zaera
 * @author Roberto Díaz
 * @author Sergio González
 */
@Component(service = MBDisplayContextProvider.class)
public class MBDisplayContextProvider {

	public MBAdminListDisplayContext getMbAdminListDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		long categoryId) {

		MBAdminListDisplayContext mbAdminListDisplayContext =
			new DefaultMBAdminListDisplayContext(request, response, categoryId);

		for (MBDisplayContextFactory mbDisplayContextFactory :
				_mbDisplayContextFactories) {

			mbAdminListDisplayContext =
				mbDisplayContextFactory.getMBAdminListDisplayContext(
					mbAdminListDisplayContext, request, response, categoryId);
		}

		return mbAdminListDisplayContext;
	}

	public MBHomeDisplayContext getMBHomeDisplayContext(
		HttpServletRequest request, HttpServletResponse response) {

		MBHomeDisplayContext mbHomeDisplayContext =
			new DefaultMBHomeDisplayContext(request, response);

		for (MBDisplayContextFactory mbDisplayContextFactory :
				_mbDisplayContextFactories) {

			mbHomeDisplayContext =
				mbDisplayContextFactory.getMBHomeDisplayContext(
					mbHomeDisplayContext, request, response);
		}

		return mbHomeDisplayContext;
	}

	public MBListDisplayContext getMbListDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		long categoryId) {

		MBListDisplayContext mbListDisplayContext =
			new DefaultMBListDisplayContext(request, response, categoryId);

		for (MBDisplayContextFactory mbDisplayContextFactory :
				_mbDisplayContextFactories) {

			mbListDisplayContext =
				mbDisplayContextFactory.getMBListDisplayContext(
					mbListDisplayContext, request, response, categoryId);
		}

		return mbListDisplayContext;
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.RELUCTANT,
		service = MBDisplayContextFactory.class
	)
	protected void setMBDisplayContextFactory(
		MBDisplayContextFactory mbDisplayContextFactory) {

		_mbDisplayContextFactories.add(mbDisplayContextFactory);
	}

	protected void unsetMBDisplayContextFactory(
		MBDisplayContextFactory mbDisplayContextFactory) {

		_mbDisplayContextFactories.remove(mbDisplayContextFactory);
	}

	private final List<MBDisplayContextFactory> _mbDisplayContextFactories =
		new CopyOnWriteArrayList<>();

}