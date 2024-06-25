/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {FrameLocator, Locator, Page} from '@playwright/test';

export class CommerceThemeMiniumCatalogPage {
	readonly addProductToCartButton: Locator;
	readonly configurationIFrame: FrameLocator;
	readonly configurationIFrameCloseButton: Locator;
	readonly configurationIFrameSaveButton: Locator;
	readonly configurationIFrameDefaultSortingDropdownMenu: Locator;
	readonly configurationMenuItem: Locator;
	readonly optionsButton: Locator;
	readonly orderByButton: Locator;
	readonly page: Page;
	readonly searchInput: Locator;

	constructor(page: Page) {
		this.addProductToCartButton = this.addProductToCartButton = page.locator('button[name="Add to Cart"]:nth-match(1)');
		this.configurationIFrame = page.frameLocator(
			'iframe[id="modalIframe"]'
		);
		this.configurationIFrameCloseButton = this.configurationIFrame.getByRole(
			'button',
			{name: 'Close'}
		);
		this.configurationIFrameSaveButton = this.configurationIFrame.getByRole(
			'button',
			{name: 'Save'}
		);
		this.configurationIFrameDefaultSortingDropdownMenu =
			this.configurationIFrame.getByLabel(
				'select-default-sorting'
			);
		this.configurationMenuItem = page.getByRole('menuitem', {
			exact: true,
			name: 'Configuration',
		});
		this.optionsButton = page
			.locator(
				'[id^="portlet_com_liferay_commerce_product_content_search_web_internal_portlet_CPSortPortlet"]'
			)
			.getByTitle('Options');
		this.orderByButton = page.locator('#commerce-order-by');
		this.page = page;
		this.searchInput = page.getByTestId('searchInput');
	}
}
