import React from 'react';
import TableTabs from '../TableTabs';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

describe('TableTabs', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<TableTabs
				activeTabId='foo'
				items={[
					{
						assetId: '123',
						assetTitle: 'Test',
						entrancesMetric: 12
					}
				]}
				onChange={jest.fn()}
				tabConfig={[
					{getColumns: noop, tabId: 'foo', title: 'Test Tab 0'},
					{getColumns: noop, tabId: 'bar', title: 'Test Tab 1'}
				]}
				total={1}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ empty message', () => {
		const emptyMessage = 'Nothing here';
		const tabTitle = 'Test Tab 0';

		const {getByText} = render(
			<TableTabs
				activeTabId='foo'
				empty
				emptyMessage={emptyMessage}
				onChange={jest.fn()}
				tabConfig={[
					{getColumns: noop, tabId: 'foo', title: tabTitle},
					{getColumns: noop, tabId: 'bar'}
				]}
			/>
		);

		expect(getByText(emptyMessage)).toBeTruthy();
		expect(getByText(tabTitle)).toBeTruthy();
	});

	it('should render w/ error message', () => {
		const tabTitle = 'Test Tab 0';

		const {getByText} = render(
			<TableTabs
				activeTabId='foo'
				error
				onChange={jest.fn()}
				tabConfig={[
					{getColumns: noop, tabId: 'foo', title: tabTitle},
					{getColumns: noop, tabId: 'bar'}
				]}
			/>
		);

		expect(getByText('An unexpected error occurred.')).toBeTruthy();
		expect(getByText(tabTitle)).toBeTruthy();
	});
});
