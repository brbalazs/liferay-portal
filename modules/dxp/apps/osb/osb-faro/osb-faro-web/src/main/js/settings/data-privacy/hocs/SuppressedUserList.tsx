import Card from 'shared/components/Card';
import ClayButton from '@clayui/button';
import DataControlRequest from '../queries/DataControlRequestMutation';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import SuppressedUsersListQuery from '../queries/SuppressedUsersListQuery';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {connect} from 'react-redux';
import {CREATE_DATE} from 'shared/util/pagination';
import {formatDateToTimeZone} from 'shared/util/date';
import {GDPR_REQUEST_STATUSES, GDPR_REQUEST_TYPES} from 'shared/util/constants';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {graphql} from '@apollo/react-hoc';
import {sub} from 'shared/util/lang';
import {useMutation} from '@apollo/react-hooks';
import {User} from 'shared/util/records';
import {withBaseResults} from 'shared/hoc';

const DATE_FORMAT = 'MMM DD, YYYY';

const withData = () =>
	graphql(
		SuppressedUsersListQuery,
		getMetricsMapper(
			({suppressions: {suppressions, total}}) => ({
				items: suppressions,
				total
			}),
			{
				fetchPolicy: 'no-cache'
			},
			SuppressedUsersListQuery
		)
	);

const withQueryOptions = Component => ({
	addAlert,
	currentUser,
	refetch,
	...otherProps
}: Pick<ISuppressedUserListProps, 'addAlert' | 'currentUser'> & {
	refetch: () => Promise<any>;
}) => {
	const [unsuppressUser] = useMutation(DataControlRequest);

	return (
		<Component
			renderInlineRowActions={({
				data: {dataControlTaskStatus, emailAddress}
			}) =>
				dataControlTaskStatus !== GDPR_REQUEST_STATUSES.PENDING && (
					<ClayButton
						className='unsuppress'
						displayType='secondary'
						onClick={() => {
							unsuppressUser({
								variables: {
									emailAddresses: [emailAddress],
									ownerId: currentUser.id,
									types: [GDPR_REQUEST_TYPES.UNSUPPRESS]
								}
							})
								.then(() => {
									addAlert({
										alertType: Alert.Types.SUCCESS,
										message: sub(
											Liferay.Language.get(
												'x-has-been-successfully-unsuppressed'
											),
											[emailAddress]
										) as string
									});

									refetch();
								})
								.catch(() => {
									addAlert({
										alertType: Alert.Types.ERROR,
										message: sub(
											Liferay.Language.get(
												'there-was-an-error-unsuppressing-x.-please-try-again'
											),
											[emailAddress]
										) as string,
										timeout: false
									});
								});
						}}
						small
					>
						{Liferay.Language.get('unsuppress')}
					</ClayButton>
				)
			}
			{...otherProps}
		/>
	);
};

const SuppressedListWithData = withBaseResults(withData, {
	defaultOrderByField: CREATE_DATE,
	emptyTitle: getFormattedTitle(Liferay.Language.get('suppressed-users')),
	getColumns: ({timeZoneId}) => [
		{
			accessor: 'emailAddress',
			className: 'table-cell-expand',
			label: Liferay.Language.get('email'),
			title: true
		},
		{
			accessor: 'dataControlTaskBatchId',
			label: Liferay.Language.get('request-id')
		},
		{
			accessor: 'dataControlTaskCreateDate',
			dataFormatter: val =>
				formatDateToTimeZone(val, DATE_FORMAT, timeZoneId),
			label: Liferay.Language.get('requested-date')
		},
		{
			accessor: 'createDate',
			dataFormatter: val =>
				formatDateToTimeZone(val, DATE_FORMAT, timeZoneId),
			label: Liferay.Language.get('suppression-date')
		}
	],
	primary: true,
	showDropdownRangeKey: false,
	withQueryOptions
});

interface ISuppressedUserListProps {
	addAlert: Alert.AddAlert;
	currentUser: User;
	timeZoneId: string;
}

const SuppressedUserList: React.FC<ISuppressedUserListProps> = props => (
	<Card className='suppressed-user-list-root' pageDisplay>
		<SuppressedListWithData
			checkDisabled={({dataControlTaskStatus}) =>
				dataControlTaskStatus === GDPR_REQUEST_STATUSES.PENDING
			}
			entityLabel={Liferay.Language.get('suppressed-users')}
			{...props}
		/>
	</Card>
);

export default connect(
	null,
	{addAlert}
)(SuppressedUserList);
