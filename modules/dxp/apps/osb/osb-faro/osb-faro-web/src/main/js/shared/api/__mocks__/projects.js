import * as data from 'test/data';
import Promise from 'metal-promise';
import {range} from 'lodash';

export const create = jest.fn(() => Promise.resolve(data.mockProject()));

export const fetch = jest.fn(() => Promise.resolve(data.mockProject()));

export const fetchAvailableTimeZones = jest.fn(() =>
	Promise.resolve(
		range(3).map(() => ({
			displayTimeZone: '(UTC) UTC',
			timezoneValue: 'UTC'
		}))
	)
);

export const fetchProjectViaCorpProjectUuid = jest.fn(() =>
	Promise.resolve(data.mockProject(0))
);

export const fetchMany = jest.fn(() =>
	Promise.resolve(range(3).map(i => data.mockProject(i)))
);

export const search = jest.fn(() =>
	Promise.resolve(data.mockSearch(data.mockProject))
);
