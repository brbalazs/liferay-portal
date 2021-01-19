import Chip from 'shared/components/Chip';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React, {useEffect, useRef, useState} from 'react';
import {Breakdown, Filter} from '../types';
import {DropTargetMonitor, useDrag, useDrop} from 'react-dnd';

const HOVER_TYPES = {
	LEFT: 'left',
	RIGHT: 'right'
};

const ITEM_TYPES = {
	BREAKDOWN_CHIP: 'breakdown_chip'
};

interface DragItem {
	index: number;
	type: string;
}

const getFilterDisplay = filter => {
	switch (filter.dataType) {
		case 'number':
			return '';
	}
};

interface IBreakdownChipProps {
	breakdown?: Breakdown; // TODO: This probably shouldn't be optional
	filter?: Filter;
	index: number;
	onMove: (from: number, to: number) => void;
	onCloseClick: (attributeId: string) => void;
}
const BreakdownChip: React.FC<IBreakdownChipProps> = ({
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

				console.log('hoverLeft');
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

	const getHeader = () => {};

	const getMain = () => {
		if (filter) {
			return getFilterDisplay(filter);
		}

		return breakdown.name;
	};

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

				<div>
					<b>{breakdown.name}</b>
				</div>
			</Chip>
		</div>
	);
};

export default BreakdownChip;
