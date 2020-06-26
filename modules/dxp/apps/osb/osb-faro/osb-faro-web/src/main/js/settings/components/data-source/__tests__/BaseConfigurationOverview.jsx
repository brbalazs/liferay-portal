import BaseConfigurationOverview from '../BaseConfigurationOverview';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('BaseConfigurationOverview', () => {
	it('should render', () => {
		const mockConfigurationItems = [
			{
				description: 'foo description',
				label: 'edit',
				title: 'foo title'
			}
		];

		const {container} = render(
			<BaseConfigurationOverview
				configurationItems={mockConfigurationItems}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
