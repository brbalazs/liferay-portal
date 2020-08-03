import BaseCard from 'cerebro-shared/components/base-card';
import HeaderCard from 'contacts/components/HeaderCard';
import ProfileCardWithData from 'contacts/individual/profile/components/ProfileCard';
import React from 'react';
import {Individual} from 'shared/util/records';

interface IProfileCardProps extends React.HTMLAttributes<HTMLElement> {
	channelId: string;
	entity: Individual;
	groupId: string;
	tabId: string;
}

const ProfileCard: React.FC<IProfileCardProps> = ({tabId, ...props}) => (
	<BaseCard
		className='individual-profile-card-root page-display'
		Header={HeaderCard}
		headerProps={{
			tabId
		}}
		label={Liferay.Language.get('individual-activities')}
		legacyDropdownRangeKey={false}
		showInterval
	>
		{({interval, rangeSelectors}) => (
			<ProfileCardWithData
				{...props}
				interval={interval}
				rangeSelectors={rangeSelectors}
				tabId={tabId}
			/>
		)}
	</BaseCard>
);

export default ProfileCard;
