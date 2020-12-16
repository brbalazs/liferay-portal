import Constants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import {Edit} from '../Edit';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const {segmentTypes} = Constants;

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<Edit groupId='23' {...props} />
		</StaticRouter>
	</Provider>
);

describe('Edit', () => {
	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should render a dynamic segment', () => {
		const {getByText} = render(
			<DefaultComponent type={segmentTypes.dynamic} />
		);

		expect(getByText('DYNAMIC Segment')).toBeTruthy();
	});

	it('should render a static segment', () => {
		const {getByText} = render(
			<DefaultComponent type={segmentTypes.static} />
		);

		expect(getByText('STATIC Segment')).toBeTruthy();
	});
});
