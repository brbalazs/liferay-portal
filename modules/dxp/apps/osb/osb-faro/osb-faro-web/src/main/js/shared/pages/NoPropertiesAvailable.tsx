import * as API from 'shared/api';
import BasePage from 'shared/components/base-page';
import Button from 'shared/components/Button';
import EmptyStateDashboard from 'shared/components/EmptyStateDashboard';
import React from 'react';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {Routes, toRoute} from 'shared/util/router';
import {setBackURL} from 'shared/actions/settings';
import {User} from 'shared/util/records';
import {withRequest} from 'shared/hoc';

interface INoPropertiesAvailableProps
	extends React.HTMLAttributes<HTMLDivElement> {
	close: () => void;
	currentUser: User;
	dataSources: boolean;
	groupId: string;
	open: (modalType: string, config: object) => void;
	setBackURL: (url: string) => void;
}

const NoPropertiesAvailable: React.FC<INoPropertiesAvailableProps> = ({
	close,
	currentUser,
	dataSources,
	groupId,
	open,
	setBackURL
}) => {
	const admin = currentUser.isAdmin();

	const description = admin
		? Liferay.Language.get(
				'to-get-your-workspace-set-up,-youll-need-to-connect-your-dxp-instance-and-add-sites-to-a-property'
		  )
		: Liferay.Language.get(
				'you-have-not-been-added-to-any-properties.-please-contact-your-analytics-cloud-administrator'
		  );

	return (
		<BasePage
			className='no-properties-available-root'
			documentTitle={Liferay.Language.get('no-properties-available')}
		>
			<BasePage.Header breadcrumbs={[]} groupId={groupId}>
				<BasePage.Header.TitleSection
					className='text-secondary'
					title={Liferay.Language.get('no-properties-available')}
				/>
			</BasePage.Header>

			<BasePage.Body>
				<EmptyStateDashboard
					description={description}
					symbol={admin ? 'ac-no-sites' : 'ac-satellite'}
					title={
						admin
							? Liferay.Language.get(
									'first-connect-your-dxp-sites'
							  )
							: Liferay.Language.get('no-properties-found')
					}
				>
					{admin && (
						<Button
							display='primary'
							href={
								dataSources
									? toRoute(Routes.SETTINGS_CHANNELS, {
											groupId
									  })
									: null
							}
							onClick={
								dataSources
									? () =>
											setBackURL(
												toRoute(
													Routes.WORKSPACE_WITH_ID,
													{
														groupId
													}
												)
											)
									: () =>
											open(modalTypes.ONBOARDING_MODAL, {
												groupId,
												onClose: close
											})
							}
						>
							{dataSources
								? Liferay.Language.get('create-property')
								: Liferay.Language.get('start')}
						</Button>
					)}
				</EmptyStateDashboard>
			</BasePage.Body>
		</BasePage>
	);
};

export default compose<any>(
	withRequest(
		({groupId}) => API.dataSource.search({groupId}),
		({total}) => ({
			dataSources: !!total
		})
	),
	connect(
		null,
		{close, open, setBackURL}
	)
)(NoPropertiesAvailable);
