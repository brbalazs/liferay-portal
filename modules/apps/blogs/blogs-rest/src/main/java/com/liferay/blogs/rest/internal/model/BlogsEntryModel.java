/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.rest.internal.model;

import com.liferay.blogs.model.BlogsEntry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alejandro Hernández
 */
@XmlRootElement
public class BlogsEntryModel {

	public BlogsEntryModel() {
	}

	public BlogsEntryModel(BlogsEntry blogsEntry) {
		_content = blogsEntry.getContent();
		_entryId = blogsEntry.getEntryId();
		_title = blogsEntry.getTitle();
	}

	public String getContent() {
		return _content;
	}

	@XmlElement(name = "id")
	public long getEntryId() {
		return _entryId;
	}

	public String getTitle() {
		return _title;
	}

	public void setContent(String content) {
		_content = content;
	}

	public void setEntryId(long entryId) {
		_entryId = entryId;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private String _content;
	private long _entryId;
	private String _title;

}