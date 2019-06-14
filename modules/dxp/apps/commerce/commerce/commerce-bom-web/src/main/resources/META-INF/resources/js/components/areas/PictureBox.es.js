import React, {
    Fragment,
    useContext,
    useEffect,
    useRef,

} from 'react';

import LocalizedText from '../utilities/LocalizedText.es';
import { StoreContext } from '../StoreContext.es';

function Resume(props) {

	const containerRef = props.containerRef;
	const numberRef = props.numberRef;
	const resumeRef = useRef(null);
    const distanceFromBorders = 50;

	const [
        orientationModifier,
        setOrientationModifier
    ] = useState(null);

	useEffect(() => {
        if (
            !orientationModifier &&
            numberRef.current &&
            containerRef.current &&
            resumeRef.current
		) {
            const numberBoundaries = numberRef.current.getBoundingClientRect();
            const containerBoundaries = containerRef.current.getBoundingClientRect();

			const requiredHeight = resumeRef.current.offsetHeight + distanceFromBorders + numberRef.current.offsetHeight;
            const requiredWidth = resumeRef.current.offsetWidth + numberRef.current.offsetWidth;

			const topSpace = numberBoundaries.top - containerBoundaries.top;
            const leftSpace = numberBoundaries.left - containerBoundaries.left;

			const verticalOrientation = topSpace > requiredHeight ? 'top' : 'bottom';
            const horizontalOrientation = leftSpace > requiredWidth ? 'right' : 'left';

			setOrientationModifier(` part-detail__resume--${verticalOrientation}-${horizontalOrientation}`);
        }
    });

    return (
        <div
			ref={resumeRef}
            className={`part-detail__resume detail-resume detail-resume--product${orientationModifier || ''}`}
        >
            <div
				className="detail-resume__thumbnail"
				style={{backgroundImage: `url(${props.thumbnailUrl})`}}
            />
            <div className="detail-resume__info">
                <div className={`detail-resume__state detail-resume__state--${props.state}`} />
                <p className="detail-resume__sku">{props.sku}</p>
                <p className="detail-resume__price">{props.price}</p>
                <p className="detail-resume__name">{props.name}</p>
            </div>
        </div>
    );
}


function PartDetail(props) {

	const numberRef = useRef(null);

	const {state, actions} = useContext(StoreContext);

	const containerClasses =


        }${props.resumeVisible ? ' part-detail--resume-visible' : ''}`;

	const product = state.area.products.reduce(
        (acc, product) => acc || (product.id === props.rel && product),
        null
    );

    return (
        <a
			className={containerClasses}
	style={{
                top: `${props.position.top  }%`,
				left: `${props.position.left  }%`
			}}
            href={props.url}
        >
            <span
				ref={numberRef}
                className="part-detail__number"
                onMouseOver={() => actions.highlightDetail(props.number)}
                onMouseOut={() => actions.highlightDetail(null)}
            >
                {props.number}
            </span>
            <Resume
	containerRef={props.containerRef}
				numberRef={numberRef}
				thumbnailUrl={product.thumbnailUrl}
                state={product.state}
                sku={product.sku}
                price={product.price}
                name={product.name}
            />
		</a>
    );
}

function SpotsList(props) {
    const {state} = useContext(StoreContext);

	let resumeShown = false;

    return state.area.spots.map(
        (detail, i) => {
            const highlightedNumber = (state.area.highlightedDetail && state.area.highlightedDetail.number) === detail.number;

			let resumeVisible = false;
            if (
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
			);
		}
    );
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
    );
}

function PictureBox() {
    const containerRef = useRef(null);
    const {state} = useContext(StoreContext);

	const highlightedModifierClass =
        (
            state.area.highlightedDetail &&
            state.area.highlightedDetail.number
        )
        ? ' picture-box--hovered-detail'
       	: '';

	return (
        <Fragment>
            {
                state.area.name
					? (
                    <div className="picture-box-wrapper">
                        <div
								className={`picture-box${highlightedModifierClass}`}
                            ref={containerRef}
                        >
                            <SpotsList containerRef={containerRef} />
                            <img
									className="picture-box__image"
                                src={state.area.imageUrl}
                                alt={state.area.name}
                            />
                        </div>
                    </div>
                )
                : <EmptyBoxMessage />
			}
        </Fragment>
    );
}

export default PictureBox;
