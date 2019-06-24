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

export function getColorHue(prevHue) {
	const hue = Math.random() * 360;

	return !prevHue ?
		hue : _.inRange(hue, (prevHue - 4), (prevHue + 5)) ?
			hue + 4 : hue;
}

export function setupDataset(data) {
	const sanitizedData = Object.assign({}, data);

	sanitizedData.organizations.length &&
	sanitizedData.organizations.forEach((orgObject, index) => {
		delete orgObject['organizations'];

		const prevColor = !!index ?
			sanitizedData.organizations[index - 1] : null;

		orgObject['colorIdentifier'] = `hsl(${getColorHue(prevColor)},75%,85%)`;

	});

	return sanitizedData;
}
