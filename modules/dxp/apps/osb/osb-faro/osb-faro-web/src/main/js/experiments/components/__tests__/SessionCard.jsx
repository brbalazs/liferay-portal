import React from 'react';
import SessionCard from '../SessionCard';
import {cleanup, render} from '@testing-library/react';
import {StateProvider} from 'experiments/state';

jest.unmock('react-dom');

describe('SessionCard', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<StateProvider>
				<SessionCard />
			</StateProvider>
		);

		expect(container).toMatchSnapshot();
	});
});
