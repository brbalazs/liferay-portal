import React from 'react';
import Trend from '../Trend';
import {shallow} from 'enzyme';

describe('Trend', () => {
	it('should render', () => {
		const component = shallow(
			<Trend color='red' label='Trend component' />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ icon', () => {
		const component = shallow(
			<Trend color='red' icon='home' label='Trend component' />
		);

		expect(component).toMatchSnapshot();
	});
});
