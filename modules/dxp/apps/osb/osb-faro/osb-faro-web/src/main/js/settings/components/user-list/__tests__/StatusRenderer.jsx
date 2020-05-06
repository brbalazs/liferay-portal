import React from 'react';
import StatusRenderer from '../StatusRenderer';
import {shallow} from 'enzyme';

describe('StatusRenderer', () => {
	it('should render', () => {
		const component = shallow(<StatusRenderer data={{status: 0}} />);
		expect(component).toMatchSnapshot();
	});
});
