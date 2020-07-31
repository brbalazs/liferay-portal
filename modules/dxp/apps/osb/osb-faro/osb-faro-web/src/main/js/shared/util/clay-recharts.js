/**
 * © 2018 Liferay, Inc. <https://liferay.com>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

const TEXT_PADDING = 4;

export const AXIS = {
	borderStroke: '#E7E7ED',
	font:
		'14px "Source Sans Pro", "Source Sans, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
	gridStroke: '#E7E7ED',
	textColor: '#6B6C7E'
};

export const BAR = {
	defaultFill: '#97C5FF',
	hoverFill: '#4B9BFF',
	selectedFill: '#0071FD'
};

export function getTextWidth(text, font = '14px Source Sans Pro') {
	const canvas =
		getTextWidth.canvas ||
		(getTextWidth.canvas = document.createElement('canvas'));
	const context = canvas.getContext('2d');
	context.font = font;
	const metrics = context.measureText(text);

	return Math.ceil(metrics.width) + TEXT_PADDING;
}
