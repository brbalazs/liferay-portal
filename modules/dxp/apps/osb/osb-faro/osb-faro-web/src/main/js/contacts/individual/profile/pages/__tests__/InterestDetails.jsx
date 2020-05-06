import * as data from 'test/data';
import InterestDetails from '../InterestDetails';
import React from 'react';
import {Individual} from 'shared/util/records';
import {shallow} from 'enzyme';

const defaultProps = {
	active: 'true',
	groupId: '23',
	id: 'test',
	individual: new Individual(data.mockIndividual()),
	interestId: 1
};

describe('InterestDetails', () => {
	it('should render', () => {
		const component = shallow(<InterestDetails {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render an active pages list tab', () => {
		const component = shallow(<InterestDetails {...defaultProps} />);

		expect(
			component
				.find('Item')
				.at(0)
				.prop('active')
		).toBe(true);
	});

	it('should render an inactive pages list tab', () => {
		const component = shallow(
			<InterestDetails {...defaultProps} active='false' />
		);

		expect(
			component
				.find('Item')
				.at(1)
				.prop('active')
		).toBe(true);
	});
});
