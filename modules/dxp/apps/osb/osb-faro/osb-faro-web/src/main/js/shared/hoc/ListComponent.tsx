import Table from 'shared/components/table';
import {compose, withPaginationBar, withToolbar} from 'shared/hoc';
import {withEmpty} from 'cerebro-shared/hocs/utils';
import {withError} from './util';

const ListComponent = compose<any>(
	withToolbar(),
	withPaginationBar(),
	withError(),
	withEmpty()
)(Table);

export default ListComponent;
