/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.changeset;

import aQute.bnd.annotation.ProviderType;

import java.util.Optional;

/**
 * @author Máté Thurzó
 */
@ProviderType
public interface ChangesetManager {

	public void addChangeset(Changeset changeset);

	public void clearChangesets();

	public boolean hasChangeset(String changesetUuid);

	public Optional<Changeset> peekChangeset(String changesetUuid);

	public Optional<Changeset> popChangeset(String changesetUuid);

	public long publishChangeset(
		Changeset changeset, ChangesetEnvironment changesetEnvironment);

}