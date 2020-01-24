import EmailsList from './emails_list/EmailsList';
import List from './list/List.es';
import Table from './table/Table.es';

const views = [
	{
		component: Table,
		id: 'table'
	},
	{
		component: EmailsList,
		id: 'emailsList'
	},
	{
		component: List,
		id: 'list'
	}
];

export function getViewById(requestedContentRendererId) {
	return new Promise(resolve => {
		views.forEach(view => {
			if (view.id === requestedContentRendererId) resolve(view.component);
		});
		throw new Error(
			`No content renderer found with the ID: "${requestedContentRendererId}"`
		);
	});
}
