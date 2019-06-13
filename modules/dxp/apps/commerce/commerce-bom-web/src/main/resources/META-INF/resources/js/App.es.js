import React from 'react';

import Datalist from './components/datalist/Datalist.es';
import PartFinder from './components/PartFinder.es';
import history from './utilities/history.es';

function App(props) {

  return (
    <div className="bom-wrapper container pt-3">
      <div className="mb-3">
        <Datalist 
          multiselect={false}
          spritemap={props.spritemap}
          datasourceSettings={{
            remote: {
              read: props.modelSelectorMakerEndpoint
            },
            labelField: 'name',
            valueField: 'id',
            on: {
              parseResponse: (response) => response.data,
              mapParameters: (data) => {
                return `/${
                  (data.filters && data.filters.length) 
                    ? data.filters[0].value 
                    : ''
                }`;
              }
            }
          }}
          connectorSettings={{
            id: 'firstDatalist'
          }}
        />

        <Datalist 
          multiselect={false}
          spritemap={props.spritemap}
          disabled={true}
          datasourceSettings={{
            remote: {
              read: props.modelSelectorModelEndpoint
            },
            labelField: 'name',
            valueField: 'id',
            on: {
              parseResponse: (response) => response.data,
              mapParameters: (data) => {
                  return `/${(data.filters && data.filters.length) ? data.filters[0].value : ''}`;
              }
            }
          }}
          connectorSettings={{
            id: 'secondDatalist',
            emitters: ['firstDatalist']
          }}
        />

        <Datalist 
          multiselect={false}
          spritemap={props.spritemap}
          disabled={true}
          datasourceSettings={{
            remote: {
              read: props.modelSelectorYearEndpoint
            },
            labelField: 'year',
            valueField: 'year',
            on: {
              parseResponse: (response) => response.data,
              mapParameters: (data) => {
                  return `/${(data.filters && data.filters.length) ? data.filters[0].value : ''}`;
              }
            }
          }}
          connectorSettings={{
            id: 'thirdDatalist',
            emitters: ['firstDatalist', 'secondDatalist']
          }}
          onchange={(values) => {
            if(values) {
              const id = values[0].value
              history.push('/folder/' + id)
            } else {
              history.push('/')
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
  )
}

export default App;