import BreakdownChip from './BreakdownChip';
import BreakdownDropdown from './breakdown-dropdown';
import Button from 'shared/components/Button';
import HTML5Backend from 'react-dnd-html5-backend';
import React from 'react';
import {Attribute, Breakdown, Filter} from '../types';
import {curry, flow} from 'lodash';
import {DndProvider} from 'react-dnd';
import {moveItem} from 'shared/util/array';

const MAX_BREAKDOWNS = 3;

interface IBreakdownSectionProps {
	attributes: Attribute[];
	breakdowns: Breakdown[];
	filters: Filter[];
	onAttributesChange: (attributes: Attribute[]) => void;
	onBreakdownsChange: (breakdowns: Breakdown[]) => void;
	onFiltersChange: (filters: Filter[]) => void;
}

const BreakdownSection: React.FC<IBreakdownSectionProps> = ({
	attributes,
	breakdowns,
	filters,
	onAttributesChange,
	onBreakdownsChange,
	onFiltersChange
}) => {
	const handleClose = attributeId => {
		onAttributesChange(
			attributes.filter(attribute => attributeId !== attribute.id)
		);

		onBreakdownsChange(
			breakdowns.filter(
				breakdown => attributeId !== breakdown.attributeId
			)
		);

		onFiltersChange(
			filters.filter(filter => attributeId !== filter.attributeId)
		);
	};

	const handleEditSubmit = (attributeId, breakdown, filter) => {
		onBreakdownsChange([
			...breakdowns.filter(
				breakdown => attributeId !== breakdown.attributeId
			),
			breakdown
		]);

		onFiltersChange([
			...filters.filter(filter => attributeId !== filter.attributeId),
			filter
		]);
	};

	return (
		<div className='breakdown-section-root flex-grow-1'>
			<div className='section-header'>
				{Liferay.Language.get('breakdown-by')}
			</div>

			<div className='breakdown-container d-flex align-items-center'>
				<DndProvider backend={HTML5Backend}>
					<div className='breakdown-list d-flex align-items-center'>
						{attributes.map((attribute, i) => (
							<BreakdownChip
								attribute={attribute}
								breakdown={breakdowns.find(
									({attributeId}) =>
										attributeId === attribute.id
								)}
								filter={filters.find(
									({attributeId}) =>
										attributeId === attribute.id
								)}
								index={i}
								key={attribute.id}
								onCloseClick={handleClose}
								onEditSubmit={handleEditSubmit}
								onMove={flow(
									curry(moveItem)(attributes),
									onAttributesChange
								)}
							/>
						))}
					</div>
				</DndProvider>

				<div>
					{filters.length < MAX_BREAKDOWNS &&
					<BreakdownDropdown
						attributes={attributes}
						breakdowns={breakdowns}
						filters={filters}
						onBreakdownChange={() => {}}
						trigger={
							<Button
								borderless
								className='add-breakdown'
								display='light'
								icon='plus'
								iconAlignment='left'
								size='sm'
							/>}
					/>}
				</div>
			</div>
		</div>
	);
};

export default BreakdownSection;
