import React from 'react';
import withBaseResults from '../WithBaseResults';
import {shallow} from 'enzyme';

describe('WithBaseResults', () => {
	it('Renders table w/ data', () => {
		const MockComponent = WrappedComponent => val => (
			<WrappedComponent {...val} />
		);
		const WrappedComponent = withBaseResults(MockComponent, {
			defaultOrderByField: 'Test'
		});

		const component = shallow(
			<WrappedComponent
				router={{params: {groupId: '123'}, query: {delta: 10, page: 1}}}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
