import React from 'react';
import {
	ActionTypes,
	attributesReducer,
	withAttributesConsumer,
	withAttributesProvider
} from '../attributes';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('attributes', () => {
	const initialAttributes = {
		attributes: {
			1: {
				dataType: 'BOOLEAN',
				id: '1',
				name: 'booleanName'
			}
		},
		breakdownOrder: ['1'],
		breakdowns: {
			1: {
				attributeId: '1',
				dataType: 'BOOLEAN',
				type: 'event'
			}
		},
		filterOrder: ['1'],
		filters: {
			1: {
				attributeId: '1',
				operator: 'eq',
				value: ['true']
			}
		}
	};

	describe('attributesReducer', () => {
		const attribute = {
			dataType: 'STRING',
			id: '0',
			name: 'testName'
		};
		const breakdown = {
			attributeId: '0',
			dataType: 'STRING',
			type: 'event'
		};
		const filter = {
			attributeId: '0',
			operator: 'eq',
			value: ['test']
		};

		it('should AddBreakdown', () => {
			const initialState = {
				attributes: {},
				breakdownOrder: [],
				breakdowns: {},
				filterOrder: [],
				filters: {}
			};
			const attributes = attributesReducer(initialState, {
				payload: {
					attribute,
					attributeId: '0',
					breakdown
				},
				type: ActionTypes.AddBreakdown
			});

			expect(initialState).not.toEqual(attributes);
			expect(attributes.attributes['0']).toEqual(attribute);
			expect(attributes.breakdownOrder[0]).toEqual('0');
			expect(attributes.breakdowns['0']).toEqual(breakdown);
		});

		it('should AddFilter', () => {
			const initialState = {
				attributes: {},
				breakdownOrder: [],
				breakdowns: {},
				filterOrder: [],
				filters: {}
			};
			const attributes = attributesReducer(initialState, {
				payload: {
					attribute,
					attributeId: '0',
					filter
				},
				type: ActionTypes.AddFilter
			});

			expect(initialState).not.toEqual(attributes);
			expect(attributes.attributes['0']).toEqual(attribute);
			expect(attributes.filterOrder[0]).toEqual('0');
			expect(attributes.filters['0']).toEqual(filter);
		});

		it('should EditBreakdown', () => {
			const attributes = attributesReducer(initialAttributes, {
				payload: {
					attribute,
					attributeId: '0',
					breakdown,
					oldAttributeId: '1'
				},
				type: ActionTypes.EditBreakdown
			});

			expect(initialAttributes).not.toEqual(attributes);
			expect(attributes.attributes['0']).toEqual(attribute);
			expect(attributes.breakdowns['0']).toEqual(breakdown);
			expect(attributes.breakdownOrder[0]).toEqual('0');
			expect(attributes.breakdownOrder.length).toBe(1);
			expect(attributes.breakdowns['1']).toBeUndefined();
			expect(attributes.attributes['1']).toBeUndefined();
		});

		it('should EditFilter', () => {
			const attributes = attributesReducer(initialAttributes, {
				payload: {
					attribute,
					attributeId: '0',
					filter,
					oldAttributeId: '1'
				},
				type: ActionTypes.EditFilter
			});

			expect(initialAttributes).not.toEqual(attributes);
			expect(attributes.attributes['0']).toEqual(attribute);
			expect(attributes.filters['0']).toEqual(filter);
			expect(attributes.filterOrder[0]).toEqual('0');
			expect(attributes.filterOrder.length).toBe(1);
			expect(attributes.attributes['1']).toBeUndefined();
		});

		it('should DeleteBreakdown', () => {
			const initialState = {
				attributes: {
					1: {
						dataType: 'BOOLEAN',
						id: '1',
						name: 'booleanName'
					},
					2: {
						dataType: 'DURATION',
						id: '2',
						name: 'durationName'
					}
				},
				breakdownOrder: ['1', '2'],
				breakdowns: {
					1: {
						attributeId: '1',
						dataType: 'BOOLEAN',
						type: 'event'
					},
					2: {
						attributeId: '2',
						dataType: 'DURATION',
						type: 'event'
					}
				},
				filterOrder: ['2'],
				filters: {
					2: {
						attributeId: '2',
						operator: 'gt',
						value: [60000]
					}
				}
			};

			const attributes = attributesReducer(initialState, {
				payload: {
					attributeId: '1'
				},
				type: ActionTypes.DeleteBreakdown
			});

			expect(initialState).not.toEqual(attributes);
			expect(attributes.breakdownOrder.length).toBe(1);
			expect(attributes.filterOrder.length).toBe(1);
			expect(attributes.attributes['2']).toBeTruthy();
			expect(attributes.attributes['1']).toBeUndefined();
			expect(attributes.breakdowns['1']).toBeUndefined();
			expect(attributes.breakdowns['2']).toBeTruthy();
			expect(attributes.filters['2']).toBeTruthy();
		});

		it('should DeleteBreakdown', () => {
			const attributes = attributesReducer(initialAttributes, {
				payload: {
					attributeId: '1'
				},
				type: ActionTypes.DeleteBreakdown
			});

			expect(initialAttributes).not.toEqual(attributes);
			expect(attributes.breakdownOrder.length).toBe(0);
			expect(attributes.filterOrder.length).toBe(1);
			expect(attributes.attributes['1']).toBeTruthy();
			expect(attributes.breakdowns['1']).toBeUndefined();
			expect(attributes.filters['1']).toBeTruthy();
		});

		it('should DeleteFilter', () => {
			const attributes = attributesReducer(initialAttributes, {
				payload: {
					attributeId: '1'
				},
				type: ActionTypes.DeleteFilter
			});

			expect(initialAttributes).not.toEqual(attributes);
			expect(attributes.breakdownOrder.length).toBe(1);
			expect(attributes.filterOrder.length).toBe(0);
			expect(attributes.attributes['1']).toBeTruthy();
			expect(attributes.breakdowns['1']).toBeTruthy();
			expect(attributes.filters['1']).toBeUndefined();
		});

		it('should DeleteFilter', () => {
			const initialState = {
				attributes: {
					1: {
						dataType: 'BOOLEAN',
						id: '1',
						name: 'booleanName'
					},
					2: {
						dataType: 'DURATION',
						id: '2',
						name: 'durationName'
					}
				},
				breakdownOrder: ['2'],
				breakdowns: {
					2: {
						attributeId: '2',
						dataType: 'DURATION',
						type: 'event'
					}
				},
				filterOrder: ['1', '2'],
				filters: {
					1: {
						attributeId: '1',
						operator: 'eq',
						value: ['true']
					},
					2: {
						attributeId: '2',
						operator: 'gt',
						value: [60000]
					}
				}
			};

			const attributes = attributesReducer(initialState, {
				payload: {
					attributeId: '1'
				},
				type: ActionTypes.DeleteFilter
			});

			expect(initialState).not.toEqual(attributes);
			expect(attributes.breakdownOrder.length).toBe(1);
			expect(attributes.filterOrder.length).toBe(1);
			expect(attributes.attributes['2']).toBeTruthy();
			expect(attributes.attributes['1']).toBeUndefined();
			expect(attributes.breakdowns['2']).toBeTruthy();
			expect(attributes.filters['1']).toBeUndefined();
			expect(attributes.filters['2']).toBeTruthy();
		});

		it('should MoveBreakdown', () => {
			const initialState = {
				attributes: {
					1: {
						dataType: 'BOOLEAN',
						id: '1',
						name: 'booleanName'
					},
					2: {
						dataType: 'DURATION',
						id: '2',
						name: 'durationName'
					}
				},
				breakdownOrder: ['1', '2'],
				breakdowns: {
					1: {
						attributeId: '1',
						dataType: 'BOOLEAN',
						type: 'event'
					},
					2: {
						attributeId: '2',
						dataType: 'DURATION',
						type: 'event'
					}
				},
				filterOrder: ['1', '2'],
				filters: {
					1: {
						attributeId: '1',
						operator: 'eq',
						value: ['true']
					},
					2: {
						attributeId: '2',
						operator: 'gt',
						value: [60000]
					}
				}
			};

			const attributes = attributesReducer(initialState, {
				payload: {
					from: 1,
					to: 0
				},
				type: ActionTypes.MoveBreakdown
			});

			expect(initialState).not.toEqual(attributes);
			expect(attributes.breakdownOrder[0]).toEqual('2');
			expect(attributes.breakdownOrder[1]).toEqual('1');
			expect(attributes.breakdownOrder.length).toBe(2);
			expect(attributes.filterOrder[0]).toEqual('1');
			expect(attributes.filterOrder[1]).toEqual('2');
			expect(attributes.filterOrder.length).toBe(2);
		});

		it('should MoveFilter', () => {
			const initialState = {
				attributes: {
					1: {
						dataType: 'BOOLEAN',
						id: '1',
						name: 'booleanName'
					},
					2: {
						dataType: 'DURATION',
						id: '2',
						name: 'durationName'
					}
				},
				breakdownOrder: ['1', '2'],
				breakdowns: {
					1: {
						attributeId: '1',
						dataType: 'BOOLEAN',
						type: 'event'
					},
					2: {
						attributeId: '2',
						dataType: 'DURATION',
						type: 'event'
					}
				},
				filterOrder: ['1', '2'],
				filters: {
					1: {
						attributeId: '1',
						operator: 'eq',
						value: ['true']
					},
					2: {
						attributeId: '2',
						operator: 'gt',
						value: [60000]
					}
				}
			};

			const attributes = attributesReducer(initialState, {
				payload: {
					from: 1,
					to: 0
				},
				type: ActionTypes.MoveFilter
			});

			expect(initialState).not.toEqual(attributes);
			expect(attributes.breakdownOrder[0]).toEqual('1');
			expect(attributes.breakdownOrder[1]).toEqual('2');
			expect(attributes.breakdownOrder.length).toBe(2);
			expect(attributes.filterOrder[0]).toEqual('2');
			expect(attributes.filterOrder[1]).toEqual('1');
			expect(attributes.filterOrder.length).toBe(2);
		});

		it('should DeleteAllAttributes', () => {
			const initialState = {
				attributes: {
					1: {
						dataType: 'BOOLEAN',
						id: '1',
						name: 'booleanName'
					},
					2: {
						dataType: 'DURATION',
						id: '2',
						name: 'durationName'
					}
				},
				breakdownOrder: ['1', '2'],
				breakdowns: {
					1: {
						attributeId: '1',
						dataType: 'BOOLEAN',
						type: 'event'
					},
					2: {
						attributeId: '2',
						dataType: 'DURATION',
						type: 'event'
					}
				},
				filterOrder: ['1', '2'],
				filters: {
					1: {
						attributeId: '1',
						operator: 'eq',
						value: ['true']
					},
					2: {
						attributeId: '2',
						operator: 'gt',
						value: [60000]
					}
				}
			};

			const attributes = attributesReducer(initialState, {
				payload: {},
				type: ActionTypes.DeleteAllAttributes
			});

			expect(initialState).not.toEqual(attributes);
			expect(attributes.attributes).toBeEmpty();
			expect(attributes.breakdownOrder).toBeEmpty();
			expect(attributes.breakdowns).toBeEmpty();
			expect(attributes.filterOrder).toBeEmpty();
			expect(attributes.filters).toBeEmpty();
		});
	});

	describe('withAttributesConsumer', () => {
		it('should pass the WrappedComponent', () => {
			const ChildComponent = ({
				addBreakdown,
				addFilter,
				attributes,
				breakdownOrder,
				breakdowns,
				deleteAllAttributes,
				deleteBreakdown,
				deleteFilter,
				editBreakdown,
				editFilter,
				filterOrder,
				filters,
				moveBreakdown,
				moveFilter
			}) => {
				if (
					addBreakdown &&
					addFilter &&
					attributes &&
					breakdowns &&
					deleteAllAttributes &&
					deleteBreakdown &&
					deleteFilter &&
					editBreakdown &&
					editFilter &&
					filters &&
					moveBreakdown &&
					moveFilter &&
					filterOrder &&
					breakdownOrder
				) {
					return <div>{'contains all'}</div>;
				}

				return <div>{'missing some'}</div>;
			};

			const WrappedComponent = withAttributesProvider(() => {
				const WrappedChildComponent = withAttributesConsumer(
					ChildComponent
				);

				return <WrappedChildComponent />;
			});

			const {container} = render(<WrappedComponent />);

			jest.runAllTimers();

			expect(container).toHaveTextContent('contains all');
		});
	});

	describe('withAttributesProvider', () => {
		it('should pass the WrappedComponent', () => {
			const WrappedComponent = withAttributesProvider(() => (
				<div>{'foo'}</div>
			));

			const {container} = render(<WrappedComponent />);

			expect(container).toHaveTextContent('foo');
		});
	});
});
