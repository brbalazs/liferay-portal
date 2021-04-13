import Edit from '../Edit';
import mockStore from 'test/mock-store';
import React from 'react';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {SegmentTypes} from 'shared/util/constants';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<Edit groupId='23' {...props} />
		</StaticRouter>
	</Provider>
);

describe('Edit', () => {
	it('should render', async () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render a dynamic segment', async () => {
		const {getByText} = render(
			<DefaultComponent type={SegmentTypes.Dynamic} />
		);

		jest.runAllTimers();

		expect(getByText('DYNAMIC Segment')).toBeTruthy();
	});

	it('should render a static segment', () => {
		const {getByText} = render(
			<DefaultComponent type={SegmentTypes.Static} />
		);

		jest.runAllTimers();

		expect(getByText('STATIC Segment')).toBeTruthy();
	});
});
