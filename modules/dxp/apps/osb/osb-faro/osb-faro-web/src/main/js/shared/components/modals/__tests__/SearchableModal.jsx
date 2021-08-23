import Promise from 'metal-promise';
import React from 'react';
import SearchableModal from '../SearchableModal';
import {cleanup, render} from '@testing-library/react';
import {mockSegment} from 'test/data';
import {noop} from 'lodash';
import {times} from 'lodash';

jest.unmock('react-dom');

describe('SearchableModal', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<SearchableModal
				dataSourceFn={() =>
					Promise.resolve({
						items: times(3, i => mockSegment(i)),
						total: 3
					})
				}
				onClose={noop}
			/>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render with an empty state', () => {
		const {queryByText} = render(
			<SearchableModal
				dataSourceFn={() => Promise.resolve({items: [], total: 0})}
				onClose={noop}
			/>
		);

		jest.runAllTimers();

		expect(queryByText('There are no items found.')).toBeTruthy();
	});
});
