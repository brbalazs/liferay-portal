import React from 'react';

import Icon from './Icon';

function NoMembers(props) {
    return(
        <div className={'no-members'}>
            <p>
                <Icon symbol={'close'} spritemap={props.spritemap} />
            </p>
            <p>No members found at this level</p>
        </div>
    );
}

export default NoMembers;
