import InterestPagesList from '../InterestPagesList';
import React from 'react';
import {shallow} from 'enzyme';

describe('InterestPagesList', () => {
	it('should render an activePages component', () => {
		const component = shallow(
			<InterestPagesList dataSourceParams={{active: true}} />
		);

		expect(component.name()).toBe('ActivePagesList');
	});

	it('should render an InactivePages component', () => {
		const component = shallow(
			<InterestPagesList dataSourceParams={{active: false}} />
		);

		expect(component.name()).toBe('InactivePagesList');
	});
});
