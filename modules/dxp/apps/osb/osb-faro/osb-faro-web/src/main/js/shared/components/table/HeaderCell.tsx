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
import {setUriQueryValues} from 'shared/util/router';

interface IHeaderCellProps {
	accessor: string;
	children?: any;
	className: string;
	headerLink?: boolean;
	onSort: (fieldName: string) => void;
	orderParams?: {OrderParams};
	sortable?: boolean;
}

const HeaderCell: React.FC<IHeaderCellProps> = ({
	accessor,
	children,
	className,
	headerLink = false,
	onSort,
	orderParams = new OrderParams(),
	sortable = true
}) => {
	const {cur: defaultPage} = faroConstants.pagination;

	const fieldName = get(ACCESSOR_TO_FIELD_MAP, [accessor], accessor);

	const getOrder = () =>
		orderParams.field === fieldName ? orderParams.sortOrder : null;

	const handleSort = () => onSort(fieldName);

	const order = getOrder();

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
										: getDefaultSortOrder(fieldName),
									orderByField: fieldName,
									page: defaultPage
							  })
							: undefined
					}
					onClick={handleSort}
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
};

export default HeaderCell;
