import ActiveIndividualsChart, {
	CHART_DATA_ID_1,
	CHART_DATA_ID_2
} from '../ActiveIndividualsChart';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ActiveIndividualsChart', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<ActiveIndividualsChart
				data={[
					{data: [], id: CHART_DATA_ID_1},
					{data: [], id: CHART_DATA_ID_2}
				]}
				rangeSelectors={{rangeKey: '30'}}
			/>
		);
		expect(container).toMatchSnapshot();
	});
});
