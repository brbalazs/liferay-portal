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
 * The extended model interface for the ScheduledTask service. Represents a row in the &quot;ScheduledTask&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see ScheduledTaskModel
 * @see com.liferay.commerce.data.integration.manager.model.impl.ScheduledTaskImpl
 * @see com.liferay.commerce.data.integration.manager.model.impl.ScheduledTaskModelImpl
 * @generated
 */
@ImplementationClassName("com.liferay.commerce.data.integration.manager.model.impl.ScheduledTaskImpl")
@ProviderType
public interface ScheduledTask extends ScheduledTaskModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.commerce.data.integration.manager.model.impl.ScheduledTaskImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<ScheduledTask, Long> SCHEDULED_TASK_ID_ACCESSOR =
		new Accessor<ScheduledTask, Long>() {
			@Override
			public Long get(ScheduledTask scheduledTask) {
				return scheduledTask.getScheduledTaskId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<ScheduledTask> getTypeClass() {
				return ScheduledTask.class;
			}
		};
}