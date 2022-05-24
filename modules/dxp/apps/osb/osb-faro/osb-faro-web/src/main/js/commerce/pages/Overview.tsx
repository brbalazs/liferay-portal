import React from 'react';
import TotalOrderValueCard from 'commerce/components/TotalOrderValueCard';

const Overview = () => (
	<div className='commerce-dashboard-overview-root'>
		<div className='row'>
			<div className='col-xl-6'>
				<TotalOrderValueCard />
			</div>
		</div>
	</div>
);

export default Overview;
