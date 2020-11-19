import Breadcrumbs from 'shared/components/Breadcrumbs';
import DocumentTitle from 'shared/components/DocumentTitle';
import getCN from 'classnames';
import MaintenanceAlert from 'shared/components/MaintenanceAlert';
import React from 'react';
import TextTruncate from 'shared/components/TextTruncate';
import TimeZoneAlert from 'shared/components/TimeZoneAlert';
import {compose} from 'shared/hoc';
import {connect} from 'react-redux';
import {PageActions} from 'shared/components/base-page/Header';
import {PropTypes} from 'prop-types';

export class SettingsBasePage extends React.Component {
	static defaultProps = {
		pageActions: []
	};

	static propTypes = {
		backURL: PropTypes.string,
		breadcrumbItems: PropTypes.array,
		documentTitle: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		pageActions: PropTypes.array,
		pageActionsDisplayLimit: PropTypes.number,
		pageDescription: PropTypes.node,
		pageTitle: PropTypes.node,
		passedChildren: PropTypes.node
	};

	render() {
		const {
			breadcrumbItems,
			className,
			documentTitle,
			pageActions,
			pageActionsDisplayLimit,
			pageDescription,
			pageTitle,
			passedChildren
		} = this.props;

		return (
			<div className={getCN('settings-base-page-root', className)}>
				<DocumentTitle
					title={`${documentTitle ||
						pageTitle} - ${Liferay.Language.get('settings')}`}
				/>

				<TimeZoneAlert />

				<MaintenanceAlert />

				{breadcrumbItems && <Breadcrumbs items={breadcrumbItems} />}

				{(!!pageTitle || !!pageDescription || !!pageActions.length) && (
					<div
						className={getCN('content-header', {
							['has-page-actions']: !!pageActions.length
						})}
					>
						<div className='header-text'>
							{pageTitle && (
								<h3>{<TextTruncate title={pageTitle} />}</h3>
							)}

							{pageDescription && (
								<div className='description'>
									{pageDescription}
								</div>
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
	}
}

export default compose(
	connect((store, ownProps) => ({
		backURL: store.getIn(['settings', 'backURL']),
		passedChildren: ownProps.children
	}))
)(SettingsBasePage);
