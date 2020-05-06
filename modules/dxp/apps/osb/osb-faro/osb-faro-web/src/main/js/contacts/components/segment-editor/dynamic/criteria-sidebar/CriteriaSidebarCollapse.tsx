import CriteriaSidebarItem from './CriteriaSidebarItem';
import React from 'react';
import {
	ACTIVITY_KEY,
	LAST_24_HOURS,
	PROPERTY_TYPES,
	RELATIONAL_OPERATORS
} from '../utils/constants';
import {createCustomValueMap} from '../utils/custom-inputs';
import {jsDatetoYYYYMMDD} from '../utils/utils';
import {List} from 'immutable';
import {Property, PropertyGroup, PropertySubgroup} from 'shared/util/records';

/**
 * Returns a default value for a property provided.
 */
const getDefaultValue = (property: Property): any => {
	const {name, options, type} = property;

	switch (type) {
		case PROPERTY_TYPES.DATE:
			return jsDatetoYYYYMMDD(new Date());
		case PROPERTY_TYPES.DATE_TIME:
			return new Date().toISOString();
		case PROPERTY_TYPES.SESSION_DATE_TIME:
		case PROPERTY_TYPES.ORGANIZATION_DATE:
		case PROPERTY_TYPES.ORGANIZATION_DATE_TIME:
			return createCustomValueMap([
				{
					key: 'criterionGroup',
					value: [
						{
							operatorName: RELATIONAL_OPERATORS.EQ,
							propertyName: name,
							value: new Date().toISOString()
						}
					]
				}
			]);
		case PROPERTY_TYPES.BOOLEAN:
			return 'true';
		case PROPERTY_TYPES.INTEREST:
			return createCustomValueMap([
				{
					key: 'criterionGroup',
					value: [
						{
							operatorName: RELATIONAL_OPERATORS.EQ,
							propertyName: 'name',
							value: name
						},
						{
							operatorName: RELATIONAL_OPERATORS.EQ,
							propertyName: 'score',
							value: 'true'
						}
					]
				}
			]);
		case PROPERTY_TYPES.ACCOUNT_NUMBER:
		case PROPERTY_TYPES.ACCOUNT_TEXT:
		case PROPERTY_TYPES.ORGANIZATION_SELECT_TEXT:
		case PROPERTY_TYPES.ORGANIZATION_TEXT:
		case PROPERTY_TYPES.ORGANIZATION_NUMBER:
			return createCustomValueMap([
				{
					key: 'criterionGroup',
					value: [
						{
							operatorName: RELATIONAL_OPERATORS.EQ,
							propertyName: name,
							value: ''
						}
					]
				}
			]);
		case PROPERTY_TYPES.BEHAVIOR:
			return createCustomValueMap([
				{
					key: 'criterionGroup',
					value: [
						{
							operatorName: RELATIONAL_OPERATORS.EQ,
							propertyName: ACTIVITY_KEY,
							value: ''
						},
						{
							operatorName: RELATIONAL_OPERATORS.GT,
							propertyName: 'day',
							value: LAST_24_HOURS
						}
					]
				},
				{key: 'operator', value: RELATIONAL_OPERATORS.GE},
				{key: 'value', value: 1}
			]);
		case PROPERTY_TYPES.ORGANIZATION_BOOLEAN:
			return createCustomValueMap([
				{
					key: 'criterionGroup',
					value: [
						{
							operatorName: RELATIONAL_OPERATORS.EQ,
							propertyName: name,
							value: 'true'
						}
					]
				}
			]);
		case PROPERTY_TYPES.SESSION_GEOLOCATION:
		case PROPERTY_TYPES.SESSION_NUMBER:
		case PROPERTY_TYPES.SESSION_TEXT:
			return createCustomValueMap([
				{
					key: 'criterionGroup',
					value: [
						{
							operatorName: RELATIONAL_OPERATORS.EQ,
							propertyName: name,
							value: options.length ? options[0].value : ''
						},
						{
							operatorName: RELATIONAL_OPERATORS.GT,
							propertyName: 'completeDate',
							value: LAST_24_HOURS
						}
					]
				}
			]);
		case PROPERTY_TYPES.TEXT:
			if (options && !!options.length) {
				return options[0].value;
			}

			return '';
		default:
			return '';
	}
};

interface ICriteriaSidebarCollapseProps {
	propertyGroupsIList: List<PropertyGroup>;
	propertyKey: string;
	searchValue: string;
}

const CriteriaSidebarCollapse: React.FC<ICriteriaSidebarCollapseProps> = ({
	propertyGroupsIList,
	propertyKey,
	searchValue
}) => {
	const filterProperties = (): List<PropertySubgroup> => {
		const propertyGroup = propertyGroupsIList.find(
			propertyGroup => propertyKey === propertyGroup.propertyKey
		);

		const propertySubgroupsIList = propertyGroup
			? propertyGroup.propertySubgroups
			: List<PropertySubgroup>();

		if (searchValue) {
			return propertySubgroupsIList.map(
				({label, properties}) =>
					new PropertySubgroup({
						label,
						properties: properties.filter(({label}) => {
							const propertyLabel = label.toLowerCase();

							return propertyLabel.includes(
								searchValue.toLowerCase()
							);
						}) as List<Property>
					})
			) as List<PropertySubgroup>;
		}

		return propertySubgroupsIList;
	};

	const filteredProperties = filterProperties();

	const noResults = filteredProperties
		.filterNot(({properties}) => properties.isEmpty())
		.isEmpty();

	return (
		<ul className='property-subgroups-list active'>
			{noResults ? (
				<li className='empty-message'>
					{Liferay.Language.get('no-results-were-found')}
				</li>
			) : (
				filteredProperties.map(({label, properties}, i) => (
					<li key={`${label}-${i}`}>
						{label && (
							<div className='property-subgroup-label'>
								{label}
							</div>
						)}

						{properties.isEmpty() ? (
							<div className='empty-message'>
								{Liferay.Language.get('no-results-were-found')}
							</div>
						) : (
							<ul className='properties-list'>
								{properties.map((property, i) => {
									const {
										label,
										name,
										propertyKey,
										type
									} = property;

									return (
										<CriteriaSidebarItem
											className={`color--${propertyKey}`}
											defaultValue={getDefaultValue(
												property
											)}
											key={`${name}-${i}`}
											label={label}
											name={name}
											property={property}
											propertyKey={propertyKey}
											type={type}
										/>
									);
								})}
							</ul>
						)}
					</li>
				))
			)}
		</ul>
	);
};

export default CriteriaSidebarCollapse;
