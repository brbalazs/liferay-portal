import getSummaryMapper from 'experiments/hocs/mappers/experiment-summary-mapper';
import {mergedVariants} from 'experiments/util/experiments';
import {mount, shallow} from 'enzyme';

const DXP_VARIANTS_MOCK = [
	{
		changes: 0,
		control: true,
		dxpVariantId: 'DEFAULT',
		dxpVariantName: 'Control',
		trafficSplit: 34,
		uniqueVisitors: 1
	},
	{
		changes: 0,
		control: false,
		dxpVariantId: '4203992243',
		dxpVariantName: 'Variant 01',
		trafficSplit: 33,
		uniqueVisitors: 1
	},
	{
		changes: 0,
		control: false,
		dxpVariantId: '2365555171',
		dxpVariantName: 'Variant 02',
		trafficSplit: 33,
		uniqueVisitors: 1
	}
];

const VARIANT_METRICS_MOCK = [
	{
		confidenceInterval: [0.3, 0.4],
		dxpVariantId: 'DEFAULT',
		improvement: 1,
		median: 0.5,
		probabilityToWin: 0.2
	},
	{
		confidenceInterval: [0.3, 0.4],
		dxpVariantId: '4203992243',
		improvement: 1,
		median: 0.5,
		probabilityToWin: 0.2
	},
	{
		confidenceInterval: [0.3, 0.4],
		dxpVariantId: '2365555171',
		improvement: 5,
		median: 0.5,
		probabilityToWin: 0.2
	}
];

const BEST_VARIANT = mergedVariants(
	DXP_VARIANTS_MOCK,
	VARIANT_METRICS_MOCK
).reduce((prev, current) =>
	prev.improvement > current.improvement ? prev : current
);

const DATA_MOCK = {
	experiment: {
		bestVariant: BEST_VARIANT,
		currentStep: 3,
		description:
			'Lorem ipsum, dolor sit amet consectetur adipisicing elit.',
		dxpExperienceName: 'Experience Name',
		dxpSegmentName: 'Segment Name',
		dxpVariants: DXP_VARIANTS_MOCK,
		finishedDate: '2019-08-14T12:00:00.063Z',
		goal: {
			metric: 'CLICK_RATE'
		},
		id: '370220349949814900',
		metrics: {
			completion: 100,
			elapsedDays: 5,
			estimatedDaysLeft: 15,
			variantMetrics: VARIANT_METRICS_MOCK
		},
		pageURL: 'http://localhost/web/guest/content-page',
		progress: 100,
		publishedDXPVariantId: '2365555171',
		sessions: 3000,
		startedDate: '2019-08-05T20:26:59.063Z',
		winnerDXPVariantId: '2365555171'
	}
};

