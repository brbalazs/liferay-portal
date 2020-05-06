import React from 'react';
import SalesforceTabRoutes from '../TabRoutes';
import {shallow} from 'enzyme';

describe('SalesforceTabRoutes', () => {
	it('should render', () => {
		const component = shallow(<SalesforceTabRoutes />);
		expect(component).toMatchSnapshot();
	});
});
