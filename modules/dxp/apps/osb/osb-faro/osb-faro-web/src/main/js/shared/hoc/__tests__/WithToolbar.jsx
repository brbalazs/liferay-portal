import React from 'react';
import withToolbar from '../WithToolbar';
import {compose} from 'redux';
import {shallow} from 'enzyme';
import {withStaticRouter} from 'test/mock-router';

describe('withToolbar', () => {
	it('renders', () => {
		const WrappedComponent = compose(
			withStaticRouter,
			withToolbar({showRangeDropdownKey: true})
		)(() => <div>{'foobar'}</div>);

		const component = shallow(<WrappedComponent />);

		expect(component.render().length).toBe(2);
	});
});
