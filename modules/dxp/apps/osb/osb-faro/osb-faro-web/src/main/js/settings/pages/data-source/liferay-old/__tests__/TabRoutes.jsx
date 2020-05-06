import LiferayTabRoutes from '../TabRoutes';
import React from 'react';
import {shallow} from 'enzyme';

describe('LiferayTabRoutes', () => {
	it('should render', () => {
		const component = shallow(<LiferayTabRoutes />);
		expect(component).toMatchSnapshot();
	});
});
