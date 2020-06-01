import autobind from 'autobind-decorator';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import FaroConstants, {LAST_30_DAYS} from 'shared/util/constants';
import PropTypes from 'prop-types';
import React from 'react';
import Toolbar from 'shared/components/toolbar';
import withHistory from './WithHistory';
import {get} from 'lodash';
import {hasChanges} from 'shared/util/react';
import {paginationDefaults} from 'shared/util/pagination';
import {setUriQueryValues} from 'shared/util/router';

const {
	pagination: {cur: DEFAULT_CUR}
} = FaroConstants;

export default configs => WrappedComponent => {
	class WithToolbarBar extends React.Component {
		static defaultProps = {
			disableSearch: false,
			orderBy: paginationDefaults.orderBy,
			orderByField: paginationDefaults.orderByField,
			query: paginationDefaults.query,
			rangeSelectors: {
				rangeEnd: '',
				rangeKey: LAST_30_DAYS,
				rangeStart: ''
			},
			toolbarProps: {}
		};

		static propTypes = {
			disableSearch: PropTypes.bool,
			history: PropTypes.object,
			onRangeSelectorsChange: PropTypes.func,
			onSearchValueChange: PropTypes.func,
			orderBy: PropTypes.string,
			orderByField: PropTypes.string,
			query: PropTypes.string,
			rangeSelectors: PropTypes.object,
			toolbarProps: PropTypes.object,
			total: PropTypes.number
		};

		constructor(props) {
			super(props);

			this.state = {
				searchValue: props.query
			};
		}

		componentDidUpdate(prevProps) {
			const {
				props: {query},
				state: {searchValue}
			} = this;

			if (
				hasChanges(prevProps, this.props, 'query') &&
				query !== searchValue
			) {
				this.setState({searchValue: query});
			}
		}

		@autobind
		handleRangeSelectorsChange(rangeSelectors) {
			const {history, onRangeSelectorsChange} = this.props;

			const {rangeEnd, rangeKey, rangeStart} = rangeSelectors;

			onRangeSelectorsChange
				? onRangeSelectorsChange(rangeSelectors)
				: history.push(
						setUriQueryValues({
							page: DEFAULT_CUR,
							rangeEnd,
							rangeKey,
							rangeStart
						})
				  );
		}

		@autobind
		handleSearchValueChange(value) {
			const {onSearchValueChange} = this.props;

			if (onSearchValueChange) {
				onSearchValueChange(value);
			}

			this.setState({
				searchValue: value
			});
		}

		render() {
			const {
				props: {
					disableSearch,
					orderBy,
					orderByField,
					query,
					rangeSelectors,
					renderNav,
					showDropdownRangeKey,
					toolbarProps,
					total
				},
				state: {searchValue}
			} = this;

			return (
				<>
					<Toolbar
						disableSearch={get(
							configs,
							'disableSearch',
							disableSearch
						)}
						onSearchValueChange={this.handleSearchValueChange}
						order={orderBy}
						orderBy={orderByField}
						query={query}
						searchValue={searchValue}
						showCheckbox={false}
						showSearch
						total={total}
						{...toolbarProps}
					>
						{get(
							configs,
							'showDropdownRangeKey',
							showDropdownRangeKey
						) && (
							<DropdownRangeKey
								legacy={get(
									configs,
									'legacyDropdownRangeKey',
									true
								)}
								onChange={this.handleRangeSelectorsChange}
								rangeSelectors={rangeSelectors}
							/>
						)}

						{renderNav && renderNav()}
					</Toolbar>

					<WrappedComponent {...this.props} />
				</>
			);
		}
	}

	return withHistory(WithToolbarBar);
};
