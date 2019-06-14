import React, {
    useState,
    useRef,
    useEffect
} from 'react';

export default function Dropdown(props) {

    const [ collapseState, setCollapseState ] = useState('collapse');
    const [ initialized, setInitialized ] = useState(false);
    const bodyRef = useRef(null);


    function toggle() {
        switch (collapseState) {
            case 'collapse':
                return open()
            case 'show':
                return close()
            default:
                break;
        }
    }

    function open() {
        setCollapseState('showing');
    }
    
    function close() {
        setCollapseState('collapsing');
    }

    function finaliseTransition() {
        if(collapseState === 'collapsing') {
            setCollapseState('collapse')
        }
        
        if(collapseState === 'showing') {
            setCollapseState('show')
        }

        bodyRef.current.removeEventListener('transitionend', finaliseTransition);
    }

    useEffect(() => {
        if(props.open && !initialized && bodyRef.current) {
            open()
            setInitialized(true)        
        }

        if(collapseState === 'showing') {
            bodyRef.current.addEventListener('transitionend', finaliseTransition);
            bodyRef.current.style.height = bodyRef.current.scrollHeight + 'px'
        }

        if(collapseState === 'collapsing') {
            bodyRef.current.addEventListener('transitionend', finaliseTransition);
            bodyRef.current.style.height = '0px'
        }
    })

    const simplifiedTitleStateMap = {
        collapsing: 'collapsed', 
        collapse: 'collapsed', 
        showing: '', 
        show: '', 
    }

    const simplifiedBodyStateMap = {
        collapsing: 'collapsing', 
        collapse: 'collapse', 
        showing: 'collapsing', 
        show: 'collapse show', 
    }

    return (
        <div className={`commerce-collapse${props.additionalWrapperClasses ? ` ${props.additionalWrapperClasses}` :``}`}>
            <span 
                aria-expanded={props.open}
                className={`collapse-icon sheet-subtitle ${simplifiedTitleStateMap[collapseState]}`}
                role="tab"
                onClick={toggle}
            >
                <span>{props.title}</span>
                <span className="collapse-icon-closed">
                    {props.closedIcon}
                </span>
                <span className="collapse-icon-open">
                    {props.openIcon}
                </span>
            </span>
            <div 
                className={`panel-collapse ${simplifiedBodyStateMap[collapseState]}`} 
                role="tabpanel"
                ref={bodyRef}
            >
                {props.content}
            </div>
        </div>
    )
}