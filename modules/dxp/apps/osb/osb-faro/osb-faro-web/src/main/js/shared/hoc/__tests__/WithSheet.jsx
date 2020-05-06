import React from 'react';
import withSheet from '../WithSheet';
import {shallow} from 'enzyme';

describe('withSheet', () => {
	it('should render', () => {
		const WrappedComponent = withSheet({large: true})(() => (
			<p>{'Test Test'}</p>
		));

		const component = shallow(<WrappedComponent />);
		expect(component).toMatchSnapshot();
	});
});
