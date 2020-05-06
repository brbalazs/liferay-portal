import KnownIndividualsListCard from '../hocs/KnownIndividualsListCard';
import React from 'react';

interface IWebContentKnownIndividualsPageProps {
	router: object;
}

const WebContentKnownIndividualsPage: React.FC<
	IWebContentKnownIndividualsPageProps
> = ({router}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<KnownIndividualsListCard router={router} />
		</div>
	</div>
);

export default WebContentKnownIndividualsPage;
