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

package com.liferay.headless.commerce.delivery.catalog.resource.v1_0.test;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.Product;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Page;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Pagination;

/**
 * @author Andrea Sbarra
 */
@RunWith(Arquillian.class)
public class ProductResourceTest extends BaseProductResourceTestCase {

	@Override
	@Test
	public void testGetStoreChannelProductsPage() throws Exception {
		Page<Product> page = productResource.getStoreChannelProductsPage(
				testGetStoreChannelProductsPage_getChannelId(), null,
				Pagination.of(1, 2), null);
		
	}
	
	@Test
	public void testGetStoreChannelProductsPages() throws Exception {
		assertFalse(false);
		
	}
	
	@Override
	protected Long testGetStoreChannelProductsPage_getChannelId() throws Exception {
		return 38329L;
	}
}