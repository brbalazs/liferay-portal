import * as data from 'test/data';
import Interests, {ContributionsCell} from '../Interests';
import React from 'react';
import {Individual} from 'shared/util/records';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('Interests', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<Interests
					entity={new Individual(data.mockIndividual())}
					groupId='23'
					id='test'
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});

describe('ContributionsCell', () => {
	it('should render', () => {
		const {container} = render(
			<ContributionsCell data={{relatedPagesCount: 8}} />
		);
		expect(container).toMatchSnapshot();
	});
});
