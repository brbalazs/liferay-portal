import getCN from 'classnames';
import Icon from './Icon';
import React, {FC, useState} from 'react';
import Spinner from './Spinner';
import Sticker from './Sticker';
import TextTruncate from './TextTruncate';
import {formatDateToTimeZone} from 'shared/util/date';
import {Link} from 'react-router-dom';
import {UserSessionAttributes} from 'shared/util/activities';

const DEVICE_ICONS_MAP = {
	desktop: {symbol: 'ac-display', title: Liferay.Language.get('desktop')},
	mobile: {symbol: 'mobile-portrait', title: Liferay.Language.get('mobile')},
	tablet: {
		className: 'tablet-icon',
		symbol: 'tablet-portrait',
		title: Liferay.Language.get('tablet')
	}
};

const ATTRIBUTE_CLASSES_MAP = {
	title: 'attribute-important'
};

type TITLE_ELEMENT_ATTRIBUTES = {
	key: string;
	props: {
		children: string;
	};
	type: string;
	ref: string;
	_owner: string;
	_store: {};
};

type ITEM_SHAPE = {
	attributes: UserSessionAttributes;
	browserName: string;
	description: string;
	device: string;
	endTime: number;
	header: boolean;
	nestedItems: ITEM_SHAPE[];
	subtitle: string | TITLE_ELEMENT_ATTRIBUTES[];
	time: string;
	title: string | TITLE_ELEMENT_ATTRIBUTES[];
	totalEvents: number;
	url: string;
};

type ITimelineItemProps = {
	channelId?: string;
	className?: string;
	groupId?: string;
	initialExpanded?: boolean;
	item: ITEM_SHAPE;
	timeZoneId: string;
};

const TimelineItem: FC<ITimelineItemProps> = ({
	className,
	initialExpanded = false,
	item: {
		attributes,
		browserName,
		description,
		device,
		endTime,
		header,
		nestedItems,
		subtitle,
		time,
		title,
		totalEvents,
		url
	},
	timeZoneId
}) => {
	const [expanded, setExpanded] = useState(initialExpanded);

	const timeRange = !nestedItems ? (
		formatDateToTimeZone(time, 'h:mma', timeZoneId)
	) : (
		<>
			<span>{formatDateToTimeZone(time, 'h:mma', timeZoneId)}</span>
			{' - '}
			<span>
				{endTime
					? formatDateToTimeZone(endTime, 'h:mma', timeZoneId)
					: Liferay.Language.get('in-progress').toLowerCase()}
			</span>
		</>
	);

	const expandable = !!attributes;

	const toggleExpand = () => {
		if (expandable) {
			setExpanded(!expanded);
		}
	};

	const bodyClasses = getCN('timeline-panel-body-content', {
		selectable: expandable
	});

	const bodyAttributes = expandable
		? {
				onClick: toggleExpand,
				onKeyPress: toggleExpand,
				role: 'button',
				tabIndex: 0
		  }
		: {};

	const {header: attributesTitle, ...otherValues} = attributes || {};
	const {title: deviceIconTitle, ...otherIconAttributes} =
		expandable && !!nestedItems && DEVICE_ICONS_MAP[device.toLowerCase()];

	const eventTitle =
		title && !header ? <TextTruncate title={`${title}`} /> : title;

	return (
		<li
			className={getCN('timeline-item', className, {
				expanded,
				header
			})}
		>
			<div className='timeline-panel'>
				<div className='timeline-panel-body'>
					{!header && (
						<div className='timeline-increment'>
							<Sticker circle display='point' size='lg' />

							{time && (
								<div className='timeline-item-label timeline-time-label label-root'>
									{timeRange}
								</div>
							)}
						</div>
					)}

					<div className={bodyClasses} {...bodyAttributes}>
						<div
							className={getCN(
								'timeline-panel-body-content-text',
								{header: !title}
							)}
						>
							{url ? (
								<span className='text-truncate'>
									<Link className='title' to={url}>
										{eventTitle}
									</Link>
								</span>
							) : (
								<span className='title'>{eventTitle}</span>
							)}

							{header && (
								<>
									<Icon
										className='event-icon'
										symbol='ac-event-icon'
									/>

									<span className='item-count'>
										{totalEvents}
									</span>
								</>
							)}

							{description && (
								<span className='description'>
									{description}
								</span>
							)}

							{subtitle && (
								<TextTruncate
									className='subtitle'
									title={subtitle}
								/>
							)}
						</div>

						<div className='timeline-panel-body-content-details'>
							{expandable && !!nestedItems && (
								<div className='icon-group'>
									<Icon
										className='event-icon'
										symbol='ac-event-icon'
									/>

									<span className='item-count'>
										{nestedItems.length}
									</span>

									<span
										className='device-icon'
										data-tooltip
										data-tooltip-align='bottom'
										title={`${deviceIconTitle}\n${browserName}`}
									>
										<Icon {...otherIconAttributes} />
									</span>
								</div>
							)}
						</div>

						{!header && (
							<Icon
								symbol={expanded ? 'caret-top' : 'caret-bottom'}
							/>
						)}
					</div>

					{expanded && (
						<div className='timeline-panel-body-content'>
							<div className='timeline-panel-body-content-text'>
								<div className='attributes-title'>
									<span className='label-root'>
										{attributesTitle}
									</span>
								</div>

								{Object.entries(otherValues).map(
									([key, value]) => (
										<div
											className='attributes-item'
											key={key}
										>
											<span className='attribute-key'>{`${key}`}</span>

											<TextTruncate
												className={getCN(
													'attribute-value',
													ATTRIBUTE_CLASSES_MAP[key]
												)}
												title={value || '""'}
											/>
										</div>
									)
								)}
							</div>
						</div>
					)}
				</div>

				{nestedItems && (
					<VerticalTimeline
						items={nestedItems}
						nested
						timeZoneId={timeZoneId}
					/>
				)}
			</div>
		</li>
	);
};

type IVerticalTimelineProps = {
	groupId?: string;
	initialExpanded?: boolean;
	items: ITEM_SHAPE[];
	loading?: boolean;
	nested?: boolean;
	timeZoneId: string;
};

const VerticalTimeline: FC<IVerticalTimelineProps> = ({
	groupId,
	initialExpanded = false,
	items = [],
	loading = false,
	nested = false,
	timeZoneId
}) =>
	loading ? (
		<Spinner alignCenter={false} className='flex-grow-1' spacer />
	) : (
		<div className='vertical-timeline-root'>
			<ul
				className={getCN('timeline', 'timeline-center', {
					'timeline-nested': nested
				})}
			>
				{items.map((item, i) => (
					<TimelineItem
						groupId={groupId}
						initialExpanded={initialExpanded}
						item={item}
						key={i}
						timeZoneId={timeZoneId}
					/>
				))}
			</ul>
		</div>
	);

export default VerticalTimeline;
