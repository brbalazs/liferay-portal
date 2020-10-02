import FormMetricsQuery from '../queries/FormMetricsQuery';
import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'shared/hoc/DevicesCard';

/**
 * HOC
 * @description Forms Devices
 */
const withFormsDevices = () =>
	graphql(
		FormMetricsQuery,
		getDevicesMapper(result => result.form.submissionsMetric)
	);

export default withDevicesCard(withFormsDevices);
