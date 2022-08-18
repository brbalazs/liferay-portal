// import * as API from 'shared/api';
import Button from 'shared/components/Button';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React, {useEffect, useState} from 'react';
import Spinner from 'shared/components/Spinner';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';

enum Frequency {
	Daily = 'daily',
	Monthly = 'monthly',
	Weekly = 'weekly'
}

type Report = {
	enabled: boolean;
	frequency: Frequency.Daily | Frequency.Monthly | Frequency.Weekly;
};

interface IDisplayReportMessageProps extends React.HTMLAttributes<HTMLElement> {
	channelId: string;
	sitesSynced: boolean;
}

const DisplayReportMessage: React.FC<IDisplayReportMessageProps> = ({
	channelId,
	className,
	sitesSynced = false
}) => {
	const {groupId} = useParams();
	const [report, setReport] = useState<Report | null>(null);

	useEffect(() => {
		// TODO: LRAC-11729 Get real data from backend
		// API.preferences
		// 	.fetchEmailReport({groupId, channelId})
		// 	.then((report: Report) => setReport(report));

		// TODO: LRAC_11729 Remove this code snippet
		// after get real data from backend
		setTimeout(() => {
			setReport({enabled: false, frequency: Frequency.Monthly});
		}, 1000);
	}, [channelId, groupId]);

	return (
		<span className={getCN('font-weight-semibold mr-3', className)}>
			{sub(
				Liferay.Language.get('email-reports-x'),
				[
					!report ? (
						<Spinner className='ml-2' key='LOADING' size='sm' />
					) : report.enabled ? (
						Liferay.Language.get('enabled')
					) : (
						Liferay.Language.get('disabled')
					)
				],
				false
			)}

			{report && (
				<Button
					borderless
					className='ml-3'
					data-tooltip
					data-tooltip-align='top'
					disabled={!sitesSynced}
					display='unstyled'
					size='sm'
					title={Liferay.Language.get('configure-email-reports')}
				>
					<span>
						<Icon symbol='cog' />
					</span>
				</Button>
			)}
		</span>
	);
};

export default DisplayReportMessage;
