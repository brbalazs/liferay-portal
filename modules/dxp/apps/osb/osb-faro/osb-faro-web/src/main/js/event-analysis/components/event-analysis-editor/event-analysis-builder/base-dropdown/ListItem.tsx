import Button from 'shared/components/Button';
import ClayDropdown from '@clayui/drop-down';
import getCN from 'classnames';
import InfoCardPopover from '../InfoCardPopover';
import Overlay from 'shared/components/Overlay';
import React, {useRef} from 'react';
import {Attribute, Event} from '../../types';
import {isAttribute} from '../../utils';

interface IListItemProps {
	active?: boolean;
	disabled?: boolean;
	item: Attribute | Event;
	onClick: () => void;
	onEditClick: () => void;
	onFilterClick: (item: Attribute) => void;
}

const ListItem: React.FC<IListItemProps> = ({
	active,
	disabled,
	item,
	onClick,
	onEditClick,
	onFilterClick
}) => {
	const _overlayRef = useRef<any>();

	const {description, displayName, id, name} = item;

	return (
		<Overlay
			alignment='rightCenter'
			hideDelay={200}
			ref={_overlayRef}
			showDelay={200}
			usePortal={false}
		>
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
					onClick={() => {
						if (_overlayRef && _overlayRef.current) {
							_overlayRef.current.hideOverlay();
						}

						onClick();
					}}
				>
					{displayName || name}
				</Button>

				{isAttribute(item as Attribute) && (
					<Button
						borderless
						className='filter-button'
						disabled={disabled}
						icon='filter'
						iconAlignment='left'
						onClick={() => {
							if (_overlayRef && _overlayRef.current) {
								_overlayRef.current.hideOverlay();
							}

							onFilterClick(item as Attribute);
						}}
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
					if (_overlayRef && _overlayRef.current) {
						_overlayRef.current.hideOverlay();
					}

					onEditClick();

					/** TODO: LRAC-7407 Open modal from settings page and reference the ID
					 *  Must check if event or attribute by using isAttribute util
					 **/
				}}
			/>
		</Overlay>
	);
};

export default ListItem;
