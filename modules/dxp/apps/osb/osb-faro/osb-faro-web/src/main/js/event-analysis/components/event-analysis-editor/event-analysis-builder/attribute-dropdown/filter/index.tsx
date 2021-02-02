import BooleanFilter from './BooleanFilter';
import Button from 'shared/components/Button';
import React from 'react';
import {
	Attribute,
	AttributeTypes,
	Breakdown,
	DataTypes,
	Filter
} from '../../../types';

const FILTERS_MAP = {
	[DataTypes.Boolean]: BooleanFilter
};

interface IBreakdownFilterProps extends React.HTMLAttributes<HTMLDivElement> {
	attribute: Attribute;
	onAttributeChange: (attribute?: Attribute) => void;
}

const BreakdownFilter: React.FC<IBreakdownFilterProps> = ({
	attribute,
	onAttributeChange
}) => {
	const FilterBody = FILTERS_MAP[attribute.defaultDataType];

	return (
		<div className='filter'>
			<div className='filter-header'>
				<Button
					display='unstyled'
					icon='angle-left'
					iconAlignment='left'
					onClick={() => onAttributeChange(null)}
				>
					{Liferay.Language.get('back-to-attributes')}
				</Button>
			</div>

			<div className='filter-body'>
				<FilterBody />
			</div>

			<div className='filter-footer'>
				<Button display='primary'>
					{Liferay.Language.get('done')}
				</Button>
			</div>
		</div>
	);
};

export default BreakdownFilter;
