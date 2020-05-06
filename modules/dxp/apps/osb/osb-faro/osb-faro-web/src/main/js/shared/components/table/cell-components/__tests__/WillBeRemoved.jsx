import React from 'react';
import WillBeRemovedCell from '../WillBeRemoved';
import {shallow} from 'enzyme';

describe('WillBeRemovedCell', () => {
	it('should render', () => {
		const component = shallow(
			<WillBeRemovedCell
				data={{
					dataSourceIndividualPKs: []
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as will be removed', () => {
		const component = shallow(
			<WillBeRemovedCell
				data={{
					dataSourceIndividualPKs: ['test']
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
