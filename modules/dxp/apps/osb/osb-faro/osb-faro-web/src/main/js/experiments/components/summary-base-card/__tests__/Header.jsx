import Header from '../Header';
import React from 'react';
import {shallow} from 'enzyme';

describe('SummaryBaseCard Header', () => {
	it('should render component', () => {
		const component = shallow(<Header title='My Header' />);

		expect(component.length).toBe(1);
		expect(
			component.find('SummaryBaseCardTitle').hasClass('mb-2')
		).toBeTruthy();
		expect(component.find('SummaryBaseCardTitle').props().label).toEqual(
			'My Header'
		);
		expect(component.render()).toMatchSnapshot();
	});

	it('should render component with Description', () => {
		const component = shallow(
			<Header
				Description={() => <div>{'My Description'}</div>}
				title='My Header'
			/>
		);

		expect(
			component.find('span').hasClass('font-size-sm font-weight-normal')
		).toBeTruthy();
		expect(component.find('Description').shallow()).toMatchSnapshot();
	});
});
