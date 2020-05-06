import autobind from 'autobind-decorator';
import DateIntervalSelector, {
	INTERVAL,
	RANGE
} from 'shared/components/DateIntervalSelector';
import React from 'react';

class DateIntervalSelectorKit extends React.Component {
	state = {
		interval: INTERVAL.DAILY,
		range: {end: null, start: null},
		rangeType: RANGE.LAST_MONTH
	};

	@autobind
	handleIntervalChange(interval) {
		this.setState({
			interval
		});
	}

	@autobind
	handleRangeChange(range) {
		this.setState({
			range
		});
	}

	@autobind
	handleRangeTypeChange(rangeType) {
		this.setState({
			rangeType
		});
	}

	render() {
		const {interval, range, rangeType} = this.state;

		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<DateIntervalSelector
					interval={interval}
					onIntervalChange={this.handleIntervalChange}
					onRangeChange={this.handleRangeChange}
					onRangeTypeChange={this.handleRangeTypeChange}
					range={range}
					rangeType={rangeType}
				/>
			</div>
		);
	}
}

export default DateIntervalSelectorKit;
