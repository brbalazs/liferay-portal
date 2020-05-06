import AddOnsList from '../AddOnsList';
import React from 'react';
import {fromJS} from 'immutable';
import {mockPlan} from 'test/data';
import {Plan} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('AddOnsList', () => {
	it('should render', () => {
		const component = shallow(
			<AddOnsList
				currentPlan={new Plan(fromJS(mockPlan()))}
				planType={'enterprise'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as inactive when the active prop is false', () => {
		const component = shallow(
			<AddOnsList
				active={false}
				currentPlan={new Plan(fromJS(mockPlan()))}
				planType={'enterprise'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with the quantity of addons subscribed if any', () => {
		const component = shallow(
			<AddOnsList
				active={false}
				currentPlan={new Plan(fromJS(mockPlan()))}
				planType={'enterprise'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
