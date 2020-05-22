import BaseCard from '../';
import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {shallow} from 'enzyme';

describe('BaseCard', () => {
	const MOCK_CONTEXT = {
		router: {
			query: {
				rangeKey: '0'
			}
		}
	};

	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<BasePage.Context.Provider value={MOCK_CONTEXT}>
				<BaseCard {...props} />
			</BasePage.Context.Provider>
		</ApolloProvider>
	);

	const Header = () => <div>{'My custom header component'}</div>;

	const props = {
		className: 'my-component-classname',
		label: 'My title'
	};

	it('should render component', () => {
		const component = shallow(
			<WrappedComponent {...props}>
				{() => <div>{'My body component'}</div>}
			</WrappedComponent>
		);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render component with custom Header', () => {
		const component = shallow(
			<WrappedComponent {...props} Header={Header}>
				{() => <div>{'My body component'}</div>}
			</WrappedComponent>
		);

		expect(component.render()).toMatchSnapshot();
	});

	it('should return the props in Body component', () => {
		let customBodyProps = {};

		const component = shallow(
			<WrappedComponent {...props}>
				{props => {
					customBodyProps = props;

					return <div>{'My custom body component'}</div>;
				}}
			</WrappedComponent>
		);

		component.render();

		expect(customBodyProps).toEqual({
			filters: undefined,
			interval: 'D',
			rangeSelectors: {rangeEnd: '', rangeKey: '0', rangeStart: ''},
			router: {
				query: {
					rangeKey: '0'
				}
			}
		});
	});

	it('should render a Card Header with an interval selector', () => {
		const component = shallow(
			<WrappedComponent {...props} showInterval>
				{() => <div>{'My body component'}</div>}
			</WrappedComponent>
		);

		expect(
			component
				.find('BaseCard')
				.shallow()
				.find('BaseCardHeaderDefault')
				.shallow()
				.find('IntervalSelector').length
		).toBe(1);
	});
});
