import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {LIST_BY} from '../utils/constants.es';
import Member from './Member';
import NoMembers from './NoMembers';

const {
    USERS,
    ACCOUNTS
} = LIST_BY;

class MembersList extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isLoading: true,
            listBy: USERS,
            data: null,
            total: 0
        };
    }

    shouldComponentUpdate(nextProps, nextState) {
        return true;
    }

    render() {
        const {
            members,
            listBy,
            imagesPath,
            spritemap,
            isLoading
        } = this.props;

        return (
            <div className='pane-members-list'>
                {isLoading &&
                <div className='is-loading'>
                    <span className='spinner fas fa-circle-notch'></span>
                </div>
                }

                {<ul>
                    {!isLoading && !!members.length &&
                        members.map((member, index) => {
                            return (
                                <Member
                                    key={index}
                                    member={member}
                                    imagesPath={imagesPath}
                                />
                            );
                        })
                    }
                </ul>
                }

                {!isLoading && !members.length &&
                <NoMembers spritemap={spritemap}/>
                }
            </div>
        );
    }
}

MembersList.defaultProps = {};

MembersList.propTypes = {};

export default MembersList;
