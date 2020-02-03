/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.order.web.internal.model;

import com.liferay.commerce.frontend.model.AuthorField;
import com.liferay.commerce.frontend.model.StatusField;

/**
 * @author Alessio Antonio Rendina
 */
public class Notification {

	public Notification(
		long notificationId, AuthorField author, String date,
		StatusField status, String subject, String summary, String url) {

		_notificationId = notificationId;
		_author = author;
		_date = date;
		_status = status;
		_subject = subject;
		_summary = summary;
		_url = url;
	}

	public AuthorField getAuthor() {
		return _author;
	}

	public String getDate() {
		return _date;
	}

	public long getNotificationId() {
		return _notificationId;
	}

	public StatusField getStatus() {
		return _status;
	}

	public String getSubject() {
		return _subject;
	}

	public String getSummary() {
		return _summary;
	}

	public String getUrl() {
		return _url;
	}

	private final AuthorField _author;
	private final String _date;
	private final long _notificationId;
	private final StatusField _status;
	private final String _subject;
	private final String _summary;
	private final String _url;

}