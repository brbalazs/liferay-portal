import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import getCN from 'classnames';
import React, {useEffect, useState} from 'react';
import Thumbs from './Thumbs';
import {ASSET_METRICS} from 'shared/util/constants';
import {isEmpty} from 'lodash';
import {PropTypes} from 'prop-types';

const CLASSNAME = 'analytics-add-report';

const AddReport = ({className, isEmptyDashboard = false, onGetReport}) => {
	const [isEnableToSave, setIsEnableToSave] = useState(false);
	const [report, setReport] = useState({
		chartType: '',
		metric: '',
		title: ''
	});
	const [showFormAddReport, setShowFormAddReport] = useState(false);

	const enableButtonSave = () => {
		setIsEnableToSave(true);

		Object.keys(report).forEach(key => {
			if (isEmpty(report[key])) {
				setIsEnableToSave(false);

				return;
			}
		});
	};

	useEffect(() => {
		enableButtonSave();
	}, [report]);

	const openReport = () => {
		setShowFormAddReport(true);
	};

	const closeReport = () => {
		setShowFormAddReport(false);
	};

	const handleClickAddReport = () => {
		openReport();

		setReport({
			chartType: '',
			metric: '',
			title: ''
		});
	};

	const handleClickCancelReport = () => {
		closeReport();
	};

	const handleClickSaveReport = () => {
		onGetReport(report);

		closeReport();
	};

	const handleChangeReportTitle = ({target}) => {
		setReport({
			...report,
			title: target.value.trim().slice(0, 90)
		});
	};

	const handleChangeSelectMetric = ({target}) => {
		setReport({
			...report,
			metric: target.value
		});
	};

	const handleGetSelectedChartType = ({value}) => {
		setReport({
			...report,
			chartType: value
		});
	};

	const renderThumbCharts = () => {
		const items = [
			{
				selected: true,
				svg: 'cerebro-thumb-line-chart',
				text: Liferay.Language.get(
					'not-possible-to-change-the-visualization-type'
				),
				value: 'line'
			}
		];

		return (
			<div className='form-group'>
				<label>{Liferay.Language.get('visualization')}</label>

				<Thumbs
					items={items}
					onSelectThumb={handleGetSelectedChartType}
				/>
			</div>
		);
	};

	const renderInputSelectMetric = () => (
		<div className='form-group'>
			<label htmlFor='metricSelector'>
				{Liferay.Language.get('metric')}
			</label>

			<select
				className='form-control'
				id='metricSelector'
				onBlur={handleChangeSelectMetric}
				onChange={handleChangeSelectMetric}
			>
				<option defaultValue value=''>
					{Liferay.Language.get('select-a-metric')}
				</option>

				{ASSET_METRICS.sort((p, c) =>
					p.selectTitle.localeCompare(c.selectTitle)
				).map(({key, selectTitle}) => (
					<option key={key} value={key}>
						{selectTitle}
					</option>
				))}
			</select>
		</div>
	);

	const renderInputReportName = () => (
		<div className='form-group'>
			<label htmlFor='reportNameInput'>
				{Liferay.Language.get('report-name')}
			</label>

			<input
				className='form-control'
				id='reportNameInput'
				maxLength={90}
				onInput={handleChangeReportTitle}
				placeholder={Liferay.Language.get(
					'enter-a-name-for-this-report'
				)}
				type='text'
			/>
		</div>
	);

	const renderFormAddReport = () => (
		<div className='w-100'>
			<div className='row'>
				<div className='col-sm-4'>{renderInputReportName()}</div>
			</div>

			<div className='row'>
				<div className='col-sm-4'>{renderInputSelectMetric()}</div>
			</div>

			<div className='row'>
				<div className='col-sm-12'>{renderThumbCharts()}</div>
			</div>
		</div>
	);

	const renderCardAddReport = () => (
		<>
			<Card.Header>
				<Card.Title>{Liferay.Language.get('add-report')}</Card.Title>
			</Card.Header>
			<Card.Body>{renderFormAddReport()}</Card.Body>
			<Card.Footer>
				<Button
					className='mr-4'
					disabled={!isEnableToSave}
					display='primary'
					onClick={isEnableToSave ? handleClickSaveReport : undefined}
				>
					{Liferay.Language.get('save')}
				</Button>

				<Button onClick={handleClickCancelReport}>
					{Liferay.Language.get('cancel')}
				</Button>
			</Card.Footer>
		</>
	);

	const renderAddButton = () => (
		<div className={`${CLASSNAME}-button`}>
			<Button onClick={handleClickAddReport}>
				{Liferay.Language.get('add-report')}
			</Button>
		</div>
	);

	const classnames = getCN(CLASSNAME, className, {
		'analytics-add-report-empty-dashboard': isEmptyDashboard
	});

	return (
		<Card className={classnames}>
			{!showFormAddReport ? renderAddButton() : renderCardAddReport()}
		</Card>
	);
};

AddReport.propTypes = {
	/**
	 * Renders the card with the higher height
	 * @type {?boolean}
	 * @default false
	 */
	isEmptyDashboard: PropTypes.bool,
	onGetReport: PropTypes.func.isRequired
};

export default AddReport;
