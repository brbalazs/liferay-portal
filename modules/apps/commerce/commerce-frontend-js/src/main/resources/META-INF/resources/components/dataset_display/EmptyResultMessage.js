/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

function EmptyResultMessage() {
	return (
		<div className="sheet taglib-empty-result-message border-0 pt-0">
			<div className="taglib-empty-result-message-header"></div>
			<div className="sheet-text text-center">
				{Liferay.Language.get('no-items-were-found')}
			</div>
		</div>
	);
}

export default EmptyResultMessage;
