import EmailsList from './emails_list/EmailsList';
import Table from './table/Table.es';
import List from './list/List.es';

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
        component: List,
        id: 'list',
    }
];