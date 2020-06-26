import React from 'react';
import SubscriptionTitle from '../SubscriptionTitle';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('SubscriptionTitle', () => {
	
	it('should render', () => {
		const {container} = render(
			<SubscriptionTitle name={'Business'} price={750} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with a label', () => {
		const {container} = render(
			<SubscriptionTitle labelText={'1x'} name={'Business'} price={750} />
		);

		expect(container).toMatchSnapshot();
	});
});
