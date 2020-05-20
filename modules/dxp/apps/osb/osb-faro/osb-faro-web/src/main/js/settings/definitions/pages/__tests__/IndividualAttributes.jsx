import IndividualAttributes from '../IndividualAttributes';
import mockStore from 'test/mock-store';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<IndividualAttributes groupId='23' {...props} />
		</StaticRouter>
	</Provider>
);

describe('IndividualAttributes', () => {
	afterEach(cleanup);

	it('should render', async() => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
