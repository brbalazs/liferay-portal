import Loading from '../Loading';
import React from 'react';
import {shallow} from 'enzyme';

describe('Loading', () => {
	it('should render', () => {
		const component = shallow(<Loading />);
		expect(component).toMatchSnapshot();
	});
});
