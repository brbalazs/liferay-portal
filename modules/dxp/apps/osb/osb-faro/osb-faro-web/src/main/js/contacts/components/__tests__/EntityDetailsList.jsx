import * as data from 'test/data';
import EntityDetailsList from '../EntityDetailsList';
import React from 'react';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {fromJS, Map} from 'immutable';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const defaultProps = {
	groupId: '23',
	title: 'Test Test'
};

const DefaultComponent = props => (
	<StaticRouter>
		<EntityDetailsList {...defaultProps} {...props} />
	</StaticRouter>
);

describe('EntityDetailsList', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<DefaultComponent demographicsIMap={new Map()} />
		);
		expect(container).toMatchSnapshot();
	});

	it('should render with items', () => {
		const {container} = render(
			<DefaultComponent
				demographicsIMap={fromJS(data.mockAccountDetails())}
			/>
		);

		jest.runAllTimers();
		expect(container).toMatchSnapshot();
	});

	it('should filter results by query', () => {
		const {container, getByPlaceholderText} = render(
			<DefaultComponent
				demographicsIMap={fromJS(data.mockAccountDetails())}
			/>
		);

		jest.runAllTimers();

		fireEvent.change(getByPlaceholderText('Search'), {
			target: {value: 'Agriculture'}
		});

		jest.runAllTimers();

		expect(
			container.querySelector('.subnav-tbar .tbar-item')
		).toHaveTextContent('1 Result for "Agriculture"');

		expect(container.querySelectorAll('table > tbody').length).toBe(1);
	});
});
