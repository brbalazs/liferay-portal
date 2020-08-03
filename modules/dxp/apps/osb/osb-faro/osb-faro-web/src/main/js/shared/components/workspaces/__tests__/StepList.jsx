import React from 'react';
import StepList from '../StepList';
import {shallow} from 'enzyme';

describe('StepList', () => {
	it('should render', () => {
		const component = shallow(<StepList />);
		expect(component).toMatchSnapshot();
	});

	it('should render with secondaryInfo and steps', () => {
		const component = shallow(
			<StepList secondaryInfo='test' steps={['test', 'test 2']} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render without bullet', () => {
		const component = shallow(
			<StepList
				hideBullets
				secondaryInfo='test'
				steps={['test', 'test 2']}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
