import Breadcrumbs from 'shared/components/Breadcrumbs';
import DocumentTitle from 'shared/components/DocumentTitle';
import getCN from 'classnames';
import MaintenanceAlert from 'shared/components/MaintenanceAlert';
import NotificationAlertList from 'shared/components/NotificationAlertList';
import React from 'react';
import TextTruncate from 'shared/components/TextTruncate';
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
	subTitle?: React.ReactNode;
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
	subTitle,
	passedChildren
}) => {
	const renderPageTitle = () => (
		<>
			{pageTitle && (
				<>
					<h3
						className={getCN({
							['inline-text']: subTitle
						})}
					>
						<TextTruncate title={pageTitle} />
					</h3>
					{subTitle && <span className='ml-2'>{subTitle}</span>}
				</>
			)}
		</>
	);

	return (
		<div className={getCN('settings-base-page-root', className)}>
			<DocumentTitle
				title={`${documentTitle || pageTitle} - ${Liferay.Language.get(
					'settings'
				)}`}
			/>

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
						{renderPageTitle()}

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
};

export default connect((store, ownProps) => ({
	passedChildren: ownProps.children
}))(SettingsBasePage);
