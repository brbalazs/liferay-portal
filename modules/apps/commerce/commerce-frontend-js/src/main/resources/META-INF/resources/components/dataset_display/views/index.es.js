import EmailsList from './emails_list/EmailsList';
import Table from './table/Table.es';
import SelectableItemsList from './selectable_items_list/SelectableItemsList.es';

export default [
    {
        component: Table,
        id: 'table',
    },
    {
        component: EmailsList,
        id: 'emailsList',
    },
    {
        component: SelectableItemsList,
        id: 'selectableItemsList',
    }
];