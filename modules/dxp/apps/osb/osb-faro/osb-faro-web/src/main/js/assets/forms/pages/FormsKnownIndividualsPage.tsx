import KnownIndividualsListCard from '../hocs/KnownIndividualsListCard';
import React from 'react';

interface IFormsKnownIndividualsPageProps {
	router: object;
}

const FormsKnownIndividualsPage: React.FC<IFormsKnownIndividualsPageProps> = ({
	router
}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<KnownIndividualsListCard router={router} />
		</div>
	</div>
);

export default FormsKnownIndividualsPage;
