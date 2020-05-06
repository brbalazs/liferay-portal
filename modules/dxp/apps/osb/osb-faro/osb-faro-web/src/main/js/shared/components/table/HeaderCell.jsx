import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import faroConstants from 'shared/util/constants';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import {
	ACCESSOR_TO_FIELD_MAP,
	getDefaultSortOrder,
	invertOrder
} from 'shared/util/pagination';
import {get, isNull} from 'lodash';
import {OrderParams} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {setUriQueryValues} from 'shared/util/router';

const {cur: defaultPage} = faroConstants.pagination;

const getFieldName = accessor =>
	get(ACCESSOR_TO_FIELD_MAP, [accessor], accessor);

class HeaderCell extends React.Component {
	static defaultProps = {
		headerLink: false,
		orderParams: new OrderParams(),
		sortable: true
	};

	static propTypes = {
		accessor: PropTypes.string,
		headerLink: PropTypes.bool,
		onSort: PropTypes.func,
		orderParams: PropTypes.instanceOf(OrderParams),
		sortable: PropTypes.bool
	};

	@autobind
	handleSort() {
		const {accessor, onSort} = this.props;

		const fieldName = getFieldName(accessor);

		onSort(fieldName);
	}

	getOrder() {
		const {accessor, orderParams} = this.props;

		const fieldName = getFieldName(accessor);

		return orderParams.field === fieldName ? orderParams.sortOrder : null;
	}

	render() {
		const {
			accessor,
			children,
			className,
			headerLink,
			sortable = true
		} = this.props;

		const order = this.getOrder();

		const orderByField = getFieldName(accessor);

		return (
			<th className={getCN('table-head-title', className)}>
				{sortable ? (
					<Button
						className='inline-item text-truncate-inline'
						display='unstyled'
						href={
							headerLink
								? setUriQueryValues({
										orderBy: order
											? invertOrder(order)
											: getDefaultSortOrder(orderByField),
										orderByField,
										page: defaultPage
								  })
								: undefined
						}
						onClick={this.handleSort}
					>
						<span className='text-truncate'>{children}</span>

						{!isNull(order) && (
							<span className='inline-item inline-item-after'>
								<Icon
									symbol={
										order === 'desc'
											? 'order-arrow-down'
											: 'order-arrow-up'
									}
								/>
							</span>
						)}
					</Button>
				) : (
					children
				)}
			</th>
		);
	}
}

export default HeaderCell;
