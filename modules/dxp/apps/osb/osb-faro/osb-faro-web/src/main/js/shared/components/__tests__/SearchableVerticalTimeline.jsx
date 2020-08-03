import * as data from 'test/data';
import Promise from 'metal-promise';
import React from 'react';
import SearchableVerticalTimeline from '../SearchableVerticalTimeline';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';
import {withStaticRouter} from 'test/mock-router';

jest.unmock('react-dom');

const DefaultComponent = withStaticRouter(SearchableVerticalTimeline);

describe('SearchableVerticalTimeline', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<DefaultComponent
				dataSourceFn={() => Promise.resolve(data.mockSearch(noop, 0))}
				groupId='23'
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
