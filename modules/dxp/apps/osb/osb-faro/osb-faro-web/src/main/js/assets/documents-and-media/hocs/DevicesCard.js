import DocumentsAndMediaMetricsQuery from '../queries/DocumentsAndMediaMetricsQuery';
import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'shared/hoc/DevicesCard';

/**
 * HOC
 * @description Documents And Media Devices
 */
const withDocumentsAndMediaDevices = () =>
	graphql(
		DocumentsAndMediaMetricsQuery,
		getDevicesMapper(result => result.document.downloadsMetric)
	);

export default withDevicesCard(withDocumentsAndMediaDevices);
