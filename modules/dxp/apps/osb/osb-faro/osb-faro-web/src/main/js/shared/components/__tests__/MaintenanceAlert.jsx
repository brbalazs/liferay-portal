import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {MaintenanceAlert, mapState} from '../MaintenanceAlert';
import {mockStoreData} from 'test/mock-store';
import {Project, RemoteData} from 'shared/util/records';

jest.unmock('react-dom');

const {projectStates} = FaroConstants;

const store = mockStoreData.setIn(
	['projects', '23'],
	new RemoteData({
		data: data.getImmutableMock(Project, data.mockProject, '23', {
			state: projectStates.scheduled,
			stateStartDate: data.getTimestamp()
		})
	})
);

const mockProject = data.getImmutableMock(Project, data.mockProject, '23', {
	state: projectStates.scheduled,
	stateStartDate: data.getTimestamp()
});

describe('MaintenanceAlert', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<MaintenanceAlert project={new Project()} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ maintenance alert', () => {
		const {container} = render(<MaintenanceAlert project={mockProject} />);

		expect(container).toMatchSnapshot();
	});
});

describe('mapState', () => {
	it('should map store state to props', () => {
		const router = {match: {params: {groupId: '23'}}};

		expect(mapState(store, router)).toMatchSnapshot();
	});
});
