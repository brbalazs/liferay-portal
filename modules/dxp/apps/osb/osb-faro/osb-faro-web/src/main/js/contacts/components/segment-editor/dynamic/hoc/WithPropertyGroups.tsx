import * as API from 'shared/api';
import client from 'shared/apollo/client';
import EventDefinitionsQuery from 'event-analysis/queries/EventDefinitionsQuery';
import Promise from 'metal-promise';
import React from 'react';
import {compose} from 'redux';
import {
	convertEventToProperty,
	convertFieldMappingToAccountProperty,
	convertFieldMappingToIndividualProperty,
	convertFieldMappingToOrganizationProperty
} from '../utils/utils';
import {createInterestProperty} from '../utils/utils';
import {EventTypes} from 'event-analysis/utils/types';
import {FieldContexts, FieldOwnerTypes} from 'shared/util/constants';
import {
	INDIVIDUAL_PROPERTIES,
	ORGANIZATION_PROPERTIES,
	SESSION_PROPERTIES,
	WEB_BEHAVIORS
} from '../utils/properties';
import {List} from 'immutable';
import {PropertyGroup, PropertySubgroup} from 'shared/util/records';
import {sub} from 'shared/util/lang';
import {withRequest} from 'shared/hoc';

const MAX_DELTA = 500;

const fetchPropertyGroups = ({
	channelId,
	groupId
}: {
	channelId: string;
	groupId: string;
}): Promise<any> =>
	Promise.all([
		API.channels.fetch({channelId, groupId}),
		API.fieldMappings.search({
			context: FieldContexts.Demographics,
			delta: MAX_DELTA,
			groupId,
			ownerType: FieldOwnerTypes.Individual
		}),
		API.fieldMappings.search({
			context: FieldContexts.Custom,
			delta: MAX_DELTA,
			groupId,
			ownerType: FieldOwnerTypes.Individual
		}),
		API.fieldMappings.search({
			context: FieldContexts.Organization,
			delta: MAX_DELTA,
			groupId,
			ownerType: FieldOwnerTypes.Account
		}),
		Promise.resolve(ORGANIZATION_PROPERTIES),
		API.fieldMappings.search({
			context: FieldContexts.Custom,
			delta: MAX_DELTA,
			groupId,
			ownerType: FieldOwnerTypes.Organization
		}),
		API.interests.searchKeywords({delta: MAX_DELTA, groupId}),
		Promise.resolve(SESSION_PROPERTIES),
		client.query({
			query: EventDefinitionsQuery,
			variables: {
				eventType: EventTypes.All, // TODO: Change EventTypes.ALL to EventTypes.Custom
				page: 0,
				size: MAX_DELTA,
				sort: {
					column: 'name',
					type: 'ASC'
				}
			}
		}),
		Promise.resolve(WEB_BEHAVIORS)
	]);

const mapResultToProps = ([
	currentChannel,
	individualDemographicsMappings,
	individualCustomMappings,
	accountMappings,
	organizationProperties,
	organizationCustomMappings,
	interestKeywords,
	sessionProperties,
	eventProperties,
	webBehaviors
]) => {
	const {tokenAuth} = currentChannel;

	const individualDemographicProperties = individualDemographicsMappings.items.map(
		convertFieldMappingToIndividualProperty
	);

	let individualSubgroupsIList = List([
		new PropertySubgroup({
			properties: List(
				tokenAuth
					? individualDemographicProperties.concat(
							INDIVIDUAL_PROPERTIES
					  )
					: individualDemographicProperties
			)
		})
	]);

	if (tokenAuth) {
		individualSubgroupsIList = individualSubgroupsIList.push(
			new PropertySubgroup({
				label: Liferay.Language.get('dxp-custom-fields'),
				properties: List(
					individualCustomMappings.items.map(
						convertFieldMappingToIndividualProperty
					)
				)
			})
		);
	}

	const organizationPropertyGroup = new PropertyGroup({
		label: sub(Liferay.Language.get('x-attributes'), [
			Liferay.Language.get('organization')
		]) as string,
		propertyKey: FieldOwnerTypes.Organization,
		propertySubgroups: List([
			new PropertySubgroup({properties: organizationProperties}),
			new PropertySubgroup({
				label: Liferay.Language.get('dxp-custom-fields'),
				properties: List(
					organizationCustomMappings.items.map(
						convertFieldMappingToOrganizationProperty
					)
				)
			})
		])
	});

	const propertyGroupsIList = List([
		new PropertyGroup({
			label: Liferay.Language.get('events'),
			propertyKey: 'web',
			propertySubgroups: List([
				new PropertySubgroup({
					label: Liferay.Language.get('default-events'),
					properties: webBehaviors
				}),
				new PropertySubgroup({
					label: Liferay.Language.get('custom-events'),
					properties: List(
						eventProperties?.data?.eventDefinitions?.eventDefinitions?.map(
							convertEventToProperty
						)
					)
				})
			])
		}),
		new PropertyGroup({
			label: sub(Liferay.Language.get('x-attributes'), [
				Liferay.Language.get('individual')
			]) as string,
			propertyKey: FieldOwnerTypes.Individual,
			propertySubgroups: individualSubgroupsIList
		}),
		new PropertyGroup({
			label: sub(Liferay.Language.get('x-attributes'), [
				Liferay.Language.get('account')
			]) as string,
			propertyKey: FieldOwnerTypes.Account,
			propertySubgroups: List([
				new PropertySubgroup({
					properties: List(
						accountMappings.items.map(
							convertFieldMappingToAccountProperty
						)
					)
				})
			])
		}),
		new PropertyGroup({
			label: Liferay.Language.get('interests'),
			propertyKey: 'interest',
			propertySubgroups: List([
				new PropertySubgroup({
					properties: List(
						interestKeywords.items.map(createInterestProperty)
					)
				})
			])
		}),
		new PropertyGroup({
			label: sub(Liferay.Language.get('x-attributes'), [
				Liferay.Language.get('session')
			]) as string,
			propertyKey: 'session',
			propertySubgroups: List([
				new PropertySubgroup({properties: sessionProperties})
			])
		})
	]);

	return {
		propertyGroupsIList: tokenAuth
			? propertyGroupsIList.push(organizationPropertyGroup)
			: propertyGroupsIList
	};
};

export const withPropertyGroups = WrappedComponent =>
	class extends React.Component<{
		propertyGroupsIList: List<PropertyGroup>;
	}> {
		render() {
			const {propertyGroupsIList, ...otherProps} = this.props;

			return (
				<WrappedComponent
					{...otherProps}
					propertyGroupsIList={propertyGroupsIList}
				/>
			);
		}
	};

export default compose(
	withRequest(fetchPropertyGroups, mapResultToProps, {fadeIn: false}),
	withPropertyGroups
);
