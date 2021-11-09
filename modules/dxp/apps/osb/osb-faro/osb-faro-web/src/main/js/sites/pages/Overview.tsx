import AcquisitionsCard from 'sites/hocs/AcquisitionsCard';
import CohortAnalysisCard from 'sites/hocs/CohortAnalysisCard';
import DevicesCard from 'sites/hocs/DevicesCard';
import InterestsCard from 'sites/hocs/InterestsCard';
import LocationsCard from 'sites/hocs/LocationsCard';
import React from 'react';
import SearchTermsCard from 'sites/hocs/SearchTermsCard';
import SiteMetricsCard from 'sites/hocs/MetricsCard';
import TopPagesCard from 'sites/hocs/TopPagesCard';
import VisitorsByTimeCard from 'sites/hocs/VisitorsByTimeCard';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

interface IOverviewProps extends React.HTMLAttributes<HTMLDivElement> {
	channelName: string;
	router: {
		params: {
			channelId: string;
			groupId: string;
		};
		query: object;
	};
}

export default class Overview extends React.Component<IOverviewProps> {
	render() {
		const {
			channelName,
			router: {
				params: {channelId, groupId}
			}
		} = this.props;

		return (
			<div className='sites-dashboard-overview-root overview-root'>
				<div className='row'>
					<div className='col-xl-12'>
						<SiteMetricsCard
							className='site-metrics'
							label={sub(Liferay.Language.get('x-activities'), [
								channelName
							])}
							legacyDropdownRangeKey={false}
						/>
					</div>
				</div>

				<div className='row'>
					<div className='col-xl-6'>
						<TopPagesCard
							footerHref={toRoute(Routes.SITES_TOUCHPOINTS, {
								channelId,
								groupId
							})}
							footerLabel={Liferay.Language.get('view-pages')}
							label={Liferay.Language.get('top-pages')}
							legacyDropdownRangeKey={false}
							metricLabel={Liferay.Language.get('pages')}
						/>
					</div>

					<div className='col-xl-6'>
						<AcquisitionsCard
							className='acquisitions-card-root table-tabs-root'
							label={Liferay.Language.get('acquisitions')}
							legacyDropdownRangeKey={false}
						/>
					</div>
				</div>

				<div className='row'>
					<div className='col-xl-4'>
						<VisitorsByTimeCard
							className='visitors-by-time-card'
							label={Liferay.Language.get(
								'visitors-by-day-and-time'
							)}
						/>
					</div>

					<div className='col-xl-4'>
						<SearchTermsCard />
					</div>

					<div className='col-xl-4'>
						<InterestsCard />
					</div>
				</div>

				<div className='row'>
					<div className='col-xl-6'>
						<LocationsCard
							label={Liferay.Language.get('sessions-by-location')}
							legacyDropdownRangeKey={false}
							metricLabel={Liferay.Language.get('sessions')}
						/>
					</div>

					<div className='col-xl-6'>
						<DevicesCard
							label={Liferay.Language.get('session-technology')}
							legacyDropdownRangeKey={false}
							metricLabel={Liferay.Language.get('sessions')}
						/>
					</div>
				</div>

				<div className='row'>
					<div className='col-xl-12'>
						<CohortAnalysisCard />
					</div>
				</div>
			</div>
		);
	}
}
