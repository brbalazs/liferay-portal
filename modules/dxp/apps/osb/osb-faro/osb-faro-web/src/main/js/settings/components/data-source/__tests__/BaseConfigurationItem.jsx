import BaseConfigurationItem from '../BaseConfigurationItem';
import React from 'react';
import {shallow} from 'enzyme';

describe('BaseConfigurationItem', () => {
	it('should render', () => {
		const component = shallow(
			<BaseConfigurationItem
				description='Test description'
				title='Test Test'
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as disabled', () => {
		const component = shallow(
			<BaseConfigurationItem
				buttonParams={{disabled: true}}
				description='Test description'
				title='Test Test'
			/>
		);

		expect(component.find('Button').prop('disabled')).toBe(true);
	});

	it('should render with a status message', () => {
		const component = shallow(
			<BaseConfigurationItem
				buttonParams={{disabled: true}}
				description='Test description'
				statusMessage='Test Status Message'
				title='Test Test'
			/>
		);

		expect(component.find('.status')).toMatchSnapshot();
	});

	it('should render with a metric bar', () => {
		const component = shallow(
			<BaseConfigurationItem
				buttonParams={{disabled: true}}
				completion={0.8}
				description='Test description'
				showBar
				statusMessage='Test Status Message'
				title='Test Test'
			/>
		);

		expect(component.find('MetricBar').length).toBe(1);
	});
});
