import {mapCardPropsToOptions, mapPropsToOptions} from '../interests-query';

const mockProps = {
	defaultSort: {
		field: 'default Test',
		sortOrder: 'DESC'
	},
	router: {
		params: {
			id: '123123'
		},
		query: {
			delta: '5',
			page: '2',
			query: 'test query'
		}
	}
};

describe('Interests Query Mapper', () => {
	describe('mapCardPropsToOptions', () => {
		it('should map interests list query card props to options', () => {
			const id = '123';

			expect(mapCardPropsToOptions({id})).toEqual(
				expect.objectContaining({
					variables: {
						active: true,
						id,
						size: 5,
						sort: {
							column: 'count',
							type: 'DESC'
						},
						start: 0
					}
				})
			);
		});
	});

	describe('mapPropsToOptions', () => {
		it('should map interests list query props to options', () => {
			const {
				defaultSort: {field, sortOrder},
				router: {
					params: {id},
					query: {delta, query}
				}
			} = mockProps;

			expect(mapPropsToOptions(mockProps)).toEqual(
				expect.objectContaining({
					variables: {
						active: true,
						id,
						keywords: query,
						size: parseInt(delta),
						sort: {
							column: field,
							type: sortOrder
						},
						start: 5
					}
				})
			);
		});
	});
});
