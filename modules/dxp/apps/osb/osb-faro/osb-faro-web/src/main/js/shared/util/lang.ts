import Constants from 'shared/util/constants';

const {
	acquisitionTypes,
	assetTypes,
	compositionTypes,
	dataSourceTypes: {csv, liferay},
	entityTypes: {
		account,
		asset,
		dataSource,
		individual,
		individualsSegment,
		page
	},
	userRoleNames
} = Constants;

const SPLIT_REGEX = /({\d+})/g;

/**
 * Utility function for substituting variables into language keys.
 *
 * @example
 * sub(Liferay.Language.get('search-x'), ['all'])
 * => 'search all'
 * sub(Liferay.Language.get('search-x'), [<b>all<b>], false)
 * => 'search <b>all</b>'
 *
 * @param {string} langKey This is the language key used from our properties file
 * @param {array} args Arguments to pass into language key
 * @param {boolean} join Boolean used to indicate whether to call `.join()` on
 * the array before it is returned. Use `false` if subbing in JSX.
 * @return {(string|Array)}
 */
export const sub = (
	langKey: string,
	args: any[],
	join: boolean = true
): string | any[] => {
	const keyArray = langKey.split(SPLIT_REGEX).filter(val => val.length !== 0);

	for (let i = 0; i < args.length; i++) {
		const arg = args[i];

		const indexKey = `{${i}}`;

		let argIndex = keyArray.indexOf(indexKey);

		while (argIndex >= 0) {
			keyArray.splice(argIndex, 1, arg);

			argIndex = keyArray.indexOf(indexKey);
		}
	}

	return join ? (keyArray.join('') as string) : (keyArray as any[]);
};

export const getPluralMessage = (
	singular: string,
	plural: string,
	count: number = 0,
	toString?: boolean,
	subArray?: any[]
) => {
	const message = count === 1 ? singular : plural;

	return sub(message, subArray || [count.toLocaleString()], toString);
};

export const ACQUISITION_LABEL_MAP = {
	[acquisitionTypes.channel]: Liferay.Language.get('channel'),
	[acquisitionTypes.referrer]: Liferay.Language.get('referrer'),
	[acquisitionTypes.sourceMedium]: Liferay.Language.get('source-medium')
};

export const ASSET_TYPE_LANG_MAP = {
	[assetTypes.blog]: Liferay.Language.get('blog'),
	[assetTypes.document]: Liferay.Language.get('document'),
	[assetTypes.form]: Liferay.Language.get('form'),
	[assetTypes.webContent]: Liferay.Language.get('web-content'),
	[assetTypes.webPage]: Liferay.Language.get('page')
};

export const COMPOSITION_LABEL_MAP = {
	[compositionTypes.accountInterests]: Liferay.Language.get('interests'),
	[compositionTypes.acquisitions]: Liferay.Language.get('acquisitions'),
	[compositionTypes.segmentInterests]: Liferay.Language.get('interests'),
	[compositionTypes.siteInterests]: Liferay.Language.get('interests'),
	[compositionTypes.searchTerms]: Liferay.Language.get('search-terms')
};

export const DETAILS_LABEL_MAP = {
	accountNumber: Liferay.Language.get('account-number'),
	accountType: Liferay.Language.get('account-type'),
	annualRevenue: Liferay.Language.get('annual-revenue'),
	billingAddress: Liferay.Language.get('billing-address'),
	description: Liferay.Language.get('description'),
	fax: Liferay.Language.get('fax-number'),
	industry: Liferay.Language.get('industry'),
	name: Liferay.Language.get('name'),
	numberOfEmployees: Liferay.Language.get('employees'),
	ownership: Liferay.Language.get('ownership'),
	phone: Liferay.Language.get('phone-number'),
	shippingAddress: Liferay.Language.get('shipping-address'),
	website: Liferay.Language.get('website'),
	yearStarted: Liferay.Language.get('year-started')
};

const ENTITY_LANG_MAP = {
	[account]: Liferay.Language.get('accounts'),
	[asset]: Liferay.Language.get('assets'),
	[dataSource]: Liferay.Language.get('data-source'),
	[individual]: Liferay.Language.get('individuals'),
	[individualsSegment]: Liferay.Language.get('segments'),
	[page]: Liferay.Language.get('pages')
};

const DATA_SOURCE_LANG_MAP = {
	[csv]: Liferay.Language.get('csv'),
	[liferay]: Liferay.Language.get('liferay-dxp')
};

export const getDataSourceLangKey = (type: string): string =>
	DATA_SOURCE_LANG_MAP[type];

export const getTypeLangKey = (type: string): string => ENTITY_LANG_MAP[type];

export const getDisplayRole = (roleName: string): string => {
	const {administrator, member, owner} = userRoleNames;

	switch (roleName) {
		case administrator:
			return Liferay.Language.get('admin');
		case owner:
			return Liferay.Language.get('owner');
		case member:
			return Liferay.Language.get('member');
		default:
			return roleName;
	}
};

const LABELS_MAP = {
	desktop: Liferay.Language.get('desktop'),
	mobile: Liferay.Language.get('other-mobile'),
	smartphone: Liferay.Language.get('phone'),
	tablet: Liferay.Language.get('tablet'),
	tv: Liferay.Language.get('tv')
};

/**
 * Get Device Label
 * @param {string} deviceType
 */

export const getDeviceLabel = (deviceType: string): string =>
	LABELS_MAP[deviceType.toLowerCase()] || deviceType;
