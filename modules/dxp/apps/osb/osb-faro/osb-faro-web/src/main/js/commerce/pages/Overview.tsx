import AverageOrderValueCard from 'commerce/components/AverageOrderValueCard';
import IncompleteOrdersCard from 'commerce/components/IncompleteOrdersCard';
import React from 'react';
import TotalOrderValueCard from 'commerce/components/TotalOrderValueCard';

const Overview = () => (
	<div className='commerce-dashboard-overview-root'>
		<div className='row'>
			<div className='col-xl-6'>
				<TotalOrderValueCard />
			</div>
			<div className='col-xl-6'>
				<IncompleteOrdersCard />
			</div>
		</div>
		<div className='row'>
			<div className='col-xl-6'>
				<AverageOrderValueCard />
			</div>
		</div>
	</div>
);

export default Overview;
