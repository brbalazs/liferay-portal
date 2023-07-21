/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link FileVersionPreview}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FileVersionPreview
 * @generated
 */
@ProviderType
public class FileVersionPreviewWrapper
	implements FileVersionPreview, ModelWrapper<FileVersionPreview> {

	public FileVersionPreviewWrapper(FileVersionPreview fileVersionPreview) {
		_fileVersionPreview = fileVersionPreview;
	}

	@Override
	public Class<?> getModelClass() {
		return FileVersionPreview.class;
	}

	@Override
	public String getModelClassName() {
		return FileVersionPreview.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("fileVersionPreviewId", getFileVersionPreviewId());
		attributes.put("groupId", getGroupId());
		attributes.put("fileEntryId", getFileEntryId());
		attributes.put("fileVersionId", getFileVersionId());
		attributes.put("previewStatus", getPreviewStatus());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long fileVersionPreviewId = (Long)attributes.get(
			"fileVersionPreviewId");

		if (fileVersionPreviewId != null) {
			setFileVersionPreviewId(fileVersionPreviewId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long fileEntryId = (Long)attributes.get("fileEntryId");

		if (fileEntryId != null) {
			setFileEntryId(fileEntryId);
		}

		Long fileVersionId = (Long)attributes.get("fileVersionId");

		if (fileVersionId != null) {
			setFileVersionId(fileVersionId);
		}

		Integer previewStatus = (Integer)attributes.get("previewStatus");

		if (previewStatus != null) {
			setPreviewStatus(previewStatus);
		}
	}

	@Override
	public Object clone() {
		return new FileVersionPreviewWrapper(
			(FileVersionPreview)_fileVersionPreview.clone());
	}

	@Override
	public int compareTo(FileVersionPreview fileVersionPreview) {
		return _fileVersionPreview.compareTo(fileVersionPreview);
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _fileVersionPreview.getExpandoBridge();
	}

	/**
	 * Returns the file entry ID of this file version preview.
	 *
	 * @return the file entry ID of this file version preview
	 */
	@Override
	public long getFileEntryId() {
		return _fileVersionPreview.getFileEntryId();
	}

	/**
	 * Returns the file version ID of this file version preview.
	 *
	 * @return the file version ID of this file version preview
	 */
	@Override
	public long getFileVersionId() {
		return _fileVersionPreview.getFileVersionId();
	}

	/**
	 * Returns the file version preview ID of this file version preview.
	 *
	 * @return the file version preview ID of this file version preview
	 */
	@Override
	public long getFileVersionPreviewId() {
		return _fileVersionPreview.getFileVersionPreviewId();
	}

	/**
	 * Returns the group ID of this file version preview.
	 *
	 * @return the group ID of this file version preview
	 */
	@Override
	public long getGroupId() {
		return _fileVersionPreview.getGroupId();
	}

	/**
	 * Returns the preview status of this file version preview.
	 *
	 * @return the preview status of this file version preview
	 */
	@Override
	public int getPreviewStatus() {
		return _fileVersionPreview.getPreviewStatus();
	}

	/**
	 * Returns the primary key of this file version preview.
	 *
	 * @return the primary key of this file version preview
	 */
	@Override
	public long getPrimaryKey() {
		return _fileVersionPreview.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _fileVersionPreview.getPrimaryKeyObj();
	}

	@Override
	public int hashCode() {
		return _fileVersionPreview.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _fileVersionPreview.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _fileVersionPreview.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _fileVersionPreview.isNew();
	}

	@Override
	public void persist() {
		_fileVersionPreview.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_fileVersionPreview.setCachedModel(cachedModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_fileVersionPreview.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_fileVersionPreview.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_fileVersionPreview.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the file entry ID of this file version preview.
	 *
	 * @param fileEntryId the file entry ID of this file version preview
	 */
	@Override
	public void setFileEntryId(long fileEntryId) {
		_fileVersionPreview.setFileEntryId(fileEntryId);
	}

	/**
	 * Sets the file version ID of this file version preview.
	 *
	 * @param fileVersionId the file version ID of this file version preview
	 */
	@Override
	public void setFileVersionId(long fileVersionId) {
		_fileVersionPreview.setFileVersionId(fileVersionId);
	}

	/**
	 * Sets the file version preview ID of this file version preview.
	 *
	 * @param fileVersionPreviewId the file version preview ID of this file version preview
	 */
	@Override
	public void setFileVersionPreviewId(long fileVersionPreviewId) {
		_fileVersionPreview.setFileVersionPreviewId(fileVersionPreviewId);
	}

	/**
	 * Sets the group ID of this file version preview.
	 *
	 * @param groupId the group ID of this file version preview
	 */
	@Override
	public void setGroupId(long groupId) {
		_fileVersionPreview.setGroupId(groupId);
	}

	@Override
	public void setNew(boolean n) {
		_fileVersionPreview.setNew(n);
	}

	/**
	 * Sets the preview status of this file version preview.
	 *
	 * @param previewStatus the preview status of this file version preview
	 */
	@Override
	public void setPreviewStatus(int previewStatus) {
		_fileVersionPreview.setPreviewStatus(previewStatus);
	}

	/**
	 * Sets the primary key of this file version preview.
	 *
	 * @param primaryKey the primary key of this file version preview
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_fileVersionPreview.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_fileVersionPreview.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<FileVersionPreview>
		toCacheModel() {

		return _fileVersionPreview.toCacheModel();
	}

	@Override
	public FileVersionPreview toEscapedModel() {
		return new FileVersionPreviewWrapper(
			_fileVersionPreview.toEscapedModel());
	}

	@Override
	public String toString() {
		return _fileVersionPreview.toString();
	}

	@Override
	public FileVersionPreview toUnescapedModel() {
		return new FileVersionPreviewWrapper(
			_fileVersionPreview.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _fileVersionPreview.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FileVersionPreviewWrapper)) {
			return false;
		}

		FileVersionPreviewWrapper fileVersionPreviewWrapper =
			(FileVersionPreviewWrapper)obj;

		if (Objects.equals(
				_fileVersionPreview,
				fileVersionPreviewWrapper._fileVersionPreview)) {

			return true;
		}

		return false;
	}

	@Override
	public FileVersionPreview getWrappedModel() {
		return _fileVersionPreview;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _fileVersionPreview.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _fileVersionPreview.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_fileVersionPreview.resetOriginalValues();
	}

	private final FileVersionPreview _fileVersionPreview;

}