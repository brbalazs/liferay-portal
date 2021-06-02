import AttributeDropdown from './attribute-dropdown';
import Button from 'shared/components/Button';
import Chip from 'shared/components/Chip';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React, {useEffect, useRef, useState} from 'react';
import {
	Attribute,
	Breakdown,
	DataTypes,
	Filter
} from 'event-analysis/utils/types';
import {DeleteAttribute, EditAttribute} from '../context/attributes';
import {DropTargetMonitor, useDrag, useDrop} from 'react-dnd';
import {getBreakdownDisplay} from 'event-analysis/utils/utils';
import {mergeRef} from 'shared/util/util';

const HOVER_TYPES = {
	LEFT: 'left',
	RIGHT: 'right'
};

const ITEM_TYPES = {
	ATTRIBUTE_CHIP: 'attribute_chip'
};

const TYPE_ICON_MAP = {
	[DataTypes.Boolean]: 'check',
	[DataTypes.Date]: 'date',
	[DataTypes.Duration]: 'time',
	[DataTypes.Number]: 'integer',
	[DataTypes.String]: 'text'
};

interface DragItem {
	index: number;
	type: string;
}

interface IAttributeChipProps {
	attribute: Attribute;
	breakdown: Breakdown;
	filter?: Filter;
	index: number;
	onCloseClick: DeleteAttribute;
	onMove: (params: {from: number; to: number}) => void;
}

const AttributeChip: React.FC<IAttributeChipProps> = React.forwardRef<
	any,
	IAttributeChipProps & {onClick?: () => void}
>(
	(
		{attribute, breakdown, filter, index, onClick, onCloseClick, onMove},
		ref
	) => {
		const _chipRef = useRef<HTMLDivElement>();
		const _wrapperRef = useRef<HTMLDivElement>();

		const [hoverPosition, setHoverPosition] = useState(null);

		const [{canDrop, isOver}, drop] = useDrop({
			accept: ITEM_TYPES.ATTRIBUTE_CHIP,
			canDrop: ({index: dragIndex}: DragItem) => {
				const dropIndex = index;

				return dragIndex !== dropIndex;
			},
			collect: (monitor: DropTargetMonitor) => ({
				canDrop: monitor.canDrop(),
				isOver: monitor.isOver()
			}),
			drop: ({index: dragIndex}: DragItem) => {
				let dropIndex = index;

				const insertLeft =
					hoverPosition === HOVER_TYPES.LEFT && dragIndex < dropIndex;

				const insertRight =
					hoverPosition === HOVER_TYPES.RIGHT &&
					dragIndex > dropIndex;

				if (insertLeft) {
					dropIndex = index - 1;
				} else if (insertRight) {
					dropIndex = index + 1;
				}

				onMove({from: dragIndex, to: dropIndex});
			},
			hover: (
				{index: dragIndex}: DragItem,
				monitor: DropTargetMonitor
			) => {
				const hoverIndex = index;

				// Determine whether hover is on left or right side of hovered AttributeChip
				if (_wrapperRef.current) {
					const {
						right,
						width
					} = _wrapperRef.current.getBoundingClientRect();

					const targetMiddleX = width / 2;

					const {x} = monitor.getClientOffset();

					const hoverLeft = x < right - targetMiddleX;

					const destIndex = hoverLeft
						? hoverIndex - 1
						: hoverIndex + 1;

					if (destIndex === dragIndex) {
						setHoverPosition(null);
					} else if (hoverLeft) {
						setHoverPosition(HOVER_TYPES.LEFT);
					} else {
						setHoverPosition(HOVER_TYPES.RIGHT);
					}
				}
			}
		});

		const [{isDragging}, drag, preview] = useDrag({
			collect: (monitor: any) => ({
				isDragging: monitor.isDragging()
			}),
			item: {
				index,
				type: ITEM_TYPES.ATTRIBUTE_CHIP
			}
		});

		useEffect(() => {
			drop(_wrapperRef);
			preview(_chipRef);
		}, []);

		const [attributeLabel, attributeValue] = getBreakdownDisplay(
			attribute,
			breakdown,
			filter
		);

		return (
			<div
				className={getCN('attribute-chip-container', {
					[`hover-${hoverPosition}`]:
						isOver && canDrop && hoverPosition
				})}
				ref={_wrapperRef}
			>
				<Chip
					className={getCN('attribute-chip-root', {
						dragging: isDragging
					})}
					onCloseClick={() =>
						onCloseClick({attributeId: attribute.id})
					}
					ref={mergeRef(ref, _chipRef)}
				>
					<div className='drag-handle' ref={drag}>
						<Icon symbol='drag' />
					</div>

					<Button
						className='edit-attribute-button d-flex'
						display='unstyled'
						onClick={onClick}
					>
						<div className='sticker'>
							<Icon symbol={TYPE_ICON_MAP[breakdown.dataType]} />
						</div>

						<div>
							<div className='attribute-label'>
								{attributeLabel}
							</div>

							<div className='attribute-value'>
								{attributeValue}
							</div>
						</div>
					</Button>
				</Chip>
			</div>
		);
	}
);

const AttributeChipWrapper: React.FC<
	IAttributeChipProps & {
		eventId: string;
		onEditSubmit: EditAttribute;
		order: string[];
	}
> = ({attribute, eventId, filter, onEditSubmit, order, ...otherProps}) => (
	<AttributeDropdown
		attribute={attribute}
		disabledIds={order}
		eventId={eventId}
		filter={filter}
		onAttributeSelect={onEditSubmit}
		trigger={
			<AttributeChip
				attribute={attribute}
				filter={filter}
				{...otherProps}
			/>
		}
	/>
);

export default AttributeChipWrapper;
