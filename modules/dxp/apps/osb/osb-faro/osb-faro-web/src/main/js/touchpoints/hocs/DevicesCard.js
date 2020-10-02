import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import TouchpointMetricsQuery from '../queries/TouchpointMetricsQuery';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'shared/hoc/DevicesCard';

/**
 * HOC
 * @description Touchpoint Devices
 */
const withTouchpointDevices = () =>
	graphql(
		TouchpointMetricsQuery,
		getDevicesMapper(result => result.page.viewsMetric)
	);

export default withDevicesCard(withTouchpointDevices);
