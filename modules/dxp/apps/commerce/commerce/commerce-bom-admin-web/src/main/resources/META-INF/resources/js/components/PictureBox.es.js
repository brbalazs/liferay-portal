import React, {
    useRef,
    useState,
    Fragment,
    useContext 
} from 'react';

import { StoreContext } from './StoreContext.es';
import LocalizedText from './utilities/LocalizedText.es';
import Icon from './utilities/Icon.es';
import EditNumberForm from './EditNumberForm.es';

function PartDetail(props) {

    const numberRef = useRef(null);

    const { actions } = useContext(StoreContext);

    const containerClasses = 
        'spot-number' + 
        (props.highlightedNumber ? ' spot-number--highlighted' : '');

    return (
        <button 
            ref={numberRef}
            className={containerClasses} 
            onMouseOver={() => actions.highlightDetail(props.number)}
            onMouseOut={() => actions.highlightDetail(null)}
            onClick={() => actions.selectSpot(props.id)}
            style={{
                bottom: props.position.y + '%', 
                left: props.position.x + '%'
            }}
        >
            {props.number}
        </button>
    )
}

function SpotsList(props) {
    const { state } = useContext(StoreContext);

    let resumeShown = false

    return state.area.spots.map(
        (detail, i) => {
            const highlightedNumber = (state.area.highlightedDetail && state.area.highlightedDetail.number) === detail.number;

            let resumeVisible = false;
            if(
                !resumeShown && 
                highlightedNumber && 
                state.area.highlightedDetail && 
                state.area.highlightedDetail.showFirstResume
            ) {
                resumeVisible = true;
                resumeShown = true;
            }

            return (
                <PartDetail 
                    key={i} 
                    containerRef={props.containerRef}
                    highlightedNumber={highlightedNumber}
                    resumeVisible={resumeVisible}
                    {...detail} 
                /> 
            )
        }
    )
}

function EmptyBoxMessage() {
    return (
        <div className="empty-box-research">
            <h3>
                <LocalizedText desc="Select Car &amp; Parts">
                    select-car-and-parts
                </LocalizedText>
            </h3>
            <h5>
                <LocalizedText desc="Please select the car maker">
                    please-select-the-carmaker-the-model-the-type-and-the-car-parts
                </LocalizedText>
            </h5>
        </div>
    )
}

function CustomCursor(props) {
    const { state } = useContext(StoreContext);
    return (
        <div 
            className={`custom-cursor${props.visible ? ` custom-cursor--visible` : ``}`} 
            style={{
                top: props.y,
                left: props.x
            }}
        >
            <Icon spritemap={state.app.spritemap} symbol={'plus'}/>
        </div>
    )
}

function PictureBox() {
    const containerRef = useRef(null);
    const { actions } = useContext(StoreContext);

    const [ cursor, updateCursor ] = useState({
        x: 0,
        y: 0,
        visible: false
    });

    const { state } = useContext(StoreContext);

    function handleMouseMove(e) {
        switch (true) {
            case e.target.className === 'custom-cursor-wrapper':
                updateCursor(
                    {
                        visible: true,
                        x: e.pageX,
                        y: e.pageY
                    }
                )
                break;
            default :
                updateCursor(
                    {
                        ...cursor,
                        visible: false
                    }
                )
                break;
        }
    }

    function handleClick(e) {
        if(e.target.className === 'custom-cursor-wrapper') {
            const containerRect = containerRef.current.getBoundingClientRect()
            const pxLeft = e.pageX - containerRect.left;
            const pxTop = e.pageY - containerRect.top;
            const x = Math.round((100 / containerRect.width * pxLeft) * 1000) / 1000;
            const y = 100 - Math.round((100 / containerRect.height * pxTop) * 1000) / 1000;

            actions.createSpot(
                {
                    x,
                    y
                }
            );
        }
    }

    const highlightedModifierClass = 
        (
            state.area && 
            state.area.highlightedDetail && 
            state.area.highlightedDetail.number
        )
        ? ' picture-box--hovered-detail' 
        : '';

    return (
        <Fragment>
            {
                state.area.name 
                && (
                    <div className="panel panel-secondary picture-box-wrapper">
                        <div 
                            className={`picture-box${highlightedModifierClass}`}
                            >
                            <div 
                                className="custom-cursor-wrapper"
                                onClick={handleClick}
                                ref={containerRef}
                                onMouseMove={handleMouseMove}
                                onMouseEnter={() => updateCursor({...cursor, visible: true})}
                                onMouseLeave={() => updateCursor({...cursor, visible: false})}
                            >
                                <CustomCursor 
                                    icon="plus"
                                    x={cursor.x}
                                    y={cursor.y}
                                    visible={cursor.visible}
                                />
                                <SpotsList containerRef={containerRef} />
                            </div>
                            <EditNumberForm />
                            <img className="picture-box__image" src={state.area.imageUrl} alt={state.area.name}/>
                        </div>
                    </div>
                )
            }
        </Fragment>
    );
}

export default PictureBox;
