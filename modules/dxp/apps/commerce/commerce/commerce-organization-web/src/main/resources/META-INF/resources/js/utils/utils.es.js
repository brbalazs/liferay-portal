export function fetchData(id) {
	return fetch(`/api/${id}.json`);
}

function serializeParams(params) {
	return Object.keys(params).map(key =>
		`${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`
	).join('&');
}

function endpointBuilder({baseURL, id = '0', path = '', queryParams = {}}) {
	const root = `${baseURL}/organizations`,
		organizationId = `/${id}`,
		collection = `/${path}`;

	let parameters = '';

	if (path) {
		parameters = `?${serializeParams(queryParams)}`;
	}

	return `${root}${organizationId}${collection}${parameters}`;
}

export function callApi(parameters) {
	return fetch(endpointBuilder(parameters))
		.then(response => response.json())
		.catch(e => {});
}

export const noop = () => {};

export const isNumber = value => typeof value === 'number';

export const isArray = value => value instanceof Array;

export function truncateTextNode() {
	let text = this.innerHTML,
		textLength = this.getComputedTextLength();

	while (textLength > 220) {
		text = text.slice(0, -3);

		this.innerHTML = `${text}...`;

		textLength = this.getComputedTextLength();
	}
}

/*
export const Liferay = {
	Language: {
		available: {
			'ca_ES': 'Catalan (Spain)',
			'zh_CN': 'Chinese (China)',
			'en_US': 'English (United States)',
			'fi_FI': 'Finnish (Finland)',
			'fr_FR': 'French (France)',
			'de_DE': 'German (Germany)',
			'iw_IL': 'Hebrew (Israel)',
			'hu_HU': 'Hungarian (Hungary)',
			'ja_JP': 'Japanese (Japan)',
			'pt_BR': 'Portuguese (Brazil)',
			'es_ES': 'Spanish (Spain)'
		},
		direction: {
			'ca_ES': 'ltr',
			'zh_CN': 'ltr',
			'en_US': 'ltr',
			'fi_FI': 'ltr',
			'fr_FR': 'ltr',
			'de_DE': 'ltr',
			'iw_IL': 'rtl',
			'hu_HU': 'ltr',
			'ja_JP': 'ltr',
			'pt_BR': 'ltr',
			'es_ES': 'ltr'
		},
		get(D) {
			const E = arguments.length > 1 ? Array.prototype.join.call(arguments, o) : String(D);

			if (!(E in Liferay.Language.available)) {
				Liferay.Language.available[E] = C.apply(C, arguments)
			}

			return Liferay.Language.available[E]
		}
	}
};
*/
