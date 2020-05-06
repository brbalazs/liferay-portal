import autobind from 'autobind-decorator';
import DateInput from 'shared/components/DateInput';
import Form from 'shared/components/form';
import moment from 'moment';
import React from 'react';
import {
	INPUT_DATE_TIME_FORMAT,
	INPUT_DISPLAY_DATE_TIME_FORMAT,
	PROPERTY_TYPES
} from '../utils/constants';
import {ISegmentEditorInputBase} from '../utils/types';

interface IDateTimeInputProps extends ISegmentEditorInputBase {
	value: string;
}

export default class DateTimeInput extends React.Component<
	IDateTimeInputProps
> {
	@autobind
	handleDateChange(value) {
		this.props.onChange({
			type: PROPERTY_TYPES.DATE,
			value
		});
	}

	render() {
		const {
			className,
			displayValue,
			operatorRenderer: OperatorDropdown,
			property: {entityName},
			value
		} = this.props;

		const date = moment(value, INPUT_DATE_TIME_FORMAT).format(
			INPUT_DATE_TIME_FORMAT
		);

		return (
			<div className='criteria-statement'>
				<Form.Group autoFit>
					<Form.GroupItem className='entity-name' label shrink>
						{entityName}
					</Form.GroupItem>

					<Form.GroupItem className='display-value' label shrink>
						{displayValue}
					</Form.GroupItem>

					<OperatorDropdown />

					<Form.GroupItem>
						<DateInput
							className={className}
							displayFormat={INPUT_DISPLAY_DATE_TIME_FORMAT}
							format={INPUT_DATE_TIME_FORMAT}
							onChange={this.handleDateChange}
							showTimeSelector
							value={date}
						/>
					</Form.GroupItem>
				</Form.Group>
			</div>
		);
	}
}
