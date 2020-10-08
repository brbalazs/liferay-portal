import * as API from 'shared/api';
import Form, {validateRequired} from 'shared/components/form';
import React, {useState} from 'react';
import {TimeZone} from 'shared/util/records';
import {useRequest} from 'shared/hooks';

interface ITimeZonePicker {
	disabled?: boolean;
	fieldName: string;
	initialTimeZone?: TimeZone;
	setFieldTouched: Function;
	setFieldValue: Function;
}

const TimeZonePicker: React.FC<ITimeZonePicker> = ({
	disabled = false,
	fieldName,
	initialTimeZone,
	setFieldTouched,
	setFieldValue
}) => {
	const {data: timezonesAvailable, loading} = useRequest(
		API.projects.fetchAvailableTimeZones,
		{}
	);

	const [selectedCountry, setSelectedCountry] = useState<string>(
		initialTimeZone && initialTimeZone.country
	);

	const getCountries = (): Array<string> =>
		timezonesAvailable
			? Array.from(
					new Set(
						timezonesAvailable
							.map(timeZone => timeZone.country)
							.sort()
					)
			  )
			: [];

	const getTimeZones = (): Array<TimeZone> =>
		timezonesAvailable
			? timezonesAvailable.filter(
					({country}) =>
						!selectedCountry || country === selectedCountry
			  )
			: [];

	const handleManipulationEvents = (event): void => {
		setSelectedCountry(event.target.value);
		setFieldTouched(fieldName, '');
		setFieldValue(fieldName, '');
	};

	return (
		<Form.Group autoFit>
			<Form.GroupItem className='col-3 country-picker'>
				<select
					className='form-control select-root'
					disabled={disabled || loading}
					onBlur={handleManipulationEvents}
					onChange={handleManipulationEvents}
					value={selectedCountry}
				>
					<option value=''>{Liferay.Language.get('country')}</option>

					{getCountries().map(country => (
						<Form.Select.Item key={country} value={country}>
							{country}
						</Form.Select.Item>
					))}
				</select>
			</Form.GroupItem>

			<Form.GroupItem className='col-9 time-zone-picker'>
				<Form.Select
					disabled={disabled || loading || !selectedCountry}
					name={fieldName}
					showBlankOption
					validate={validateRequired}
				>
					{getTimeZones().map(({displayTimeZone, timeZoneId}) => (
						<Form.Select.Item key={timeZoneId} value={timeZoneId}>
							{displayTimeZone}
						</Form.Select.Item>
					))}
				</Form.Select>
			</Form.GroupItem>
		</Form.Group>
	);
};

export default TimeZonePicker;
