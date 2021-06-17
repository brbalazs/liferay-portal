import AttributeFilterChip from './AttributeFilterChip';
import AttributeFilterDropdown from './attribute-filter-dropdown';
import Button from 'shared/components/Button';
import HTML5Backend from 'react-dnd-html5-backend';
import React from 'react';
import {
	AddFilter,
	DeleteFilter,
	EditFilter,
	MoveFilter,
	withAttributesConsumer
} from '../context/attributes';
import {Attributes, Breakdowns, Filters} from 'event-analysis/utils/types';
import {DndProvider} from 'react-dnd';

const MAX_ATTRIBUTES = 3;

interface IAttributeSectionProps {
	addFilter: AddFilter;
	attributes: Attributes;
	breakdownOrder: string[];
	breakdowns: Breakdowns;
	deleteFilter: DeleteFilter;
	editFilter: EditFilter;
	eventId: string;
	filterOrder: string[];
	filters: Filters;
	moveFilter: MoveFilter;
}

const AttributeSection: React.FC<IAttributeSectionProps> = ({
	addFilter,
	attributes,
	deleteFilter,
	editFilter,
	eventId,
	filterOrder,
	filters,
	moveFilter
}) => (
	<div className='attribute-section-root flex-grow-1'>
		<div className='section-header'>
			{Liferay.Language.get('filter-by')}
		</div>

		<div className='attribute-container d-flex align-items-center'>
			<DndProvider backend={HTML5Backend}>
				<div className='attribute-list d-flex align-items-center'>
					{filterOrder.map((id, i) => (
						<AttributeFilterChip
							attribute={attributes[id]}
							eventId={eventId}
							filter={filters[id]}
							index={i}
							key={id}
							onCloseClick={deleteFilter}
							onEditSubmit={({attribute, attributeId, filter}) =>
								editFilter({
									attribute,
									attributeId,
									filter,
									oldAttributeId: id
								})
							}
							onMove={moveFilter}
							order={filterOrder}
						/>
					))}
				</div>
			</DndProvider>

			<div>
				{filterOrder.length < MAX_ATTRIBUTES && (
					<AttributeFilterDropdown
						disabledIds={filterOrder}
						eventId={eventId}
						onAttributeSelect={addFilter}
						trigger={
							<Button
								borderless
								className='add-attribute'
								display='light'
								icon='plus'
								iconAlignment='left'
								size='sm'
							/>
						}
					/>
				)}
			</div>
		</div>
	</div>
);

export default withAttributesConsumer(AttributeSection);
