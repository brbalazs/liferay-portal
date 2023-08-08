/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.repository;

import com.liferay.osb.asah.test.util.entity.BQBlog;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Robson Pastor
 */
@Repository
public interface CrudBQBlogRepository extends CrudRepository<BQBlog, Long> {
}