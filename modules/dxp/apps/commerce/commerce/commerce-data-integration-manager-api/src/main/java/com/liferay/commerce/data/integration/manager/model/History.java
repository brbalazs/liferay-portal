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

package com.liferay.commerce.data.integration.manager.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the History service. Represents a row in the &quot;History&quot; database table, with each column mapped to a property of this class.
 *
 * @author Marco Leo
 * @see HistoryModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.commerce.data.integration.manager.model.impl.HistoryImpl"
)
@ProviderType
public interface History extends HistoryModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.commerce.data.integration.manager.model.impl.HistoryImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<History, Long> HISTORY_ID_ACCESSOR =
		new Accessor<History, Long>() {

			@Override
			public Long get(History history) {
				return history.getHistoryId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<History> getTypeClass() {
				return History.class;
			}

		};

	public ScheduledTask getScheduledTask();

	public String getScheduledTaskName();

}