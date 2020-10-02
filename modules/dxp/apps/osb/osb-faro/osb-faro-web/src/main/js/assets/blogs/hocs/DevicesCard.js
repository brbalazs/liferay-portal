import BlogMetricsQuery from '../queries/BlogMetricsQuery';
import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'shared/hoc/DevicesCard';

/**
 * HOC
 * @description Blogs Devices
 */
const withBlogsDevices = () =>
	graphql(
		BlogMetricsQuery,
		getDevicesMapper(result => result.blog.viewsMetric)
	);

export default withDevicesCard(withBlogsDevices);
