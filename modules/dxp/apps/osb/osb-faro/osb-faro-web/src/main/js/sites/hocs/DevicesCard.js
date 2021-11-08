import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import SiteDevicesQuery from 'shared/queries/SiteDevicesQuery';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'shared/hoc/DevicesCard';

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
