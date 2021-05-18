import React from 'react';
import ReferencedEntityDisplay from './ReferencedEntityDisplay';
import {ENTITY_MAP} from 'contacts/components/segment-editor/dynamic/inputs/IndividualSelectInput';
import {
	formatDuration,
	getLargestNaturalUnit,
	getUnitLabel
} from 'shared/util/time';
import {
	getOperatorLabel,
	maybeFormatToKnownType,
	maybeFormatValue
} from '../utils';
import {IDisplayComponentProps} from '../types';
import {isOfKnownType} from 'contacts/components/segment-editor/dynamic/utils/utils';
import {PropertyTypes} from 'contacts/components/segment-editor/dynamic/utils/constants';

const IndividualDisplay: React.FC<IDisplayComponentProps> = ({
	criterion,
	property,
	timeZoneId
}) => {
	const {operatorName, propertyName, value} = criterion;

	const {entityName, label, type} = property;

	const getDurationFormat = value => {
		const largestNaturalUnit = getLargestNaturalUnit(value);

		return (
			<>
				<b>{formatDuration(value, largestNaturalUnit)}</b>

				<span>{getUnitLabel(largestNaturalUnit)}</span>
			</>
		);
	};

	const renderContent = () => {
		switch (type) {
			case PropertyTypes.SelectText:
				return (
					<ReferencedEntityDisplay
						id={value}
						label={label}
						type={ENTITY_MAP[propertyName]}
					/>
				);
			case PropertyTypes.Duration:
				return getDurationFormat(value);
			default:
				return <b>{maybeFormatValue(value, type, timeZoneId)}</b>;
		}
	};

	const operatorKey = maybeFormatToKnownType(operatorName, value);
	const operatorLabel = getOperatorLabel(operatorKey, type);

	return (
		<>
			{entityName}

			<b>{label}</b>

			<span>{operatorLabel}</span>

			{!isOfKnownType(operatorKey) && renderContent()}
		</>
	);
};

export default IndividualDisplay;
