import * as API from 'shared/api';
import * as breadcrumbs from 'shared/util/breadcrumbs';
import autobind from 'autobind-decorator';
import BaseDataSourcePage from '../../../components/data-source/BasePage';
import DataTransformation, {
	processFieldMappings
} from 'settings/components/DataTransformation';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';

import omitDefinedProps from 'shared/util/omitDefinedProps';

import ProgressTimeline from 'shared/components/ProgressTimeline';
import React from 'react';
import Sheet from 'shared/components/Sheet';
import {close, open} from 'shared/actions/modals';
import {
	compose,
	redirectIf,
	withAdminPermission,
	withDataSource
} from 'shared/hoc';
import {connect} from 'react-redux';
import {CONTACTS, Routes, toRoute} from 'shared/util/router';
import {DataSource} from 'shared/util/records';
import {dataSourceRedirectFn} from 'shared/util/data-sources';
import {PropTypes} from 'prop-types';
import {TITLE_MAP} from './SyncContacts';

const {active} = FaroConstants.dataSourceStatuses;
const STEP = 2;

export class ConfigureLiferayContacts extends React.Component {
	static propTypes = {
		close: PropTypes.func.isRequired,
		dataSource: PropTypes.instanceOf(DataSource).isRequired,
		groupId: PropTypes.string.isRequired,
		history: PropTypes.object.isRequired,
		id: PropTypes.string.isRequired,
		open: PropTypes.func.isRequired
	};

	state = {
		submitting: false
	};

	componentDidMount() {
		const {
			dataSource: {provider},
			groupId,
			history,
			id
		} = this.props;

		const contactsConfiguration = provider.get('contactsConfiguration');

		if (!contactsConfiguration) {
			history.push(
				toRoute(Routes.SETTINGS_LIFERAY_CONTACTS, {
					groupId,
					id
				})
			);
		}
	}

	@autobind
	handleSubmit(fieldMappings) {
		const {
			dataSource: {name},
			groupId,
			history,
			id
		} = this.props;

		this.setState({
			submitting: true
		});

		API.dataSource
			.updateLiferay({
				fieldMappingMaps: processFieldMappings(fieldMappings),
				groupId,
				id,
				name,
				status: active
			})
			.then(({id}) => {
				this.setState({
					submitting: false
				});

				history.push(
					toRoute(Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS, {
						groupId,
						id
					})
				);
			})
			.catch(() => {
				this.setState({
					submitting: false
				});
			});
	}

	render() {
		const {
			props: {className, dataSource, groupId, id, ...otherProps},
			state: {submitting}
		} = this;

		return (
			<BaseDataSourcePage
				{...omitDefinedProps(
					otherProps,
					ConfigureLiferayContacts.propTypes
				)}
				breadcrumbItems={[
					breadcrumbs.getDataSources({groupId}),
					breadcrumbs.getDataSourceName({
						groupId,
						id,
						label: dataSource.name
					}),
					{
						href: toRoute(Routes.SETTINGS_LIFERAY_CONTACTS, {
							groupId,
							id
						}),
						label: Liferay.Language.get('sync-contacts')
					},
					{
						active: true,
						label: Liferay.Language.get('configure-contacts')
					}
				]}
				className={getCN('configure-liferay-contacts-root', className)}
				dataSource={dataSource}
				documentTitle={dataSource.name}
				groupId={groupId}
				key='ConfigureContacts'
				pageTitle={Liferay.Language.get('configuration-options')}
				showDelete={false}
			>
				<Sheet>
					<Sheet.Header divider>
						<ProgressTimeline
							activeIndex={STEP - 1}
							items={TITLE_MAP[CONTACTS]}
						/>
					</Sheet.Header>

					<DataTransformation
						cancelHref={toRoute(
							Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS,
							{
								groupId,
								id
							}
						)}
						groupId={groupId}
						id={id}
						key='DATA_TRANSFORMATION'
						name={dataSource.url}
						onSubmit={this.handleSubmit}
						showUnmatchedFields={false}
						submitMessage={Liferay.Language.get('configure')}
						submitting={submitting}
					/>
				</Sheet>
			</BaseDataSourcePage>
		);
	}
}

export default compose(
	connect(
		null,
		{
			close,
			open
		}
	),
	withAdminPermission,
	withDataSource,
	redirectIf(dataSourceRedirectFn)
)(ConfigureLiferayContacts);
