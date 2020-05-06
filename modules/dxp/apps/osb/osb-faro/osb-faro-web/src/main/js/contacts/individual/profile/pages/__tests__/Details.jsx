import * as data from 'test/data';
import Details from '../Details';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {Individual} from 'shared/util/records';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('IndividualDetails', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<Details
					groupId='23'
					id='test'
					individual={data.getImmutableMock(
						Individual,
						data.mockIndividual
					)}
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
