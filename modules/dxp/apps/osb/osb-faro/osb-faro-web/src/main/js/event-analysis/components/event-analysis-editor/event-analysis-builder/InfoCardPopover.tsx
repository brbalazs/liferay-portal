import Button from 'shared/components/Button';
import Label from 'shared/components/Label';
import React from 'react';
import {DataTypes} from '../types';

interface IInfoCardPopoverProps {
	dataType: DataTypes;
	description: string;
	name: string;
	onEditClick: (id: string) => void;
}

const InfoCardPopover: React.FC<IInfoCardPopoverProps> = ({
	dataType,
	description,
	name,
	onEditClick
}) => (
	<div className='info-card-popover-root'>
		<div className='info-card-popover-header d-flex align-items-center justify-content-between'>
			{name}

			<Button
				borderless
				icon='pencil'
				iconAlignment='left'
				onClick={onEditClick}
				size='sm'
			/>
		</div>

		<div className='description'>
			{description || Liferay.Language.get('no-description')}
		</div>

		{dataType && (
			<Label display='info' uppercase>
				{dataType}
			</Label>
		)}
	</div>
);

export default InfoCardPopover;
