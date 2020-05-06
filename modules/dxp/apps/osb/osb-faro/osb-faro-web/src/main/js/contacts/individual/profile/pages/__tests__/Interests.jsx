import * as data from 'test/data';
import Interests, {ContributionsCell} from '../Interests';
import React from 'react';
import {Individual} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('Interests', () => {
	it('should render', () => {
		const component = shallow(
			<Interests
				entity={new Individual(data.mockIndividual())}
				groupId={'23'}
				id={'test'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});

describe('ContributionsCell', () => {
	it('should render', () => {
		const component = shallow(
			<ContributionsCell data={{relatedPagesCount: 8}} />
		);

		expect(component).toMatchSnapshot();
	});
});
