import CurrentStatusCell from '../CurrentStatus';
import React from 'react';
import {shallow} from 'enzyme';

describe('CurrentStatusCell', () => {
	it('should render', () => {
		const component = shallow(
			<CurrentStatusCell
				data={{
					currentMember: true
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as a non-member', () => {
		const component = shallow(
			<CurrentStatusCell
				data={{
					currentMember: false
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
