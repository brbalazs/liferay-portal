import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import {ConnectDragSource, DragSource as dragSource} from 'react-dnd';
import {Criterion} from '../utils/types';
import {DragTypes} from '../utils/drag-types';
import {generateRowId} from '../utils/utils';
import {Property} from 'shared/util/records';
import {PROPERTY_TYPES} from '../utils/constants';

const TYPE_ICON_MAP = {
	[PROPERTY_TYPES.BEHAVIOR]: 'web-content',
	[PROPERTY_TYPES.BOOLEAN]: 'check',
	[PROPERTY_TYPES.ACCOUNT_NUMBER]: 'integer',
	[PROPERTY_TYPES.ACCOUNT_TEXT]: 'text',
	[PROPERTY_TYPES.DATE]: 'date',
	[PROPERTY_TYPES.DATE_TIME]: 'date',
	[PROPERTY_TYPES.DURATION]: 'time',
	[PROPERTY_TYPES.NUMBER]: 'integer',
	[PROPERTY_TYPES.ORGANIZATION_BOOLEAN]: 'check',
	[PROPERTY_TYPES.ORGANIZATION_DATE]: 'date',
	[PROPERTY_TYPES.ORGANIZATION_DATE_TIME]: 'date',
	[PROPERTY_TYPES.ORGANIZATION_NUMBER]: 'integer',
	[PROPERTY_TYPES.ORGANIZATION_SELECT_TEXT]: 'text',
	[PROPERTY_TYPES.ORGANIZATION_TEXT]: 'text',
	[PROPERTY_TYPES.SESSION_DATE_TIME]: 'date',
	[PROPERTY_TYPES.SESSION_NUMBER]: 'integer',
	[PROPERTY_TYPES.SESSION_TEXT]: 'text',
	[PROPERTY_TYPES.INTEREST]: 'check',
	[PROPERTY_TYPES.TEXT]: 'text'
};

/**
 * Passes the required values to the drop target.
 * This method must be called `beginDrag`.
 * @param {Object} props Component's current props
 * @returns {Object} The props to be passed to the drop target.
 */
const beginDrag = ({
	defaultValue,
	name,
	property,
	type
}: {
	defaultValue: any;
	name: string;
	property: Property;
	type: string;
}): {
	criterion: Criterion;
	property: Property;
} => {
	let touched: boolean | object = false;
	let valid: boolean | object = true;

	if (type === PROPERTY_TYPES.BEHAVIOR) {
		touched = {asset: false, dateFilter: false, occurenceCount: false};
		valid = {asset: false, dateFilter: true, occurenceCount: true};
	} else if (type === PROPERTY_TYPES.SESSION_GEOLOCATION) {
		touched = {country: false, dateFilter: false};
		valid = {country: false, dateFilter: true};
	} else if (
		[PROPERTY_TYPES.SESSION_NUMBER, PROPERTY_TYPES.SESSION_TEXT].includes(
			type
		)
	) {
		touched = {customInput: false, dateFilter: false};
		valid = {customInput: false, dateFilter: true};
	} else if (
		[
			PROPERTY_TYPES.ACCOUNT_NUMBER,
			PROPERTY_TYPES.ACCOUNT_TEXT,
			PROPERTY_TYPES.DURATION,
			PROPERTY_TYPES.NUMBER,
			PROPERTY_TYPES.ORGANIZATION_NUMBER,
			PROPERTY_TYPES.ORGANIZATION_SELECT_TEXT,
			PROPERTY_TYPES.ORGANIZATION_TEXT,
			PROPERTY_TYPES.SELECT_TEXT,
			PROPERTY_TYPES.TEXT
		].includes(type)
	) {
		valid = false;
	}

	return {
		criterion: {
			defaultValue,
			propertyName: name,
			rowId: generateRowId(),
			touched,
			type,
			valid
		},
		property
	};
};

interface ICriteriaSidebarItemProps {
	className: string;
	connectDragSource: ConnectDragSource;
	defaultValue: any;
	dragging: boolean;
	label: string;
	name: string;
	property: Property;
	propertyKey: string;
	type: string;
}

export class CriteriaSidebarItem extends React.Component<
	ICriteriaSidebarItemProps
> {
	render() {
		const {
			className,
			connectDragSource,
			dragging,
			label,
			type
		} = this.props;

		const classes = getCN(
			'criteria-sidebar-item-root',
			{dragging},
			className
		);

		return connectDragSource(
			<li className={classes}>
				<span className='inline-item'>
					<Icon symbol='drag' />
				</span>

				<span className='criteria-sidebar-item-type sticker'>
					<span className='inline-item'>
						<Icon symbol={TYPE_ICON_MAP[type] || 'text'} />
					</span>
				</span>

				{label}
			</li>
		);
	}
}

export default dragSource(
	DragTypes.Property,
	{
		beginDrag
	},
	(connect, monitor) => ({
		connectDragSource: connect.dragSource(),
		dragging: monitor.isDragging()
	})
)(CriteriaSidebarItem);
