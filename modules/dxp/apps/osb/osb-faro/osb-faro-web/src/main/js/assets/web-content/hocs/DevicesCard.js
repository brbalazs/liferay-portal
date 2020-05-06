import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import WebContentMetricsQuery from '../queries/WebContentMetricsQuery';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'cerebro-shared/hocs/DevicesCard';

/**
 * HOC
 * @description Web Content Devices
 */
const withWebContentDevices = () =>
	graphql(
		WebContentMetricsQuery,
		getDevicesMapper(result => result.journal.viewsMetric)
	);

export default withDevicesCard(withWebContentDevices);
