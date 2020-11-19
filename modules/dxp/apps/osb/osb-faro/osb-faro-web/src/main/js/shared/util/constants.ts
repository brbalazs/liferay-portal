import {Align} from 'metal-position';

export const DEVELOPER_MODE = FARO_ENV === 'asah-local' || FARO_ENV === 'local';

/**
 * Metal-Position Alignments
 */
export const ALIGNMENTS_MAP = {
	bottom: Align.Bottom,
	'bottom-left': Align.BottomLeft,
	'bottom-right': Align.BottomRight,
	left: Align.Left,
	right: Align.Right,
	top: Align.Top,
	'top-left': Align.TopLeft,
	'top-right': Align.TopRight
};

export const POSITIONS = [
	'top',
	'top',
	'right',
	'bottom',
	'bottom',
	'bottom',
	'left',
	'top'
];

/**
 * Assets
 */
const acquisitionTypes = {
	channel: 'CHANNEL',
	referrer: 'REFERRER',
	sourceMedium: 'SOURCE_MEDIUM'
};

export const assetTypes = {
	asset: 'Asset',
	blog: 'Blog',
	document: 'Document',
	form: 'Form',
	webContent: 'WebContent',
	webPage: 'Page'
};

const average = Liferay.Language.get('average').toLowerCase();
const ratio = Liferay.Language.get('ratio').toLowerCase();
const sum = Liferay.Language.get('sum').toLowerCase();

export const ASSET_METRICS = [
	{
		key: 'abandonmentsMetric',
		selectTitle: `${Liferay.Language.get('form-abandonment')} (${ratio})`,
		title: Liferay.Language.get('form-abandonment'),
		type: 'percentage'
	},
	{
		key: 'clicksMetric',
		selectTitle: `${Liferay.Language.get('asset-clicks')} (${sum})`,
		title: Liferay.Language.get('clicks'),
		type: 'number'
	},
	{
		key: 'completionTimeMetric',
		selectTitle: `${Liferay.Language.get(
			'form-completion-time'
		)} (${average})`,
		title: Liferay.Language.get('completion-time'),
		type: 'time'
	},
	{
		key: 'downloadsMetric',
		selectTitle: `${Liferay.Language.get('asset-downloads')} (${sum})`,
		title: Liferay.Language.get('downloads'),
		type: 'number'
	},
	{
		key: 'readingTimeMetric',
		selectTitle: `${Liferay.Language.get(
			'asset-interaction-time'
		)} (${average})`,
		title: Liferay.Language.get('interaction-time'),
		type: 'time'
	},
	{
		key: 'submissionsMetric',
		selectTitle: `${Liferay.Language.get('form-submissions')} (${sum})`,
		title: Liferay.Language.get('form-submissions'),
		type: 'number'
	},
	{
		key: 'viewsMetric',
		selectTitle: `${Liferay.Language.get('asset-views')} (${sum})`,
		title: Liferay.Language.get('views'),
		type: 'number'
	}
];

export const assetNames = {
	blogViewed: 'blogViewed',
	commentPosted: 'commentPosted',
	documentDownloaded: 'documentDownloaded',
	documentPreviewed: 'documentPreviewed',
	formSubmitted: 'formSubmitted',
	formViewed: 'formViewed',
	pageViewed: 'pageViewed',
	webContentViewed: 'webContentViewed'
};

const compositionTypes = {
	accountInterests: 'accountInterests',
	acquisitions: 'acquisitions',
	individualInterests: 'individualInterests',
	searchTerms: 'searchTerms',
	segmentInterests: 'individualSegmentInterests',
	siteInterests: 'siteInterests'
};

export enum credentialTypes {
	oAuth1 = 'OAuth 1 Authentication',
	oAuth2 = 'OAuth 2 Authentication',
	token = 'Token Authentication'
}

