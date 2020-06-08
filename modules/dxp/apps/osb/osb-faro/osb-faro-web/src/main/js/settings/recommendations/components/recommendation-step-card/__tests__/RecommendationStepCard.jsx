import React from 'react';
import RecommendationStepCard from '../index';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('RecommendationStepCard', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<RecommendationStepCard router={{groupId: '123'}} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
