import AnalysisChip from '../AnalysisChip';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AnalysisChip', () => {
	it('render', () => {
		const {container} = render(
			<AnalysisChip event={{name: 'View Article'}} />
		);

		expect(container).toMatchSnapshot();
	});
});
