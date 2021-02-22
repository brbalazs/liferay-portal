import BasePage from 'shared/components/base-page';
import React from 'react';
import VariantCard from '../index';
import {
	cleanup,
	fireEvent,
	render,
	waitForElementToBeRemoved
} from '@testing-library/react';
import {ExperimentResolver as Experiment} from 'shared/apollo/resolvers';
import {MockedProvider} from '@apollo/react-testing';
import {
	mockExperimentReq,
	mockExperimentVariantsHistogramReq
} from 'test/graphql-data';
import {StateProvider} from 'experiments/state';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');
jest.useRealTimers();

const PER_DAY = 'Per day';

const MOCK_CONTEXT = {
	filters: {},
	router: {
		params: {
			channelId: '456',
			groupId: '2000',
			id: '123'
		},
		query: {
			rangeKey: '30'
		}
	}
};

describe('VariantCard', () => {
	afterEach(cleanup);

	it('should render', async () => {
		const {container} = render(
			<StaticRouter>
				<MockedProvider
					mocks={[
						mockExperimentReq(),
						mockExperimentVariantsHistogramReq()
					]}
					resolvers={{Experiment}}
				>
					<BasePage.Context.Provider value={MOCK_CONTEXT}>
						<StateProvider>
							<VariantCard label='test' />
						</StateProvider>
					</BasePage.Context.Provider>
				</MockedProvider>
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});

	it('should render a Per day chart', async () => {
		const {container, getAllByText} = render(
			<StaticRouter>
				<MockedProvider
					mocks={[
						mockExperimentReq(),
						mockExperimentVariantsHistogramReq()
					]}
					resolvers={{Experiment}}
				>
					<BasePage.Context.Provider value={MOCK_CONTEXT}>
						<StateProvider>
							<VariantCard label='test' />
						</StateProvider>
					</BasePage.Context.Provider>
				</MockedProvider>
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getAllByText(PER_DAY)[0].className).not.toContain('active');

		fireEvent.click(getAllByText(PER_DAY)[0]);

		expect(getAllByText(PER_DAY)[1].className).toContain('active');
	});
});
