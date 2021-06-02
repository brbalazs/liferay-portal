import BooleanFilter from './BooleanFilter';
import Button from 'shared/components/Button';
import DateFilter from './DateFilter';
import DurationFilter from './DurationFilter';
import FilterInfo from '../../FilterInfo';
import NumberFilter from './NumberFilter';
import React from 'react';
import StringFilter from './StringFilter';
import {
	AddAttribute,
	EditAttribute,
	withAttributesConsumer
} from '../../../context/attributes';
import {
	Attribute,
	AttributeOwnerTypes,
	Breakdowns,
	DataTypes,
	Filters
} from 'event-analysis/utils/types';

const FILTERS_MAP = {
	[DataTypes.Boolean]: BooleanFilter,
	[DataTypes.Date]: DateFilter,
	[DataTypes.Duration]: DurationFilter,
	[DataTypes.Number]: NumberFilter,
	[DataTypes.String]: StringFilter
};

interface IAttributeFilterProps extends React.HTMLAttributes<HTMLDivElement> {
	addAttribute: AddAttribute;
	attribute: Attribute;
	attributeOwnerType: AttributeOwnerTypes;
	breakdowns: Breakdowns;
	editAttribute: EditAttribute;
	filters: Filters;
	oldAttributeId?: string;
	onActiveChange: (active: boolean) => void;
	onAttributeChange: (attribute?: Attribute) => void;
	onEditClick?: (id: string) => void;
}

const AttributeFilter: React.FC<IAttributeFilterProps> = ({
	addAttribute,
	attribute,
	attributeOwnerType,
	breakdowns,
	editAttribute,
	filters,
	oldAttributeId,
	onActiveChange,
	onAttributeChange,
	onEditClick
}) => {
	const {dataType, displayName, id, name} = attribute;

	const FilterBody = FILTERS_MAP[dataType];

	return (
		<div className='attribute-filter'>
			<div className='filter-header'>
				<Button
					className='back-to-attributes-button'
					display='unstyled'
					icon='angle-left'
					iconAlignment='left'
					onClick={() => onAttributeChange(null)}
					size='sm'
				>
					{Liferay.Language.get('back-to-attributes')}
				</Button>

				<FilterInfo
					dataType={dataType}
					name={displayName || name}
					onEditClick={onEditClick}
				/>
			</div>

			<FilterBody
				attributeId={id}
				attributeOwnerType={attributeOwnerType}
				breakdown={breakdowns[id]}
				filter={filters[id]}
				onFilterSubmit={({breakdown, filter}) => {
					if (oldAttributeId) {
						editAttribute({
							attribute,
							attributeId: id,
							breakdown,
							filter,
							oldAttributeId
						});
					} else {
						addAttribute({
							attribute,
							attributeId: id,
							breakdown,
							filter
						});
					}

					onAttributeChange(null);

					onActiveChange(false);
				}}
			/>
		</div>
	);
};

export default withAttributesConsumer(AttributeFilter);
