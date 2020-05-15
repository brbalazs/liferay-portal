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
			toolbarProps: {}
		};

		static propTypes = {
			disableSearch: PropTypes.bool,
			history: PropTypes.object,
			onChangeRangeKey: PropTypes.func,
			onSearchValueChange: PropTypes.func,
			orderBy: PropTypes.string,
			orderByField: PropTypes.string,
			query: PropTypes.string,
			rangeKey: PropTypes.string,
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
		handleRangeKeyValueChange(rangeKey) {
			const {history, onChangeRangeKey} = this.props;

			onChangeRangeKey
				? onChangeRangeKey(rangeKey)
				: history.push(
						setUriQueryValues({page: DEFAULT_CUR, rangeKey})
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
					rangeKey = LAST_30_DAYS,
					renderNav,
					showRangeKeyDropdown,
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
							'showRangeKeyDropdown',
							showRangeKeyDropdown
						) && (
							<DropdownRangeKey
								legacy={get(
									configs,
									'legacyDropdownRangeKey',
									true
								)}
								onChange={this.handleRangeKeyValueChange}
								rangeKey={rangeKey}
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
