import KnownIndividualsListCard from '../hocs/KnownIndividualsListCard';
import React from 'react';

interface IDocumentsAndMediaKnownIndividualsPageProps {
	router: object;
}

const DocumentsAndMediaKnownIndividualsPage: React.FC<
	IDocumentsAndMediaKnownIndividualsPageProps
> = ({router}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<KnownIndividualsListCard router={router} />
		</div>
	</div>
);

export default DocumentsAndMediaKnownIndividualsPage;
