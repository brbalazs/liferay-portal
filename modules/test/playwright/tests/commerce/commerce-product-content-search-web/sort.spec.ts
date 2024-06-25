/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {apiHelpersTest} from '../../../fixtures/apiHelpersTest';
import {commercePagesTest} from '../../../fixtures/commercePagesTest';
import {dataApiHelpersTest} from '../../../fixtures/dataApiHelpersTest';
import {loginTest} from '../../../fixtures/loginTest';
import {
	applicationsMenuPageTest
} from "../../../fixtures/applicationsMenuPageTest";
import {getRandomInt} from "../../../utils/getRandomInt";

export const test = mergeTests(
	apiHelpersTest,
	applicationsMenuPageTest,
	commercePagesTest,
	dataApiHelpersTest,
	loginTest()
);

async function setAndCheckDefaultSorting({
										   siteName,
										   sortingOption,
										   applicationsMenuPage,
										   commerceThemeMiniumPage,
										   commerceThemeMiniumCatalogPage,
										   page
									   }) {
	await applicationsMenuPage.goToSite(siteName);

	// await commerceThemeMiniumPage.catalogLink.click();
	// await page.waitForSelector('[id^="portlet_com_liferay_commerce_product_content_search_web_internal_portlet_CPSortPortlet"][title="Options"]');
	// 'button[name="Add to Cart"]:nth-match(1)'
	// await page.waitForSelector('button[name="Search"]');
	// await commerceThemeMiniumCatalogPage.page.waitForSelector('checkbox[aria-label="Compare"]:nth-match(1)');
	// await commerceThemeMiniumCatalogPage.addProductToCartButton.isVisible();
	// await page.waitForTimeout(1000);
	// const isSearchInputVisible = await commerceThemeMiniumCatalogPage.optionsButton.isVisible();
	// expect(isSearchInputVisible).toBe(true);
	await expect(page.getByRole('button', { name: 'Search' })).toBeEnabled();
	// await expect(page.getByRole('button', { name: 'Select Account & Order' })).toBeEnabled();
	// await expect(page.getByRole('button', { name: 'Options' })).toBeEnabled();

	await commerceThemeMiniumCatalogPage.optionsButton.click();
	// await commerceThemeMiniumCatalogPage.configurationMenuItem.waitFor({state: 'attached', timeout: 200}).catch(async () => {
	// 	await commerceThemeMiniumCatalogPage.optionsButton.click();
	// }).finally(async () => {
		await commerceThemeMiniumCatalogPage.configurationMenuItem.click();
		await commerceThemeMiniumCatalogPage.configurationIFrameDefaultSortingDropdownMenu.selectOption(sortingOption);
		await commerceThemeMiniumCatalogPage.configurationIFrameSaveButton.click();
		await commerceThemeMiniumCatalogPage.configurationIFrameCloseButton.click();
		await page.reload();

		expect(await commerceThemeMiniumCatalogPage.orderByButton.innerText()).toContain(sortingOption);
	// });
	// await expect(commerceThemeMiniumCatalogPage.configurationMenuItem).toBeVisible();
	// await page.waitForTimeout(1500);
}

test('LPD-18714 Setting default sort for commerce products', async ({
	apiHelpers,
	applicationsMenuPage,
	commerceThemeMiniumPage,
	commerceThemeMiniumCatalogPage,
	page,
}) => {
	// const siteName1 = 'Minium' + getRandomInt();
	const siteName1 = 'Min1';

	// const site1 = await apiHelpers.headlessSite.createSite({
	// 	name: siteName1,
	// 	templateKey: 'minium-initializer',
	// 	templateType: 'site-initializer',
	// });
	//
	// apiHelpers.data.push({id: site1.id, type: 'site'});

	// const siteName2 = 'Minium' + getRandomInt()
	const siteName2 = 'Min2';

	// const site2 = await apiHelpers.headlessSite.createSite({
	// 	name: siteName2,
	// 	templateKey: 'minium-initializer',
	// 	templateType: 'site-initializer',
	// });
	//
	// apiHelpers.data.push({id: site2.id, type: 'site'});

	const sortingOption = 'Name Ascending';

	await setAndCheckDefaultSorting({
		applicationsMenuPage,
		commerceThemeMiniumCatalogPage,
		commerceThemeMiniumPage,
		page,
		siteName: siteName1,
		sortingOption: sortingOption,
	});

	await setAndCheckDefaultSorting({
		applicationsMenuPage,
		commerceThemeMiniumCatalogPage,
		commerceThemeMiniumPage,
		page,
		siteName: siteName2,
		sortingOption: 'Price Low to High',
	});

	await applicationsMenuPage.goToSite(siteName1);

	expect(await commerceThemeMiniumCatalogPage.orderByButton.innerText()).toContain(sortingOption);
});