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
			onDeltaChange: PropTypes.func,
			onPageChange: PropTypes.func,
			page: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
			paginationProps: PropTypes.object,
			total: PropTypes.number
		};

		render() {
			const {
				delta,
				page,
				onDeltaChange,
				onPageChange,
				paginationProps,
				total
			} = this.props;

			console.log(paginationProps); // TODO: REmove me

			return (
				<>
					<WrappedComponent {...this.props} />

					{!!total && (
						<PaginationBar
							href={window.location.href}
							key='PAGINATION_BAR'
							onDeltaChange={onDeltaChange}
							onPageChange={onPageChange}
							page={page}
							selectedDelta={delta}
							totalItems={total}
						/>
					)}
				</>
			);
		}
	}

	return WithPaginationBar;
};
