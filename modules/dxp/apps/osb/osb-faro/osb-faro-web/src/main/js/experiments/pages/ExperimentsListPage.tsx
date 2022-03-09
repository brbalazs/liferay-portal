import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import Button from 'shared/components/Button';
import EmptyStateDashboard from 'shared/components/EmptyStateDashboard';
import ExperimentListCard from '../hocs/ExperimentListCard';
import Icon from 'shared/components/Icon';
import React from 'react';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import URLConstants from 'shared/util/url-constants';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {
	createOrderIOMap,
	getGraphQLVariablesFromPagination,
	MODIFIED_DATE
} from 'shared/util/pagination';
import {EXPERIMENT_LIST_QUERY} from '../queries/ExperimentQuery';
import {get} from 'lodash';
import {IBasePageContext, Router} from 'shared/types';
import {RootState} from 'shared/store';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useChannelContext} from 'shared/context/channel';
import {useDataSource} from 'shared/hooks/useDataSource';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';
import {useQueryPagination} from 'shared/hooks';
import {User} from 'shared/util/records';

const connector = connect(
	(
		store: RootState,
		{
			router: {
				params: {groupId}
			}
		}: {router: Router}
	) => ({
		timeZoneId: store.getIn([
			'projects',
			groupId,
			'data',
			'timeZone',
			'timeZoneId'
		])
	})
);

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IExperimentsListPage extends IBasePageContext, PropsFromRedux {
	currentUser: User;
	router: {
		params: {
			channelId: string;
			groupId: string;
		};
		query: {
			query: string;
		};
	};
	timeZoneId: string;
}

const ExperimentsListPage: React.FC<IExperimentsListPage> = ({
	currentUser,
	router,
	timeZoneId
}) => {
	const {channelId, groupId} = useParams();
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(MODIFIED_DATE)
	});
	const dataSourceStates = useDataSource();
	const {selectedChannel} = useChannelContext();

	const {data = {}, error, loading} = useQuery(EXPERIMENT_LIST_QUERY, {
		fetchPolicy: 'network-only',
		variables: {
			...getGraphQLVariablesFromPagination({
				delta,
				orderIOMap,
				page,
				query
			}),
			channelId
		}
	});

	const authorized = currentUser.isAdmin();

	return (
		<BasePage documentTitle={Liferay.Language.get('tests')}>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: selectedChannel && selectedChannel.name
					})
				]}
				groupId={groupId}
			>
				<BasePage.Header.TitleSection
					title={Liferay.Language.get('tests')}
				/>
			</BasePage.Header>

			<BasePage.Context.Provider
				value={{
					filters: {},
					router
				}}
			>
				<BasePage.Body>
					<StatesRenderer {...dataSourceStates}>
						<StatesRenderer.Empty
							className='bg-white mt-4 py-5 rounded sites-dashboard'
							description={
								<>
									{Liferay.Language.get(
										'connect-a-data-source-with-sites-data'
									)}

									<a
										className='d-block mb-3'
										href={URLConstants.DataSourceConnection}
										key='DOCUMENTATION'
										target='_blank'
									>
										{Liferay.Language.get(
											'access-our-documentation-to-learn-more'
										)}
									</a>

									{authorized && (
										<Button
											display='primary'
											href={toRoute(
												Routes.SETTINGS_ADD_DATA_SOURCE,
												{
													groupId
												}
											)}
										>
											{Liferay.Language.get(
												'connect-data-source'
											)}
										</Button>
									)}
								</>
							}
							title={Liferay.Language.get(
								'no-sites-synced-from-data-sources'
							)}
						/>

						<StatesRenderer.Success>
							<div className='row'>
								<div className='col-sm-12'>
									{loading ||
									!!get(data, ['experiments', 'total'], 0) ||
									!!query ? (
										<ExperimentListCard
											{...get(data, 'experiments', {})}
											delta={delta}
											error={error}
											loading={loading}
											orderIOMap={orderIOMap}
											page={page}
											query={query}
											timeZoneId={timeZoneId}
										/>
									) : (
										<EmptyStateDashboard
											description={
												<>
													<p className='mb-1'>
														{Liferay.Language.get(
															'create-a-new-test-from-liferay-dxp-to-optimize-your-experiences'
														)}
													</p>
													<p className='mb-0'>
														{sub(
															Liferay.Language.get(
																'click-on-the-x-icon-in-the-toolbar-when-viewing-a-page-in-dxp-to-get-started'
															),
															[
																<Icon
																	className='font-size-md-2x'
																	key='test-icon'
																	symbol='test'
																/>
															],
															false
														)}
													</p>
												</>
											}
											symbol='ac-satellite'
											title={Liferay.Language.get(
												'no-tests-yet'
											)}
										/>
									)}
								</div>
							</div>
						</StatesRenderer.Success>
					</StatesRenderer>
				</BasePage.Body>
			</BasePage.Context.Provider>
		</BasePage>
	);
};

export default compose<any>(withCurrentUser, connector)(ExperimentsListPage);
