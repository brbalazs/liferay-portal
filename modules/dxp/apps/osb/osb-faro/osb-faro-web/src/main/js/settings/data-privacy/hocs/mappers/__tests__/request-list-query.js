import {FILTER_BY_OPTIONS} from '../../RequestList';
import {Map, Set} from 'immutable';
import {mapPropsToOptions} from '../request-list-query';

const mockFilterBy = new Map({
	rangeKey: new Set(['7']),
	statuses: new Set(['COMPLETED'])
});

const mockProps = {
	defaultSort: {
		field: 'createDate',
		sortOrder: 'DESC'
	},
	filterBy: mockFilterBy,
	router: {
		params: {
			id: '123123'
		},
		query: {
			delta: '5',
			page: '2',
			query: 'foo'
		}
	},
	toolbarProps: {filterByOptions: FILTER_BY_OPTIONS}
};

const {
	defaultSort: {field, sortOrder},
	router: {
		query: {delta, query}
	}
} = mockProps;

describe('Request List Query Mapper', () => {
	describe('mapPropsToOptions', () => {
		it('should map props to options', () => {
			expect(mapPropsToOptions(mockProps)).toEqual(
				expect.objectContaining({
					variables: {
						keywords: query,
						rangeKey: 7,
						size: parseInt(delta),
						sort: {
							column: field,
							type: sortOrder
						},
						start: 5,
						statuses: ['COMPLETED']
					}
				})
			);
		});
	});
});
