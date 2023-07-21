/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.webdav.bundle.webdavutil;

import com.liferay.portal.kernel.lock.Lock;
import com.liferay.portal.kernel.webdav.Resource;
import com.liferay.portal.kernel.webdav.Status;
import com.liferay.portal.kernel.webdav.WebDAVRequest;
import com.liferay.portal.kernel.webdav.WebDAVStorage;
import com.liferay.portal.kernel.webdav.methods.MethodFactory;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"service.ranking:Integer=" + Integer.MAX_VALUE,
		"webdav.storage.token=" + TestWebDAVStorage.TOKEN
	},
	service = WebDAVStorage.class
)
public class TestWebDAVStorage implements WebDAVStorage {

	public static final String TOKEN = "TOKEN";

	@Override
	public int copyCollectionResource(
		WebDAVRequest webDAVRequest, Resource resource, String destination,
		boolean overwrite, long depth) {

		return 0;
	}

	@Override
	public int copySimpleResource(
		WebDAVRequest webDAVRequest, Resource resource, String destination,
		boolean overwrite) {

		return 0;
	}

	@Override
	public int deleteResource(WebDAVRequest webDAVRequest) {
		return 0;
	}

	@Override
	public MethodFactory getMethodFactory() {
		return null;
	}

	@Override
	public Resource getResource(WebDAVRequest webDAVRequest) {
		return null;
	}

	@Override
	public List<Resource> getResources(WebDAVRequest webDAVRequest) {
		return null;
	}

	@Override
	public String getRootPath() {
		return null;
	}

	@Override
	public String getToken() {
		return _token;
	}

	@Override
	public boolean isAvailable(WebDAVRequest webDAVRequest) {
		return false;
	}

	@Override
	public boolean isSupportsClassTwo() {
		return false;
	}

	@Override
	public Status lockResource(
		WebDAVRequest webDAVRequest, String owner, long timeout) {

		return null;
	}

	@Override
	public Status makeCollection(WebDAVRequest webDAVRequest) {
		return null;
	}

	@Override
	public int moveCollectionResource(
		WebDAVRequest webDAVRequest, Resource resource, String destination,
		boolean overwrite) {

		return 0;
	}

	@Override
	public int moveSimpleResource(
		WebDAVRequest webDAVRequest, Resource resource, String destination,
		boolean overwrite) {

		return 0;
	}

	@Override
	public int putResource(WebDAVRequest webDAVRequest) {
		return 0;
	}

	@Override
	public Lock refreshResourceLock(
		WebDAVRequest webDAVRequest, String uuid, long timeout) {

		return null;
	}

	@Override
	public void setRootPath(String rootPath) {
	}

	@Override
	public void setToken(String token) {
		_token = token;
	}

	@Override
	public boolean unlockResource(WebDAVRequest webDAVRequest, String token) {
		return false;
	}

	private String _token;

}