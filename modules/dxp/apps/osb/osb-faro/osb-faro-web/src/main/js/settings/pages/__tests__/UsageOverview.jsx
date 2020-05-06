import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {fromJS} from 'immutable';
import {Project, User} from 'shared/util/records';
import {shallow} from 'enzyme';
import {UsageOverview} from '../UsageOverview';

const {subscriptionStatuses, userRoleNames} = FaroConstants;

const defaultProps = {
	currentUser: new User(data.mockUser()),
	groupId: '23',
	project: new Project(
		data.mockProject(23, {
			faroSubscription: fromJS(data.mockSubscription())
		})
	)
};

describe('UsageOverview', () => {
	it('should render', () => {
		const component = shallow(<UsageOverview {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render with a warning type and danger type warning if one metric is approaching limit and the other is over', () => {
		const mockProject = new Project(
			data.mockProject(23, {
				faroSubscription: fromJS(
					data.mockSubscription({
						individualsStatus: subscriptionStatuses.approaching,
						pageViewsStatus: subscriptionStatuses.over
					})
				)
			})
		);

		const component = shallow(
			<UsageOverview {...defaultProps} project={mockProject} />
		);

		expect(
			component
				.find('Alert')
				.at(0)
				.prop('type')
		).toBe('warning');
		expect(
			component
				.find('Alert')
				.at(1)
				.prop('type')
		).toBe('danger');
	});

	it('should render with an approaching limit warning if a metric is approaching plan limit', () => {
		const mockProject = new Project(
			data.mockProject(23, {
				faroSubscription: fromJS(
					data.mockSubscription({
						pageViewsCount: 0,
						pageViewsLimit: 7000000,
						pageViewsStatus: subscriptionStatuses.approaching
					})
				)
			})
		);

		const component = shallow(
			<UsageOverview {...defaultProps} project={mockProject} />
		);

		expect(component.find('Alert').at(0)).toMatchSnapshot();
	});

	it('should render with an overage warning if a metric has exceeded the plan limit', () => {
		const mockProject = new Project(
			data.mockProject(23, {
				faroSubscription: fromJS(
					data.mockSubscription({
						individualsStatus: subscriptionStatuses.over
					})
				)
			})
		);

		const component = shallow(
			<UsageOverview {...defaultProps} project={mockProject} />
		);

		expect(component.find('Alert').at(0)).toMatchSnapshot();
	});

	it('should render with a member-specific message overage warning if a metric is approaching plan limit and the user is a member role', () => {
		const mockProject = new Project(
			data.mockProject(23, {
				faroSubscription: fromJS(
					data.mockSubscription({
						pageViewsStatus: subscriptionStatuses.approaching
					})
				)
			})
		);

		const component = shallow(
			<UsageOverview
				{...defaultProps}
				currentUser={
					new User(data.mockUser(0, {roleName: userRoleNames.member}))
				}
				project={mockProject}
			/>
		);

		expect(component.find('Alert').at(0)).toMatchSnapshot();
	});

	it('should use default addons for basic plans', () => {
		const mockProject = new Project(
			data.mockProject(23, {
				faroSubscription: fromJS(
					data.mockSubscription({
						name: 'Liferay Analytics Cloud Basic'
					})
				)
			})
		);

		const component = shallow(
			<UsageOverview {...defaultProps} project={mockProject} />
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});
});
