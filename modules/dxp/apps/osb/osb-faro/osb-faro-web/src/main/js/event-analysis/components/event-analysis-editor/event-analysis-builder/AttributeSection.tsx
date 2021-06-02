import AttributeChip from './AttributeChip';
import AttributeDropdown from './attribute-dropdown';
import Button from 'shared/components/Button';
import HTML5Backend from 'react-dnd-html5-backend';
import React from 'react';
import {
	AddAttribute,
	DeleteAttribute,
	EditAttribute,
	MoveAttribute,
	withAttributesConsumer
} from '../context/attributes';
import {Attributes, Breakdowns, Filters} from 'event-analysis/utils/types';
import {DndProvider} from 'react-dnd';

const MAX_ATTRIBUTES = 3;

interface IAttributeSectionProps {
	addAttribute: AddAttribute;
	attributes: Attributes;
	breakdowns: Breakdowns;
	deleteAttribute: DeleteAttribute;
	editAttribute: EditAttribute;
	eventId: string;
	filters: Filters;
	moveAttribute: MoveAttribute;
	order: string[];
}

const AttributeSection: React.FC<IAttributeSectionProps> = ({
	addAttribute,
	attributes,
	breakdowns,
	deleteAttribute,
	editAttribute,
	eventId,
	filters,
	moveAttribute,
	order
}) => (
	<div className='attribute-section-root flex-grow-1'>
		<div className='section-header'>
			{Liferay.Language.get('breakdown-by')}
		</div>

		<div className='attribute-container d-flex align-items-center'>
			<DndProvider backend={HTML5Backend}>
				<div className='attribute-list d-flex align-items-center'>
					{order.map((id, i) => (
						<AttributeChip
							attribute={attributes[id]}
							breakdown={breakdowns[id]}
							eventId={eventId}
							filter={filters[id]}
							index={i}
							key={id}
							onCloseClick={deleteAttribute}
							onEditSubmit={({
								attribute,
								attributeId,
								breakdown,
								filter
							}) =>
								editAttribute({
									attribute,
									attributeId,
									breakdown,
									filter,
									oldAttributeId: id
								})
							}
							onMove={moveAttribute}
							order={order}
						/>
					))}
				</div>
			</DndProvider>

			<div>
				{order.length < MAX_ATTRIBUTES && (
					<AttributeDropdown
						disabledIds={order}
						eventId={eventId}
						onAttributeSelect={addAttribute}
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
