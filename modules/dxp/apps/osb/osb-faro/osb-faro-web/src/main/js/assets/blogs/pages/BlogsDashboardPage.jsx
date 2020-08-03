import AudienceReportCard from '../hocs/AudienceReportCard';
import DevicesCard from '../hocs/DevicesCard';
import LocationsCard from '../hocs/LocationsCard';
import MetricsCard from '../hocs/MetricsCard';
import React from 'react';
import TouchpointsListCard from '../../shared/hocs/TouchpointsListCard';

/**
 * Blogs Dashboard Page
 * @function
 */

export default function BlogsDashboardPage() {
	return (
		<>
			<div className='row'>
				<div className='col-sm-12'>
					<MetricsCard
						label={Liferay.Language.get('visitors-behavior')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-sm-12'>
					<AudienceReportCard
						label={Liferay.Language.get('audience')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-lg-6 col-md-12'>
					<LocationsCard
						label={Liferay.Language.get('views-by-location')}
						legacyDropdownRangeKey={false}
					/>
				</div>
				<div className='col-lg-6 col-md-12'>
					<DevicesCard
						label={Liferay.Language.get('views-by-technology')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-sm-12'>
					<TouchpointsListCard
						assetType='BLOG'
						label={Liferay.Language.get('asset-appears-on')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>
		</>
	);
}
