import Table from '../content_renderer/table/Table.es'
import EmailsList from '../content_renderer/emails_list/EmailsList';

export const defaultContentRenderers = [
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

export function getContentRenderers(contentRenderers) {
    if(!contentRenderers) {
        return defaultContentRenderers;
    }

    const enrichedRenderers = contentRenderers.map(contentRenderer => {
        if(!contentRenderer.component) {
            const matchedDefaultComponent = defaultContentRenderers.find(
                defaultRenderer => defaultRenderer.id === contentRenderer.id
            )

            return {
                ...contentRenderer,
                component: matchedDefaultComponent.component
            }
        }
        return contentRenderer
    })

    return enrichedRenderers;

}