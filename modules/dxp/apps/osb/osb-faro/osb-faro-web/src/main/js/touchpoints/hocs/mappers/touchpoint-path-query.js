import {Colors} from 'shared/util/charts';
import {getVariables, safeResultToProps} from 'shared/util/mappers';
import {removeProtocol} from 'shared/util/util';

const mapResultToProps = safeResultToProps(({page}, context, {router}) => {
	const {
		directAccessMetric,
		indirectAccessMetric,
		pageReferrerMetrics,
		viewsMetric
	} = page;

	const {title, touchpoint} = router.params;

	const referrers = pageReferrerMetrics.filter(
		({accessMetric: {value}}) => value > 0
	);

	let nodes = referrers.map(({assetTitle, external, referrer}) => {
		const url = referrer || '';

		const node = {
			external,
			name:
				url === 'others'
					? Liferay.Language.get('others')
					: assetTitle || removeProtocol(url),
			url
		};

		if (url === 'others') {
			node.color = Colors.gray;
		}

		return node;
	});

	nodes = [
		...nodes,
		{
			directAccessMetric: directAccessMetric.value,
			indirectAccessMetric: indirectAccessMetric.value,
			name: title,
			total: viewsMetric.value,
			url: touchpoint
		}
	];

	const links = referrers.map(({accessMetric}, index) => ({
		source: index,
		target: nodes.length - 1,
		value: accessMetric.value
	}));

	return {
		data: {
			links,
			nodes
		}
	};
});

/**
 * Map Props to Options
 * @param {object} param0 props
 * @param {object} param1 context
 */
const mapPropsToOptions = ({filters, rangeSelectors, router: {params}}) =>
	getVariables({filters, params, rangeSelectors});

export {mapPropsToOptions, mapResultToProps};
