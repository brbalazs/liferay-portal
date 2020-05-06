import ItemField from '../ItemField';
import React from 'react';
import {shallow} from 'enzyme';

describe('ItemField', () => {
	it('should render', () => {
		const component = shallow(<ItemField />);
		expect(component).toMatchSnapshot();
	});
});
