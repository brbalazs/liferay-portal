import BaseInterestDetails from '../BaseInterestDetails';
import PropTypes from 'prop-types';
import React from 'react';
import {Account} from 'shared/util/records';
import {ACCOUNTS} from 'shared/util/router';
import {Routes} from 'shared/util/router';

export default class InterestDetails extends React.Component {
	static propTypes = {
		account: PropTypes.instanceOf(Account).isRequired
	};

	render() {
		const {account, ...otherProps} = this.props;

		return (
			<BaseInterestDetails
				{...otherProps}
				entity={account}
				interestDetailsRoute={Routes.CONTACTS_ACCOUNT_INTEREST_DETAILS}
				type={ACCOUNTS}
			/>
		);
	}
}