describe('Summary Mapper for status in DRAFT', () => {
	const mapper = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			status: 'DRAFT'
		}
	});

	it('should return formatted data', () => {
		expect(mapper.status).toEqual('draft');
		expect(mapper).toMatchSnapshot();
	});

	it('should render step 1 description', () => {
		expect(mapper.setup.steps[0].Description()).toMatchSnapshot();
	});

	it('should render step 2 description', () => {
		expect(mapper.setup.steps[1].Description()).toMatchSnapshot();
	});

	it('should render step 3 description', () => {
		expect(mapper.setup.steps[2].Description()).toMatchSnapshot();
	});

	it('should render step 4 description', () => {
		expect(mapper.setup.steps[3].Description()).toMatchSnapshot();
	});

	it('should return formatted header', () => {
		expect(mapper.header.title).toEqual('Test Is in Draft Mode');
		expect(mapper.header.Description()).toEqual(
			'Finish the setup to run the test.'
		);
	});

	it('should return 4 steps', () => {
		expect(mapper.setup.steps.length).toBe(4);
	});

	it('should return formatted setup', () => {
		expect(mapper.setup.current).toBe(3);
		expect(mapper.setup.title).toEqual('Setup');
	});

	it('should return formatted summary', () => {
		expect(mapper.summary).toEqual({
			description:
				'Lorem ipsum, dolor sit amet consectetur adipisicing elit.',
			subtitle: 'Description',
			title: 'Summary'
		});
	});

	it('should display empty state', () => {
		expect(mapper).toMatchSnapshot();
	});

	it('should display empty state for step 1', () => {
		const mapper = getSummaryMapper({
			experiment: {
				status: 'DRAFT'
			}
		});
		const Description = mount(mapper.setup.steps[0].Description());

		expect(Description.text()).toEqual(
			'Select a control experience and target segment for your test.'
		);
		expect(Description).toMatchSnapshot();
	});

	it('should display empty state for step 1', () => {
		const mapper = getSummaryMapper({
			experiment: {
				status: 'DRAFT'
			}
		});
		const Description = mount(mapper.setup.steps[1].Description());

		expect(Description.text()).toEqual(
			"Choose a metric that determines your campaign's success."
		);
		expect(Description).toMatchSnapshot();
	});

	it('should display empty state for step 1', () => {
		const mapper = getSummaryMapper({
			experiment: {
				status: 'DRAFT'
			}
		});
		const Description = mount(mapper.setup.steps[2].Description());

		expect(Description.text()).toEqual('No variants created.');
		expect(Description).toMatchSnapshot();
	});

	it('should display empty state for step 1', () => {
		const mapper = getSummaryMapper({
			experiment: {
				status: 'DRAFT'
			}
		});
		const Description = mount(mapper.setup.steps[3].Description());

		expect(Description.text()).toEqual(
			'Review traffic split and run your test.'
		);
		expect(Description).toMatchSnapshot();
	});
});

describe('Summary Mapper for status in RUNNING', () => {
	const mapper = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			status: 'RUNNING'
		}
	});

	it('should return formatted data for status in RUNNING', () => {
		expect(mapper.status).toEqual('running');
		expect(mapper).toMatchSnapshot();
	});

	it('should return formatted header', () => {
		expect(mapper.header.modals.length).toBe(1);
		expect(mapper.header.modals[0].title).toEqual('Terminate Test');
		expect(mapper.header.Description()).toEqual('Started: Aug 5, 2019');
		expect(mapper.header.title).toEqual('Test Is Running');
	});

	it('should return total sections', () => {
		expect(mapper.sections.length).toBe(4);
	});

	it('should return Body for section 1', () => {
		const Body = mount(mapper.sections[0].Body());

		expect(Body.props().title).toEqual('Test Completion');
		expect(Body.find('SummarySectionHeading').props().value).toEqual(
			'100%'
		);
		expect(Body.find('SummarySectionProgressBar').props().value).toEqual(
			100
		);
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 2', () => {
		const Body = mount(mapper.sections[1].Body());

		expect(Body.props().title).toEqual('Days Running');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('5');
		expect(Body.find('SummarySectionDescription').props().value).toEqual(
			'About 15 Days Left'
		);
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 3', () => {
		const Body = mount(mapper.sections[2].Body());

		expect(Body.props().title).toEqual('Total Test Sessions');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('3K');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 4', () => {
		const Body = mount(mapper.sections[3].Body());

		expect(Body.props().title).toEqual('Test Metric');
		expect(Body.find('SummarySectionMetricType').props().value).toEqual(
			'Click-Through Rate'
		);
		expect(Body.find('SummarySectionVariant').props().lift).toEqual('5%');
		expect(Body.find('SummarySectionVariant').props().status).toEqual('up');
		expect(Body.render()).toMatchSnapshot();
	});
});

