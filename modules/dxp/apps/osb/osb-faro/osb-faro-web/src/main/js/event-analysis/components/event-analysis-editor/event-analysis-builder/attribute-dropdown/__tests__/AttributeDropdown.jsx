import * as data from 'test/data';
import AttributeDropdown from '../index';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {fireEvent, render} from '@testing-library/react';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventAttributeDefinitionsReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
import {range} from 'lodash';

jest.unmock('react-dom');

describe('AttributeDropdown', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<MockedProvider
					mocks={[
						mockEventAttributeDefinitionsReq(
							range(10).map(i =>
								data.mockEventAttributeDefinition(i, {
									__typename: 'EventAttributeDefinition'
								})
							),
							{keyword: '', size: 200}
						)
					]}
				>
					<AttributeDropdown
						onAttributeSelect={jest.fn()}
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

	it('render w/ selected attribute', () => {
		const {getByTestId} = render(
			<WrappedComponent
				attribute={{
					dataType: 'string',
					displayName: 'Filed Ticket',
					id: '4',
					name: 'filedTicket'
				}}
			/>
		);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(
			document.body.getElementsByClassName('dropdown-item active').length
		).toBe(1);
	});

	it('render w/ disabled attributes', () => {
		const {getByTestId} = render(
			<WrappedComponent disabledIds={['1', '2']} />
		);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(
			document.body.getElementsByClassName('dropdown-item disabled')
				.length
		).toBe(2);
	});
});
