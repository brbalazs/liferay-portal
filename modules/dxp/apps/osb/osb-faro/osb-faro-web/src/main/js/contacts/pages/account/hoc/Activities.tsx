import ActivitiesWithData from 'contacts/pages/account/Activities';
import BaseCard from 'cerebro-shared/components/base-card';
import HeaderCard from 'contacts/components/HeaderCard';
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
		className={'page-display'}
		Header={HeaderCard}
		headerProps={{
			tabId
		}}
		label={Liferay.Language.get('account-activities')}
		legacyDropdownRangeKey={false}
		showInterval
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
