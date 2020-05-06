import autobind from 'autobind-decorator';
import Button from './Button';
import Card from './Card';
import DatePicker from './date-picker';
import Icon from './Icon';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Overlay from './Overlay';
import RadioGroup from './RadioGroup';
import React from 'react';
import SearchableSelect from './SearchableSelect';
import {mapValues, noop} from 'lodash';
import {PropTypes} from 'prop-types';

export const RANGE = {
	CUSTOM: 'CUSTOM',
	LAST_MONTH: 'LAST_MONTH',
	LAST_THREE_MONTHS: 'LAST_THREE_MONTHS',
	LAST_WEEK: 'LAST_WEEK',
	LAST_YEAR: 'LAST_YEAR'
};

export const INTERVAL = {
	DAILY: 'DAILY',
	MONTHLY: 'MONTHLY',
	QUARTERLY: 'QUARTERLY',
	WEEKLY: 'WEEKLY',
	YEARLY: 'YEARLY'
};

const RANGE_LANG_MAP = {
	[RANGE.CUSTOM]: Liferay.Language.get('custom'),
	[RANGE.LAST_MONTH]: Liferay.Language.get('last-month'),
	[RANGE.LAST_THREE_MONTHS]: Liferay.Language.get('last-three-months'),
	[RANGE.LAST_WEEK]: Liferay.Language.get('last-week'),
	[RANGE.LAST_YEAR]: Liferay.Language.get('last-year')
};

const INTERVAL_LANG_MAP = {
	[INTERVAL.DAILY]: Liferay.Language.get('daily'),
	[INTERVAL.MONTHLY]: Liferay.Language.get('monthly'),
	[INTERVAL.QUARTERLY]: Liferay.Language.get('quarterly'),
	[INTERVAL.WEEKLY]: Liferay.Language.get('weekly'),
	[INTERVAL.YEARLY]: Liferay.Language.get('yearly')
};

const INTERVAL_OPTIONS = [
	INTERVAL.DAILY,
	INTERVAL.WEEKLY,
	INTERVAL.MONTHLY,
	INTERVAL.QUARTERLY,
	INTERVAL.YEARLY
];

const RANGE_OPTIONS = [
	RANGE.LAST_WEEK,
	RANGE.LAST_MONTH,
	RANGE.LAST_THREE_MONTHS,
	RANGE.LAST_YEAR,
	RANGE.CUSTOM
].map(value => ({
	name: RANGE_LANG_MAP[value],
	value
}));

class DateIntervalSelector extends React.Component {
	static defaultProps = {
		buttonDateFormat: 'MMM D, YYYY',
		interval: INTERVAL.LAST_MONTH,
		onIntervalChange: noop,
		onRangeChange: noop,
		onRangeTypeChange: noop,
		rangeType: RANGE.DAILY,
		readOnly: false
	};

	static propTypes = {
		buttonDateFormat: PropTypes.string,
		interval: PropTypes.oneOf(Object.values(INTERVAL)),
		onIntervalChange: PropTypes.func,
		onRangeChange: PropTypes.func,
		onRangeTypeChange: PropTypes.func,
		range: PropTypes.object,
		rangeType: PropTypes.oneOf(Object.values(RANGE)),
		readOnly: PropTypes.bool
	};

	state = {
		active: false
	};

	getIntervalLabel() {
		const {interval} = this.props;

		return INTERVAL_LANG_MAP[interval];
	}

	getRangeLabel() {
		const {buttonDateFormat, range, rangeType} = this.props;

		if (rangeType === RANGE.CUSTOM) {
			const {end, start} = mapValues(range, value =>
				value ? value.format(buttonDateFormat) : '****'
			);

			return `${start} - ${end}`;
		}

		return RANGE_LANG_MAP[rangeType];
	}

	@autobind
	handleRangeTypeSelect(item) {
		this.props.onRangeTypeChange(item.value);
	}

	@autobind
	handleToggleActive() {
		if (!this.props.readOnly) {
			this.setState({
				active: !this.state.active
			});
		}
	}

	@autobind
	handleOutsideClick() {
		this.setState({
			active: false
		});
	}

	render() {
		const {
			props: {
				interval,
				onIntervalChange,
				onRangeChange,
				range,
				rangeType,
				...otherProps
			},
			state: {active}
		} = this;

		const classes = 'date-interval-selector-root';

		return (
			<Overlay
				active={active}
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
				containerClass={classes}
				onOutsideClick={this.handleOutsideClick}
			>
				<Button
					className={classes}
					display='unstyled'
					onClick={this.handleToggleActive}
				>
					<div className='label-container'>
						{this.getRangeLabel()}

						<span className='button-label-separator'>{'|'}</span>

						<span className='interval-label'>
							{this.getIntervalLabel()}
						</span>
					</div>

					<div className='icon-container'>
						<Icon symbol='calendar' />
					</div>
				</Button>

				<Card horizontal>
					<Card.Body>
						<div className='date-interval-selector-content'>
							<div className='interval-container'>
								<div className='form-group'>
									<label>
										{Liferay.Language.get('date-range')}
									</label>

									<SearchableSelect
										buttonProps={{
											size: 'sm'
										}}
										items={RANGE_OPTIONS}
										onSelect={this.handleRangeTypeSelect}
										selectedItem={{
											name: RANGE_LANG_MAP[rangeType],
											value: rangeType
										}}
										showSearch={false}
									/>
								</div>

								<div className='form-group'>
									<label>
										{Liferay.Language.get('time-interval')}
									</label>

									<RadioGroup
										checked={interval}
										onChange={onIntervalChange}
									>
										{INTERVAL_OPTIONS.map(value => (
											<RadioGroup.Option
												key={value}
												label={INTERVAL_LANG_MAP[value]}
												value={value}
											/>
										))}
									</RadioGroup>
								</div>
							</div>

							<div className='date-picker-container'>
								<DatePicker
									{...omitDefinedProps(
										otherProps,
										DateIntervalSelector.propTypes
									)}
									date={range}
									disabled={rangeType !== RANGE.CUSTOM}
									onSelect={onRangeChange}
								/>
							</div>
						</div>
					</Card.Body>
				</Card>
			</Overlay>
		);
	}
}

export default DateIntervalSelector;
