import Alert, {AlertTypes} from 'shared/components/Alert';
import React from 'react';
import {connect} from 'react-redux';
import {sub} from 'shared/util/lang';

const TIME_ZONE_COUNTRY_REGEX = /\([^)]+.*/;

interface ITimeZoneAlertModalProps {
	onClose: () => void;
	stripe: boolean;
	timeZone: string;
}

const TimeZoneAlert: React.FC<ITimeZoneAlertModalProps> = ({
	onClose,
	stripe,
	timeZone
}) => (
	<Alert
		iconSymbol='exclamation-full'
		onClose={onClose}
		stripe={stripe}
		title={Liferay.Language.get('info')}
		type={AlertTypes.Info}
	>
		{sub(
			Liferay.Language.get(
				'workspace-timezone-has-changed-to-x-as-of-today.-please-allow-1-2-days-for-the-data-to-adjust-to-this-new-setting.'
			),
			[timeZone]
		)}
	</Alert>
);

export default connect(
	(state, {groupId}) => ({
		timeZone: state
			.getIn(
				['projects', groupId, 'data', 'timeZone', 'displayTimeZone'],
				''
			)
			.replace(TIME_ZONE_COUNTRY_REGEX, '')
	}),
	null
)(TimeZoneAlert);
