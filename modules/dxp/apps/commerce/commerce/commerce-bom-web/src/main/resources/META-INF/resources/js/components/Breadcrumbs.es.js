import React from 'react';

import { Link } from 'react-router-dom';

function Breadcrumbs(props) {
    return props.data ? (
        <ol className="breadcrumb mb-3">
            {props.data.map(
                (el, i) => {
                    const content = <span className="breadcrumb-text-truncate">{el.label}</span>
                    return (
                        <li className="breadcrumb-item" key={i}>
                            {
                                el.url 
                                    ? <Link to={el.url} key={i} data-senna-off>{content}</Link>
                                    : content
                            }
                        </li>
                    )
                })
            }
        </ol>
    ) : null;
}

export default Breadcrumbs;
