import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import ExperimentListTitle from '../ExperimentListTitle';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {shallow} from 'enzyme';

describe('ExperimentListTitle', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	const MOCK_CONTEXT = {
		router: {
			params: {
				groupId: '2000'
			},
			query: {
				rangeKey: '30'
			}
		}
	};

	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<BasePage.Context.Provider value={MOCK_CONTEXT}>
				<BrowserRouter>
					<ExperimentListTitle {...props} />
				</BrowserRouter>
			</BasePage.Context.Provider>
		</ApolloProvider>
	);

	it('should render component', () => {
		component = shallow(
			<WrappedComponent
				id='8a254c3f121aa322bc6ea3a53d787113531915a495d97fafba6d5f4c4bfb9550'
				title='My Experiment Title'
				touchpoint='http://www.liferay.com'
			/>
		);

		expect(component.render()).toMatchSnapshot();
	});
});
