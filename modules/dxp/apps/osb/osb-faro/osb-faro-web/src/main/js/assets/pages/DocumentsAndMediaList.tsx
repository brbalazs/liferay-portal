import DocumentsAndMediaListCard from '../hocs/DocumentsAndMediaListCard';
import React from 'react';

interface IDocumentsAndMediaListPageProps {
	router: object;
}

const DocumentsAndMediaListPage: React.FC<IDocumentsAndMediaListPageProps> = ({
	router
}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<DocumentsAndMediaListCard router={router} />
		</div>
	</div>
);

export default DocumentsAndMediaListPage;
