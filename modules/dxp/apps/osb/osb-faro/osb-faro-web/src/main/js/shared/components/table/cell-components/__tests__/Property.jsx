import PropertyCell from '../Property';
import React from 'react';
import {shallow} from 'enzyme';

describe('PropertyCell', () => {
	it('should render', () => {
		const component = shallow(
			<PropertyCell
				data={{
					name: 'email',
					value: 'TestTest@liferay.com'
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
