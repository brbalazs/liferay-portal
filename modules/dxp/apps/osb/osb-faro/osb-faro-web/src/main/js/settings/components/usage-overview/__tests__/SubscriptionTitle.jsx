import React from 'react';
import SubscriptionTitle from '../SubscriptionTitle';
import {shallow} from 'enzyme';

describe('SubscriptionTitle', () => {
	it('should render', () => {
		const component = shallow(
			<SubscriptionTitle name={'Business'} price={750} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with a label', () => {
		const component = shallow(
			<SubscriptionTitle labelText={'1x'} name={'Business'} price={750} />
		);
		expect(component).toMatchSnapshot();
	});
});
