/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {dataApiHelpersTest} from '../../../fixtures/dataApiHelpersTest';
import {featureFlagsTest} from '../../../fixtures/featureFlagsTest';
import {loginTest} from '../../../fixtures/loginTest';
import {getRandomInt} from '../../../utils/getRandomInt';
import {PORTLET_URLS} from '../../../utils/portletUrls';
import {cmsPagesTest} from './fixtures/cmsPagesTest';

const test = mergeTests(
	cmsPagesTest,
	dataApiHelpersTest,
	featureFlagsTest({
		'LPD-17564': {enabled: true},
		'LPD-32050': {enabled: true},
		'LPS-179669': {enabled: true},
	}),
	loginTest()
);

async function createSpace(page, spaceName: string) {
	await page.getByTestId('fdsCreationActionButton').click();
	await page.getByLabel('Space Name').fill(spaceName);
	await page.getByRole('button', {name: 'Continue'}).click();
	await page.getByRole('button', {name: 'Continue Without Members'}).click();
}

async function deleteSpace(page, spaceName: string) {
	await expect(async () => {
		await (await getTableRowByText(page, spaceName))
			.getByRole('button', {name: 'Actions'})
			.click();
		await page.getByRole('menuitem', {name: 'Delete'}).click();
	}).toPass();

	await page.getByRole('button', {name: 'Delete'}).click();
}

async function getTableRowByText(page, text: string) {
	return page.locator('table.table tbody tr', {hasText: text}).first();
}

async function verifyPermissions(
	checked,
	defaultPermissionsPage,
	objectName: string,
	page,
	permissions: Array<{action: string; role: string}>
) {
	await expect(async () => {
		await (await getTableRowByText(page, objectName))
			.getByRole('button', {name: 'Actions'})
			.click();
		await page
			.getByRole('menuitem', {
				exact: true,
				name: 'Default Permissions',
			})
			.click();
	}).toPass();

	await defaultPermissionsPage.verifyPermissions(checked, permissions);

	await defaultPermissionsPage.permissionsModalCancelButton.click();
}

test(
	'Space and folder contents inherit parent default permissions',
	{tag: '@LPD-62475'},
	async ({defaultPermissionsPage, folderPage, page, spaceSummaryPage}) => {
		await page.goto(PORTLET_URLS.cmsAllSpaces);

		const spaceName = 'Space' + getRandomInt();

		await createSpace(page, spaceName);

		try {
			await page.goto(PORTLET_URLS.cmsAllSpaces);

			await expect(async () => {
				await (await getTableRowByText(page, spaceName))
					.getByRole('button', {name: 'Actions'})
					.click();
				await page
					.getByRole('menuitem', {name: 'Default Permissions'})
					.click();
			}).toPass();

			const permissions = [
				{action: 'DELETE', role: 'Power User'},
				{action: 'PERMISSIONS', role: 'User'},
			];

			await defaultPermissionsPage.checkPermissionsAndSave(permissions);

			await spaceSummaryPage.goto(spaceName);

			await spaceSummaryPage.viewAllContentLink.click();

			const folderName = 'Folder' + getRandomInt();

			await folderPage.createFolder(folderName);

			await verifyPermissions(
				true,
				defaultPermissionsPage,
				folderName,
				page,
				permissions
			);

			await page.getByRole('link', {name: folderName}).click();

			const subFolderName = 'SubFolder' + getRandomInt();

			await folderPage.createFolder(subFolderName);

			await verifyPermissions(
				true,
				defaultPermissionsPage,
				subFolderName,
				page,
				permissions
			);

			await page.getByTestId('fdsCreationActionButton').click();
			await page
				.getByRole('menuitem', {name: 'Basic Web Content'})
				.click();
			await page.getByRole('button', {name: 'Publish'}).click();

			await (await getTableRowByText(page, 'Basic Web Content'))
				.getByRole('button', {name: 'Actions'})
				.click();

			await expect(
				page.getByRole('menuitem', {name: 'Permissions'})
			).toBeVisible();
			await expect(
				page.getByRole('menuitem', {name: 'Default Permissions'})
			).not.toBeVisible();
		}
		finally {
			await page.goto(PORTLET_URLS.cmsAllSpaces);

			await deleteSpace(page, spaceName);
		}
	}
);

test(
	'Change space permissions in bulk',
	{tag: '@LPD-62475'},
	async ({defaultPermissionsPage, page}) => {
		await page.goto(PORTLET_URLS.cmsAllSpaces);

		const spaceName1 = 'Space' + getRandomInt();
		const spaceName2 = 'Space' + getRandomInt();

		await createSpace(page, spaceName1);

		await page.goto(PORTLET_URLS.cmsAllSpaces);

		await createSpace(page, spaceName2);

		try {
			await page.goto(PORTLET_URLS.cmsAllSpaces);

			await (await getTableRowByText(page, spaceName1))
				.getByRole('checkbox')
				.check();
			await (await getTableRowByText(page, spaceName2))
				.getByRole('checkbox')
				.check();

			await expect(async () => {
				await page.getByLabel('Actions').click();
				await page
					.getByRole('menuitem', {name: 'Default Permissions'})
					.click();
			}).toPass();

			const permissions = [
				{action: 'DELETE', role: 'Power User'},
				{action: 'PERMISSIONS', role: 'User'},
			];

			await defaultPermissionsPage.checkPermissionsAndSave(permissions);

			await verifyPermissions(
				true,
				defaultPermissionsPage,
				spaceName1,
				page,
				permissions
			);
			await verifyPermissions(
				true,
				defaultPermissionsPage,
				spaceName2,
				page,
				permissions
			);
			await verifyPermissions(
				false,
				defaultPermissionsPage,
				'Default',
				page,
				permissions
			);
		}
		finally {
			await page.goto(PORTLET_URLS.cmsAllSpaces);

			await deleteSpace(page, spaceName1);
			await deleteSpace(page, spaceName2);
		}
	}
);
