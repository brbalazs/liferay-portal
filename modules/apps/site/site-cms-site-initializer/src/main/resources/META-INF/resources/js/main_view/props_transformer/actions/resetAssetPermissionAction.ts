/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {sub} from 'frontend-js-web';

export default function resetAssetPermissionAction({
                                                       className,
                                                       classPK,
                                                       loadData,
                                                   }: {
    className: string;
    classPK: number;
    loadData: () => void;
}) {
    Liferay.Util.openModal({
        bodyHTML: `<p>${sub(
            Liferay.Language.get(
                'are-you-sure-you-want-to-reset-the-permissions'
            ),
            ''
        )}</p>`,
        buttons: [
            {
                displayType: 'secondary',
                label: Liferay.Language.get('cancel'),
                type: 'cancel',
            },
            {
                displayType: 'primary',
                label: Liferay.Language.get('ok'),
                onClick: async ({processClose}: {processClose: () => void}) => {
                    try {
                        const response = await fetch(
                            '/o/headless-cms/v1.0/asset-permission/',
                            {
                                body: JSON.stringify({
                                    className,
                                    classPK,
                                    type: 'ResetAssetPermissionAction',
                                }),
                                headers: {
                                    'Content-Type': 'application/json',
                                    'X-CSRF-Token': Liferay.authToken,
                                },
                                method: 'POST',
                            }
                        );

                        if (!response.ok) {
                            const error = await response.json();
                            console.error('Error response:', error);

                            throw new Error('Failed to reset permissions');
                        }

                        Liferay.Util.openToast({
                            message: Liferay.Language.get(
                                'permissions-reset-successfully'
                            ),
                            type: 'success',
                        });

                        loadData();
                    }
                    catch (error) {
                        console.error('Error resetting permissions:', error);

                        Liferay.Util.openToast({
                            message: Liferay.Language.get('an-error-occurred'),
                            type: 'danger',
                        });
                    }
                    finally {
                        processClose();
                    }
                },
            },
        ],
        size: 'md',
        status: 'warning',
        title: Liferay.Language.get('confirm-reset-permissions'),
    });
}