import CSV from './CSV';
import FaroConstants from 'shared/util/constants';
import LiferayDataSource from './Liferay';
import LiferayDataSourceOld from './LiferayOld';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import Salesforce from './Salesforce';
import {compose, withDataSource} from 'shared/hoc';
import {DataSource} from 'shared/util/records';
import {hasLegacyDXPConnection} from 'shared/util/data-sources';
import {PropTypes} from 'prop-types';
import {withRouter} from 'react-router-dom';

const {dataSourceTypes} = FaroConstants;

const PAGE_MAP = {
	[dataSourceTypes.csv]: CSV,
	[dataSourceTypes.liferay]: LiferayDataSource,
	[dataSourceTypes.salesforce]: Salesforce
};

const getPageComponent = dataSource =>
	hasLegacyDXPConnection(dataSource)
		? LiferayDataSourceOld
		: PAGE_MAP[dataSource.providerType];

export class View extends React.Component {
	static propTypes = {
		dataSource: PropTypes.instanceOf(DataSource).isRequired
	};

	render() {
		const {dataSource, ...otherProps} = this.props;

		const Page = getPageComponent(dataSource);

		if (Page) {
			return (
				<Page
					{...omitDefinedProps(otherProps, View.propTypes)}
					dataSource={dataSource}
				/>
			);
		}
	}
}

export default compose(
	withRouter,
	withDataSource
)(View);
