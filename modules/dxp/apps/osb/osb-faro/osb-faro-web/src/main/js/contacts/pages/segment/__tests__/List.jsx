import * as data from 'test/data';
import List from '../List';
import mockStore from 'test/mock-store';
import React from 'react';
import {ChannelContext} from 'shared/context/channel';
import {cleanup, render} from '@testing-library/react';
import {mockChannelContext} from 'test/mock-channel-context';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router-dom';
import {UnassignedSegmentsContext} from 'shared/context/unassignedSegments';
import {User} from 'shared/util/records';

jest.unmock('react-dom');

const MOCK_UNASSIGNED_SEGMENTS_CONTEXT = {
	showUnassignedAlert: false,
	unassignedSegments: [],
	unassignedSegmentsDispatch: jest.fn()
};

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<UnassignedSegmentsContext.Provider
				value={MOCK_UNASSIGNED_SEGMENTS_CONTEXT}
			>
				<ChannelContext.Provider value={mockChannelContext()}>
					<List
						channelId='123'
						currentUser={data.getImmutableMock(User, data.mockUser)}
						groupId='23'
						{...props}
					/>
				</ChannelContext.Provider>
			</UnassignedSegmentsContext.Provider>
		</StaticRouter>
	</Provider>
);

describe('List', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render with an active DISABLED segments filter', () => {
		const {container} = render(<DefaultComponent state='DISABLED' />);

		jest.runAllTimers();

		expect(
			container.querySelector('.subnav-tbar .label')
		).toHaveTextContent('Disabled Segments');
	});
});
