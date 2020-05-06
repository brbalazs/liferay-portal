import Interests from '../hocs/Interests';
import React from 'react';

interface IInterestsProps {
	router: {
		params: {
			groupId: string;
		};
	};
}

const InterestsPage: React.FC<IInterestsProps> = ({router}) => (
	<div className='individuals-dashboard-interests-root'>
		<div className='row'>
			<div className='col-xl-12'>
				<Interests router={router} />
			</div>
		</div>
	</div>
);

export default InterestsPage;
