import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import SiteDevicesQuery from '../queries/SiteDevicesQuery';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'cerebro-shared/hocs/DevicesCard';

/**
 * HOC
 * @description Site Devices
 */
const withSiteDevices = () =>
	graphql(
		SiteDevicesQuery,
		getDevicesMapper(result => result.site.sessionsMetric)
	);

export default withDevicesCard(withSiteDevices);
