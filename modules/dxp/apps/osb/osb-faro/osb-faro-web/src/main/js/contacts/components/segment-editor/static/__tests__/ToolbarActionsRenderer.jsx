import React from 'react';
import ToolbarActionsRenderer from '../ToolbarActionsRenderer';
import {OrderedMap} from 'immutable';
import {shallow} from 'enzyme';

describe('ToolbarActionsRenderer', () => {
	it('should render', () => {
		const component = shallow(<ToolbarActionsRenderer />);
		expect(component).toMatchSnapshot();
	});

	it('should render secondary button actions if items are selected', () => {
		const component = shallow(
			<ToolbarActionsRenderer
				selectedItemsIOMap={new OrderedMap([[1, {}], [2, {}]])}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
