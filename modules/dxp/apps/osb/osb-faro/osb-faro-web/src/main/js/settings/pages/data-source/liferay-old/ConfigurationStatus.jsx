import * as breadcrumbs from 'shared/util/breadcrumbs';
import BaseTabsPage, {
	tabIds
} from 'settings/components/data-source/BaseTabsPage';
import ConfigurationOverview from 'settings/components/liferay-old/ConfigurationOverview';
import React from 'react';
import Sheet from 'shared/components/Sheet';
import {compose, redirectIf, withCurrentUser} from 'shared/hoc';
import {DataSource, User} from 'shared/util/records';
import {dataSourceRedirectFn} from 'shared/util/data-sources';
import {PropTypes} from 'prop-types';
import {Routes} from 'shared/util/router';

export class ConfigurationStatus extends React.Component {
	static propTypes = {
		currentUser: PropTypes.instanceOf(User).isRequired,
		dataSource: PropTypes.instanceOf(DataSource).isRequired,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string
	};

	render() {
		const {currentUser, dataSource, groupId, id} = this.props;

		return (
			<BaseTabsPage
				activeTabId={tabIds.CONFIGURE_DATA_SOURCE}
				addRoute={Routes.SETTINGS_LIFERAY_ADD}
				breadcrumbItems={[
					breadcrumbs.getDataSources({groupId}),
					breadcrumbs.getDataSourceName({
						groupId,
						id,
						label: dataSource.name
					}),
					{
						active: true,
						label: Liferay.Language.get('configuration-status')
					}
				]}
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
				configurationRoute={
					Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS
				}
				currentUser={currentUser}
				dataSource={dataSource}
				documentTitle={
					dataSource
						? dataSource.name
						: Liferay.Language.get('configure-liferay-dxp')
				}
				groupId={groupId}
				id={id}
				key='configurationStatus'
				pageTitle={Liferay.Language.get('configure-liferay-dxp')}
				showDelete
			>
				<Sheet.Body>
					<ConfigurationOverview
						dataSource={dataSource}
						disabled={!currentUser.isAdmin()}
						groupId={groupId}
						id={id}
					/>
				</Sheet.Body>
			</BaseTabsPage>
		);
	}
}

export default compose(
	withCurrentUser,
	redirectIf(dataSourceRedirectFn)
)(ConfigurationStatus);
