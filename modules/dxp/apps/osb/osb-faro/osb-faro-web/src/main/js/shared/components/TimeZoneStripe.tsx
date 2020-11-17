import Alert from 'shared/components/Alert';
import EmbeddedAlertList from 'shared/components/EmbeddedAlertList';
import React, {useState} from 'react';
import {sub} from 'shared/util/lang';

const TimeZoneStripe = () => {
	const [showAlert, setShowAlert] = useState(true);
	// TODO: LRAC-6961 Add the request to show the TimeZone Stripe and UTC
	return (
		showAlert && (
			<EmbeddedAlertList
				alerts={[
					{
						iconSymbol: 'exclamation-full',
						message: (
							<>
								{sub(
									Liferay.Language.get(
										'workspace-timezone-has-changed-to-x-time-as-of-today.-please-allow-1-2-days-for-the-data-to-adjust-to-this-new-setting.'
									),
									['-UTC 3:00 brasilia']
								)}
							</>
						),
						onClose: () => {
							setShowAlert(false);
						},
						stripe: true,
						title: 'Info',
						type: Alert.TYPES.info
					}
				]}
			/>
		)
	);
};

export default TimeZoneStripe;
