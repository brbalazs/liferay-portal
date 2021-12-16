import * as API from 'shared/api';
import BaseScreen from './BaseScreen';
import Button from 'shared/components/Button';
import CopyButton from 'shared/components/CopyButton';
import DataSourceQuery from 'shared/queries/DataSourceQuery';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import InfoPopover from 'shared/components/InfoPopover';
import Input from 'shared/components/Input';
import Label from 'shared/components/form/Label';
import Modal from 'shared/components/modal';
import React, {useEffect, useRef, useState} from 'react';
import Select from 'shared/components/Select';
import URLConstants from 'shared/util/url-constants';
import {ActionType, useChannelContext} from 'shared/context/channel';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {CredentialTypes, DataSourceTypes} from 'shared/util/constants';
import {DataSource} from 'shared/util/records';
import {fetchDataSource} from 'shared/actions/data-sources';
import {get, noop, upperFirst} from 'lodash';
import {getDefaultChannel} from 'shared/components/channels-menu';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useLazyQuery} from '@apollo/react-hooks';
import {withHistory} from 'shared/hoc';

const TIMEOUT_INTERVAL = 5000;

const DXP_VERSIONS = {
	'dxp-70-fixpack-98': {
		label: 'DXP 7.0 Fix Pack 98',
		url: URLConstants.DownloadDXP70FixPack98
	},
	'dxp-71-fixpack-22': {
		label: 'DXP 7.1 Fix Pack 22',
		url: URLConstants.DownloadDXP71FixPack22
	},
	'dxp-72-fixpack-1': {
		label: 'DXP 7.2 Fix Pack 11',
		url: URLConstants.DownloadDXP72FixPack11
	},
	'dxp-73-fixpack-1': {
		label: 'DXP 7.3 Fix Pack 1',
		url: URLConstants.DownloadDXP73FixPack1
	}
};

interface IConnectDXPProps {
	dataSourceId?: string;
	dxpConnected: boolean;
	fetchDataSource: ({
		groupId,
		id
	}: {
		groupId: string;
		id: string;
	}) => DataSource;
	groupId: string;
	history: {
		push: (path: string) => void;
	};
	isUpgrading: boolean;
	onboarding?: boolean;
	onClose: () => void;
	onDxpConnected: (dxpConnected: boolean) => void;
	onNext?: (increment?: number) => void;
	onPrevious?: () => void;
}

