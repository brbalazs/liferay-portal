import React from 'react';
import WebContentListCard from '../hocs/WebContentListCard';

interface IWebContentListPageProps {
	router: object;
}

const WebContentListPage: React.FC<IWebContentListPageProps> = ({router}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<WebContentListCard router={router} />
		</div>
	</div>
);

export default WebContentListPage;
