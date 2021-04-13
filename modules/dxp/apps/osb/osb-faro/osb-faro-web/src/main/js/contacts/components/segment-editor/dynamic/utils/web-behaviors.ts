import FaroConstants, {AssetTypes} from 'shared/util/constants';
import {List} from 'immutable';
import {Property} from 'shared/util/records';

const {
	assetNames: {
		blogViewed,
		commentPosted,
		documentDownloaded,
		documentPreviewed,
		formSubmitted,
		formViewed,
		pageViewed,
		webContentViewed
	}
} = FaroConstants;

const createWebProperty = ({
	entityType,
	label,
	name,
	type = 'behavior'
}: {
	entityType: string;
	label: string;
	name: string;
	type: string;
}): Property =>
	new Property({
		entityName: Liferay.Language.get('individual'),
		entityType,
		label,
		name,
		propertyKey: 'web',
		type
	});

const WEB_BEHAVIORS = List(
	[
		{
			entityType: AssetTypes.Document,
			label: Liferay.Language.get('downloaded-document-&-media'),
			name: documentDownloaded
		},
		{
			entityType: AssetTypes.Form,
			label: Liferay.Language.get('submitted-form'),
			name: formSubmitted
		},
		{
			entityType: AssetTypes.Form,
			label: Liferay.Language.get('viewed-form'),
			name: formViewed
		},
		{
			entityType: AssetTypes.WebPage,
			label: Liferay.Language.get('viewed-page'),
			name: pageViewed
		},
		{
			entityType: AssetTypes.Blog,
			label: Liferay.Language.get('commented-on-blog'),
			name: commentPosted
		},
		{
			entityType: AssetTypes.Blog,
			label: Liferay.Language.get('viewed-blog'),
			name: blogViewed
		},
		{
			entityType: AssetTypes.Document,
			label: Liferay.Language.get('viewed-document-&-media'),
			name: documentPreviewed
		},
		{
			entityType: AssetTypes.WebContent,
			label: Liferay.Language.get('viewed-web-content'),
			name: webContentViewed
		}
	].map(createWebProperty)
);

export default WEB_BEHAVIORS;