const ConnectDXP: React.FC<IConnectDXPProps> = ({
	dataSourceId,
	dxpConnected,
	fetchDataSource,
	groupId,
	history,
	onboarding,
	onClose,
	onDxpConnected,
	onNext
}) => {
	const {channelDispatch} = useChannelContext();

	const [getDataSources, {data}] = useLazyQuery(DataSourceQuery, {
		fetchPolicy: 'network-only',
		onCompleted: () => {
			onDxpConnected(true);
		},
		variables: {
			credentialsType: CredentialTypes.Token,
			size: 1,
			sort: {
				column: 'dateCreated',
				type: 'DESC'
			},
			type: DataSourceTypes.Liferay
		}
	});

	const [tokenCopied, setTokenCopied] = useState(false);
	const [token, setToken] = useState('');
	const [dxpVersion, setDxpVersion] = useState<string>('dxp-70-fixpack-98');

	const _inputRef = useRef<any>();

	const copyButtonClassName = getCN({'input-success': tokenCopied});

	const getNavHref = () => {
		const id = get(data, ['dataSources', 0, 'id'], null);

		if (id) {
			return toRoute(Routes.SETTINGS_DATA_SOURCE, {groupId, id});
		}

		return toRoute(Routes.SETTINGS_DATA_SOURCE_LIST, {groupId});
	};

	const selectAll = () => {
		analytics.track('Clicked Copy Token Button - TEST', null, {
			ip: '0'
		});

		_inputRef.current && _inputRef.current.selectAll();
	};

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
					analytics.track(
						'Established connection w/ DXP - TEST',
						null,
						{
							ip: '0'
						}
					);

					if (onboarding) {
						onDxpConnected(true);

						updateChannels();
					} else {
						// if it's an upgrade from oauth to token, we need to fetch the DataSource
						if (dataSourceId) {
							fetchDataSource({groupId, id: dataSourceId});
						} else {
							API.dataSource
								.fetchDataSourceId({groupId, token: prevToken})
								.then(newDataSourceId => {
									if (newDataSourceId) {
										fetchDataSource({
											groupId,
											id: newDataSourceId
										});
									}
								});
						}

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

	const updateChannels = () => {
		API.channels.fetchAll({groupId}).then(({items}) => {
			const channelId = get(items, [0, 'id']);

			history.push(toRoute(Routes.SITES, {channelId, groupId}));

			channelDispatch({
				payload: getDefaultChannel(channelId, items),
				type: ActionType.setSelectedChannel
			});

			channelDispatch({
				payload: items,
				type: ActionType.setChannels
			});
		});
	};

	useEffect(() => {
		_tokenRequest = getNextToken().then(setToken);

		return () => {
			clearTimeout(_tokenRequest);
		};
	}, []);

	return (
		<BaseScreen className='connect-dxp' onClose={onClose}>
			<Modal.Body className='d-flex flex-column align-items-center flex-grow-1 justify-content-center'>
				<div className='analytics-to-dxp-container'>
					<Icon size='xl' symbol='ac-logo' />

					<Icon
						className={getCN('arrows', {connected: dxpConnected})}
						size='lg'
						symbol='ac-horizontal-arrows'
					/>

					<Icon
						size='xl'
						symbol={
							dxpConnected ? 'dxp-icon' : 'dxp-icon-grayscale'
						}
					/>
				</div>

				<span className='title d-flex justify-content-center'>
					{onboarding
						? Liferay.Language.get(
								'first-connect-your-dxp-analytics'
						  )
						: Liferay.Language.get('connect-your-dxp-instance')}
				</span>

				<Input.Group>
					<Input.GroupItem position='prepend'>
						<Input
							className={getCN('text-truncate', {
								'input-success': dxpConnected || tokenCopied
							})}
							inset='after'
							onChange={noop}
							onClick={selectAll}
							ref={_inputRef}
							value={token}
						/>

						{!dxpConnected && (
							<Input.Inset
								className={copyButtonClassName}
								position='after'
							>
								<CopyButton
									className={copyButtonClassName}
									display='light'
									onClick={() => {
										setTokenCopied(true);

										analytics.track(
											'Clicked Copy Token Button - TEST',
											null,
											{ip: '0'}
										);
									}}
									text={token}
								/>
							</Input.Inset>
						)}
					</Input.GroupItem>
				</Input.Group>

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
						<div className='documentation-link-text w-100 ml-6 mt-1'>
							{sub(
								Liferay.Language.get(
									'x-to-learn-how-to-connect-liferay-dxp-to-analytics-cloud'
								),
								[
									<a
										href={URLConstants.HelpConnectDxp}
										key='helpConnectDxpText'
										target='_blank'
									>
										{upperFirst(
											Liferay.Language.get(
												'click-here'
											).toLowerCase()
										)}
									</a>
								],
								false
							)}
						</div>

						<div className='fix-pack-container'>
							<div className='fix-pack-select'>
								<Label>
									{Liferay.Language.get(
										'dxp-fix-pack-requirements'
									)}

									<InfoPopover
										className='ml-2'
										content={Liferay.Language.get(
											'minimum-fix-pack-version-required-for-full-functionality'
										)}
										popOverAttr={{
											className: 'popover-background-dark'
										}}
									/>
								</Label>
								<Select
									className='mt-1'
									onChange={({target: {value}}) =>
										setDxpVersion(value)
									}
									value={dxpVersion}
								>
									{Object.keys(DXP_VERSIONS).map(key => (
										<Select.Item key={key} value={key}>
											{DXP_VERSIONS[key].label}
										</Select.Item>
									))}
								</Select>
							</div>

							<div className='fix-pack-button'>
								<Button
									borderless
									className='more-information-link mt-4'
									externalLink
									href={DXP_VERSIONS[dxpVersion].url}
									icon='shortcut'
									iconAlignment='right'
									target='_blank'
								>
									{Liferay.Language.get('download')}
								</Button>
							</div>
						</div>
					</>
				)}
			</Modal.Body>

			<Modal.Footer className='d-flex justify-content-end'>
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
						href={onboarding || !dxpConnected ? null : getNavHref()}
						onClick={onboarding ? () => onNext() : onClose}
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

export default compose<any>(
	withHistory,
	connect(null, {
		fetchDataSource
	})
)(ConnectDXP);
