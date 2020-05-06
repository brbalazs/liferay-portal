import * as data from 'test/data';
import Promise from 'metal-promise';

export const fetch = jest.fn(() =>
	Promise.resolve(data.mockSearch(data.mockIndividualEngagement))
);

export const fetchHistory = jest.fn(() =>
	Promise.resolve(data.mockEngagementData())
);

export const fetchHistories = jest.fn(() =>
	Promise.resolve(data.mockEngagementHistories())
);
