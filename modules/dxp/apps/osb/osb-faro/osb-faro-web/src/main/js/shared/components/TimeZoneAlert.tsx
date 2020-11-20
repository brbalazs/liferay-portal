import Alert from 'shared/components/Alert';
import React, {useState} from 'react';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {sub} from 'shared/util/lang';
import {withRouter} from 'react-router-dom';

const TIME_ZONE_COUNTRY_REGEX = /\([^)]+.*/;

interface ITimeZoneAlertModalProps {
	stripe: boolean;
	timeZone: string;
}

const TimeZoneAlert: React.FC<ITimeZoneAlertModalProps> = ({
	stripe,
	timeZone
}) => {
	const [showAlert, setShowAlert] = useState(false);
	// TODO: LRAC-6961 Add the request to show the TimeZone Stripe and UTC

	return showAlert ? (
		<Alert
			iconSymbol='exclamation-full'
			onClose={() => {
				setShowAlert(false);
			}}
			stripe={stripe}
			title={Liferay.Language.get('info')}
			type={Alert.TYPES.info}
		>
			{sub(
				Liferay.Language.get(
					'workspace-timezone-has-changed-to-x-as-of-today.-please-allow-1-2-days-for-the-data-to-adjust-to-this-new-setting.'
				),
				[timeZone]
			)}
		</Alert>
	) : null;
};

export default compose<any>(
	withRouter,
	connect(
		(
			state,
			{
				match: {
					params: {groupId}
				}
			}
		) => ({
			timeZone: state
				.getIn(
					[
						'projects',
						groupId,
						'data',
						'timeZone',
						'displayTimeZone'
					],
					''
				)
				.replace(TIME_ZONE_COUNTRY_REGEX, '')
		}),
		null
	)
)(TimeZoneAlert);
