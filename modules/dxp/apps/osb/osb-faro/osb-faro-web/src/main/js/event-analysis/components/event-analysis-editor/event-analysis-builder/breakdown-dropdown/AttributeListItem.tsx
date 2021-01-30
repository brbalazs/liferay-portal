import Button from 'shared/components/Button';
import ClayDropdown from '@clayui/drop-down';
import InfoCardPopover from '../InfoCardPopover';
import Overlay, {Align} from 'shared/components/Overlay';
import React from 'react';
import {Attribute} from '../../types';

interface IAttributeListItemProps {
	attribute: Attribute;
	onSelectedAttributeChange: (selectedAttribute: Attribute) => void;
}

const AttributeListItem: React.FC<IAttributeListItemProps> = ({
	attribute,
	onSelectedAttributeChange
}) => {
	const {defaultDataType, description, displayName, id, name} = attribute;

	return (
		<Overlay alignment={Align.RightCenter}>
			<ClayDropdown.Item
				className='d-flex justify-content-between'
				key={id}
			>
				<Button
					className='dropdown-item-primary-button'
					display='unstyled'
					onClick={() => {}}
				>
					{displayName || name}
				</Button>

				<Button
					borderless
					display='unstyled'
					icon='filter'
					iconAlignment='left'
					onClick={() => onSelectedAttributeChange(attribute)}
					size='sm'
				/>
			</ClayDropdown.Item>

			<InfoCardPopover
				dataType={defaultDataType}
				description={description}
				name={displayName || name}
				onEditClick={() => {
					/** TODO: Open modal from settings page and reference the ID **/
				}}
			/>
		</Overlay>
	);
};

export default AttributeListItem;
