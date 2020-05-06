import Promise from 'metal-promise';
import {mockBlockedKeyword, mockSearch} from 'test/data';

export const fetch = jest.fn(() =>
	Promise.resolve(mockSearch(mockBlockedKeyword))
);
