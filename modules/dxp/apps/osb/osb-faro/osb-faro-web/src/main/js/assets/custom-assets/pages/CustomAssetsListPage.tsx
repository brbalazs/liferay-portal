import CustomAssetsListCard from '../hocs/CustomAssetsListCard';
import React from 'react';

interface ICustomAssetsListPageProps {
	router: object;
}

const CustomAssetsListPage: React.FC<ICustomAssetsListPageProps> = ({
	router
}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<CustomAssetsListCard router={router} />
		</div>
	</div>
);

export default CustomAssetsListPage;
