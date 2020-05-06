import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import Salesforce from '../Salesforce';
import {DataSource, User} from 'shared/util/records';
import {shallow} from 'enzyme';

const {
	userRoleNames: {administrator}
} = FaroConstants;

const defaultProps = {
	currentUser: new User({roleName: administrator}),
	dataSource: data.getImmutableMock(
		DataSource,
		data.mockSalesforceDataSource
	),
	groupId: '23',
	id: 'test'
};

describe('Salesforce', () => {
	it('should render', () => {
		const component = shallow(<Salesforce {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});
});
