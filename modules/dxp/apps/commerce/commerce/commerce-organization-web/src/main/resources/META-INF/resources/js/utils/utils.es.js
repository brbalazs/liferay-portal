function serializeParams(params) {
	return Object.keys(params).map(key =>
		`${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`
	).join('&');
}

export function endpointBuilder({baseURL, id = '0', path = '', queryParams = {}}) {
	if (!baseURL) {
		throw new Error('No API baseURL provided.')
	}

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

export function bindAll() {
	const clazz = arguments[0];

	Object.keys(arguments).forEach((argNo, i) => {
		const method = arguments[argNo];

		if (i !== 0 && typeof clazz[method] === 'function') {
			clazz[method] = clazz[method].bind(clazz);
		}
	});

	return clazz;
}

function isInRange(value, lower, upper) {
	return value >= lower && value < upper;
}

export function getColorHue(prevHue) {
	const hue = Math.random() * 360;

	return !prevHue ?
		hue : isInRange(hue, (prevHue - 2), (prevHue + 3)) ?
			((hue + 5) > 360) ? hue - 5 : hue + 5 : hue;
}

export function setupDataset(data) {
	const sanitizedData = Object.assign({}, data);

	sanitizedData.organizations.length &&
	sanitizedData.organizations.forEach((orgObject, index) => {
		delete orgObject['organizations'];

		const prevColor = !!index ?
			sanitizedData.organizations[index - 1] : null;

		orgObject['colorIdentifier'] = `hsl(${getColorHue(prevColor)},75%,75%)`;
	});

	return sanitizedData;
}
