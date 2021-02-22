import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import Salesforce from './Salesforce';
import {compose, withAdminPermission, withDataSource} from 'shared/hoc';
import {ConfigureCSV} from './ConfigureCSV';
import {DataSource} from 'shared/util/records';
import {PropTypes} from 'prop-types';
const {dataSourceTypes} = FaroConstants;

const PAGE_MAP = {
	[dataSourceTypes.csv]: ConfigureCSV,
	[dataSourceTypes.salesforce]: Salesforce
};

export class Edit extends React.Component {
	static propTypes = {
		dataSource: PropTypes.instanceOf(DataSource).isRequired
	};

	render() {
		const {dataSource, ...otherProps} = this.props;

		const Page = PAGE_MAP[dataSource.providerType];

		if (Page) {
			return (
				<Page
					{...omitDefinedProps(otherProps, Edit.propTypes)}
					dataSource={dataSource}
				/>
			);
		}
	}
}

export default compose(
	withAdminPermission,
	withDataSource
)(Edit);
