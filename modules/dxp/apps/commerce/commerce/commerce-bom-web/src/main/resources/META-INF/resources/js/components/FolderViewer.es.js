import React, 
{
    useState,
    useContext
} from 'react';

import { Link } from 'react-router-dom';

import Icon from './utilities/Icon.es';
import Collapse from './collapse/Collapse.es';
import LocalizedText from './utilities/LocalizedText.es';
import { StoreContext } from './StoreContext.es';

function ModelsList(props) {
    return (
        <ul className="list-group">
            {props.models.map(
                (
                    {
                        name,
                        power,
                        productionYears
                    },
                    i
                ) => (
                    <li 
                        className="list-group-item" 
                        key={i}
                    >
                        <div className="row">
                            <div className="col">{name}</div>
                            <div className="col-auto text-right">{power}</div>
                            <div className="col-4 text-right">{productionYears}</div>
                        </div>
                    </li>
                )
            )}
        </ul>
    )
}

function Compatibilities(props) {
    const [selectedBrand, setSelectedBrand] = useState(0);

    const { state } = useContext(StoreContext);

    return (
        <div className="suitable-veichles panel panel-secondary sticky-panel">
            <div className="panel-heading">
                <h2 className="panel-title">
                    <LocalizedText 
                        desc="Suitable Veichles"
                    >
                        suitable-veichles
                    </LocalizedText>
                </h2>
            </div>
            <div className="panel-body">
                {
                    props.data.map(
                        (el, i) => <Collapse 
                            key={i}
                            title={el.name}
                            additionalWrapperClasses={i !== 0 && 'mt-3'}
                            content={<ModelsList models={el.models}  />}
                            open={i === selectedBrand}
                            handleOpen={() => setSelectedBrand(i)}
                            handleClose={() => setSelectedBrand(null)}
                            openIcon={<Icon spritemap={state.app.spritemap} symbol={'minus'}/>}
                            closedIcon={<Icon spritemap={state.app.spritemap} symbol={'plus'}/>}
                        />
                    )
                }
            </div>
        </div>
    )
}

function Card(props) {
    const contentCard = (
        <div className="area-card card image-card">
            <div className="aspect-ratio aspect-ratio-4-to-3 card-item-first bg-checkered">
                <img 
                    alt={props.title}
                    src={props.thumbnail}
                    className="aspect-ratio-item-center-middle"
                />
            </div>
            <div className="card-body">
                <div className="card-row">
                    <div className="autofit-col autofit-col-expand">
                        <div 
                            className="card-title text-truncate" 
                            data-title={props.title}
                            title={props.title}
                        >
                            {props.title}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
    return props.url
        ? (
            <Link 
                className="card-link"
                to={props.url}
                data-senna-off
            >
                {contentCard}
            </Link>
        )
        : contentCard
}

function CardContainer(props) {
    return (
        <div className="area-list row">
            {props.content.map((el, i) => (
                <div 
                    key={i}
                    className={props.cardsWrapperAdditionalClasses}
                >   
                    <Card 
                        title={el.name}
                        thumbnail={el.thumbnail}
                        url={el.url}
                    />
                </div>
            ))}
        </div>
    )
}

export default function FolderViewer() {

    const { state } = useContext(StoreContext);

    return state.folder.content 
        ? (
            <div className="row">
                <div className="col">
                    <CardContainer 
                        content={state.folder.content}
                        cardsWrapperAdditionalClasses={
                            state.folder.compatibilities 
                                ? 'col-3' 
                                : 'col-2'
                        }
                    />
                </div>
                {state.folder.compatibilities && (
                    <div className="col-sm-4 position-relative">
                        <Compatibilities 
                            data={state.folder.compatibilities}
                        />
                    </div>
                )}
            </div>
        )
        : null
}