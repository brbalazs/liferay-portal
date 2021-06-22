import AttributeBreakdownChip from './AttributeBreakdownChip';
import AttributeBreakdownDropdown from './attribute-breakdown-dropdown';
import Button from 'shared/components/Button';
import HTML5Backend from 'react-dnd-html5-backend';
import React from 'react';
import {
	AddBreakdown,
	DeleteBreakdown,
	EditBreakdown,
	MoveBreakdown,
	withAttributesConsumer
} from '../context/attributes';
import {Align} from '@clayui/drop-down';
import {Attributes, Breakdowns, Filters} from 'event-analysis/utils/types';
import {DndProvider} from 'react-dnd';

const MAX_ATTRIBUTES = 3;

interface IAttributeBreakdownSectionProps {
	addBreakdown: AddBreakdown;
	attributes: Attributes;
	breakdownOrder: string[];
	breakdowns: Breakdowns;
	deleteBreakdown: DeleteBreakdown;
	editBreakdown: EditBreakdown;
	eventId: string;
	filters: Filters;
	moveBreakdown: MoveBreakdown;
}

export const AttributeBreakdownSection: React.FC<IAttributeBreakdownSectionProps> = ({
	addBreakdown,
	attributes,
	breakdownOrder,
	breakdowns,
	deleteBreakdown,
	editBreakdown,
	eventId,
	moveBreakdown
}) => (
	<div className='attribute-breakdown-section-root d-flex align-items-center'>
		<div className='section-header'>
			{Liferay.Language.get('breakdown')}
		</div>

		{!!eventId && (
			<div className='attribute-container d-flex align-items-center justify-content-between'>
				<DndProvider backend={HTML5Backend}>
					<div className='attribute-list d-flex align-items-center'>
						{breakdownOrder.map((id, i) => (
							<AttributeBreakdownChip
								attribute={attributes[id]}
								breakdown={breakdowns[id]}
								eventId={eventId}
								index={i}
								key={id}
								onCloseClick={deleteBreakdown}
								onEditSubmit={({
									attribute,
									attributeId,
									breakdown
								}) =>
									editBreakdown({
										attribute,
										attributeId,
										breakdown,
										oldAttributeId: id
									})
								}
								onMove={moveBreakdown}
								order={breakdownOrder}
							/>
						))}
					</div>
				</DndProvider>

				<div>
					{breakdownOrder.length < MAX_ATTRIBUTES && (
						<AttributeBreakdownDropdown
							alignmentPosition={Align.LeftTop}
							disabledIds={breakdownOrder}
							eventId={eventId}
							onAttributeSelect={addBreakdown}
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
		)}
	</div>
);

export default withAttributesConsumer(AttributeBreakdownSection);
