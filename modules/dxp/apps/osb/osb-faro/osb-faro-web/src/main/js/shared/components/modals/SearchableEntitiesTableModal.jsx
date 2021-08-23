import Button from 'shared/components/Button';
import Modal from 'shared/components/modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {noop, omit} from 'lodash';
import {PropTypes} from 'prop-types';
import {withStatefulPagination} from 'shared/hoc';

const SearchableTable = withStatefulPagination(
	SearchableEntityTable,
	({defaultParams}) => ({defaultDelta: 10, ...defaultParams}),
	props => omit(props, 'onSearchValueChange')
);

export default class SearchableEntitiesTableModal extends React.Component {
	static defaultProps = {
		defaultParams: {},
		onClose: noop,
		size: 'xxl',
		title: 'entities'
	};

	static propTypes = {
		defaultParams: PropTypes.object,
		onClose: PropTypes.func,
		size: PropTypes.string,
		title: PropTypes.oneOfType([PropTypes.string, PropTypes.array])
	};

	constructor(props) {
		super(props);
	}

	render() {
		const {
			className,
			defaultParams,
			onClose,
			size,
			title,
			...otherProps
		} = this.props;

		return (
			<Modal className={className} size={size}>
				<Modal.Header onClose={onClose} title={title} />

				<SearchableTable
					{...omitDefinedProps(
						otherProps,
						SearchableEntitiesTableModal.propTypes
					)}
					defaultParams={defaultParams}
					toolbarProps={{autoFocus: true}}
				/>

				<Modal.Footer>
					<Button display='primary' onClick={onClose}>
						{Liferay.Language.get('done')}
					</Button>
				</Modal.Footer>
			</Modal>
		);
	}
}
