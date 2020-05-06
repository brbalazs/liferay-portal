import BasePage from 'settings/components/BasePage';
import React from 'react';
import SuppressedUserList from '../hocs/SuppressedUserList';
import {getDataPrivacy} from 'shared/util/breadcrumbs';
import {RouterType} from 'shared/types';
import {User} from 'shared/util/records';
import {withAdminPermission} from 'shared/hoc';

interface ISuppressedUsersProps {
	currentUser: User;
	router: RouterType;
}

export const SuppressedUsers: React.FC<ISuppressedUsersProps> = ({
	currentUser,
	router
}) => {
	const {
		params: {groupId}
	} = router;

	return (
		<BasePage
			breadcrumbItems={[
				getDataPrivacy({groupId}),
				{
					active: true,
					label: Liferay.Language.get('suppressed-user-list')
				}
			]}
			className='suppressed-users-page-root'
			documentTitle={Liferay.Language.get('suppressed-user-list')}
			groupId={groupId}
		>
			<SuppressedUserList currentUser={currentUser} router={router} />
		</BasePage>
	);
};

export default withAdminPermission(SuppressedUsers);
