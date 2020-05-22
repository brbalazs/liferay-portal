import AssetsListCard from '../hocs/AssetsListCard';
import AudienceReportCard from '../hocs/AudienceReportCard';
import DevicesCard from '../hocs/DevicesCard';
import LocationsCard from '../hocs/LocationsCard';
import MetricsCard from '../hocs/MetricsCard';
import React from 'react';

/**
 * Touchpoint Overview Page
 * @function
 */

export default function TouchpointOverviewPage() {
	return (
		<>
			<div className='row'>
				<div className='col-sm-12'>
					<MetricsCard
						label={Liferay.Language.get('visitors-behavior')}
					/>
				</div>
			</div>

			<div className='row'>
				<div className='col-sm-12'>
					<AudienceReportCard
						label={Liferay.Language.get('audience')}
					/>
				</div>
			</div>

			<div className='row'>
				<div className='col-lg-6 col-md-12'>
					<LocationsCard
						label={Liferay.Language.get('views-by-location')}
					/>
				</div>

				<div className='col-lg-6 col-md-12'>
					<DevicesCard
						label={Liferay.Language.get('views-by-technology')}
					/>
				</div>
			</div>

			<div className='row'>
				<div className='col-md-12'>
					<AssetsListCard label={Liferay.Language.get('assets')} />
				</div>
			</div>
		</>
	);
}
