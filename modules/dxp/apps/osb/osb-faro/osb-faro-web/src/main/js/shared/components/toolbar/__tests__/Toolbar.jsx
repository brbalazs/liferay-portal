import React from 'react';
import Toolbar from '../index';
import {Map, Set} from 'immutable';
import {render} from 'enzyme';
import {withStaticRouter} from 'test/mock-router';

const DefaultComponent = withStaticRouter(Toolbar);

describe('Toolbar', () => {
	it('should render', () => {
		const component = render(<DefaultComponent />);

		expect(component).toMatchSnapshot();
	});

	it('should NOT render with a search input when alwaysShowSearch is false', () => {
		const component = render(
			<DefaultComponent
				alwaysShowSearch={false}
				selectEntirePageIndeterminate
			/>
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with a search input when alwaysShowSearch is true', () => {
		const component = render(
			<DefaultComponent alwaysShowSearch selectEntirePageIndeterminate />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render as disabled', () => {
		const component = render(<DefaultComponent disabled />);
		expect(component).toMatchSnapshot();
	});

	it('should render w/ a search query bar when there is a query', () => {
		const component = render(
			<DefaultComponent
				alwaysShowSearch
				query='Test'
				selectEntirePageIndeterminate
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render a list of filter tags when there are active filters', () => {
		const component = render(
			<DefaultComponent
				alwaysShowSearch
				filterBy={new Map({fooField: new Set(['fooValue'])})}
				filterByOptions={[
					{
						key: 'fooField',
						values: [{label: 'fooValue', value: 'fooValue'}]
					}
				]}
				selectEntirePageIndeterminate
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
