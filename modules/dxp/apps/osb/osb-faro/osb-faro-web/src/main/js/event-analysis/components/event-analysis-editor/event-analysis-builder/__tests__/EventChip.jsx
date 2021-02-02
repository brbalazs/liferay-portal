import EventChip from '../EventChip';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('EventChip', () => {
	it('render', () => {
		const {container} = render(
			<EventChip event={{name: 'View Article'}} />
		);

		expect(container).toMatchSnapshot();
	});
});
