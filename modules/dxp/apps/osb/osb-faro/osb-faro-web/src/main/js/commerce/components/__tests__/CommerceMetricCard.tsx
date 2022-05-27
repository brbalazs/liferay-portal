import client from 'shared/apollo/client';
import CommerceMetricCard from 'commerce/components/CommerceMetricCard';
import CommerceTotalOrderValueQuery, {
	CommerceTotalOrderValueData
} from 'commerce/queries/TotalOrderValueQuery';
import React from 'react';
import {ApolloProvider} from '@apollo/react-hooks';
import {cleanup, render} from '@testing-library/react';
import {
	mockCommerceTotalOrderValueReq,
	mockTimeRangeReq
} from 'test/graphql-data';
import {MockedProvider} from '@apollo/react-testing';
import {RangeKeyTimeRanges} from 'shared/util/constants';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '123',
		query: {
			rangeKey: RangeKeyTimeRanges.Last30Days
		}
	})
}));

const COMMERCE_TOTAL_ORDER_VALUE = '$100,000.00';
const COMMERCE_TREND_PERCENTAGE = 50;

const data = (
	classification = 'POSITIVE',
	percentage = COMMERCE_TREND_PERCENTAGE
) => ({
	commerceTotalOrderValue: {
		__typename: 'CommerceTotalOrderValue',
		currencies: {
			__typename: 'CommerceTotalOrderCurrencies',
			USD: {
				__typename: 'CommerceTotalOrderCurreny',
				trend: {
					__typename: 'CommerceTotalOrderTrend',
					percentage,
					trendClassification: classification
				},
				value: COMMERCE_TOTAL_ORDER_VALUE
			}
		}
	}
});

const variables = {
	channelId: '123',
	rangeEnd: '',
	rangeKey: '30',
	rangeStart: ''
};

const WrappedComponent = ({data}: {data?: any}) => (
	<ApolloProvider client={client}>
		<StaticRouter>
			<MockedProvider
				mocks={[
					mockTimeRangeReq(),
					mockCommerceTotalOrderValueReq({
						data,
						Query: CommerceTotalOrderValueQuery,
						variables
					})
				]}
			>
				<CommerceMetricCard<CommerceTotalOrderValueData>
					description='this is the description'
					emptyTitle='There are no orders on the selected period.'
					label='this is the label'
					mapper={result => result?.commerceTotalOrderValue}
					Query={CommerceTotalOrderValueQuery}
				/>
			</MockedProvider>
		</StaticRouter>
	</ApolloProvider>
);

describe('CommerceMetricCard', () => {
	it('should render', () => {
		const {container, getByText} = render(
			<WrappedComponent data={data()} />
		);

		jest.runAllTimers();

		const dropdownRangeSelector = document.querySelector(
			'.dropdown-range-key-menu-root'
		);

		expect(getByText('this is the description')).toBeInTheDocument();
		expect(getByText('this is the label')).toBeInTheDocument();
		expect(dropdownRangeSelector).toBeInTheDocument();
		expect(getByText(COMMERCE_TOTAL_ORDER_VALUE)).toBeInTheDocument();
		expect(getByText(`${COMMERCE_TREND_PERCENTAGE}%`)).toBeInTheDocument();
		expect(container).toMatchSnapshot();
	});

	it('should render with empty state message', () => {
		const {getByText} = render(<WrappedComponent data={[]} />);

		jest.runAllTimers();

		expect(
			getByText('There are no orders on the selected period.')
		).toBeInTheDocument();
		expect(
			getByText(
				'Check back later to verify if data has been received from your data sources.'
			)
		).toBeInTheDocument();
	});
});

describe('CommerceMetricCard Classifications', () => {
	afterEach(cleanup);

	it('should render with POSITIVE classification', () => {
		const {container} = render(
			<WrappedComponent data={data('POSITIVE')} />
		);

		jest.runAllTimers();

		const trendElement = container.querySelector('.analytics-trend');
		expect(window.getComputedStyle(trendElement).color).toEqual(
			'rgb(40, 125, 60)'
		);
		expect(
			trendElement.querySelector('.lexicon-icon-caret-top-l')
		).toBeInTheDocument();
	});

	it('should render with NEGATIVE classification', () => {
		const {container} = render(
			<WrappedComponent data={data('NEGATIVE')} />
		);

		jest.runAllTimers();

		const trendElement = container.querySelector('.analytics-trend');
		expect(window.getComputedStyle(trendElement).color).toEqual(
			'rgb(218, 20, 20)'
		);
		expect(
			trendElement.querySelector('.lexicon-icon-caret-top-l')
		).toBeInTheDocument();
	});

	it('should render with NEUTRAL classification', () => {
		const {container} = render(<WrappedComponent data={data('NEUTRAL')} />);

		jest.runAllTimers();

		const trendElement = container.querySelector('.analytics-trend');
		expect(window.getComputedStyle(trendElement).color).toEqual(
			'rgb(174, 176, 187)'
		);
		expect(
			trendElement.querySelector('.lexicon-icon-caret-top-l')
		).toBeInTheDocument();
	});
});

describe('CommerceMetricCard Trend', () => {
	afterEach(cleanup);

	it('should render with POSITIVE trend', () => {
		const {container} = render(
			<WrappedComponent data={data('POSITIVE', 50)} />
		);

		jest.runAllTimers();

		const trendElement = container.querySelector('.analytics-trend');
		expect(
			trendElement.querySelector('.lexicon-icon-caret-top-l')
		).toBeInTheDocument();
	});

	it('should render with NEGATIVE trend', () => {
		const {container} = render(
			<WrappedComponent data={data('NEGATIVE', -50)} />
		);

		jest.runAllTimers();

		const trendElement = container.querySelector('.analytics-trend');
		expect(
			trendElement.querySelector('.lexicon-icon-caret-bottom-l')
		).toBeInTheDocument();
	});

	it('should render with NEUTRAL trend', () => {
		const {container} = render(
			<WrappedComponent data={data('NEUTRAL', 0)} />
		);

		jest.runAllTimers();

		const trendElement = container.querySelector('.analytics-trend');
		expect(
			trendElement.querySelector('.lexicon-icon-caret-top-l')
		).not.toBeInTheDocument();
		expect(
			trendElement.querySelector('.lexicon-icon-caret-bottom-l')
		).not.toBeInTheDocument();
	});
});
