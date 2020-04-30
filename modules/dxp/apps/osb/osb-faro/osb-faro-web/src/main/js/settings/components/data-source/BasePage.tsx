import BasePage from 'settings/components/BasePage';
import DataSourceStatus from './DataSourceStatus';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import PropTypes from 'prop-types';
import React from 'react';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {DataSource, User} from 'shared/util/records';
import {getDataSourceDisplayObject} from 'shared/util/data-sources';
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

interface IBaseDataSourcePageProps extends React.HTMLAttributes<HTMLElement> {
	currentUser: User;
	documentTitle: string;
	dataSource: DataSource;
	groupId: string;
	id: string;
	pageDescription: string;
	pageTitle: React.ReactNode;
	passedChildren: React.ReactNode;
	showDelete: boolean;
}

const BaseDataSourcePage: React.FC<IBaseDataSourcePageProps> = ({
	className = '',
	currentUser,
	documentTitle = Liferay.Language.get('configure-data-source'),
	dataSource,
	groupId,
	id,
	pageDescription,
	pageTitle = Liferay.Language.get('configure-data-source'),
	passedChildren,
	showDelete = false,
	...otherProps
}) => (
	<BasePage
		{...otherProps}
		className={getCN('data-source-base-page-root', className)}
		documentTitle={`${documentTitle || pageTitle} - ${Liferay.Language.get(
			'data-sources'
		)}`}
		groupId={groupId}
		pageActions={
			id && showDelete && currentUser.isAdmin()
				? [
						{
							href: toRoute(Routes.SETTINGS_DATA_SOURCE_DELETE, {
								groupId,
								id
							}),
							label: Liferay.Language.get('delete-data-source')
						}
				  ]
				: []
		}
		pageDescription={pageDescription || getPageDescription(dataSource)}
		pageTitle={pageTitle}
	>
		<div className='page-container'>
			<div className='content-main'>{passedChildren}</div>

			<div className='content-side'>
				<DataSourceStatus {...getDataSourceDisplayObject(dataSource)} />
			</div>
		</div>
	</BasePage>
);

export default compose(connect(getOwnChildren))(BaseDataSourcePage);
