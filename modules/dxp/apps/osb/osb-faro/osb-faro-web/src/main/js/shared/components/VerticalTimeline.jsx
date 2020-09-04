import autobind from 'autobind-decorator';
import getCN from 'classnames';
import Icon from './Icon';
import moment from 'moment';
import NoResultsDisplay, {getFormattedTitle} from './NoResultsDisplay';
import React from 'react';
import Spinner from './Spinner';
import Sticker from './Sticker';
import TextTruncate from './TextTruncate';
import {get} from 'lodash';
import {Link} from 'react-router-dom';
import {onEnter} from 'shared/util/key-constants';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const ITEM_SHAPE = {
	header: PropTypes.bool,
	individual: PropTypes.object,
	nestedItems: PropTypes.array,
	subtitle: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
	symbol: PropTypes.string,
	time: PropTypes.number,
	title: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
	url: PropTypes.string
};

class TimelineItem extends React.Component {
	static defaultProps = {
		initialExpanded: true
	};

	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string,
		initialExpanded: PropTypes.bool,
		item: PropTypes.shape(ITEM_SHAPE).isRequired
	};

	state = {
		expanded: true
	};

	constructor(props) {
		super(props);

		const {initialExpanded} = this.props;

		this.state = {
			...this.state,
			expanded: initialExpanded
		};
	}

	toggleExpand() {
		if (this.isExpandable()) {
			this.setState({
				expanded: !this.state.expanded
			});
		}
	}

	@autobind
	handleClick() {
		this.toggleExpand();
	}

	@autobind
	@onEnter
	handleKeyPress() {
		this.toggleExpand();
	}

	isExpandable() {
		const {nestedItems} = this.props.item;

		return nestedItems && !!nestedItems.length;
	}

	render() {
		const {
			props: {
				channelId,
				className,
				groupId,
				item: {
					header,
					individual,
					nestedItems,
					subtitle,
					symbol,
					time,
					title,
					url
				}
			},
			state: {expanded}
		} = this;

		const classes = getCN('timeline-item', className, {
			header
		});

		const expandable = this.isExpandable();

		const bodyClasses = getCN('timeline-panel-body-content', {
			selectable: expandable
		});

		const bodyAttributes = expandable
			? {
					onClick: this.handleClick,
					onKeyPress: this.handleKeyPress,
					role: 'button',
					tabIndex: '0'
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
								{moment(time).format('h:mma')}
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
											<span className='title'>
												{title}
											</span>
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

							{this.isExpandable() && (
								<div className='timeline-panel-body-content-details'>
									<span className='item-count'>
										{nestedItems.length}
									</span>

									<Icon
										symbol={
											expanded
												? 'caret-bottom'
												: 'caret-top'
										}
									/>
								</div>
							)}
						</div>
					</div>

					{expanded && nestedItems && (
						<VerticalTimeline items={nestedItems} nested />
					)}
				</div>
			</li>
		);
	}
}

const HEADER_LABEL_SHAPE = {
	count: PropTypes.string,
	label: PropTypes.string,
	title: PropTypes.string
};

export default class VerticalTimeline extends React.Component {
	static defaultProps = {
		initialExpanded: true,
		items: [],
		loading: false,
		nested: false
	};

	static propTypes = {
		groupId: PropTypes.string,
		headerLabels: PropTypes.shape(HEADER_LABEL_SHAPE),
		initialExpanded: PropTypes.bool,
		items: PropTypes.arrayOf(PropTypes.shape(ITEM_SHAPE)),
		loading: PropTypes.bool,
		nested: PropTypes.bool
	};

	render() {
		const {
			groupId,
			headerLabels,
			initialExpanded,
			items,
			loading,
			nested
		} = this.props;

		const classes = getCN('timeline', 'timeline-center', {
			'timeline-nested': nested
		});

		if (loading) {
			return (
				<Spinner alignCenter={false} className='flex-grow-1' spacer />
			);
		} else if (!items.length && !nested) {
			return <NoResultsDisplay title={getFormattedTitle()} />;
		} else {
			return (
				<div className='vertical-timeline-root'>
					{headerLabels && (
						<div className='timeline-header'>
							<div className='header-label'>
								{headerLabels.label}
							</div>

							<div className='header-title'>
								{headerLabels.title}
							</div>

							<div className='header-count'>
								{headerLabels.count}
							</div>
						</div>
					)}

					<ul className={classes}>
						{items.map((item, i) => (
							<TimelineItem
								groupId={groupId}
								initialExpanded={initialExpanded}
								item={item}
								key={i}
							/>
						))}
					</ul>
				</div>
			);
		}
	}
}
