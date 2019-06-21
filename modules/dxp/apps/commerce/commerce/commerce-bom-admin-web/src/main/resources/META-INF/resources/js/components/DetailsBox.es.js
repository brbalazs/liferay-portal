import React, { useContext } from 'react';

import { StoreContext } from './StoreContext.es';

import LocalizedText from './utilities/LocalizedText.es';

function DetailsListElement(props) {
    const {
        state,
        actions
    } = useContext(StoreContext);

    const highlightedModifierClass = 
        (
            state.area.highlightedDetail && 
            state.area.highlightedDetail.number === props.number 
        )
        ? 'table-active' 
        : '';

    return (
        <tr 
            className={highlightedModifierClass}
            onMouseOver={() => actions.highlightDetail(props.number)}
            onMouseOut={() => actions.highlightDetail(null)}
        >
            <td>{props.number}</td>
            <td>
                <a href={props.url}>
                    {props.name}
                </a>
            </td>
            <td>{props.sku}</td>
        </tr>
    )
}

function DetailsBox() {
    const { state } = useContext(StoreContext);

    const spotsFilteredByNumbers = state.area.spots.reduce((filtered, spot, i) => {
        if(i && filtered[filtered.length - 1].number === spot.number) {
            return filtered;
        }
        return filtered.concat(spot);
    },[])

    const list = spotsFilteredByNumbers.map(spot => {
        const relatedProduct = state.area.mappedProducts.reduce(
            (acc, prod) => acc || (prod.id === spot.productId ? prod : false),
            false
        );
        return {
            number: spot.number,
            name: relatedProduct.name,
            sku: relatedProduct.sku,
            url: relatedProduct.url
        }
    })

    return (
        <div className="panel panel-secondary h-100">
            <div className="panel-header panel-heading">
                <span className="panel-title">
                    <LocalizedText desc="Mapped products">mapped-products</LocalizedText>
                </span>
            </div>
            <div className="panel-body">
                {
                    list && list.length 
                    ? (
                        <div className="table-responsive-sm">
                            <table className="show-quick-actions-on-hover table table-autofit table-list">
                                <thead>
                                    <tr>
                                        <th>
                                            <LocalizedText desc="N*">n</LocalizedText>
                                        </th>
                                        <th className="table-cell-expand">
                                            <LocalizedText desc="Name">name</LocalizedText>
                                        </th>
                                        <th>
                                            <LocalizedText desc="Sku">sku</LocalizedText>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    { list && list.map((detail, i) => <DetailsListElement key={i} {...detail} />) }
                                </tbody>
                            </table>
                        </div>
                    ) : (
                        <div className="text-center my-5 p-5 w-100">
                            <h3>
                                <LocalizedText desc="No products mapped yet!">no-products-mapped-yet</LocalizedText>
                            </h3>
                            <p>
                                <LocalizedText desc="Click on the picture to start mapping products!">click-on-the-picture-to-start-mapping-products</LocalizedText>
                            </p>
                        </div>
                    ) 
                }
            </div>
        </div>
    );
}

export default DetailsBox;
