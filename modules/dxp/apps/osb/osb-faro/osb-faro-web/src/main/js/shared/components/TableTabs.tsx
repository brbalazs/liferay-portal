import CardTabs from 'shared/components/CardTabs';
import React from 'react';
import Table from 'shared/components/table';
import {compose} from 'redux';
import {withEmpty} from 'cerebro-shared/hocs/utils';
import {withError} from 'shared/hoc/util';

type tabId = string | number;

export type Tab = {
	getColumns: (props: ITableTabsProps) => object[];
	rowIdentifier: string | number | (string | number)[];
	tabId: tabId;
	title: string;
};

const WrappedTable = compose<any>(
	withError({page: false}),
	withEmpty()
)(Table);

interface ITableTabsProps {
	activeTabId: tabId;
	items: any[];
	loading: boolean;
	onActiveTabChange: () => void;
	tabConfig: Tab[];
	total: number;
}

/**
 * Component for rendering different Tables per CardTab.
 * The parent of this component should provide the activeTabId as props.
 */
const TableTabs: React.FC<ITableTabsProps> = (props: ITableTabsProps) => {
	const {
		activeTabId,
		items,
		loading,
		onActiveTabChange,
		tabConfig,
		total,
		...otherProps
	} = props;

	const {getColumns, rowIdentifier} = tabConfig.find(
		({tabId}) => tabId === activeTabId
	);

	return (
		<div className='w-100 d-flex flex-column flex-grow-1'>
			<CardTabs
				activeTabId={activeTabId}
				onChange={onActiveTabChange}
				tabs={tabConfig.map(({tabId, title}) => ({tabId, title}))}
			/>

			<WrappedTable
				className='flex-grow-1 table-hover'
				columns={getColumns(props)}
				items={items}
				loading={loading}
				rowIdentifier={rowIdentifier}
				total={total}
				{...otherProps}
			/>
		</div>
	);
};

export default TableTabs;
