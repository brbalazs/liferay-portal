import React, { useContext } from 'react';

import { StoreContext } from '../StoreContext.es';
import LocalizedText from '../utilities/LocalizedText.es';

function DetailsListElement(props) {
    const { state, actions } = useContext(StoreContext);

    const highlightedModifierClass = 
        (
            state.area.highlightedDetail && 
            state.area.highlightedDetail.number === props.number 
        )
        ? ' detail-row--highlighted' 
        : '';

    return (
        <a
            className={`detail-row d-table-row${highlightedModifierClass}`} 
            href={props.url}
            onFocus={() => actions.highlightDetail(props.number, true)}
            onMouseOver={() => actions.highlightDetail(props.number, true)}
            onMouseOut={() => actions.highlightDetail(null)}
        >
            <div className="d-table-cell">
                <span className="autocomplete-item">{props.number}</span>
            </div>
            <div className="d-table-cell">
                {props.name}
            </div>
            <div className="d-table-cell u-tar">{props.sku}</div>
        </a>
    )
}

function DetailsBox() {
    const { state } = useContext(StoreContext);

    const list = state.area.products.map(product => {
        return {
            ...product,
            number: state.area.spots.reduce(
                (number, spot) => number || (spot.rel === product.id && spot.number),
                null
            )
        }
    })

    return (
        <div className="panel panel-secondary grid-panel">
            <div className="panel-heading">
                <h2 className="panel-title">{state.area.name}</h2>
            </div>
            <div className="panel-body">
                <div className="products-table commerce-small-table d-table">
                    <div className="d-table-head-group">
                        <div className="d-table-row">
                            <div className="d-table-cell">
                                <LocalizedText desc="N*">n.</LocalizedText>
                            </div>
                            <div className="d-table-cell">
                                <LocalizedText desc="Name">name</LocalizedText>
                            </div>
                            <div className="d-table-cell u-tar">
                                <LocalizedText desc="Sku">sku</LocalizedText>
                            </div>
                        </div>
                    </div>
                    <div className="d-table-row-group">
                        { list.map((detail, i) => <DetailsListElement key={i} {...detail} />) }
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DetailsBox;
