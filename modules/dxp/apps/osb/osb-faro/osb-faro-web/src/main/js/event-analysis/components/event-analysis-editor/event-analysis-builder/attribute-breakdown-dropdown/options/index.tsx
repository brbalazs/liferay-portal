import Button from 'shared/components/Button';
import DateBreakdown from './DateBreakdown';
import DurationBreakdown from './DurationBreakdown';
import FilterInfo from '../../FilterInfo'; // TODO Rename to OptionInfo
import NumberBreakdown from './NumberBreakdown';
import React from 'react';
import {
	AddBreakdown,
	EditBreakdown,
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
	[DataTypes.Date]: DateBreakdown,
	[DataTypes.Duration]: DurationBreakdown,
	[DataTypes.Number]: NumberBreakdown
};

interface IBreakdownOptionsProps extends React.HTMLAttributes<HTMLDivElement> {
	addBreakdown: AddBreakdown;
	attribute: Attribute;
	attributeOwnerType: AttributeOwnerTypes;
	breakdowns: Breakdowns;
	editBreakdown: EditBreakdown;
	filters: Filters;
	oldAttributeId?: string;
	onActiveChange: (active: boolean) => void;
	onAttributeChange: (attribute?: Attribute) => void;
	onEditClick?: (id: string) => void;
}

const BreakdownOptions: React.FC<IBreakdownOptionsProps> = ({
	addBreakdown,
	attribute,
	attributeOwnerType,
	breakdowns,
	editBreakdown,
	filters,
	oldAttributeId,
	onActiveChange,
	onAttributeChange,
	onEditClick
}) => {
	const {dataType, displayName, id, name} = attribute;

	const BreakdownBody = FILTERS_MAP[dataType];

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

			<BreakdownBody
				attributeId={id}
				attributeOwnerType={attributeOwnerType}
				breakdown={breakdowns[id]}
				filter={filters[id]}
				onSubmit={breakdown => {
					if (oldAttributeId) {
						editBreakdown({
							attribute,
							attributeId: id,
							breakdown,
							oldAttributeId
						});
					} else {
						addBreakdown({
							attribute,
							attributeId: id,
							breakdown
						});
					}

					onAttributeChange(null);

					onActiveChange(false);
				}}
			/>
		</div>
	);
};

export default withAttributesConsumer(BreakdownOptions);
