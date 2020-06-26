import React from 'react';
import ToolbarActionsRenderer from '../ToolbarActionsRenderer';
import {OrderedMap} from 'immutable';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ToolbarActionsRenderer', () => {
	it('should render', () => {
		const {container} = render(<ToolbarActionsRenderer />);

		expect(container).toMatchSnapshot();
	});

	it('should render secondary button actions if items are selected', () => {
		const {container} = render(
			<ToolbarActionsRenderer
				selectedItemsIOMap={new OrderedMap([[1, {}], [2, {}]])}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