describe('Summary Mapper for status in FINISHED_WINNER and winner no declared', () => {
	const mapper = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			status: 'FINISHED_NO_WINNER'
		}
	});

	it('should return formatted data for status in FINISHED_NO_WINNER', () => {
		expect(mapper.status).toEqual('finished_no_winner');
		expect(mapper).toMatchSnapshot();
	});

	it('should return formatted alert', () => {
		expect(mapper.alert.description).toEqual(
			'We recommend that you use any of the test candidates, as they will perform similarly.'
		);
		expect(mapper.alert.symbol).toEqual('exclamation-circle');
		expect(mapper.alert.title).toEqual('There is no clear winner.');
	});

	it('should return formatted header', () => {
		expect(mapper.header.modals.length).toBe(2);
		expect(mapper.header.modals[0].title).toEqual('Complete Test');
		expect(mapper.header.modals[1].title).toEqual('Publish Variant');
		expect(mapper.header.Description()).toEqual('Started: Aug 5, 2019');
		expect(mapper.header.title).toEqual('No Clear Winner');
	});

	it('should return total sections', () => {
		expect(mapper.sections.length).toBe(4);
	});

	it('should return Body for section 1', () => {
		const Body = mount(mapper.sections[0].Body());

		expect(Body.props().title).toEqual('Test Completion');
		expect(Body.find('SummarySectionHeading').props().value).toEqual(
			'100%'
		);
		expect(Body.find('SummarySectionProgressBar').props().value).toEqual(
			100
		);
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 2', () => {
		const Body = mount(mapper.sections[1].Body());

		expect(Body.props().title).toEqual('Days Running');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('5');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 3', () => {
		const Body = mount(mapper.sections[2].Body());

		expect(Body.props().title).toEqual('Total Test Sessions');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('3K');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 4', () => {
		const Body = mount(mapper.sections[3].Body());

		expect(Body.props().title).toEqual('Test Metric');
		expect(Body.find('SummarySectionMetricType').props().value).toEqual(
			'Click-Through Rate'
		);
		expect(Body.find('SummarySectionVariant').props().lift).toEqual('5%');
		expect(Body.find('SummarySectionVariant').props().status).toEqual('up');
		expect(Body.render()).toMatchSnapshot();
	});
});

describe('Summary Mapper for status FINISHED_WINNER and winner declared', () => {
	const mapper = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			status: 'FINISHED_WINNER'
		}
	});

	const mapperControlWinner = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			status: 'FINISHED_WINNER',
			winnerDXPVariantId: 'DEFAULT'
		}
	});

	it('should return formatted data for status in FINISHED_WINNER', () => {
		expect(mapper.status).toEqual('finished_winner');
		expect(mapper).toMatchSnapshot();
	});

	it('should return formatted modals', () => {
		expect(mapper.header.modals.length).toBe(3);
		expect(mapper.header.modals[0].title).toEqual('Publish Winner');
		expect(mapper.header.modals[1].title).toEqual('Publish Other Variant');
		expect(mapper.header.modals[2].title).toEqual('Complete Test');
	});

	it('should return formatted modals with Control winner', () => {
		expect(mapperControlWinner.header.modals.length).toBe(2);
		expect(mapper.header.modals[0].title).toEqual('Publish Winner');
		expect(mapper.header.modals[1].title).toEqual('Publish Other Variant');
	});

	it('should return formatted alert', () => {
		expect(mapper.alert.description).toEqual(
			'We recommend that you publish the winning variant.'
		);
		expect(mapper.alert.symbol).toEqual('check-circle');
		expect(mapper.alert.title).toEqual(
			'Variant 02 has outperformed control by at least 5%'
		);
	});

	it('should return formatted alert with Control winner', () => {
		expect(mapperControlWinner.alert.description).toEqual(
			'We recommend that you keep control published and complete this test.'
		);
		expect(mapperControlWinner.alert.symbol).toEqual('check-circle');
		expect(mapperControlWinner.alert.title).toEqual(
			'Control has outperformed all variants by at least 1%'
		);
	});

	it('should return formatted header', () => {
		expect(mapper.header.Description()).toEqual('Started: Aug 5, 2019');
		expect(mapper.header.title).toEqual('Winner Declared');
	});

	it('should return total sections', () => {
		expect(mapper.sections.length).toBe(4);
	});

	it('should return Body for section 1', () => {
		const Body = mount(mapper.sections[0].Body());

		expect(Body.props().title).toEqual('Test Completion');
		expect(Body.find('SummarySectionHeading').props().value).toEqual(
			'100%'
		);
		expect(Body.find('SummarySectionProgressBar').props().value).toEqual(
			100
		);
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 2', () => {
		const Body = mount(mapper.sections[1].Body());

		expect(Body.props().title).toEqual('Days Running');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('5');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 3', () => {
		const Body = mount(mapper.sections[2].Body());

		expect(Body.props().title).toEqual('Total Test Sessions');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('3K');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 4', () => {
		const Body = mount(mapper.sections[3].Body());

		expect(Body.props().title).toEqual('Test Metric');
		expect(Body.find('SummarySectionMetricType').props().value).toEqual(
			'Click-Through Rate'
		);
		expect(Body.find('SummarySectionVariant').props().lift).toEqual('5%');
		expect(Body.find('SummarySectionVariant').props().status).toEqual('up');
		expect(Body.render()).toMatchSnapshot();
	});
});

