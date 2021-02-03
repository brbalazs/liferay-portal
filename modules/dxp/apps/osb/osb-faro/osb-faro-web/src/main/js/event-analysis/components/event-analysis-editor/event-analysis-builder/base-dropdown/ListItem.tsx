import Button from 'shared/components/Button';
import ClayDropdown from '@clayui/drop-down';
import getCN from 'classnames';
import InfoCardPopover from '../InfoCardPopover';
import Overlay from 'shared/components/Overlay';
import React from 'react';
import {Attribute, Event} from '../../types';
import {isAttribute} from '../../utils';

interface IListItemProps {
	active?: boolean;
	disabled?: boolean;
	item: Attribute | Event;
	onClick: () => void;
	onFilterClick: (item: Attribute) => void;
}

const ListItem: React.FC<IListItemProps> = ({
	active,
	disabled,
	item,
	onClick,
	onFilterClick
}) => {
	const {description, displayName, id, name} = item;

	return (
		<Overlay alignment='rightCenter'>
			<ClayDropdown.Item
				className={getCN('d-flex justify-content-between', {
					active,
					disabled
				})}
				key={id}
			>
				<Button
					className='dropdown-item-primary-button'
					disabled={disabled}
					display='unstyled'
					onClick={onClick}
				>
					{displayName || name}
				</Button>

				{isAttribute(item as Attribute) && (
					<Button
						borderless
						className='filter-button'
						icon='filter'
						iconAlignment='left'
						onClick={() => onFilterClick(item as Attribute)}
						size='sm'
					/>
				)}
			</ClayDropdown.Item>

			<InfoCardPopover
				dataType={
					isAttribute(item as Attribute)
						? (item as Attribute).defaultDataType
						: null
				}
				description={description}
				name={displayName || name}
				onEditClick={() => {
					/** TODO: Open modal from settings page and reference the ID **/
				}}
			/>
		</Overlay>
	);
};

export default ListItem;
