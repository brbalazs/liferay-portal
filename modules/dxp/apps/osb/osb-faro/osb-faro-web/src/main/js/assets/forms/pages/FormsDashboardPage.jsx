import AudienceReportCard from '../hocs/AudienceReportCard';
import DevicesCard from '../hocs/DevicesCard';
import FormAbandonmentCard from '../hocs/FormAbandonmentCard';
import LocationsCard from '../hocs/LocationsCard';
import MetricsCard from '../hocs/MetricsCard';
import React from 'react';
import TouchpointsListCard from '../../shared/hocs/TouchpointsListCard';
import {AUDIENCE_VIEWER_MODE} from 'shared/util/constants';

/**
 * Forms Dashboard Page
 * @function
 */

export default function FormsDashboardPage() {
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
						knownIndividualsTitle={Liferay.Language.get(
							'segmented-views'
						)}
						label={Liferay.Language.get('audience')}
						legacyDropdownRangeKey={false}
						uniqueVisitorsTitle={Liferay.Language.get('views')}
						viewerMode={AUDIENCE_VIEWER_MODE.VIEW}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-lg-6 col-md-12'>
					<LocationsCard
						label={Liferay.Language.get('submissions-by-location')}
						legacyDropdownRangeKey={false}
						metricLabel={Liferay.Language.get('submissions')}
					/>
				</div>
				<div className='col-lg-6 col-md-12'>
					<DevicesCard
						label={Liferay.Language.get(
							'submissions-by-technology'
						)}
						legacyDropdownRangeKey={false}
						metricLabel={Liferay.Language.get('submissions')}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-sm-12'>
					<FormAbandonmentCard
						label={Liferay.Language.get('form-abandonment')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-sm-12'>
					<TouchpointsListCard
						assetType='FORM'
						label={Liferay.Language.get('asset-appears-on')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>
		</>
	);
}
