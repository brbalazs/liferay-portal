import autobind from 'autobind-decorator';
import BasePage from 'shared/components/base-page';
import Filter from 'cerebro-shared/components/Filter';
import React from 'react';
import {compose} from 'redux';
import {PropTypes} from 'prop-types';
import {withEmpty, withError} from 'cerebro-shared/hocs/utils';

/**
 * HOC
 * @description Filter
 * @param {object} withFilter
 */
const withFilterComponent = withFilter => {
	const FilterWithData = compose(
		withFilter(),
		withEmpty(),
		withError()
	)(Filter);

	/**
	 * Filter Component
	 * @class
	 */
	class FilterComponent extends React.Component {
		static contextType = BasePage.Context;

		static propTypes = {
			onChange: PropTypes.func
		};

		state = {
			/**
			 * @type {string}
			 */
			rangeKey: null
		};

		componentDidMount() {
			this.setState({
				rangeKey: this.rangeKeyValueFn()
			});
		}

		/**
		 * Receive applied filters and set state
		 * @param {object} filters
		 */
		@autobind
		handleApplyFilters(appliedFilters) {
			const {onChange} = this.props;

			onChange(appliedFilters);
		}

		/**
		 * Range Key Value
		 */
		rangeKeyValueFn() {
			const {
				rangeKey: {lastValue}
			} = this.context;

			return lastValue;
		}

		/**
		 * Lifecycle Render - ReactJS
		 */
		render() {
			const {
				context: {router},
				props: {className},
				state: {rangeKey}
			} = this;

			return (
				<FilterWithData
					className={className}
					isTopLevel
					onChange={this.handleApplyFilters}
					rangeKey={rangeKey}
					router={router}
				/>
			);
		}
	}

	return FilterComponent;
};

export {withFilterComponent};
export default withFilterComponent;
