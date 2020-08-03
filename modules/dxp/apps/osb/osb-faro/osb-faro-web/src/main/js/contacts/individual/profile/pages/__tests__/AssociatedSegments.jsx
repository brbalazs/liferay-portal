import * as data from 'test/data';
import AssociatedSegments from '../AssociatedSegments';
import React from 'react';
import {Individual} from 'shared/util/records';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('IndividualAssociatedSegments', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<AssociatedSegments
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
