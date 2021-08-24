import CustomAssetsListCard from '../hocs/CustomAssetsListCard';
import React from 'react';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from 'shared/store';
import {Router} from 'shared/types';

const connector = connect(
	(
		store: RootState,
		{
			router: {
				params: {groupId}
			}
		}: {router: Router}
	) => ({
		timeZoneId: store.getIn([
			'projects',
			groupId,
			'data',
			'timeZone',
			'timeZoneId'
		])
	})
);

type PropsFromRedux = ConnectedProps<typeof connector>;

interface ICustomAssetsListPageProps extends PropsFromRedux {
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

export default connector(CustomAssetsListPage);
