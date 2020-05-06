import PaginationBar from 'shared/components/PaginationBar';
import PropTypes from 'prop-types';
import React from 'react';
import {paginationDefaults} from 'shared/util/pagination';

const defaultOptions = {defaultDelta: paginationDefaults.delta};

export default (options = {}) => WrappedComponent => {
	const {defaultDelta} = {...defaultOptions, ...options};

	class WithPaginationBar extends React.Component {
		static defaultProps = {
			delta: defaultDelta,
			page: paginationDefaults.page,
			paginationProps: {}
		};

		static propTypes = {
			delta: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
			page: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
			paginationProps: PropTypes.object,
			total: PropTypes.number
		};

		render() {
			const {delta, page, paginationProps, total} = this.props;

			return (
				<>
					<WrappedComponent {...this.props} />

					{!!total && (
						<PaginationBar
							{...paginationProps}
							href={window.location.href}
							key='PAGINATION_BAR'
							page={parseInt(page)}
							selectedDelta={parseInt(delta)}
							totalItems={total}
						/>
					)}
				</>
			);
		}
	}

	return WithPaginationBar;
};
