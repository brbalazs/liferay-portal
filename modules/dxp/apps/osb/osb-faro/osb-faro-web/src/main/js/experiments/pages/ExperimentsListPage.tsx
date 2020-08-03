import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import EmptyStateDashboard from 'shared/components/EmptyStateDashboard';
import ExperimentListCard from '../hocs/ExperimentListCard';
import Icon from 'shared/components/Icon';
import React from 'react';
import {EXPERIMENT_LIST_QUERY} from '../queries/ExperimentQuery';
import {get} from 'lodash';
import {getMapPropsToOptions} from 'shared/hoc/mappers/metrics';
import {IBasePageContext} from 'shared/types';
import {sub} from 'shared/util/lang';
import {useChannelContext} from 'shared/context/channel';
import {useQuery} from '@apollo/react-hooks';

const DEFAULT_FIELD = 'modifiedDate';
const DEFAULT_SORT_ORDER = 'DESC';

interface IExperimentsListPage extends IBasePageContext {
	router: {
		params: {
			channelId: string;
			groupId: string;
		};
		query: {
			query: string;
		};
	};
}

const ExperimentsListPage: React.FC<IExperimentsListPage> = ({router}) => {
	const {channelId, groupId} = router.params;
	const {query} = router.query;
	const {variables} = getMapPropsToOptions(EXPERIMENT_LIST_QUERY)({
		defaultSort: {
			field: DEFAULT_FIELD,
			sortOrder: DEFAULT_SORT_ORDER
		},
		router
	});

	const {selectedChannel} = useChannelContext();

	const {data = {}, error, loading} = useQuery(EXPERIMENT_LIST_QUERY, {
		fetchPolicy: 'network-only',
		variables
	});

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
					<div className='row'>
						<div className='col-sm-12'>
							{loading ||
							!!get(data, ['experiments', 'total'], 0) ||
							!!query ? (
								<ExperimentListCard
									{...get(data, 'experiments', {})}
									error={error}
									loading={loading}
									router={router}
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
									title={Liferay.Language.get('no-tests-yet')}
								/>
							)}
						</div>
					</div>
				</BasePage.Body>
			</BasePage.Context.Provider>
		</BasePage>
	);
};

export default ExperimentsListPage;
