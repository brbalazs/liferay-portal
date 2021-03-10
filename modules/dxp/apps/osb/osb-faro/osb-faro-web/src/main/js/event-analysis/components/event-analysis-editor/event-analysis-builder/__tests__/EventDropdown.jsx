import * as data from 'test/data';
import client from 'shared/apollo/client';
import EventDropdown from '../EventDropdown';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {fireEvent, render} from '@testing-library/react';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventDefinitionsReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
import {range} from 'lodash';

jest.unmock('react-dom');

describe('EventDropdown', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<MockedProvider
					mocks={[
						mockEventDefinitionsReq(
							range(10).map(i =>
								data.mockEventDefinition(i, {
									__typename: 'EventDefinition'
								})
							),
							{eventType: 'ALL', keyword: '', size: 200}
						)
					]}
				>
					<EventDropdown
						trigger={
							<button data-testid='target'>{'click me'}</button>
						}
						{...props}
					/>
				</MockedProvider>
			</Provider>
		</ApolloProvider>
	);

	it('render', () => {
		const {container, getByTestId} = render(<WrappedComponent />);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(container).toMatchSnapshot();

		const dropdownMenu = document.body.getElementsByClassName(
			'base-dropdown-menu-root'
		)[0];

		expect(dropdownMenu).toMatchSnapshot();

		expect(
			dropdownMenu.getElementsByClassName('dropdown-item active')
		).toBeEmpty();
	});

	it('render with selected event', () => {
		const {getByTestId} = render(<WrappedComponent eventId='3' />);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(
			document.body.getElementsByClassName('dropdown-item active').length
		).toBe(1);
	});
});
