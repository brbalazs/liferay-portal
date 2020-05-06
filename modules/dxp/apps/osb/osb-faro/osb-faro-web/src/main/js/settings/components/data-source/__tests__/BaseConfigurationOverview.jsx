import BaseConfigurationOverview from '../BaseConfigurationOverview';
import React from 'react';
import {shallow} from 'enzyme';

describe('BaseConfigurationOverview', () => {
	it('should render', () => {
		const mockConfigurationItems = [
			{
				description: 'foo description',
				label: 'edit',
				title: 'foo title'
			}
		];

		const component = shallow(
			<BaseConfigurationOverview
				configurationItems={mockConfigurationItems}
			/>
		);
		expect(component).toMatchSnapshot();
	});
});
