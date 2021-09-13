import mockDate from 'test/mock-date';
import React from 'react';
import VerticalTimeline from '../VerticalTimeline';
import {cleanup, render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const ITEMS = [
	{
		header: true,
		title: 'Today'
	},
	{
		subtitle: '3 Document Downloads, 2 Form Submissions, 24 Page Visits',
		symbol: 'web-content',
		time: 1518648993917,
		title: 'www.liferay.com',
		type: 'Document'
	},
	{
		subtitle: '3 Document Downloads, 2 Form Submissions, 24 Page Visits',
		symbol: 'web-content',
		time: 1518648993917,
		title: 'New Business Purchase',
		type: 'Download'
	},
	{
		header: true,
		title: 'Yesterday'
	},
	{
		subtitle: '3 Document Downloads, 2 Form Submissions, 24 Page Visits',
		symbol: 'web-content',
		time: 1518648993917,
		title: 'Opened Email',
		type: 'Download'
	}
];

const ITEMS_NESTED = [
	{
		header: true,
		title: 'Yesterday'
	},
	{
		nestedItems: [
			{
				subtitle: 'www.liferay.com/testing',
				symbol: 'web-content',
				time: 1518648993917,
				title: 'Visited Liferay: Testing'
			},
			{
				subtitle: 'www.liferay.com/testing 2',
				symbol: 'web-content',
				time: 1518648993917,
				title: 'Visited Liferay: Testing 2'
			}
		],
		subtitle: '3 Document Downloads, 2 Form Submissions, 24 Page Visits',
		symbol: 'web-content',
		time: 1518648993917,
		title: 'Opened Email',
		type: 'Download'
	}
];

const DefaultComponent = props => (
	<StaticRouter>
		<VerticalTimeline items={ITEMS} {...props} />
	</StaticRouter>
);

describe('VerticalTimeline', () => {
	afterEach(cleanup);

	beforeAll(mockDate);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);
		expect(container).toMatchSnapshot();
	});

	it('should render with a header', () => {
		const {container} = render(
			<DefaultComponent
				headerLabels={{
					count: 'count',
					label: 'label',
					title: 'title'
				}}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with nested items', () => {
		const {container} = render(<DefaultComponent items={ITEMS_NESTED} />);
		expect(container).toMatchSnapshot();
	});

	it('should render with nested items collapsed', () => {
		const {container} = render(
			<DefaultComponent initialExpanded={false} items={ITEMS_NESTED} />
		);
		expect(container).toMatchSnapshot();
	});
});
