import Button from 'shared/components/Button';
import Label from 'shared/components/Label';
import React from 'react';
import {DataTypes} from '../types';

interface IFilterInfoProps {
	dataType: DataTypes;
	description?: string;
	name: string;
	onEditClick: (id: string) => void;
	showDescription?: boolean;
}

const FilterInfo: React.FC<IFilterInfoProps> = ({
	dataType,
	description,
	name,
	onEditClick,
	showDescription
}) => (
	<div className='filter-info-root'>
		<div className='filter-name d-flex align-items-center justify-content-between'>
			{name}

			<Button
				borderless
				icon='pencil'
				iconAlignment='left'
				onClick={onEditClick}
				size='sm'
			/>
		</div>

		{showDescription && (
			<div className='description'>
				{description || Liferay.Language.get('no-description')}
			</div>
		)}

		{dataType && (
			<Label display='info' uppercase>
				{dataType}
			</Label>
		)}
	</div>
);

export default FilterInfo;
