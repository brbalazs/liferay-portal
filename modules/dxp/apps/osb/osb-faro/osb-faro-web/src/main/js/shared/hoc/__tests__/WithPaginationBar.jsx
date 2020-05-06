import React from 'react';
import withPaginationBar from '../WithPaginationBar';
import {compose} from 'redux';
import {shallow} from 'enzyme';
import {withStaticRouter} from 'test/mock-router';

describe('withPaginationBar', () => {
	it('renders', () => {
		const WrappedComponent = compose(
			withStaticRouter,
			withPaginationBar({defaultDelta: 10})
		)(() => <div>{'foobar'}</div>);

		const component = shallow(
			<WrappedComponent delta={5} page={1} total={15} />
		);

		expect(component.render().length).toBe(2);
	});

	it('renders w/o the pagination bar', () => {
		const WrappedComponent = compose(
			withStaticRouter,
			withPaginationBar({defaultDelta: 10})
		)(() => <div>{'foobar'}</div>);

		const component = shallow(<WrappedComponent total={0} />);

		expect(component.render().length).toBe(1);
	});
});
