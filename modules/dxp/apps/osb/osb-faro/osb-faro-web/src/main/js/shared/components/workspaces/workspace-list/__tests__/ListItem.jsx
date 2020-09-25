import * as API from 'shared/api';
import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import WorkspaceListItem from '../ListItem';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

const {projectStates} = FaroConstants;

jest.unmock('react-dom');

describe('WorkspaceListItem', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<WorkspaceListItem
					accountName=''
					projectState={projectStates.ready}
				/>
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render a workspace item as enabled if is unavailable and the user clicks the item to reload and it then changes to available', () => {
		API.projects.fetch.mockImplementationOnce(() =>
			Promise.resolve(data.mockProject('23'))
		);

		const {queryByText} = render(
			<StaticRouter>
				<WorkspaceListItem projectState={projectStates.unavailable} />
			</StaticRouter>
		);

		const button = queryByText('Workspace unavailable; click to reload.');

		expect(button).toBeTruthy();

		fireEvent.click(button);

		jest.runAllTimers();

		expect(
			queryByText('Workspace unavailable; click to reload.')
		).toBeNull();
	});

	it('should render a workspace with projects you can join', () => {
		const {queryByText} = render(
			<StaticRouter>
				<WorkspaceListItem
					isJoinableProjects
					projectState={projectStates.READY}
				/>
			</StaticRouter>
		);

		expect(queryByText('Request Access')).toBeTruthy();
	});

	it('should render a workspace with a deactivated project', () => {
		const {queryByText} = render(
			<StaticRouter>
				<WorkspaceListItem projectState={projectStates.deactivated} />
			</StaticRouter>
		);
		expect(queryByText('Activate')).toBeTruthy();
	});
});
