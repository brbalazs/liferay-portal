/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.TreeModel;

import java.util.List;

/**
 * @author     Shinn Lok
 * @deprecated As of Wilberforce (7.0.x), moved to {@link
 *             com.liferay.portal.kernel.tree.TreeModelTasks}
 */
@Deprecated
public interface TreeModelTasks<T extends TreeModel> {

	public List<T> findTreeModels(
		long previousId, long companyId, long parentPrimaryKey, int size);

	public void rebuildDependentModelsTreePaths(
			long parentPrimaryKey, String treePath)
		throws PortalException;

	public void reindexTreeModels(List<TreeModel> treeModels)
		throws PortalException;

}