import * as data from 'test/data';
import Overview from '../Overview';
import React from 'react';
import {Individual} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('IndividualOverview', () => {
	it('should render', () => {
		const component = shallow(
			<Overview
				groupId={'23'}
				id={'test'}
				individual={data.getImmutableMock(
					Individual,
					data.mockIndividual
				)}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
