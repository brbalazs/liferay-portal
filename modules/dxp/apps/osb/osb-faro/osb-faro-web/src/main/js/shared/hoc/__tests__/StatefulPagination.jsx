import FaroConstants from 'shared/util/constants';
import React from 'react';
import withStatefulPagination from '../StatefulPagination';
import {DATE_CREATED} from 'shared/util/pagination';
import {Map, Set} from 'immutable';
import {OrderParams} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {shallow} from 'enzyme';

const {
	cur: DEFAULT_PAGE,
	delta: DEFAULT_DELTA,
	orderAscending,
	orderDescending
} = FaroConstants.pagination;

const DELTA = 10;
const ORDER_BY_FIELD = 'test';
const PAGE = 5;

class WrappedComponent extends React.Component {
	static propTypes = {
		onQueryChange: PropTypes.func,
		onSortChange: PropTypes.func,
		paginationProps: PropTypes.shape({
			onDeltaChange: PropTypes.func,
			onPageChange: PropTypes.func
		})
	};

	handleDeltaChange() {
		this.props.paginationProps.onDeltaChange(DELTA);
	}

	handleOrderByChange() {
		this.props.onOrderClick(orderDescending);
	}

	handleOrderByFieldChange() {
		this.props.onOrderByFieldChange(ORDER_BY_FIELD);
	}

	handleOrderByFieldsChange() {
		this.props.onOrderByFieldsChange({
			orderByFields: [
				{columnAccessor: 'foo', desc: true},
				{columnAccessor: 'bar', desc: true}
			]
		});
	}

	handlePageChange() {
		this.props.paginationProps.onPageChange(PAGE);
	}

	render() {
		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				{this.props.val}
			</div>
		);
	}
}

const WrappedComponentWithStatefulPagination = withStatefulPagination(
	WrappedComponent
);

describe('withStatefulPagination', () => {
	it('should render', () => {
		const val = 'test';
		const component = shallow(
			<WrappedComponentWithStatefulPagination val={val} />
		);
		expect(component.shallow()).toMatchSnapshot();
	});

	it('should set delta value on handleDeltaChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		expect(component.find(WrappedComponent).prop('delta')).toEqual(
			DEFAULT_DELTA
		);
		component.instance().handleDeltaChange(DELTA);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('delta')).toEqual(DELTA);
	});

	it('should set page value on handlePageChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		expect(component.find(WrappedComponent).prop('page')).toEqual(
			DEFAULT_PAGE
		);
		component.instance().handlePageChange(PAGE);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(PAGE);
	});

	it('should set orderByFields value on handleOrderByFieldsChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		expect(
			component.find(WrappedComponent).prop('orderByFields').length
		).toEqual(1);
		component.instance().handleOrderByFieldsChange({
			orderByFields: [{}, {}],
			orderParams: new OrderParams()
		});
		jest.runAllTimers();
		expect(
			component.find(WrappedComponent).prop('orderByFields').length
		).toEqual(2);
	});

	it('should set orderByField value on handleOrderByFieldChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		expect(component.find(WrappedComponent).prop('orderByField')).toEqual(
			'name'
		);
		component.instance().handleOrderByFieldChange(ORDER_BY_FIELD);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('orderByField')).toEqual(
			ORDER_BY_FIELD
		);
	});

	it('should set orderByField value on handleOrderByChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		expect(component.find(WrappedComponent).prop('orderBy')).toEqual(
			orderAscending
		);
		component.instance().handleOrderByChange(orderDescending);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('orderBy')).toEqual(
			orderDescending
		);
	});

	it('should set page value to defaultPage on handleOrderByFieldsChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		component.instance().handlePageChange(PAGE);
		expect(component.find(WrappedComponent).prop('page')).toEqual(PAGE);
		component.instance().handleOrderByFieldsChange({
			orderByFields: [],
			orderParams: new OrderParams()
		});
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(
			DEFAULT_PAGE
		);
	});

	it('should set page value to defaultPage on handleOrderByChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		component.instance().handlePageChange(PAGE);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(PAGE);
		component.instance().handleOrderByChange();
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(
			DEFAULT_PAGE
		);
	});

	it('should set page value to defaultPage on handleOrderByFieldChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		component.instance().handlePageChange(PAGE);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(PAGE);
		component.instance().handleOrderByFieldChange();
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(
			DEFAULT_PAGE
		);
	});

	it('should reset the page state on resetPage', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		component.instance().handlePageChange(PAGE);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(PAGE);
		component.instance().resetPage();
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(
			DEFAULT_PAGE
		);
	});

	it('should set page value to defaultPage on handleDeltaChange', () => {
		const component = shallow(<WrappedComponentWithStatefulPagination />);
		component.instance().handlePageChange(PAGE);
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(PAGE);
		component.instance().handleDeltaChange();
		jest.runAllTimers();
		expect(component.find(WrappedComponent).prop('page')).toEqual(
			DEFAULT_PAGE
		);
	});

	it('should pass props mapped through the mapPropsFn', () => {
		const WrapedComponentWithMapPropsFn = withStatefulPagination(
			WrappedComponent,
			null,
			props => ({fooNamespace: props})
		);

		const component = shallow(<WrapedComponentWithMapPropsFn />);

		const props = component.find('WrappedComponent').props();
		const hasFooNamespaceProperty = Object.prototype.hasOwnProperty.call(
			props,
			'fooNamespace'
		);

		expect(hasFooNamespaceProperty).toBe(true);
	});

	it('should set default parameters if getDefaultProps is a function', () => {
		const defaultParams = {
			delta: 5,
			filterBy: new Map({
				biz: new Set(['buz'])
			}),
			orderBy: orderDescending,
			orderByField: DATE_CREATED,
			orderByFields: [
				{fieldName: DATE_CREATED, orderBy: orderDescending}
			],
			page: 3,
			query: 'foo'
		};

		const WrapedComponentWithDefaultParamsFn = withStatefulPagination(
			WrappedComponent,
			({
				delta,
				filterBy,
				orderBy,
				orderByField,
				orderByFields,
				page,
				query
			}) => ({
				defaultDelta: delta,
				defaultFilterBy: filterBy,
				defaultOrderBy: orderBy,
				defaultOrderByField: orderByField,
				defaultOrderByFields: orderByFields,
				defaultPage: page,
				defaultQuery: query
			})
		);

		const component = shallow(
			<WrapedComponentWithDefaultParamsFn {...defaultParams} />
		);

		expect(component.find('WrappedComponent').prop('delta')).toEqual(
			defaultParams.delta
		);
		expect(
			component
				.find('WrappedComponent')
				.prop('filterBy')
				.isSubset(defaultParams.filterBy)
		).toBe(true);
		expect(component.find('WrappedComponent').prop('orderBy')).toEqual(
			defaultParams.orderBy
		);
		expect(component.find('WrappedComponent').prop('orderByField')).toEqual(
			defaultParams.orderByField
		);
		expect(
			component.find('WrappedComponent').prop('orderByFields')
		).toEqual(defaultParams.orderByFields);
		expect(component.find('WrappedComponent').prop('page')).toEqual(
			defaultParams.page
		);
		expect(component.find('WrappedComponent').prop('query')).toEqual(
			defaultParams.query
		);
	});
});