describe('Summary Mapper for status in COMPLETED', () => {
	const mapper = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			publishedDXPVariantId: 'DEFAULT',
			status: 'COMPLETED'
		}
	});

	const mapperWithWinnerVariant = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			publishedDXPVariantId: '2365555171',
			status: 'COMPLETED'
		}
	});

	it('should return formatted data', () => {
		expect(mapper.status).toEqual('completed');
		expect(mapper).toMatchSnapshot();
	});

	it('should return formatted data and complete a variant', () => {
		expect(mapperWithWinnerVariant.status).toEqual('completed');
		expect(mapperWithWinnerVariant.alert.description).toEqual(
			'Your new experience was successfully published and no more data will be collected for this test.'
		);
		expect(mapperWithWinnerVariant.alert.symbol).toEqual('check-circle');
		expect(mapperWithWinnerVariant.alert.title).toEqual(
			'Variant 02 has been published.'
		);

		expect(mapperWithWinnerVariant).toMatchSnapshot();
	});

	it('should return formatted alert', () => {
		expect(mapper.alert.description).toEqual(
			'No more data will be collected for this test.'
		);
		expect(mapper.alert.symbol).toEqual('check-circle');
		expect(mapper.alert.title).toEqual(
			'Control has been kept as the experience.'
		);
	});

	it('should return formatted cardModals', () => {
		expect(mapper.header.cardModals.length).toBe(1);
		expect(mapper.header.cardModals[0].title).toEqual('Delete Test');
	});

	it('should return formatted header', () => {
		const Description = shallow(mapper.header.Description());

		expect(Description.html()).toEqual(
			'<div class="date"><div>Started: Aug 5, 2019</div><div>Ended: Aug 14, 2019</div></div>'
		);
		expect(mapper.header.title).toEqual('Test Complete');
	});

	it('should return total sections', () => {
		expect(mapper.sections.length).toBe(4);
	});

	it('should return Body for section 1', () => {
		const Body = mount(mapper.sections[0].Body());

		expect(Body.props().title).toEqual('Test Completion');
		expect(Body.find('SummarySectionHeading').props().value).toEqual(
			'100%'
		);
		expect(Body.find('SummarySectionProgressBar').props().value).toEqual(
			100
		);
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 2', () => {
		const Body = mount(mapper.sections[1].Body());

		expect(Body.props().title).toEqual('Days Ran');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('5');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 3', () => {
		const Body = mount(mapper.sections[2].Body());

		expect(Body.props().title).toEqual('Total Test Sessions');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('3K');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 4', () => {
		const Body = mount(mapper.sections[3].Body());

		expect(Body.props().title).toEqual('Test Metric');
		expect(Body.find('SummarySectionMetricType').props().value).toEqual(
			'Click-Through Rate'
		);
		expect(Body.render()).toMatchSnapshot();
	});
});

