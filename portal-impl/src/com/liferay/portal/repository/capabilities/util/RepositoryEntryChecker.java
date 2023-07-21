/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.repository.capabilities.util;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.DocumentRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Iván Zaera
 */
public class RepositoryEntryChecker {

	public RepositoryEntryChecker(DocumentRepository documentRepository) {
		_documentRepository = documentRepository;
	}

	public DLFileEntry checkDLFileEntry(DLFileEntry dlFileEntry) {
		long repositoryId = _documentRepository.getRepositoryId();

		if (dlFileEntry.getRepositoryId() != repositoryId) {
			throw new SystemException(
				StringBundler.concat(
					"File entry ", String.valueOf(dlFileEntry.getFileEntryId()),
					" does not belong to repository ",
					String.valueOf(repositoryId)));
		}

		return dlFileEntry;
	}

	public FileEntry checkFileEntry(FileEntry fileEntry) {
		long repositoryId = _documentRepository.getRepositoryId();

		if (fileEntry.getRepositoryId() != repositoryId) {
			throw new SystemException(
				StringBundler.concat(
					"File entry ", String.valueOf(fileEntry.getFileEntryId()),
					" does not belong to repository ",
					String.valueOf(repositoryId)));
		}

		return fileEntry;
	}

	private final DocumentRepository _documentRepository;

}