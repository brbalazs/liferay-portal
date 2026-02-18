/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import Autocomplete from '@clayui/autocomplete';
import ClayForm from '@clayui/form';
import ClayIcon from '@clayui/icon';
import Sticker from '@clayui/sticker';
import {ApiHelper} from '@liferay/site-cms-site-initializer';
import classNames from 'classnames';
import {debounce, sub} from 'frontend-js-web';
import React, {useCallback, useContext, useEffect, useMemo, useState} from 'react';

import {displayErrorToast} from '../common/utils/toastUtil';
import {TRoomContext, TRoomStepProps} from '../common/utils/types';
import FieldErrorMessage from './FieldErrorMessage';
import {RoomContext} from './RoomInitializer';

type TAccount = {
	key: string;
	logoURL?: string;
	name: string;
};

function getInitials(name: string) {
	return name
		.split(' ')
		.map((chunk) => chunk.charAt(0).toUpperCase())
		.join('');
}

function getRandomColor(str: string) {
	const colors = [
		'#0B5FFF',
		'#AF78FF',
		'#50D2A0',
		'#FF73C3',
		'#FFB46E',
		'#FF5F5F',
	];
	let hash = 0;

	for (let i = 0; i < str.length; i++) {
		hash = str.charCodeAt(i) + ((hash << 5) - hash);
	}

	return colors[Math.abs(hash) % colors.length];
}

function getAccountId(
	accountName: string | undefined,
	accounts: Array<TAccount>
) {
	return (
		accounts.find((item) => item.name === (accountName || ''))?.key || '0'
	);
}

async function getAccounts(accountName?: string): Promise<{items: Array<{id: number; logoURL?: string; name: string}>}> {
	let url = '/o/headless-commerce-admin-account/v1.0/accounts?sort=name:asc';

	if (accountName) {
		url += `&search=${encodeURIComponent(accountName)}`;
	}

	const {data, error} = await ApiHelper.get<{items: Array<{id: number; logoURL?: string; name: string}>}>(
		url
	);

	if (error) {
		throw new Error(error);
	}

	return data || {items: []};
}

