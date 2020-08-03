import React from 'react';
import {Changeset} from 'shared/util/records';
import {cleanup, render} from '@testing-library/react';
import {SegmentEditStatic} from '../SegmentEditStatic';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('SegmentEditStatic', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<SegmentEditStatic changeset={new Changeset()} groupId='23' />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
