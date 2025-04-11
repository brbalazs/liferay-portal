/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Autocomplete} from 'commerce-frontend-js';

export default function ({
	addressSubtypeConfiguration = {
		billing: '',
		billingAndShipping: '',
		shipping: '',
	},
	initialAddressType,
	initialLabel,
	initialValue,
	namespace,
}) {
	const addressTypeSelect = document.getElementById(
		`${namespace}commerceAddress`
	);
	const useAsBillingCheckbox = document.getElementById(
		`${namespace}use-as-billing`
	);

	const initAutocomplete = (
		autoload,
		externalReferenceCode,
		initialLabel,
		initialValue,
		readOnly
	) => {
		Autocomplete('autocomplete', 'autocomplete-root', {
			apiUrl: `/o/headless-admin-list-type/v1.0/list-type-definitions/by-external-reference-code/${externalReferenceCode}/list-type-entries`,
			autoload,
			initialLabel,
			initialValue,
			inputId: `${namespace}subtype`,
			inputName: `${namespace}subtype`,
			inputPlaceholder: 'Subtype',
			itemsKey: 'key',
			itemsLabel: 'name',
			readOnly,
			required: false,
		});
	};

	const getExternalReferenceCode = (type, useAsBilling) => {
		if (type === 'billing') {
			return addressSubtypeConfiguration.billing;
		}
		else if (type === 'shipping' || !useAsBilling) {
			return addressSubtypeConfiguration.shipping;
		}
		else if (type === 'billing-and-shipping' || useAsBilling) {
			return addressSubtypeConfiguration.billingAndShipping;
		}

		return '';
	};

	let addressType = initialAddressType;
	let externalReferenceCode = getExternalReferenceCode(
		addressType,
		useAsBillingCheckbox.checked
	);
	let subtype = '';

	addressTypeSelect.addEventListener('change', (event) => {
		addressType = Array.from(event.target.children).filter(
			(item) => item.value === event.target.value
		)[0].dataset.listtypekey;

		subtype = Array.from(event.target.children).filter(
			(item) => item.value === event.target.value
		)[0].dataset.subtypekey;

		externalReferenceCode = getExternalReferenceCode(
			addressType,
			useAsBillingCheckbox.checked
		);

		if (externalReferenceCode) {
			if (addressTypeSelect.value > 0) {
				initAutocomplete(
					true,
					externalReferenceCode,
					subtype,
					subtype,
					true
				);
			}
			else {
				initAutocomplete(
					true,
					externalReferenceCode,
					subtype,
					subtype,
					false
				);
			}
		}
		else {
			initAutocomplete(false, '', subtype, subtype, true);
		}
	});

	useAsBillingCheckbox.addEventListener('change', (event) => {
		addressTypeSelect.dispatchEvent(new Event('change'));
	});

	if (externalReferenceCode) {
		if (addressTypeSelect.value > 0) {
			initAutocomplete(
				true,
				externalReferenceCode,
				subtype,
				subtype,
				true
			);
		}
		else {
			initAutocomplete(
				true,
				externalReferenceCode,
				subtype,
				subtype,
				false
			);
		}
	}
	else {
		initAutocomplete(false, '', subtype, subtype, true);
	}
}
