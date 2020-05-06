import Footer from '../Footer';
import React from 'react';
import {shallow} from 'enzyme';

describe('Modal Footer', () => {
	it('should render', () => {
		const component = shallow(<Footer />);
		expect(component).toMatchSnapshot();
	});
});
