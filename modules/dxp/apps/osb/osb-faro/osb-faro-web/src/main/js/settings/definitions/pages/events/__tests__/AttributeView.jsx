import AttributeView from '../AttributeView';
import mockStore from 'test/mock-store';
import React from 'react';
import {MemoryRouter} from 'react-router';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {Route} from 'react-router-dom';
import {Routes} from 'shared/util/router';

jest.unmock('react-dom');

const RenderWithRouter = ({children}) => (
	<MemoryRouter
		initialEntries={[
			'/workspace/23/settings/definitions/events/attributes/12345'
		]}
	>
		<Route path={Routes.SETTINGS_DEFINITIONS_ATTRIBUTES_VIEW}>
			{children}
		</Route>
	</MemoryRouter>
);

describe('AttributeView', () => {
	it('should render', async() => {
		const {container} = render(
			<Provider store={mockStore()}>
				<RenderWithRouter>
					<AttributeView groupId='23' />
				</RenderWithRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with a table', async() => {
		const {getByText} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<AttributeView groupId='23' />
				</StaticRouter>
			</Provider>
		);

		expect(getByText('Sample Data')).toBeTruthy();
	});
});
