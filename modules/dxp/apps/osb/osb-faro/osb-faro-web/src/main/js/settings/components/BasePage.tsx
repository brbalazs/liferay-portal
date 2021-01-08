import Breadcrumbs from 'shared/components/Breadcrumbs';
import DocumentTitle from 'shared/components/DocumentTitle';
import getCN from 'classnames';
import MaintenanceAlert from 'shared/components/MaintenanceAlert';
import ModalNotificationManager from 'shared/components/ModalNotificationManager';
import NotificationAlertList from 'shared/components/NotificationAlertList';
import React from 'react';
import TextTruncate from 'shared/components/TextTruncate';
import {compose} from 'shared/hoc';
import {connect} from 'react-redux';
import {PageActions} from 'shared/components/base-page/Header';

interface ISettingsBasePageProps {
	breadcrumbItems?: Array<any>;
	className?: string;
	documentTitle?: string;
	groupId: string;
	pageActions?: Array<any>;
	pageActionsDisplayLimit?: number;
	pageDescription?: React.ReactNode;
	pageTitle?: React.ReactNode;
	passedChildren?: React.ReactNode;
}

const SettingsBasePage: React.FC<ISettingsBasePageProps> = ({
	breadcrumbItems,
	className,
	documentTitle,
	groupId,
	pageActions = [],
	pageActionsDisplayLimit,
	pageDescription,
	pageTitle,
	passedChildren
}) => (
	<div className={getCN('settings-base-page-root', className)}>
		<DocumentTitle
			title={`${documentTitle || pageTitle} - ${Liferay.Language.get(
				'settings'
			)}`}
		/>

		<ModalNotificationManager groupId={groupId} />

		<NotificationAlertList groupId={groupId} />

		<MaintenanceAlert />

		{breadcrumbItems && <Breadcrumbs items={breadcrumbItems} />}

		{(!!pageTitle || !!pageDescription || !!pageActions.length) && (
			<div
				className={getCN('content-header', {
					['has-page-actions']: !!pageActions.length
				})}
			>
				<div className='header-text'>
					{pageTitle && <h3>{<TextTruncate title={pageTitle} />}</h3>}

					{pageDescription && (
						<div className='description'>{pageDescription}</div>
					)}
				</div>

				<div className='page-actions-container'>
					<PageActions
						actions={pageActions}
						actionsDisplayLimit={pageActionsDisplayLimit}
					/>
				</div>
			</div>
		)}

		<div>{passedChildren}</div>
	</div>
);

export default compose(
	connect((store, ownProps) => ({
		passedChildren: ownProps.children
	}))
)(SettingsBasePage);
