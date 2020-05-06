import * as API from 'shared/api';
import Modal from 'shared/components/modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';
const ROW_DELTA = 10;

export default class CSVPreviewModal extends React.Component {
	static defaultProps = {
		onClose: noop
	};

	static propTypes = {
		fileVersionId: PropTypes.oneOfType([
			PropTypes.string,
			PropTypes.number
		]),
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string,
		name: PropTypes.string,
		onClose: PropTypes.func
	};

	state = {
		data: [],
		numOfRows: 0
	};

	componentDidMount() {
		this.getCSVData();
	}

	getCSVData() {
		const {fileVersionId, groupId, id} = this.props;

		const count = this.state.numOfRows + ROW_DELTA;

		this.setState({
			numOfRows: count
		});

		API.dataSource
			.fetchFieldValues({count, fileVersionId, groupId, id})
			.then(data => {
				this.setState({
					data
				});
			})
			.catch(noop);
	}

	render() {
		const {
			props: {name, onClose, ...otherProps},
			state: {data}
		} = this;

		return (
			<Modal
				className={
					`csv-preview-modal-root${this.props.className}`
						? ` ${this.props.className}`
						: ''
				}
				size='lg'
				{...omitDefinedProps(otherProps, CSVPreviewModal.propTypes)}
			>
				<Modal.Header
					onClose={onClose}
					title={sub(Liferay.Language.get('data-preview-x'), [name])}
				/>

				<table>
					<tr>
						{data.map(({name}, i) => (
							<th key={i}>{name}</th>
						))}
					</tr>

					{!!data.length &&
						data[0].values.map((val, row) => (
							<tr key={row}>
								{data.map((val, column) => (
									<td key={`${column}-${row}`}>
										{data[column].values[row]}
									</td>
								))}
							</tr>
						))}
				</table>
			</Modal>
		);
	}
}
