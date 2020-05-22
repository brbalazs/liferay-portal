import BasePage from 'settings/components/BasePage';
import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import moment from 'moment';
import Promise from 'metal-promise';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import withStatefulPagination from 'shared/hoc/StatefulPagination';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {getDefinitions} from 'shared/util/breadcrumbs';
import {HasModal} from 'shared/types';
import {mockIndividualAttributes, mockSearch} from 'test/data';
import {sub} from 'shared/util/lang';

const {
	pagination: {orderDefault}
} = Constants;

const SearchableEntityTableHOC = withStatefulPagination(SearchableEntityTable, {
	defaultOrderByFields: [
		{
			fieldName: 'fieldName',
			orderBy: orderDefault
		}
	]
});

// To be replaced on LRAC-6019
const MOCK_RESPONSE = () =>
	Promise.resolve(mockSearch(mockIndividualAttributes, 5));

interface IIndividualAttributesProps
	extends HasModal,
		React.HTMLAttributes<HTMLElement> {
	groupId: string;
}

const IndividualAttributes: React.FC<IIndividualAttributesProps> = ({
	close,
	groupId,
	open
}) => {
	const openModal = ({dataSources, fieldName}) => () => {
		open(modalTypes.INDIVIDUALS_ATTRIBUTES_MODAL, {
			dataSources,
			fieldName,
			onClose: close
		});
	};

	const FieldNameCell = ({data: {dataSources, fieldName}}) => (
		<td className='table-cell-expand'>
			<div className='content-container'>
				<Button
					display='unstyled'
					onClick={openModal({dataSources, fieldName})}
				>
					{fieldName}
				</Button>
			</div>
		</td>
	);

	return (
		<BasePage
			breadcrumbItems={[
				getDefinitions({groupId}),
				{
					active: true,
					label: Liferay.Language.get('individual-attributes')
				}
			]}
			className='individual-attributes-root'
			groupId={groupId}
			pageDescription={Liferay.Language.get(
				'this-is-the-data-model-of-an-individual.-analytics-cloud-will-take-and-store-the–newest-data-from-all-your-sources'
			)}
			pageTitle={Liferay.Language.get('individual-attributes')}
		>
			<SearchableEntityTableHOC
				columns={[
					{
						cellRenderer: FieldNameCell,
						className: 'table-cell-expand',
						label: Liferay.Language.get('attribute')
					},
					{
						accessor: 'dataSources',
						className: 'pr-6',
						dataFormatter: dataSources =>
							dataSources.length > 1
								? sub(Liferay.Language.get('x-sources'), [
										dataSources.length
								  ])
								: dataSources[0].dataSourceName,
						label: Liferay.Language.get('sources')
					},
					{
						accessor: 'dateModified',
						className: 'pr-5',
						dataFormatter: dateModified =>
							moment(dateModified).fromNow(),
						label: Liferay.Language.get('last-synced'),
						sortable: false
					}
				]}
				dataSourceFn={MOCK_RESPONSE}
				internalSort
				rowIdentifier='fieldName'
				showPagination={false}
			/>
		</BasePage>
	);
};

export default connect(
	null,
	{close, open}
)(IndividualAttributes);
