import EventAnalysis from '../EventAnalysis';
import mockStore from 'test/mock-store';
import React from 'react';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '456',
		groupId: '123'
	})
}));

const WrappedComponent = () => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<EventAnalysis />
		</StaticRouter>
	</Provider>
);

describe('Event Analysis', () => {
	it('should render', () => {
		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should render the button of Create Analysis', () => {
		const {getByText} = render(<WrappedComponent />);

		expect(getByText('Create Analysis')).toBeInTheDocument();
	});
});
