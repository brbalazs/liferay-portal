import ActivitiesWithData from 'contacts/pages/account/Activities';
import BaseCard from 'cerebro-shared/components/base-card';
import React from 'react';
import {Account} from 'shared/util/records';

interface IActivitiesProps extends React.HTMLAttributes<HTMLElement> {
	channelId: string;
	account: Account;
	groupId: string;
	tabId: string;
}

const ActivitiesPage: React.FC<IActivitiesProps> = ({tabId, ...props}) => (
	<BaseCard
		className='account-activities-card page-display'
		headerProps={{
			showRangeKey: false,
			tabId
		}}
		label={Liferay.Language.get('account-activities')}
		legacyDropdownRangeKey={false}
		showInterval={false}
	>
		{({interval, rangeSelectors}) => (
			<ActivitiesWithData
				{...props}
				interval={interval}
				rangeSelectors={rangeSelectors}
				tabId={tabId}
			/>
		)}
	</BaseCard>
);

export default ActivitiesPage;
