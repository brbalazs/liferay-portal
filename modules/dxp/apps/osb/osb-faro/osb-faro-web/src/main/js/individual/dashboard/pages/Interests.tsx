import Interests from '../hocs/Interests';
import React from 'react';

const InterestsPage: React.FC = () => (
	<div className='individuals-dashboard-interests-root'>
		<div className='row'>
			<div className='col-xl-12'>
				<Interests />
			</div>
		</div>
	</div>
);

export default InterestsPage;