function RoomAccountAssociationStep({
	numberOfSteps,
	setHandleStepSubmit,
	step = 1,
}: TRoomStepProps) {
	const {dataContext, loading, setDataContext, setLoading} =
		useContext<TRoomContext>(RoomContext);
	const [accountName, setAccountName] = useState(dataContext.accountName);
	const [accounts, setAccounts] = useState<Array<TAccount>>([]);
	const [currentAccountName, setCurrentAccountName] = useState(
		dataContext.accountName
	);

	const debouncedSetAccountName = useMemo(
		() =>
			debounce((currentValue) => {
				setAccountName(currentValue);
			}, 250),
		[]
	);

	const handleFieldChange = useCallback(
		({
			target: {id, name, value},
		}: {
			target: {
				id: string;
				name: string;
				value: string;
			};
		}) => {
			if (name === 'accountId') {
				const accountId = parseInt(id || getAccountId(value, accounts), 10) || 0;

				setDataContext((prevState) => ({
					...prevState,
					accountId: accountId || undefined,
					accountName: value || '',
					errors: {
						...prevState.errors,
						accountId: accountId
							? ''
							: Liferay.Language.get('this-field-is-mandatory'),
					},
				}));
			}
		},
		[accounts, setDataContext]
	);

	const handleAccountIdFieldChange = useCallback(
		(value: string) => {
			setCurrentAccountName(value);
			debouncedSetAccountName(value);

			const accountId = getAccountId(value, accounts);

			if (parseInt(accountId, 10)) {
				handleFieldChange({
					target: {id: accountId, name: 'accountId', value},
				});
			}
		},
		[accounts, debouncedSetAccountName, handleFieldChange]
	);

	useEffect(() => {
		setCurrentAccountName(dataContext.accountName || '');
	}, [dataContext.accountName]);

	useEffect(() => {
		getAccounts(accountName)
			.then((data) => {
				setAccounts(
					data.items.map((item) => {
						const hasLogo =
							item.logoURL &&
							!item.logoURL.includes('img_id=0');

						return {
							key: String(item.id),
							logoURL: hasLogo
								? item.logoURL
								: undefined,
							name: item.name,
						};
					})
				);
			})
			.catch((error) => {
				displayErrorToast((error as Error).message);
			});
	}, [accountName]);

	useEffect(() => {
		setHandleStepSubmit(() => async (event: Event): Promise<boolean> => {
			event.preventDefault();

			if (!dataContext.accountId) {
				setDataContext((prevState) => ({
					...prevState,
					errors: {
						...prevState.errors,
						accountId: Liferay.Language.get(
							'this-field-is-mandatory'
						),
					},
				}));

				return Promise.resolve(false);
			}

			return Promise.resolve(true);
		});
	}, [dataContext, setDataContext, setHandleStepSubmit]);

	return (
		<>
			<div>
				<div className="mb-1 text-secondary" data-qa-id="stepLocator">
					{sub(
						Liferay.Language.get('step-x-of-x'),
						step,
						numberOfSteps
					)}
				</div>

				<div
					className="mb-1 text-6 text-weight-bold"
					data-qa-id="stepTitle"
				>
					{Liferay.Language.get('account-association')}
				</div>

				<div className="text-secondary">
					{Liferay.Language.get(
						'choose-the-account-you-want-to-associate-with-this-digital-sales-room.-this-will-determine-which-team-members-and-contacts-can-be-invited-to-the-space'
					)}
				</div>
			</div>
			<div className="mt-4 row">
				<ClayForm.Group
					className={classNames('col-12', {
						'has-error': !!dataContext.errors.accountId,
					})}
				>
					<label className="d-block" htmlFor="dsr-account-id">
						{Liferay.Language.get('select-account')}

						<span className="c-ml-2 reference-mark">
							<ClayIcon symbol="asterisk" />

							<span className="hide-accessible sr-only">
								{Liferay.Language.get('required')}
							</span>
						</span>
					</label>

					<Autocomplete
                    						aria-label={Liferay.Language.get('select-account')}
                    						className="mb-3"
                    						data-qa-id="selectAccountInput"
                    						defaultValue={String(dataContext.accountId || '')}
                    						disabled={loading}
                    						filterKey="name"
                    						id="accountId"
                    						items={accounts}
                    						menuTrigger="focus"
                    						name="dsr-account-id"
                    						onChange={(value: string) => {
                    							if (!value) {
                    								handleFieldChange({
                    									target: {
                    										id: '0',
                    										name: 'accountId',
                    										value: '',
                    									},
                    								});
                    							}

                    							handleAccountIdFieldChange(value);
                    						}}
                    						placeholder=""
                    						value={currentAccountName}
                    					>
                    						{(item: TAccount) => (
                    							<Autocomplete.Item
                    								key={item.key}
                    								onClick={() => {
                    									handleFieldChange({
                    										target: {
                    											id: item.key,
                    											name: 'accountId',
                    											value: item.name,
                    										},
                    									});
                    								}}
                    								textValue={item.name}
                    							>
                    								<div className="align-items-center d-flex">
                    									<Sticker
                    										className="c-mr-2 flex-shrink-0"
                    										shape="circle"
                    										size="sm"
                    										style={
                    											item.logoURL
                    												? undefined
                    												: {
                    													backgroundColor: getRandomColor(item.name),
                    													color: '#FFF',
                    												}
                    										}
                    									>
                    										{item.logoURL ? (
                    											<Sticker.Image
                    												alt={item.name}
                    												src={item.logoURL}
                    											/>
                    										) : (
                    											getInitials(item.name)
                    										)}
                    									</Sticker>

                    									<span>{item.name}</span>
                    								</div>
                    							</Autocomplete.Item>
                    						)}
                    					</Autocomplete>

					<FieldErrorMessage
						error={dataContext.errors.accountId}
						name="accountId"
					/>
				</ClayForm.Group>
			</div>
		</>
	);
}

export default RoomAccountAssociationStep;
