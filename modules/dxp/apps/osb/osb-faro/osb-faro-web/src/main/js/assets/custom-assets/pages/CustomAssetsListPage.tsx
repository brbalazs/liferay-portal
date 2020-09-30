import CustomAssetsListCard from '../hocs/CustomAssetsListCard';
import React from 'react';
import {connect} from 'react-redux';

interface ICustomAssetsListPageProps {
	router: object;
	timeZoneId: string;
}

const CustomAssetsListPage: React.FC<ICustomAssetsListPageProps> = ({
	router,
	timeZoneId
}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<CustomAssetsListCard router={router} timeZoneId={timeZoneId} />
		</div>
	</div>
);

export default connect((store, {router: {params: {groupId}}}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))(CustomAssetsListPage);
