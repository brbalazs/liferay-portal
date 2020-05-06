import withAction from './WithAction';
import {fetchCurrentUser} from '../actions/users';

export default withAction(
	({groupId}) => fetchCurrentUser(groupId),
	state => {
		const currentUser = state.get('currentUser');

		return currentUser.data
			? state.getIn(['users', currentUser.data])
			: currentUser;
	},
	{propName: 'currentUser'}
);
