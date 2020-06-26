import AddOnsList from '../AddOnsList';
import React from 'react';
import {fromJS} from 'immutable';
import {mockPlan} from 'test/data';
import {Plan} from 'shared/util/records';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AddOnsList', () => {
	it('should render', () => {
		const {container} = render(
			<AddOnsList
				currentPlan={new Plan(fromJS(mockPlan()))}
				planType={'enterprise'}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render as inactive when the active prop is false', () => {
		const {container} = render(
			<AddOnsList
				active={false}
				currentPlan={new Plan(fromJS(mockPlan()))}
				planType={'enterprise'}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with the quantity of addons subscribed if any', () => {
		const {container} = render(
			<AddOnsList
				active={false}
				currentPlan={new Plan(fromJS(mockPlan()))}
				planType={'enterprise'}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
