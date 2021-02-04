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
				defaultDataType: 'boolean',
				id: '1',
				name: 'booleanName'
			}
		},
		breakdowns: {
			1: {
				attributeId: '1',
				dataType: 'boolean',
				type: 'event'
			}
		},
		filters: {
			1: {
				attributeId: '1',
				operator: 'eq',
				value: ['true']
			}
		},
		order: ['1']
	};

	describe('attributesReducer', () => {
		const attribute = {
			defaultDataType: 'string',
			id: '0',
			name: 'testName'
		};
		const breakdown = {
			attributeId: '0',
			dataType: 'string',
			type: 'event'
		};
		const filter = {
			attributeId: '0',
			operator: 'eq',
			value: ['test']
		};

		it('should AddAttribute', () => {
			const attributes = attributesReducer(
				{
					attributes: {},
					breakdowns: {},
					filters: {},
					order: []
				},
				{
					payload: {
						attribute,
						attributeId: '0',
						breakdown,
						filter
					},
					type: ActionTypes.AddAttribute
				}
			);

			expect(attributes.attributes['0']).toEqual(attribute);
			expect(attributes.breakdowns['0']).toEqual(breakdown);
			expect(attributes.filters['0']).toEqual(filter);
			expect(attributes.order[0]).toEqual('0');
		});

		it('should EditAttribute', () => {
			const attributes = attributesReducer(initialAttributes, {
				payload: {
					attribute,
					attributeId: '0',
					breakdown,
					filter,
					oldAttributeId: '1'
				},
				type: ActionTypes.EditAttribute
			});

			expect(attributes.attributes['0']).toEqual(attribute);
			expect(attributes.breakdowns['0']).toEqual(breakdown);
			expect(attributes.filters['0']).toEqual(filter);
			expect(attributes.order[0]).toEqual('0');
			expect(attributes.order.length).toBe(1);
			expect(attributes.attributes['1']).toBeUndefined();
			expect(attributes.breakdowns['1']).toBeUndefined();
			expect(attributes.attributes['1']).toBeUndefined();
		});

		it('should DeleteAttribute', () => {
			const attributes = attributesReducer(initialAttributes, {
				payload: {
					attributeId: '1'
				},
				type: ActionTypes.DeleteAttribute
			});

			expect(attributes.order.length).toBe(0);
			expect(attributes.attributes['1']).toBeUndefined();
			expect(attributes.breakdowns['1']).toBeUndefined();
			expect(attributes.attributes['1']).toBeUndefined();
		});

		it('should MoveAttribute', () => {
			const attributes = attributesReducer(
				{
					attributes: {
						1: {
							defaultDataType: 'boolean',
							id: '1',
							name: 'booleanName'
						},
						2: {
							defaultDataType: 'duration',
							id: '2',
							name: 'durationName'
						}
					},
					breakdowns: {
						1: {
							attributeId: '1',
							dataType: 'boolean',
							type: 'event'
						},
						2: {
							attributeId: '2',
							dataType: 'duration',
							type: 'event'
						}
					},
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
					},
					order: ['1', '2']
				},
				{
					payload: {
						from: 1,
						to: 0
					},
					type: ActionTypes.MoveAttribute
				}
			);

			expect(attributes.order[0]).toEqual('2');
			expect(attributes.order[1]).toEqual('1');
			expect(attributes.order.length).toBe(2);
		});

		it('should DeleteAllAttributes', () => {
			const attributes = attributesReducer(
				{
					attributes: {
						1: {
							defaultDataType: 'boolean',
							id: '1',
							name: 'booleanName'
						},
						2: {
							defaultDataType: 'duration',
							id: '2',
							name: 'durationName'
						}
					},
					breakdowns: {
						1: {
							attributeId: '1',
							dataType: 'boolean',
							type: 'event'
						},
						2: {
							attributeId: '2',
							dataType: 'duration',
							type: 'event'
						}
					},
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
					},
					order: ['1', '2']
				},
				{
					payload: {},
					type: ActionTypes.DeleteAllAttributes
				}
			);

			expect(attributes.attributes).toBeEmpty();
			expect(attributes.breakdowns).toBeEmpty();
			expect(attributes.filters).toBeEmpty();
			expect(attributes.order).toBeEmpty();
		});
	});

	describe('withAttributesConsumer', () => {
		it('should pass the WrappedComponent', () => {
			const ChildComponent = ({
				addAttribute,
				attributes,
				breakdowns,
				deleteAllAttributes,
				deleteAttribute,
				editAttribute,
				filters,
				moveAttribute,
				order
			}) => {
				if (
					addAttribute &&
					attributes &&
					breakdowns &&
					deleteAllAttributes &&
					deleteAttribute &&
					editAttribute &&
					filters &&
					moveAttribute &&
					order
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
