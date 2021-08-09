import FormsListCard from '../hocs/FormsListCard';
import React from 'react';

interface IFormsListPageProps {
	router: any;
}

const FormsListPage: React.FC<IFormsListPageProps> = ({router}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<FormsListCard router={router} />
		</div>
	</div>
);

export default FormsListPage;
