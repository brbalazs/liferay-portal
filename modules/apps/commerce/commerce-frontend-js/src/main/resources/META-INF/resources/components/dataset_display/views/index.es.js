import EmailsList from './emails_list/EmailsList';
import Table from './table/Table.es'

export const defaultViews = [
    {
        component: Table,
        icon: 'table',
        id: 'table',
        label: Liferay.Language.get('table'),
        main: true,
    },
    {
        component: EmailsList,
        icon: 'email',
        id: 'emails-list',
        label: Liferay.Language.get('emails-list'),
    }
]

export function getViews(views) {
    if(!views) {
        return defaultViews;
    }

    const enrichedViews = views.map(view => {
        if(!view.component) {
            const matchedDefaultView = defaultViews.find(
                defaultRenderer => defaultRenderer.id === view.id
            )

            return matchedDefaultView ? {
                ...view,
                component: matchedDefaultView.component
            } : view 
        }
        return view
    })

    return enrichedViews;
}