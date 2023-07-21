/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.exception;

/**
 * @author Brian Wing Shun Chan
 * @author Isaac Obrist
 */
public class RequiredGroupException extends PortalException {

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	public static final int CURRENT_GROUP = 3;

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	public static final int PARENT_GROUP = 2;

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	public static final int SYSTEM_GROUP = 1;

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by the inner classes
	 */
	@Deprecated
	public int getType() {
		return _type;
	}

	public static class MustNotDeleteCurrentGroup
		extends RequiredGroupException {

		public MustNotDeleteCurrentGroup(long groupId) {
			super(
				String.format(
					"Site %s cannot be deleted because it is currently being " +
						"accessed",
					groupId),
				CURRENT_GROUP);

			this.groupId = groupId;
		}

		public long groupId;

	}

	public static class MustNotDeleteGroupThatHasChild
		extends RequiredGroupException {

		public MustNotDeleteGroupThatHasChild(long groupId) {
			super(
				String.format(
					"Site %s cannot be deleted because it has child sites",
					groupId),
				PARENT_GROUP);

			this.groupId = groupId;
		}

		public long groupId;

	}

	public static class MustNotDeleteSystemGroup
		extends RequiredGroupException {

		public MustNotDeleteSystemGroup(long groupId) {
			super(
				String.format(
					"Site %s cannot be deleted because it is a system " +
						"required site",
					groupId),
				SYSTEM_GROUP);

			this.groupId = groupId;
		}

		public long groupId;

	}

	private RequiredGroupException(String message, int type) {
		super(message);

		_type = type;
	}

	private final int _type;

}