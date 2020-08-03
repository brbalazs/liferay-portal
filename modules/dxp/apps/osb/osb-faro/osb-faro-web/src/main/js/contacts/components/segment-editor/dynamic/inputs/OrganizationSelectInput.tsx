import Constants from 'shared/util/constants';
import CustomSelectEntityInput from './components/CustomSelectEntityInput';
import OrganizationsQuery from '../queries/OrganizationsQuery';
import React from 'react';
import {EntityType} from '../context/referencedObjects';
import {
	getMapResultToProps,
	mapPropsToOptions
} from '../mappers/dxp-entity-bag-mapper';
import {ISegmentEditorCustomInputBase} from '../utils/types';
import {NAME} from 'shared/util/pagination';
import {organizationsListColumns} from 'shared/util/table-columns';

const {
	pagination: {orderAscending}
} = Constants;

interface IOrganizationSelectProps extends ISegmentEditorCustomInputBase {
	touched: boolean;
	valid: boolean;
}

const OrganizationSelectInput: React.FC<IOrganizationSelectProps> = ({
	...otherProps
}) => (
	<CustomSelectEntityInput
		className='organization-select-input-root'
		columns={organizationsListColumns}
		entityLabel={Liferay.Language.get('organizations')}
		entityType={EntityType.Organizations}
		graphqlProps={{
			graphqlQuery: OrganizationsQuery,
			mapPropsToOptions,
			mapResultToProps: getMapResultToProps('organizations')
		}}
		orderBy={orderAscending}
		orderByField={NAME}
		orderByOptions={[
			{
				label: Liferay.Language.get('name'),
				value: NAME
			}
		]}
		{...otherProps}
	/>
);

export default OrganizationSelectInput;
