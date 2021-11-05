import HeaderCell from './HeaderCell';
import React from 'react';

interface IHeaderRowProps {
	className?: string;
	showCheckbox?: boolean;
	showInlineRowActions?: boolean;
}

const HeaderRow: React.FC<IHeaderRowProps> = ({
	className,
	columns,
	headerLink,
	onSort,
	orderParams,
	showCheckbox,
	showInlineRowActions
}) => (
	<thead>
		<tr className={className}>
			{showCheckbox && <th />}

			{columns.map((column, i) => {
				const {
					accessor,
					className,
					headProps = {},
					label,
					sortable
				} = column;

				return (
					<HeaderCell
						accessor={accessor}
						className={className}
						headerLink={headerLink}
						key={`${label}-${i}`}
						onSort={onSort}
						orderParams={orderParams}
						sortable={sortable}
						{...headProps}
					>
						{label}
					</HeaderCell>
				);
			})}

			{showInlineRowActions && <th />}
		</tr>
	</thead>
);

export default HeaderRow;
