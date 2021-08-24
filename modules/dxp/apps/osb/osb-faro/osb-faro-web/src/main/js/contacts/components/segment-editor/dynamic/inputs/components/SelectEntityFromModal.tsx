import FaroConstants from 'shared/util/constants';
import Form from 'shared/components/form';
import getCN from 'classnames';
import Input from 'shared/components/Input';
import Promise from 'metal-promise';
import React from 'react';
import {close, modalTypes, open} from 'shared/actions/modals';
import {Columns} from 'shared/types';
import {connect, ConnectedProps} from 'react-redux';
import {detailsListColumns} from 'shared/util/table-columns';
import {OrderedMap} from 'immutable';

const {
	pagination: {orderDescending}
} = FaroConstants;

const connector = connect(null, {close, open});

type PropsFromRedux = ConnectedProps<typeof connector>;

interface ISelectEntityFromModalProps extends PropsFromRedux {
	columns: Columns;
	dataSourceFn?: (params: {[key: string]: any}) => typeof Promise;
	delta?: number;
	entity: {dataSourceName?: string; [key: string]: any};
	error: boolean;
	graphqlProps?: {[key: string]: any};
	groupId?: string;
	noResultsIcon?: string;
	noResultsProps?: {[key: string]: any};
	onSubmit: (items: OrderedMap<string, object>) => void;
	orderBy?: string;
	orderByField?: string;
	orderByOptions?: {label: string; value: any}[];
	page?: number;
	query?: string;
	renderEntity: (entity: any) => React.ReactNode;
	submitMessage?: string;
	title: string;
}

const SelectEntityFromModal: React.FC<ISelectEntityFromModalProps> = ({
	close,
	columns,
	dataSourceFn,
	delta = 10,
	entity,
	error,
	graphqlProps,
	groupId,
	onSubmit,
	orderBy = orderDescending,
	orderByField,
	open,
	renderEntity,
	submitMessage = Liferay.Language.get('add'),
	title,
	...otherProps
}) => {
	const handleModal = () => {
		const dataSourceOptions = graphqlProps || {dataSourceFn};
		const modalType = graphqlProps
			? modalTypes.SEARCHABLE_TABLE_MODAL_GRAPHQL
			: modalTypes.SEARCHABLE_TABLE_MODAL;

		open(modalType, {
			...dataSourceOptions,
			columns: [
				...columns,
				{
					...detailsListColumns.getDataSourceName(groupId),
					className: 'table-cell-expand',
					sortable: false
				}
			],
			delta,
			groupId,
			onClose: close,
			onSubmit: (items: OrderedMap<string, object>) => {
				onSubmit(items);

				close();
			},
			orderBy,
			orderByField,
			selectedItems: entity ? [entity] : undefined,
			submitMessage,
			title,
			...otherProps
		});
	};

	return (
		<Form.GroupItem>
			<Input.Group className='select-entity-group'>
				<Input.GroupItem
					className={getCN({
						'has-error': error
					})}
				>
					<Input.Group className='select-input-root'>
						<Input.GroupItem>
							<Input />
						</Input.GroupItem>

						<div className='selected-item-container'>
							{renderEntity(entity)}
						</div>
					</Input.Group>
				</Input.GroupItem>

				<Input.Button onClick={handleModal} position='append'>
					{Liferay.Language.get('select')}
				</Input.Button>
			</Input.Group>
		</Form.GroupItem>
	);
};

export default connector(SelectEntityFromModal);
