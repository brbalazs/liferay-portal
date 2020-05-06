import FaroConstants from 'shared/util/constants';
import withAction from './WithAction';
import {fetchDefaultChannelId} from '../actions/preferences';
import {RemoteData} from '../util/records';

const {
	preferencesScopes: {user}
} = FaroConstants;

export default withAction(
	({groupId}) => fetchDefaultChannelId(groupId),
	state =>
		state.getIn(
			['preferences', user, 'defaultChannelId'],
			new RemoteData()
		),
	{propName: 'defaultChannelId'}
);
