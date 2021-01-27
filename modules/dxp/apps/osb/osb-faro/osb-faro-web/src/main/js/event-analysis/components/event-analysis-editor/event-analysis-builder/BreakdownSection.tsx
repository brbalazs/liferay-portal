import BreakdownChip from './BreakdownChip';
import Button from 'shared/components/Button';
import HTML5Backend from 'react-dnd-html5-backend';
import React from 'react';
import {Attribute, Breakdown, Filter} from '../types';
import {curry, flow} from 'lodash';
import {DndProvider} from 'react-dnd';
import {moveItem} from 'shared/util/array';

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
		<div className='breakdown-section-root'>
			<div className='section-header'>
				{Liferay.Language.get('breakdown-by')}
			</div>

			<div className='d-flex'>
				<DndProvider backend={HTML5Backend}>
					<div className='breakdown-list d-flex'>
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

				{filters.length < 3 && (
					<Button display='light' icon='plus' iconAlignment='left' />
				)}
			</div>
		</div>
	);
};

export default BreakdownSection;
