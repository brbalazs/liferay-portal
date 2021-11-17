import Form from 'shared/components/form';
import getCN from 'classnames';
import Promise from 'metal-promise';
import React, {useContext, useEffect} from 'react';
import SelectEntityFromModal from '../components/SelectEntityFromModal';
import {Columns} from 'shared/types';
import {
	EntityType,
	ReferencedObjectsContext
} from '../../context/referencedObjects';
import {get} from 'lodash';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {Map, OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';

interface ISelectEntityInputProps {
	columns: Columns;
	dataSourceFn?: () => typeof Promise;
	delta?: number;
	entityLabel: string;
	entityType: EntityType;
	graphqlProps?: {[key: string]: any};
	groupId?: string;
	initialOrderIOMap?: OrderedMap<string, OrderParams>;
	onItemsChange: (items: OrderedMap<string, any>) => void;
	onValidChange: ({
		touched,
		valid
	}: {
		touched: boolean;
		valid: boolean;
	}) => void;
	page?: number;
	query?: string;
	touched: boolean;
	valid: boolean;
	value: string;
	[key: string]: any;
}

const SelectEntityInput: React.FC<ISelectEntityInputProps> = ({
	className,
	displayValue,
	entityLabel,
	entityType,
	onItemsChange,
	onValidChange,
	operatorRenderer: OperatorDropdown,
	property,
	touched,
	valid,
	value,
	...otherProps
}) => {
	const {addEntities, addEntity, referencedEntities} = useContext(
		ReferencedObjectsContext
	);

	const reference = referencedEntities.getIn([entityType, value]);

	useEffect(() => {
		if (value && !reference && valid) {
			onValidChange({touched: true, valid: false});
		}
	});

	const handleEntitySelect = (items: OrderedMap<string, any>) => {
		const entity = items.first();

		if (items.size === 1) {
			addEntity({
				entityType,
				payload: Map(entity)
			});

			onItemsChange(items);
		} else {
			addEntities({
				entityType,
				payload: items.map(Map).valueSeq().toArray()
			});

			onItemsChange(items);
		}
	};

	return (
		<div
			className={getCN(
				className,
				'criteria-statement',
				'select-entity-input-root'
			)}
		>
			<Form.Group autoFit>
				<Form.GroupItem className='entity-name' label shrink>
					{property.entityName}
				</Form.GroupItem>

				{property.entityName !== displayValue && (
					<Form.GroupItem className='display-value' label shrink>
						<b>{displayValue}</b>
					</Form.GroupItem>
				)}

				<OperatorDropdown />

				<SelectEntityFromModal
					{...otherProps}
					entity={reference && reference.toJS()}
					error={touched && !valid}
					noResultsProps={{
						spacer: true,
						title: getFormattedTitle(entityLabel)
					}}
					onSubmit={handleEntitySelect}
					renderEntity={entity =>
						get(entity, 'name') && (
							<div>
								<span>{entity.name}</span>
								<span className='text-secondary ml-1'>
									{get(entity, 'dataSourceName')}
								</span>
							</div>
						)
					}
					title={property.label}
				/>
			</Form.Group>
		</div>
	);
};

export default SelectEntityInput;