describe('Summary Mapper for status in COMPLETED and a variant published', () => {
	const mapper = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			status: 'COMPLETED'
		}
	});

	it('should return formatted data', () => {
		expect(mapper.status).toEqual('completed');
		expect(mapper).toMatchSnapshot();
	});

	it('should return formatted alert', () => {
		expect(mapper.alert.description).toEqual(
			'Your new experience was successfully published and no more data will be collected for this test.'
		);
		expect(mapper.alert.symbol).toEqual('check-circle');
		expect(mapper.alert.title).toEqual('Variant 02 has been published.');
	});

	it('should return formatted cardModals', () => {
		expect(mapper.header.cardModals.length).toBe(1);
		expect(mapper.header.cardModals[0].title).toEqual('Delete Test');
	});

	it('should return formatted header', () => {
		const Description = shallow(mapper.header.Description());

		expect(Description.html()).toEqual(
			'<div class="date"><div>Started: Aug 5, 2019</div><div>Ended: Aug 14, 2019</div></div>'
		);
		expect(mapper.header.title).toEqual('Test Complete');
	});

	it('should return total sections', () => {
		expect(mapper.sections.length).toBe(4);
	});

	it('should return Body for section 1', () => {
		const Body = mount(mapper.sections[0].Body());

		expect(Body.props().title).toEqual('Test Completion');
		expect(Body.find('SummarySectionHeading').props().value).toEqual(
			'100%'
		);
		expect(Body.find('SummarySectionProgressBar').props().value).toEqual(
			100
		);
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 2', () => {
		const Body = mount(mapper.sections[1].Body());

		expect(Body.props().title).toEqual('Days Ran');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('5');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 3', () => {
		const Body = mount(mapper.sections[2].Body());

		expect(Body.props().title).toEqual('Total Test Sessions');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('3K');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 4', () => {
		const Body = mount(mapper.sections[3].Body());

		expect(Body.props().title).toEqual('Test Metric');
		expect(Body.find('SummarySectionMetricType').props().value).toEqual(
			'Click-Through Rate'
		);
		expect(Body.render()).toMatchSnapshot();
	});
});

describe('Summary Mapper for status in TERMINATED', () => {
	const mapper = getSummaryMapper({
		...DATA_MOCK,
		experiment: {
			...DATA_MOCK.experiment,
			status: 'TERMINATED'
		}
	});

	it('should return formatted data for status in TERMINATED', () => {
		expect(mapper.status).toEqual('terminated');
		expect(mapper).toMatchSnapshot();
	});

	it('should return formatted cardModals', () => {
		expect(mapper.header.cardModals.length).toBe(1);
		expect(mapper.header.cardModals[0].title).toEqual('Delete Test');
	});

	it('should return formatted header', () => {
		const Description = shallow(mapper.header.Description());

		expect(Description.html()).toEqual(
			'<div class="date"><div>Started: Aug 5, 2019</div><div>Stopped: Aug 14, 2019</div></div>'
		);
		expect(mapper.header.title).toEqual('Test Was Terminated');
	});

	it('should return total sections', () => {
		expect(mapper.sections.length).toBe(4);
	});

	it('should return Body for section 1', () => {
		const Body = mount(mapper.sections[0].Body());

		expect(Body.props().title).toEqual('Test Completion');
		expect(Body.find('SummarySectionHeading').props().value).toEqual(
			'100%'
		);
		expect(Body.find('SummarySectionProgressBar').props().value).toEqual(
			100
		);
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 2', () => {
		const Body = mount(mapper.sections[1].Body());

		expect(Body.props().title).toEqual('Days Running');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('5');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 3', () => {
		const Body = mount(mapper.sections[2].Body());

		expect(Body.props().title).toEqual('Total Test Sessions');
		expect(Body.find('SummarySectionHeading').props().value).toEqual('3K');
		expect(Body.render()).toMatchSnapshot();
	});

	it('should return Body for section 4', () => {
		const Body = mount(mapper.sections[3].Body());

		expect(Body.props().title).toEqual('Test Metric');
		expect(Body.find('SummarySectionMetricType').props().value).toEqual(
			'Click-Through Rate'
		);
		expect(Body.find('SummarySectionVariant').props().lift).toEqual('5%');
		expect(Body.find('SummarySectionVariant').props().status).toEqual('up');
		expect(Body.render()).toMatchSnapshot();
	});
});
