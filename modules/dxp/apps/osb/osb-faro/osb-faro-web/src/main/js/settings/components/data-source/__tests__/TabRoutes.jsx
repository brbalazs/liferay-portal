import React from 'react';
import TabRoutes from '../TabRoutes';
import {shallow} from 'enzyme';

describe('TabRoutes', () => {
	it('should render', () => {
		const component = shallow(
			<TabRoutes routes={[{component: jest.fn(), path: 'foo/path'}]} />
		);
		expect(component).toMatchSnapshot();
	});
});
