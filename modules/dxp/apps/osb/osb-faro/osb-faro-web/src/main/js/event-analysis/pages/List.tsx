import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import EventAnalysisListCard from '../hocs/EventAnalysisListCard';
import React from 'react';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import URLConstants from 'shared/util/url-constants';
import {Routes, toRoute} from 'shared/util/router';
import {useDataSource} from 'shared/hooks/useDataSource';
import {useParams} from 'react-router-dom';

const List: React.FC = () => {
	const {channelId, groupId} = useParams();

	const dataSourceStates = useDataSource();
	const {empty, error, loading} = dataSourceStates;

	const pageAction = [
		{
			disabled: empty || error || loading,
			display: 'primary',
			href: toRoute(Routes.EVENT_ANALYSIS_CREATE, {
				channelId,
				groupId
			}),
			label: Liferay.Language.get('create-analysis')
		}
	];

	return (
		<BasePage documentTitle={Liferay.Language.get('event-analysis')}>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: Liferay.Language.get('home')
					})
				]}
				groupId={groupId}
			>
				<BasePage.Row>
					<BasePage.Header.TitleSection
						title={Liferay.Language.get('event-analysis')}
					/>

					<BasePage.Header.Section>
						<BasePage.Header.PageActions actions={pageAction} />
					</BasePage.Header.Section>
				</BasePage.Row>
			</BasePage.Header>

			<BasePage.Body>
				<StatesRenderer {...dataSourceStates}>
					<StatesRenderer.Empty
						className='sites-dashboard bg-white mt-4 py-5'
						description={
							<>
								{Liferay.Language.get(
									'connect-a-data-source-with-events-data'
								)}

								<a
									className='pl-1'
									href={URLConstants.DataSourceConnection}
									key='DOCUMENTATION'
									target='_blank'
								>
									{Liferay.Language.get(
										'access-our-documentation-to-learn-more'
									)}
								</a>
							</>
						}
						title={Liferay.Language.get(
							'no-event-analysis-synced-from-data-sources'
						)}
					/>

					<StatesRenderer.Success>
						<EventAnalysisListCard />
					</StatesRenderer.Success>
				</StatesRenderer>
			</BasePage.Body>
		</BasePage>
	);
};

export default List;
