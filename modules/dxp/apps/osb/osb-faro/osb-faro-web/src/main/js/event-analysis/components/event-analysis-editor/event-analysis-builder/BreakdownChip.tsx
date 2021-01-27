import Chip from 'shared/components/Chip';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React, {useEffect, useRef, useState} from 'react';
import {Attribute, Breakdown, DataTypes, Filter} from '../types';
import {DropTargetMonitor, useDrag, useDrop} from 'react-dnd';
import {getBreakdownDisplay} from '../utils';

const HOVER_TYPES = {
	LEFT: 'left',
	RIGHT: 'right'
};

const ITEM_TYPES = {
	BREAKDOWN_CHIP: 'breakdown_chip'
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

interface IBreakdownChipProps {
	attribute: Attribute;
	breakdown: Breakdown;
	filter?: Filter;
	index: number;
	onCloseClick: (attributeId: string) => void;
	onEditSubmit: (
		attributeId: string,
		breakdown: Breakdown,
		filter: Filter
	) => void;
	onMove: (from: number, to: number) => void;
}
const BreakdownChip: React.FC<IBreakdownChipProps> = ({
	attribute,
	breakdown,
	filter,
	index,
	onCloseClick,
	onMove
}) => {
	const _chipRef = useRef<HTMLDivElement>();
	const _wrapperRef = useRef<HTMLDivElement>();

	const [hoverPosition, setHoverPosition] = useState(null);

	const [{canDrop, isOver}, drop] = useDrop({
		accept: ITEM_TYPES.BREAKDOWN_CHIP,
		canDrop: (item: DragItem) => {
			const {index: dragIndex} = item;

			const dropIndex = index;

			return dragIndex !== dropIndex;
		},
		collect: (monitor: DropTargetMonitor) => ({
			canDrop: monitor.canDrop(),
			isOver: monitor.isOver()
		}),
		drop: (item: DragItem) => {
			const {index: dragIndex} = item;

			let dropIndex = index;

			const insertLeft =
				hoverPosition === HOVER_TYPES.LEFT && dragIndex < dropIndex;

			const insertRight =
				hoverPosition === HOVER_TYPES.RIGHT && dragIndex > dropIndex;

			if (insertLeft) {
				dropIndex = index - 1;
			} else if (insertRight) {
				dropIndex = index + 1;
			}

			onMove(dragIndex, dropIndex);
		},
		hover: (item: DragItem, monitor: DropTargetMonitor) => {
			const {index: dragIndex} = item;

			const hoverIndex = index;

			if (_wrapperRef.current) {
				const {
					right,
					width
				} = _wrapperRef.current.getBoundingClientRect();

				const targetMiddleX = width / 2;

				const {x} = monitor.getClientOffset();

				const hoverLeft = x < right - targetMiddleX;

				const destIndex = hoverLeft ? hoverIndex - 1 : hoverIndex + 1;

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
			type: ITEM_TYPES.BREAKDOWN_CHIP
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

	// TODO: LRAC-7247 Add onClick to BreakdownChip to edit current values

	return (
		<div
			className={getCN('breakdown-chip-wrapper', {
				[`hover-${hoverPosition}`]: isOver && canDrop && hoverPosition
			})}
			ref={_wrapperRef}
		>
			<Chip
				className={getCN('breakdown-chip-root', {
					dragging: isDragging
				})}
				onCloseClick={() => onCloseClick(breakdown.attributeId)}
				ref={_chipRef}
			>
				<div className='drag-handle' ref={drag}>
					<Icon symbol='drag' />
				</div>

				<div className='sticker'>
					<Icon symbol={TYPE_ICON_MAP[breakdown.dataType]} />
				</div>

				<div>
					<div className='attribute-label'>{attributeLabel}</div>

					<div className='attribute-value'>{attributeValue}</div>
				</div>
			</Chip>
		</div>
	);
};

export default BreakdownChip;
