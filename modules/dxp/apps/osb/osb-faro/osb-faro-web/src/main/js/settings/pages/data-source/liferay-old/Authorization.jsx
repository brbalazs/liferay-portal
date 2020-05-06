import * as breadcrumbs from 'shared/util/breadcrumbs';
import autobind from 'autobind-decorator';
import BaseTabsPage, {
	tabIds
} from 'settings/components/data-source/BaseTabsPage';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import OAuthForm from 'settings/components/data-source/OAuthForm';
import React from 'react';
import {connect} from 'react-redux';
import {
	createLiferayDataSource,
	updateLiferayDataSource
} from 'shared/actions/data-sources';
import {DataSource, User} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

const {dataSourceTypes, documentationURLs} = FaroConstants;

export class LiferayAuthorization extends React.Component {
	static propTypes = {
		createLiferayDataSource: PropTypes.func.isRequired,
		currentUser: PropTypes.instanceOf(User).isRequired,
		dataSource: PropTypes.instanceOf(DataSource),
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string,
		updateLiferayDataSource: PropTypes.func.isRequired
	};

	@autobind
	handleUpdateLiferay({dataSourceName, tempCredentials, url}) {
		const {
			createLiferayDataSource,
			groupId,
			id,
			updateLiferayDataSource
		} = this.props;

		const request = id ? updateLiferayDataSource : createLiferayDataSource;

		return request({
			credentials: tempCredentials,
			groupId,
			id,
			name: dataSourceName,
			url
		});
	}

	render() {
		const {className, currentUser, dataSource, groupId, id} = this.props;

		const breadcrumbItems = id
			? [
					breadcrumbs.getDataSourceName({
						active: true,
						label: dataSource.name
					})
			  ]
			: [
					{
						href: toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
							groupId
						}),
						label: Liferay.Language.get('add-data-source')
					},
					{
						active: true,
						label: Liferay.Language.get('new-liferay')
					}
			  ];

		return (
			<BaseTabsPage
				activeTabId={tabIds.AUTHORIZATION}
				addRoute={Routes.SETTINGS_LIFERAY_ADD}
				breadcrumbItems={[
					breadcrumbs.getDataSources({groupId}),
					...breadcrumbItems
				]}
				className={getCN('liferay-data-source-old-root', className)}
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
				pageTitle={Liferay.Language.get('configure-liferay-dxp')}
				showDelete
			>
				<OAuthForm
					authorized={currentUser.isAdmin()}
					dataSource={dataSource}
					groupId={groupId}
					id={id}
					instruction={sub(
						Liferay.Language.get(
							'please-enter-the-url-of-the-target-instance-to-start-configuring-the-x-data-source.-you-will-need-to-enter-the-credentials-of-the-data-sources-administrator.-if-you-need-help-setting-this-up,-please-refer-to-the-{0}'
						),
						[
							Liferay.Language.get('liferay'),
							<a
								href={documentationURLs.addLiferayDataSource}
								key='documentationLink'
							>
								{Liferay.Language.get('documentation-fragment')}
							</a>
						],
						false
					)}
					onSubmit={this.handleUpdateLiferay}
					type={dataSourceTypes.liferay}
				/>
			</BaseTabsPage>
		);
	}
}

export default connect(
	null,
	{
		createLiferayDataSource,
		updateLiferayDataSource
	}
)(LiferayAuthorization);
