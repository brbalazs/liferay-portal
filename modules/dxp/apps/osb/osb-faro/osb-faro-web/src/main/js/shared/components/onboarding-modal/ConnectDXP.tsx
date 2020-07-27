import * as API from 'shared/api';
import BaseScreen from './BaseScreen';
import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import CopyButton from 'shared/components/CopyButton';
import DataSourceQuery from 'shared/queries/DataSourceQuery';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import Input from 'shared/components/Input';
import Modal from '../modal';
import React, {useEffect, useRef, useState} from 'react';
import {get, noop} from 'lodash';
import {Routes, toRoute} from 'shared/util/router';
import {useLazyQuery} from '@apollo/react-hooks';

const {credentialTypes, dataSourceTypes} = Constants;

const TIMEOUT_INTERVAL = 5000;

interface IConnectDXPProps {
	dataSourceId?: string;
	dxpConnected: boolean;
	groupId: string;
	isUpgrading?: boolean;
	onboarding?: boolean;
	onClose: (data?: any) => void;
	onDxpConnected: (dxpConnected: boolean) => void;
	onNext?: (increment?: number) => void;
	onPrevious: () => void;
}

const ConnectDXP: React.FC<IConnectDXPProps> = ({
	dataSourceId,
	dxpConnected,
	groupId,
	isUpgrading,
	onboarding,
	onClose,
	onDxpConnected,
	onNext,
	onPrevious
}) => {
	const [getDataSources, {data}] = useLazyQuery(DataSourceQuery, {
		fetchPolicy: 'network-only',
		onCompleted: () => {
			onDxpConnected(true);
		},
		variables: {
			credentialsType: credentialTypes.token,
			size: 1,
			sort: {
				column: 'dateCreated',
				type: 'DESC'
			},
			type: dataSourceTypes.liferay
		}
	});

	const getDataSource = () =>
		API.dataSource
			.fetch({
				groupId,
				id: dataSourceId
			})
			.then(dataSource => {
				setIsALegacyDXPConnection(hasALegacyDXPConnection(dataSource));
			})
			.catch(noop);

	const hasALegacyDXPConnection = dataSource =>
		dataSource &&
		dataSource.providerType === dataSourceTypes.liferay &&
		dataSource.credentials.type !== credentialTypes.token;

	const [isALegacyDXPConnection, setIsALegacyDXPConnection] = useState(true);

	const handleClose = () => onClose(isALegacyDXPConnection);

	const [token, setToken] = useState('');

	const _inputRef = useRef<any>();

	const getNavHref = () => {
		const id = get(data, ['dataSources', 0, 'id'], null);

		if (id) {
			return toRoute(Routes.SETTINGS_DATA_SOURCE, {groupId, id});
		}

		return toRoute(Routes.SETTINGS_DATA_SOURCE_LIST, {groupId});
	};

	const selectAll = () => _inputRef.current && _inputRef.current.selectAll();

	let _tokenRequest;

	const getNextToken: (prevToken?: string) => Promise<any> = prevToken =>
		API.dataSource
			.fetchToken(groupId, dataSourceId)
			.then(nextToken => {
				if (!prevToken || prevToken === nextToken) {
					_tokenRequest = setTimeout(
						() => getNextToken(nextToken),
						TIMEOUT_INTERVAL
					);
				} else {
					if (onboarding) {
						onDxpConnected(true);
					} else if (isUpgrading) {
						onDxpConnected(true);
						getDataSource();
					} else {
						getDataSources();
					}
				}

				return nextToken;
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					_tokenRequest = setTimeout(
						() => getNextToken(prevToken),
						TIMEOUT_INTERVAL
					);
				}

				return prevToken;
			});

	useEffect(() => {
		_tokenRequest = getNextToken().then(setToken);

		return () => {
			clearTimeout(_tokenRequest);
		};
	}, []);

	return (
		<BaseScreen
			className='connect-dxp'
			onClose={onClose}
			title={
				onboarding
					? Liferay.Language.get('first-connect-your-dxp-analytics')
					: Liferay.Language.get('connect-your-dxp-instance')
			}
		>
			<Modal.Body className='d-flex flex-column align-items-center flex-grow-1 justify-content-center'>
				<div className='dxp-to-analytics-container d-flex align-items-center justify-content-center'>
					<Icon
						size='xxl'
						symbol={
							dxpConnected ? 'dxp-icon' : 'dxp-icon-grayscale'
						}
					/>

					<Icon
						className={getCN('arrows', {connected: dxpConnected})}
						size='lg'
						symbol='ac-horizontal-arrows'
					/>

					<Icon size='xxl' symbol='ac-logo' />
				</div>

				{dxpConnected ? (
					<div className='success-info d-flex align-items-center'>
						<div>
							<Icon
								className='success-invert'
								symbol='check-circle-full'
							/>
						</div>

						<span className='success-message'>
							{Liferay.Language.get('connected')}
						</span>
					</div>
				) : (
					<>
						<div className='description'>
							<div>
								{Liferay.Language.get(
									'copy-this-token-and-enter-it-in-the-analytics-cloud-configuration-in-your-dxp-instance-settings'
								)}
							</div>

							<div>
								{Liferay.Language.get(
									'then-choose-what-sites-youd-like-to-sync-to-analytics-cloud'
								)}
							</div>
						</div>

						<Input.Group>
							<Input.GroupItem position='prepend'>
								<Input
									className='text-truncate'
									inset='after'
									onChange={noop}
									onClick={selectAll}
									ref={_inputRef}
									value={token}
								/>

								<Input.Inset position='after'>
									<CopyButton display='light' text={token} />
								</Input.Inset>
							</Input.GroupItem>
						</Input.Group>
					</>
				)}
			</Modal.Body>

			<Modal.Footer className='d-flex justify-content-between'>
				<div>
					{!dxpConnected && (
						<Button onClick={onPrevious}>
							{Liferay.Language.get('back')}
						</Button>
					)}
				</div>

				<div>
					{!(dxpConnected && onboarding) && (
						<Button
							disabled={dxpConnected}
							onClick={onboarding ? () => onNext() : onClose}
						>
							{onboarding
								? Liferay.Language.get('skip')
								: Liferay.Language.get('cancel')}
						</Button>
					)}

					<Button
						disabled={!dxpConnected}
						display='primary'
						href={
							onboarding || !dxpConnected || isUpgrading
								? null
								: getNavHref()
						}
						onClick={onboarding ? () => onNext() : handleClose}
					>
						{onboarding
							? Liferay.Language.get('next')
							: Liferay.Language.get('done')}
					</Button>
				</div>
			</Modal.Footer>
		</BaseScreen>
	);
};

export default ConnectDXP;
