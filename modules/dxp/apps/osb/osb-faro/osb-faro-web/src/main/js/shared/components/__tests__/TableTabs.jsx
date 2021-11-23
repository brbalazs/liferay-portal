import React from 'react';
import TableTabs from '../TableTabs';
import {cleanup, render} from '@testing-library/react';
import {MemoryRouter, Route} from 'react-router-dom';
import {noop} from 'lodash';
import {Routes} from 'shared/util/router';

jest.unmock('react-dom');

describe('TableTabs', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<MemoryRouter
				initialEntries={[
					'/workspace/23/settings/definitions/events/custom?delta=1'
				]}
			>
				<Route path={Routes.SETTINGS_DEFINITIONS_EVENTS_CUSTOM}>
					<TableTabs
						activeTabId='foo'
						items={[
							{
								assetId: '123',
								assetTitle: 'Test',
								entrancesMetric: 12
							}
						]}
						loading={false}
						onChange={jest.fn()}
						tabConfig={[
							{
								getColumns: () => [
									{accessor: 'assetId'},
									{accessor: 'assetTitle'}
								],
								tabId: 'foo',
								title: 'Test Tab 0'
							},
							{
								getColumns: () => [
									{accessor: 'assetId'},
									{accessor: 'assetTitle'}
								],
								tabId: 'bar',
								title: 'Test Tab 1'
							}
						]}
						total={1}
					/>
				</Route>
			</MemoryRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ empty message', () => {
		const emptyMessage = 'Nothing here';
		const tabTitle = 'Test Tab 0';

		const {getByText} = render(
			<MemoryRouter
				initialEntries={[
					'/workspace/23/settings/definitions/events/custom?delta=1'
				]}
			>
				<Route path={Routes.SETTINGS_DEFINITIONS_EVENTS_CUSTOM}>
					<TableTabs
						activeTabId='foo'
						emptyMessage={emptyMessage}
						items={[]}
						loading={false}
						onChange={jest.fn()}
						tabConfig={[
							{getColumns: noop, tabId: 'foo', title: tabTitle},
							{getColumns: noop, tabId: 'bar'}
						]}
					/>
				</Route>
			</MemoryRouter>
		);

		expect(getByText(emptyMessage)).toBeTruthy();
		expect(getByText(tabTitle)).toBeTruthy();
	});

	it('should render w/ error message', () => {
		const tabTitle = 'Test Tab 0';

		const {getByText} = render(
			<MemoryRouter
				initialEntries={[
					'/workspace/23/settings/definitions/events/custom?delta=1'
				]}
			>
				<Route path={Routes.SETTINGS_DEFINITIONS_EVENTS_CUSTOM}>
					<TableTabs
						activeTabId='foo'
						error
						loading={false}
						onChange={jest.fn()}
						tabConfig={[
							{getColumns: noop, tabId: 'foo', title: tabTitle},
							{getColumns: noop, tabId: 'bar'}
						]}
					/>
				</Route>
			</MemoryRouter>
		);

		expect(getByText('An unexpected error occurred.')).toBeTruthy();
		expect(getByText(tabTitle)).toBeTruthy();
	});
});
