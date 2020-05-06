import * as data from 'test/data';
import React from 'react';
import {AccountFieldMapping} from '../AccountFieldMapping';
import {shallow} from 'enzyme';

const defaultProps = {
	dataSource: data.mockSalesforceDataSource(),
	groupId: '23',
	id: '27'
};

describe('AccountFieldMapping', () => {
	it('should render', () => {
		const component = shallow(<AccountFieldMapping {...defaultProps} />);

		expect(component.shallow()).toMatchSnapshot();
	});
});
