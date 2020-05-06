import * as data from 'test/data';
import React from 'react';
import {IndividualFieldMapping} from '../IndividualFieldMapping';
import {shallow} from 'enzyme';

const defaultProps = {
	dataSource: data.mockSalesforceDataSource(),
	groupId: '23',
	id: '27'
};

describe('IndividualFieldMapping', () => {
	it('should render', () => {
		const component = shallow(<IndividualFieldMapping {...defaultProps} />);

		expect(component.shallow()).toMatchSnapshot();
	});
});
