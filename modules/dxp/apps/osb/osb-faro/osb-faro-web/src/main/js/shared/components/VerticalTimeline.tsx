import getCN from 'classnames';
import Icon from './Icon';
import NoResultsDisplay, {getFormattedTitle} from './NoResultsDisplay';
import React from 'react';
import Spinner from './Spinner';
import Sticker from './Sticker';
import TextTruncate from './TextTruncate';
import {FC} from 'react';
import {formatDateToTimeZone} from 'shared/util/date';
import {get} from 'lodash';
import {Link} from 'react-router-dom';
import {Routes, toRoute} from 'shared/util/router';
import {useState} from 'react';

interface ITEM_SHAPE {
	header: boolean;
	individual: object;
	nestedItems: [];
	subtitle: string | [];
	symbol: string;
	time: number;
	title: string | string[];
	url: string;
}

interface ITimelineItemProps {
	channelId?: string;
	className?: string;
	groupId?: string;
	initialExpanded?: boolean;
	item: ITEM_SHAPE;
	timeZoneId: string;
}

const TimelineItem: FC<ITimelineItemProps> = ({
	channelId,
	className,
	groupId,
	initialExpanded = true,
	item: {header, individual, nestedItems, subtitle, symbol, time, title, url},
	timeZoneId
}) => {
	const [expanded, setExpanded] = useState(initialExpanded);

	const toggleExpand = () => {
		if (isExpandable()) {
			setExpanded(!expanded);
		}
	};

	const handleClick = () => {
		toggleExpand();
	};

	const handleKeyPress = () => {
		toggleExpand();
	};

	const isExpandable = () => {
		return nestedItems?.length;
	};

	const classes = getCN('timeline-item', className, {
		header
	});

	const expandable = isExpandable();

	const bodyClasses = getCN('timeline-panel-body-content', {
		selectable: expandable
	});

	const bodyAttributes = expandable
		? {
				onClick: handleClick,
				onKeyPress: handleKeyPress,
				role: 'button',
				tabIndex: 0
		  }
		: {};

	const individualName = get(individual, 'name');
	const individualId = get(individual, 'id');

	return (
		<li className={classes}>
			<div className='timeline-panel'>
				<div className='timeline-panel-body'>
					{!header && (
						<div className='timeline-increment'>
							<Sticker circle display='point' size='lg' />
						</div>
					)}

					{time && (
						<div className='timeline-item-label'>
							{formatDateToTimeZone(time, 'h:mma', timeZoneId)}
						</div>
					)}

					<div className={bodyClasses} {...bodyAttributes}>
						{symbol && (
							<div className='sticker-container'>
								<Sticker display='light' symbol={symbol} />
							</div>
						)}

						<div className='timeline-panel-body-content-text'>
							<div>
								{!!individualName && (
									<Link
										className='entity-link'
										to={toRoute(
											Routes.CONTACTS_INDIVIDUAL,
											{
												channelId,
												groupId,
												id: individualId
											}
										)}
									>
										{individualName}
									</Link>
								)}

								<span className='text-truncate'>
									{url ? (
										<Link className='title' to={url}>
											{title}
										</Link>
									) : (
										<span className='title'>{title}</span>
									)}
								</span>
							</div>

							{subtitle && (
								<TextTruncate
									className='subtitle'
									title={subtitle}
								/>
							)}
						</div>

						{isExpandable() && (
							<div className='timeline-panel-body-content-details'>
								<span className='item-count'>
									{nestedItems.length}
								</span>

								<Icon
									symbol={
										expanded ? 'caret-bottom' : 'caret-top'
									}
								/>
							</div>
						)}
					</div>
				</div>

				{expanded && nestedItems && (
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

interface HEADER_LABEL_SHAPE {
	count: string;
	label: string;
	title: string;
}

interface IVerticalTimelineProps {
	groupId?: string;
	headerLabels?: HEADER_LABEL_SHAPE;
	initialExpanded?: boolean;
	items: ITEM_SHAPE[];
	loading?: boolean;
	nested?: boolean;
	timeZoneId: string;
}

const VerticalTimeline: FC<IVerticalTimelineProps> = ({
	groupId,
	headerLabels,
	initialExpanded = true,
	items = [],
	loading = false,
	nested = false,
	timeZoneId
}) => {
	const classes = getCN('timeline', 'timeline-center', {
		'timeline-nested': nested
	});

	if (loading) {
		return <Spinner alignCenter={false} className='flex-grow-1' spacer />;
	} else if (!items.length && !nested) {
		return <NoResultsDisplay title={getFormattedTitle()} />;
	} else {
		return (
			<div className='vertical-timeline-root'>
				{headerLabels && (
					<div className='timeline-header'>
						<div className='header-label'>{headerLabels.label}</div>

						<div className='header-title'>{headerLabels.title}</div>

						<div className='header-count'>{headerLabels.count}</div>
					</div>
				)}

				<ul className={classes}>
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
	}
};

export default VerticalTimeline;
