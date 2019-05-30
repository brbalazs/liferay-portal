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
 * The extended model interface for the Process service. Represents a row in the &quot;Process&quot; database table, with each column mapped to a property of this class.
 *
 * @author Marco Leo
 * @see ProcessModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.commerce.data.integration.manager.model.impl.ProcessImpl"
)
@ProviderType
public interface Process extends PersistedModel, ProcessModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.commerce.data.integration.manager.model.impl.ProcessImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Process, Long> PROCESS_ID_ACCESSOR =
		new Accessor<Process, Long>() {

			@Override
			public Long get(Process process) {
				return process.getProcessId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<Process> getTypeClass() {
				return Process.class;
			}

		};

}