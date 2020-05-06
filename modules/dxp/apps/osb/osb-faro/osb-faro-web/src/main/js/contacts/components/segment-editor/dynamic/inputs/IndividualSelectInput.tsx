import Constants from 'shared/util/constants';
import DXPUsersQuery from '../queries/DXPUsersQuery';
import getDXPEntitiesQuery from '../queries/DXPEntitiesQuery';
import React from 'react';
import SelectEntityInput from './components/SelectEntityInput';
import {EntityType} from '../context/referencedObjects';
import {
	getMapResultToProps,
	mapPropsToOptions
} from '../mappers/dxp-entity-bag-mapper';
import {ISegmentEditorInputBase} from '../utils/types';
import {NAME} from 'shared/util/pagination';
import {OrderedMap} from 'immutable';

const QUERY_MAP = {
	userId: DXPUsersQuery
};

const {
	pagination: {orderAscending}
} = Constants;

export const ENTITY_MAP = {
	groupIds: EntityType.Groups,
	roleIds: EntityType.Roles,
	teamIds: EntityType.Teams,
	userGroupIds: EntityType.UserGroups,
	userId: EntityType.Users
};

const LABEL_MAP = {
	groupIds: Liferay.Language.get('site-memberships'),
	roleIds: Liferay.Language.get('roles'),
	teamIds: Liferay.Language.get('teams'),
	userGroupIds: Liferay.Language.get('user-groups')
};

const nameCol = {
	accessor: 'name',
	className: 'table-cell-expand',
	label: Liferay.Language.get('name'),
	title: true
};

const PROPERTY_COLUMNS_MAP = {
	userId: [
		nameCol,
		{
			accessor: 'screenName',
			className: 'table-cell-expand',
			label: Liferay.Language.get('screen-name')
		}
	]
};

interface IIndividualSelectProps extends ISegmentEditorInputBase {
	channelId: string;
	touched: boolean;
	valid: boolean;
	value: string;
}

const IndividualSelectInput: React.FC<IIndividualSelectProps> = ({
	channelId,
	onChange,
	property,
	value,
	...otherProps
}) => {
	const entityType: EntityType = ENTITY_MAP[property.name];

	const graphqlEntityType =
		entityType === EntityType.UserGroups
			? 'userGroups'
			: (entityType as string);

	const handleItemsChange = (items: OrderedMap<string, any>) => {
		const entity = items.first();

		if (items.size === 1) {
			onChange({
				valid: true,
				value: entity.id
			});
		} else {
			onChange(
				items
					.valueSeq()
					.toArray()
					.map(({id}) => ({
						valid: true,
						value: id
					}))
			);
		}
	};

	return (
		<SelectEntityInput
			channelId={channelId}
			className='individual-select-input-root'
			columns={PROPERTY_COLUMNS_MAP[property.name] || [nameCol]}
			entityLabel={LABEL_MAP[property.name]}
			entityType={entityType}
			graphqlProps={{
				graphqlQuery:
					QUERY_MAP[property.name] ||
					getDXPEntitiesQuery(graphqlEntityType),
				mapPropsToOptions,
				mapResultToProps: getMapResultToProps(graphqlEntityType)
			}}
			onItemsChange={handleItemsChange}
			onValidChange={onChange}
			orderBy={orderAscending}
			orderByField={NAME}
			orderByOptions={[
				{
					label: Liferay.Language.get('name'),
					value: NAME
				}
			]}
			property={property}
			value={value}
			{...otherProps}
		/>
	);
};

export default IndividualSelectInput;
