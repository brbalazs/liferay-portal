import * as data from 'test/data';
import AssociatedSegments from '../AssociatedSegments';
import React from 'react';
import {Individual} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('IndividualAssociatedSegments', () => {
	it('should render', () => {
		const component = shallow(
			<AssociatedSegments
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
