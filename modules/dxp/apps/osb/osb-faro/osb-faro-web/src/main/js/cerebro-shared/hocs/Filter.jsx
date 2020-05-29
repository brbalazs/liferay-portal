import autobind from 'autobind-decorator';
import BasePage from 'shared/components/base-page';
import Filter from 'cerebro-shared/components/Filter';
import PropTypes from 'prop-types';
import React from 'react';
import {compose} from 'redux';
import {LAST_YEAR} from 'shared/util/constants';
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
			onChange: PropTypes.func,
			rangeSelectors: PropTypes.object
		};

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
		 * Lifecycle Render - ReactJS
		 */
		render() {
			const {
				context: {router},
				props: {className}
			} = this;

			return (
				<FilterWithData
					className={className}
					isTopLevel
					onChange={this.handleApplyFilters}
					rangeSelectors={{rangeKey: LAST_YEAR}}
					router={router}
				/>
			);
		}
	}

	return FilterComponent;
};

export {withFilterComponent};
export default withFilterComponent;
