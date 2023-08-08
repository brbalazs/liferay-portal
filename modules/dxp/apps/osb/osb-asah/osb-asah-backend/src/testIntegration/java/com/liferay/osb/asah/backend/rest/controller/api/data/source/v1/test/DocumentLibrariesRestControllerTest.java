/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.DocumentLibrariesRestController;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQDocumentLibraryRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Matthew Kong
 */
@Disabled
public class DocumentLibrariesRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = CrudBQDocumentLibraryRepository.class,
		resourcePath = "osbasahcerebroinfo/document_libraries.json"
	)
	@Test
	public void testGetDownloadCountWithDates() {
		Assertions.assertEquals(
			"8",
			_documentLibrariesRestController.getDownloadsCount(
				"26413838", "480920708921930174", "480920708626120668",
				LocalDate.of(2021, 5, 20), LocalDate.of(2021, 5, 1)));
	}

	@RepositoryResource(
		repositoryClass = CrudBQDocumentLibraryRepository.class,
		resourcePath = "osbasahcerebroinfo/document_libraries.json"
	)
	@Test
	public void testGetDownloadsCount() {
		Assertions.assertEquals(
			"10",
			_documentLibrariesRestController.getDownloadsCount(
				"26413838", "480920708921930174", "480920708626120668", null,
				null));
	}

	@RepositoryResource(
		repositoryClass = CrudBQDocumentLibraryRepository.class,
		resourcePath = "osbasahcerebroinfo/document_libraries.json"
	)
	@Test
	public void testGetPreviewCountWithDates() {
		Assertions.assertEquals(
			"3",
			_documentLibrariesRestController.getPreviewsCount(
				"26413838", "480920708921930174", "480920708626120668",
				LocalDate.of(2021, 5, 20), LocalDate.of(2021, 5, 1)));
	}

	@RepositoryResource(
		repositoryClass = CrudBQDocumentLibraryRepository.class,
		resourcePath = "osbasahcerebroinfo/document_libraries.json"
	)
	@Test
	public void testGetPreviewsCount() {
		Assertions.assertEquals(
			"11",
			_documentLibrariesRestController.getPreviewsCount(
				"26413838", "480920708921930174", "480920708626120668", null,
				null));
	}

	@Autowired
	private DocumentLibrariesRestController _documentLibrariesRestController;

}