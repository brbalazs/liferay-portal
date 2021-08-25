import Button from 'shared/components/Button';
import Modal, {Size} from 'shared/components/modal';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {noop, omit} from 'lodash';
import {withStatefulPagination} from 'shared/hoc';

const SearchableTable = withStatefulPagination(
	SearchableEntityTable,
	({defaultParams}) => ({defaultDelta: 10, ...defaultParams}),
	(props: {[key: string]: any}) => omit(props, 'onSearchValueChange')
);

interface ISearchableEntitiesTableModalProps {
	className: string;
	defaultParams: {[key: string]: any};
	onClose: () => void;
	size: Size;
	title: string;
}

const SearchableEntitiesTableModal: React.FC<ISearchableEntitiesTableModalProps> = ({
	className,
	defaultParams = {},
	onClose = noop,
	size = 'xxl',
	title = 'entities',
	...otherProps
}) => (
	<Modal className={className} size={size}>
		<Modal.Header onClose={onClose} title={title} />

		<SearchableTable
			{...otherProps}
			defaultParams={defaultParams}
			toolbarProps={{autoFocus: true}}
		/>

		<Modal.Footer>
			<Button display='primary' onClick={onClose}>
				{Liferay.Language.get('done')}
			</Button>
		</Modal.Footer>
	</Modal>
);

export default SearchableEntitiesTableModal;
