import Button from 'shared/components/Button';
import Card from './Card';
import DatePicker from './date-picker';
import getCN from 'classnames';
import Icon from './Icon';
import Input from './Input';
import moment from 'moment';
import Overlay from './Overlay';
import React, {useState} from 'react';
import {FORMAT} from 'shared/util/date';
import {isNil, noop} from 'lodash';
import {sub} from 'shared/util/lang';

const convertToMoment = (value: string, format): moment.Moment => {
	const date = moment(value, format);

	return date.isValid() ? date : null;
};

export type DateRange = {
	end: string;
	start: string;
};

export type MomentDateRange = {
	end: moment.Moment;
	start: moment.Moment;
};

interface IDateInputProps {
	className?: string;
	displayFormat?: string;
	format?: string;
	onBlur?: () => void;
	onChange: (range: DateRange) => void;
	value: DateRange;
}

const DateInput: React.FC<IDateInputProps> = ({
	className,
	displayFormat,
	format = FORMAT,
	onBlur = noop,
	onChange = noop,
	value
}) => {
	const [active, setActive] = useState(false);

	const convertMomentToDisplayFormat = (value: moment.Moment): string =>
		isNil(value) ? null : value.format(displayFormat || format);

	const handleClick = () => setActive(!active);

	const handleDateSelect = ({end, start}: MomentDateRange) => {
		onChange({
			end: convertMomentToDisplayFormat(end),
			start: convertMomentToDisplayFormat(start)
		});
	};

	const getDateRangeDisplay = ({end, start}: MomentDateRange): string => {
		if (end || start) {
			return sub(Liferay.Language.get('x-to-x'), [
				convertMomentToDisplayFormat(start),
				convertMomentToDisplayFormat(end)
			]) as string;
		}

		return '';
	};

	const momentDateRange = {
		end: convertToMoment(value.end, format),
		start: convertToMoment(value.start, format)
	};

	return (
		<Overlay
			active={active}
			className={getCN('date-range-input-root', className)}
			containerClass='date-range-input-root'
			forceAlignment={false}
			onOutsideClick={() => {
				if (onBlur && active) {
					onBlur();
				}

				setActive(false);
			}}
		>
			<Input.Group>
				<Input.GroupItem>
					<Input
						data-testid='date-range-input'
						inset='after'
						onClick={handleClick}
						placeholder={sub(Liferay.Language.get('x-to-x'), [
							Liferay.Language.get('yyyy-mm-dd'),
							Liferay.Language.get('yyyy-mm-dd')
						])}
						readOnly
						value={getDateRangeDisplay(momentDateRange)}
					/>

					<Input.Inset position='after'>
						<Button
							aria-label={Liferay.Language.get(
								'choose-date-range'
							)}
							display='unstyled'
							onClick={handleClick}
						>
							<Icon symbol='calendar' />
						</Button>
					</Input.Inset>
				</Input.GroupItem>
			</Input.Group>

			<Card>
				<Card.Body>
					<DatePicker
						date={momentDateRange}
						minDate={moment().subtract(100, 'years')}
						onSelect={handleDateSelect}
					/>
				</Card.Body>
			</Card>
		</Overlay>
	);
};

export default DateInput;
