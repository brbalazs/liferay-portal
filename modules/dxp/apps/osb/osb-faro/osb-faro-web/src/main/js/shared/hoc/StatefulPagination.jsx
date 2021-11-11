import autobind from 'autobind-decorator';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import PropTypes from 'prop-types';
import React from 'react';
import {invoke, isFunction} from 'lodash';
import {Map} from 'immutable';

const {
	pagination: {cur: DEFAULT_PAGE, delta: DEFAULT_DELTA}
} = FaroConstants;

// TODO: update "default" to "initial"
const DEFAULT_PAGINATION_PROPS = {
	defaultDelta: DEFAULT_DELTA,
	defaultFilterBy: new Map(),
	defaultPage: DEFAULT_PAGE,
	defaultQuery: ''
};

export default function withStatefulPagination(
	WrappedComponent,
	defaultPaginationProps,
	mapPropsFn,
	useRef = true
) {
	const getDefaultProps = props => {
		const defaultProps = isFunction(defaultPaginationProps)
			? defaultPaginationProps(props)
			: defaultPaginationProps;

		return {...DEFAULT_PAGINATION_PROPS, ...defaultProps};
	};

	class StatefulPagination extends React.Component {
		static defaultProps = {
			toolbarProps: {}
		};

		static propTypes = {
			toolbarProps: PropTypes.object
		};

		constructor(props) {
			super(props);

			const {
				defaultDelta,
				defaultFilterBy,
				defaultPage,
				defaultQuery
			} = getDefaultProps(props);

			const {initialOrderIOMap} = this.props;

			this.state = {
				delta: defaultDelta,
				filterBy: defaultFilterBy,
				orderIOMap: initialOrderIOMap, // TODO: Figure out way to set default state for this. maybe initialOrderIOMap?
				page: defaultPage,
				query: defaultQuery
			};

			this._wrappedComponentRef = React.createRef();
		}

		@autobind
		handleDeltaChange(delta) {
			this.setState({
				delta,
				page: DEFAULT_PAGE
			});
		}

		@autobind
		handleFilterByChange(value) {
			this.setState({filterBy: value, page: DEFAULT_PAGE});
		}

		@autobind
		handleOrderIOMapChange(orderIOMap) {
			this.setState({
				orderIOMap,
				page: DEFAULT_PAGE
			});
		}

		@autobind
		handlePageChange(page) {
			this.setState({
				page
			});
		}

		@autobind
		handleQueryChange(query) {
			this.setState({
				page: DEFAULT_PAGE,
				query
			});
		}

		/**
		 * Public method for refreshing data
		 */
		reload() {
			invoke(this._wrappedComponentRef.current, 'reload');
		}

		/**
		 * Public method for resetting the page state to default value.
		 */
		resetPage() {
			this.handlePageChange(DEFAULT_PAGE);
		}

		render() {
			const {
				props: {paginationProps, toolbarProps, ...otherProps},
				state: {delta, filterBy, orderIOMap, page, query}
			} = this;

			const statefulProps = {
				delta,
				filterBy,
				onOrderByFieldChange: this.handleOrderByFieldsChange,
				onOrderByFieldsChange: this.handleOrderByFieldsChange,
				onOrderIOMapChange: this.handleOrderIOMapChange,
				onSearchValueChange: this.handleQueryChange,
				orderIOMap,
				page,
				paginationProps: {
					...paginationProps,
					onDeltaChange: this.handleDeltaChange,
					onPageChange: this.handlePageChange
				},
				query,
				toolbarProps: {
					...toolbarProps,
					onFilterByChange: this.handleFilterByChange,
					onOrderByFieldChange: this.handleOrderByFieldChange,
					onOrderClick: this.handleOrderByChange,
					onSearchSubmit: this.handleQueryChange
				}
			};

			const mappedStatefulProps = mapPropsFn
				? mapPropsFn(statefulProps, this.props)
				: statefulProps;

			const ref = useRef ? {ref: this._wrappedComponentRef} : {};

			return (
				<WrappedComponent
					{...omitDefinedProps(
						otherProps,
						StatefulPagination.propTypes
					)}
					{...mappedStatefulProps}
					{...ref}
				/>
			);
		}
	}

	return StatefulPagination;
}
