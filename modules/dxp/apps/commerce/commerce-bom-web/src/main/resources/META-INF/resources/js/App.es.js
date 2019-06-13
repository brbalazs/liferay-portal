import React from 'react';

import Datalist from './components/datalist/Datalist.es';
import PartFinder from './components/PartFinder.es';
import history from './utilities/history.es';
import LocalizedText from './components/utilities/LocalizedText.es';

import { convertString } from './utilities/localization.es';

function convertFiltersToQueryString(filters) {
  return filters.reduce((queryParams, current, i) => {

    const value = Array.isArray(current.value) 
      ? current.value.join(',') 
      : current.value

    return queryParams + (
      current.field + '=' + value + ((i !== (filters.length - 1) ? '&' : ''))
    )
  }, '')
}

function App(props) {
	return (
		<div className="bom-wrapper container pt-3">
			<div className="mb-3">
				<Datalist
					label={
						<LocalizedText desc="Car Maker">
							car-maker
						</LocalizedText>
					}
					additionalClasses="mr-3"
					multiselect={false}
					placeholder={convertString('search-input')}
					spritemap={props.spritemap}
					datasourceSettings={{
						remote: {
							read: props.modelSelectorMakerEndpoint
						},
						labelField: 'name',
						valueField: 'id',
						on: {
							parseResponse: response => response.data,
							mapParameters: data => {
								return `/${
									data.filters && data.filters.length
										? convertFiltersToQueryString(data.filters)
										: ''
								}`;
							}
						}
					}}
					connectorSettings={{
						id: 'carMakerDatalist'
					}}
				/>

				<Datalist
					label={<LocalizedText desc="Model">model</LocalizedText>}
					additionalClasses="mr-3"
					multiselect={false}
					placeholder={convertString('search-input')}
					spritemap={props.spritemap}
					disabled={true}
					datasourceSettings={{
						remote: {
							read: props.modelSelectorModelEndpoint
						},
						labelField: 'name',
						valueField: 'id',
						on: {
							parseResponse: response => response.data,
							mapParameters: data => {
								return `/${
									data.filters && data.filters.length
										? convertFiltersToQueryString(data.filters)
										: ''
								}`;
							}
						}
					}}
					connectorSettings={{
						id: 'modelDatalist',
						emitters: ['carMakerDatalist'],
						on: {
              notified: (values, setState, datasource) => {
                const emittersHaveValuesSelected = Object.values(values).reduce(
									(acc, el) => acc && !!el,
									true
                );

								if (emittersHaveValuesSelected) {
									setState({
										disabled: false
                  });
                  datasource.setFilter('car-maker', values['carMakerDatalist'])
									datasource.read();
								} else {
                  datasource.setFilter('car-maker', null)
                  datasource.setFilter('keyword', null)
									setState({
										disabled: true,
										data: null,
										selected: null
									});
								}
							}
						}
					}}
				/>

				<Datalist
					label={<LocalizedText desc="Year">year</LocalizedText>}
					multiselect={false}
					placeholder={convertString('search-input')}
					spritemap={props.spritemap}
					disabled={true}
					datasourceSettings={{
						remote: {
							read: props.modelSelectorYearEndpoint
						},
						labelField: 'year',
						valueField: 'year',
						on: {
							parseResponse: response => response.data,
							mapParameters: data => {
								return `/${
									data.filters && data.filters.length
										? convertFiltersToQueryString(data.filters)
										: ''
								}`;
							}
						}
					}}
					connectorSettings={{
						id: 'yearDatalist',
            emitters: ['carMakerDatalist', 'modelDatalist'],
            on: {
              notified: (values, setState, datasource) => {
                const emittersHaveValuesSelected = Object.values(values).reduce(
									(acc, el) => acc && !!el,
									true
                );

								if (emittersHaveValuesSelected) {
									setState({
										disabled: false
                  });
                  datasource.setFilter('car-maker', values['carMakerDatalist'])
                  datasource.setFilter('model', values['modelDatalist'])
									datasource.read();
								} else {
                  datasource.setFilter('model', null)
                  datasource.setFilter('car-maker', null)
                  datasource.setFilter('keyword', null)
									setState({
										disabled: true,
										data: null,
										selected: null
									});
								}
							}
						}
					}}
					onchange={values => {
						if (values) {
							const id = values[0].value;
							history.push('/folder/' + id);
						} else {
							history.push('/');
						}
					}}
				/>
			</div>

			<PartFinder
				spritemap={props.spritemap}
				areaApiEndpoint={props.areasEndpoint}
				foldersApiEndpoint={props.foldersEndpoint}
			/>
		</div>
	);
}

export default App;
