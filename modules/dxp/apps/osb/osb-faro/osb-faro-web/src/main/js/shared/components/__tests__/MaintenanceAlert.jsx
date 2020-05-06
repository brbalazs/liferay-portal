import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {MaintenanceAlert, mapState} from '../MaintenanceAlert';
import {mockStoreData} from 'test/mock-store';
import {ProjectState, RemoteData} from 'shared/util/records';

jest.unmock('react-dom');

const {projectStates} = FaroConstants;

const store = mockStoreData.setIn(
	['projectStates', '23'],
	new RemoteData({
		data: data.getImmutableMock(ProjectState, data.mockProjectState, '23', {
			state: projectStates.scheduled,
			stateStartDate: data.getTimestamp()
		})
	})
);

const mockProjectState = data.getImmutableMock(
	ProjectState,
	data.mockProjectState,
	'23',
	{
		state: projectStates.scheduled,
		stateStartDate: data.getTimestamp()
	}
);

describe('MaintenanceAlert', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<MaintenanceAlert projectState={new ProjectState()} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ maintenance alert', () => {
		const {container} = render(
			<MaintenanceAlert projectState={mockProjectState} />
		);

		expect(container).toMatchSnapshot();
	});
});

describe('mapState', () => {
	it('should map store state to props', () => {
		const router = {match: {params: {groupId: '23'}}};

		expect(mapState(store, router)).toMatchSnapshot();
	});
});
