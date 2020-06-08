import CreateItemSimilarity from '../CreateItemSimilarity';
import mockStore from 'test/mock-store';
import React from 'react';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

const defaultProps = {
	router: {params: {groupId: '23'}, query: {delta: '10', page: '1'}}
};

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<CreateItemSimilarity {...defaultProps} {...props} />
		</StaticRouter>
	</Provider>
);

describe('Recommendations', () => {
	it('should render', async() => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
