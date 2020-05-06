import IndividualDetailsCard from '../DetailsCard';
import React from 'react';
import {fromJS} from 'immutable';
import {Individual} from 'shared/util/records';
import {mockIndividual} from 'test/data';
import {shallow} from 'enzyme';

describe('IndividualDetailsCard', () => {
	it('should render', () => {
		const component = shallow(
			<IndividualDetailsCard
				entity={new Individual(fromJS(mockIndividual()))}
				groupId={'23'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