export enum dataSourceStates {
	actionNeeded = 'ACTION_NEEDED',
	analyticsClientConfigurationFailure = 'ANALYTICS_CLIENT_CONFIGURATION_FAILURE',
	credentialsInvalid = 'CREDENTIALS_INVALID',
	credentialsValid = 'CREDENTIALS_VALID',
	disconnected = 'DISCONNECTED',
	inProgressDeleting = 'IN_PROGRESS_DELETING',
	liferayVersionInvalid = 'LIFERAY_VERSION_INVALID',
	undefinedError = 'UNDEFINED_ERROR',
	ready = 'READY',
	urlInvalid = 'URL_INVALID',
	unconfigured = 'UNCONFIGURED'
}

export enum userStatuses {
	approved = 0,
	pending = 1,
	requested = 2
}

/**
 * GDPR
 */
export enum GDPR_REQUEST_STATUSES {
	COMPLETED = 'COMPLETED',
	ERROR = 'ERROR',
	EXPIRED = 'EXPIRED',
	PENDING = 'PENDING',
	RUNNING = 'RUNNING'
}

export enum GDPR_REQUEST_TYPES {
	ACCESS = 'ACCESS',
	DELETE = 'DELETE',
	SUPPRESS = 'SUPPRESS',
	UNSUPPRESS = 'UNSUPPRESS'
}

/**
 * TimeRange
 */
export const CUSTOM_RANGE = 'CUSTOM';
export const LAST_180_DAYS = '180';
export const LAST_24_HOURS = '0';
export const LAST_28_DAYS = '28';
export const LAST_30_DAYS = '30';
export const LAST_7_DAYS = '7';
export const LAST_90_DAYS = '90';
export const LAST_YEAR = '365';
export const YESTERDAY = '1';

export const TIME_RANGE_LABELS = {
	[LAST_180_DAYS]: Liferay.Language.get('last-180-days'),
	[LAST_24_HOURS]: Liferay.Language.get('last-24-hours'),
	[LAST_28_DAYS]: Liferay.Language.get('last-28-days'),
	[LAST_30_DAYS]: Liferay.Language.get('last-30-days'),
	[LAST_7_DAYS]: Liferay.Language.get('last-seven-days'),
	[LAST_90_DAYS]: Liferay.Language.get('last-90-days'),
	[LAST_YEAR]: Liferay.Language.get('last-year'),
	[YESTERDAY]: Liferay.Language.get('yesterday')
};

/**
 * Sprite
 */
export const spritemap = '/o/osb-faro-web/dist/sprite.svg';

/**
 * AudienceCard viewer mode
 */
export enum AUDIENCE_VIEWER_MODE {
	PREVIEW = 'PREVIEW',
	VIEW = 'VIEW'
}

/**
 * Jobs
 */
export enum jobRunStatuses {
	completed = 'COMPLETED',
	failed = 'FAILED',
	published = 'PUBLISHED',
	running = 'RUNNING'
}

export enum jobStatuses {
	failed = 'FAILED',
	pending = 'PENDING',
	ready = 'READY',
	running = 'RUNNING',
	scheduled = 'SCHEDULED'
}

export enum jobRunFrequencies {
	every7Days = 'EVERY_7_DAYS',
	every14Days = 'EVERY_14_DAYS',
	every30Days = 'EVERY_30_DAYS',
	manual = 'MANUAL'
}

export enum jobRunDataPeriods {
	last7Days = 'LAST_7_DAYS',
	last30Days = 'LAST_30_DAYS',
	last180Days = 'LAST_180_DAYS',
	last365Days = 'LAST_365_DAYS'
}

export enum jobTypes {
	itemSimilarity = 'CONTENT_RECOMMENDATION_ITEM_SIMILARITY'
}

const Constants: any = {
	...window.faroConstants,
	acquisitionTypes,
	assetNames,
	assetTypes,
	compositionTypes,
	credentialTypes,
	dataSourceStates
};

export default Constants;
