import AudienceReportCard from '../hocs/AudienceReportCard';
import DevicesCard from '../hocs/DevicesCard';
import LocationsCard from '../hocs/LocationsCard';
import MetricsCard from '../hocs/MetricsCard';
import React from 'react';
import TouchpointsListCard from '../../shared/hocs/TouchpointsListCard';
import {AUDIENCE_VIEWER_MODE} from '../../../shared/util/constants';

/**
 * Documents And Media Dashboard Page
 * @class
 */

export default function DocumentsAndMediaDashboardPage() {
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
							'segmented-previews'
						)}
						label={Liferay.Language.get('audience')}
						legacyDropdownRangeKey={false}
						uniqueVisitorsTitle={Liferay.Language.get('previews')}
						viewerMode={AUDIENCE_VIEWER_MODE.PREVIEW}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-lg-6 col-md-12'>
					<LocationsCard
						label={Liferay.Language.get('downloads-by-location')}
						legacyDropdownRangeKey={false}
						metricLabel={Liferay.Language.get('downloads')}
					/>
				</div>
				<div className='col-lg-6 col-md-12'>
					<DevicesCard
						label={Liferay.Language.get('downloads-by-technology')}
						legacyDropdownRangeKey={false}
						metricLabel={Liferay.Language.get('downloads')}
					/>
				</div>
			</div>
			<div className='row'>
				<div className='col-sm-12'>
					<TouchpointsListCard
						assetType='DOCUMENT'
						label={Liferay.Language.get('asset-appears-on')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>
		</>
	);
}
