import BasePage from 'settings/components/BasePage';
import DataSourceStatus from './DataSourceStatus';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {DataSource, User} from 'shared/util/records';
import {getDataSourceDisplayObject} from 'shared/util/data-sources';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {truncate} from 'lodash';

const getPageDescription = dataSource =>
	dataSource
		? [dataSource.name, dataSource.url]
				.filter(item => item)
				.map(item => truncate(item, {length: 50}))
				.join(' - ')
		: '';

const getOwnChildren = (store, ownProps) => ({
	passedChildren: ownProps.children
});

export class BaseDataSourcePage extends React.Component {
	static defaultProps = {
		className: '',
		documentTitle: Liferay.Language.get('configure-data-source'),
		pageTitle: Liferay.Language.get('configure-data-source'),
		showDelete: false
	};

	static propTypes = {
		className: PropTypes.string,
		currentUser: PropTypes.instanceOf(User).isRequired,
		dataSource: PropTypes.instanceOf(DataSource),
		documentTitle: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string,
		pageDescription: PropTypes.string,
		pageTitle: PropTypes.node,
		passedChildren: PropTypes.node,
		showDelete: PropTypes.bool
	};

	render() {
		const {
			className,
			currentUser,
			dataSource,
			documentTitle,
			groupId,
			id,
			pageDescription,
			pageTitle,
			passedChildren,
			showDelete,
			...otherProps
		} = this.props;

		return (
			<BasePage
				{...omitDefinedProps(otherProps, BaseDataSourcePage.propTypes)}
				className={getCN('data-source-base-page-root', className)}
				documentTitle={`${documentTitle ||
					pageTitle} - ${Liferay.Language.get('data-sources')}`}
				groupId={groupId}
				pageActions={
					id && showDelete && currentUser.isAdmin()
						? [
								{
									href: toRoute(
										Routes.SETTINGS_DATA_SOURCE_DELETE,
										{
											groupId,
											id
										}
									),
									label: Liferay.Language.get(
										'delete-data-source'
									)
								}
						  ]
						: []
				}
				pageDescription={
					pageDescription || getPageDescription(dataSource)
				}
				pageTitle={pageTitle}
			>
				<div className='page-container'>
					<div className='content-main'>{passedChildren}</div>

					<div className='content-side'>
						<DataSourceStatus
							{...getDataSourceDisplayObject(dataSource)}
						/>
					</div>
				</div>
			</BasePage>
		);
	}
}

export default compose(connect(getOwnChildren))(BaseDataSourcePage);
