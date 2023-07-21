/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.model.impl;

import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.model.MBTreeWalker;
import com.liferay.message.boards.service.MBMessageLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class MBTreeWalkerImpl implements MBTreeWalker {

	public MBTreeWalkerImpl(
		long threadId, int status, MBMessageLocalService messageLocalService,
		Comparator<MBMessage> comparator) {

		this(0, threadId, status, messageLocalService, comparator);
	}

	public MBTreeWalkerImpl(
		long userId, long threadId, int status,
		MBMessageLocalService messageLocalService,
		Comparator<MBMessage> comparator) {

		_messageIdsMap = new HashMap<>();

		List<MBMessage> messages = null;
		MBMessage rootMessage = null;

		try {
			if (userId > 0) {
				messages = messageLocalService.getThreadMessages(
					userId, threadId, status, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, comparator);
			}
			else {
				messages = messageLocalService.getThreadMessages(
					threadId, status, comparator);
			}

			for (int i = 0; i < messages.size(); i++) {
				MBMessage curMessage = messages.get(i);

				if (curMessage.isRoot()) {
					rootMessage = curMessage;
				}

				long parentMessageId = curMessage.getParentMessageId();

				if (!curMessage.isRoot() &&
					!_messageIdsMap.containsKey(parentMessageId)) {

					_messageIdsMap.put(parentMessageId, i);
				}
			}
		}
		catch (Exception e) {
			_log.error("Unable to initialize tree walker", e);
		}

		_messages = messages;
		_rootMessage = rootMessage;
	}

	@Override
	public List<MBMessage> getChildren(MBMessage message) {
		List<MBMessage> children = new ArrayList<>();

		int[] range = getChildrenRange(message);

		for (int i = range[0]; i < range[1]; i++) {
			children.add(_messages.get(i));
		}

		return children;
	}

	@Override
	public int[] getChildrenRange(MBMessage message) {
		long messageId = message.getMessageId();

		Integer pos = _messageIdsMap.get(messageId);

		if (pos == null) {
			return new int[] {0, 0};
		}

		int[] range = new int[2];

		range[0] = pos.intValue();

		for (int i = range[0]; i < _messages.size(); i++) {
			MBMessage curMessage = _messages.get(i);

			if (curMessage.getParentMessageId() == messageId) {
				range[1] = i + 1;
			}
			else {
				break;
			}
		}

		return range;
	}

	@Override
	public List<MBMessage> getMessages() {
		return _messages;
	}

	@Override
	public MBMessage getRoot() {
		return _rootMessage;
	}

	@Override
	public boolean isLeaf(MBMessage message) {
		Long messageIdObj = Long.valueOf(message.getMessageId());

		if (_messageIdsMap.containsKey(messageIdObj)) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isOdd() {
		_odd = !_odd;

		return _odd;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MBTreeWalkerImpl.class);

	private final Map<Long, Integer> _messageIdsMap;
	private final List<MBMessage> _messages;
	private boolean _odd;
	private final MBMessage _rootMessage;

}