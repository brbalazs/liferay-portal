import React, {Component} from 'react';
import PropTypes from 'prop-types';

import PaneHeader from './PaneHeader';
import MembersList from './MembersList';
import {LIST_BY} from '../utils/constants.es';
import {callApi} from '../utils/utils.es';

const {USERS, ACCOUNTS} = LIST_BY;

let membersListCopy = [];

function fetchMembers(apiURL, orgId, listBy, q = '') {
    const collectionPath = listBy + 's',
        apiParams = {
            baseURL: apiURL,
            id: orgId
        },
        apiParamsMembers = Object.assign({},
            apiParams,
            {
                path: collectionPath,
                queryParams: {
                    page: 1,
                    pageSize: 100,
                    q
                }
            });

    return Promise.all([
        callApi(apiParams),
        callApi(apiParamsMembers)
    ])
        .then(data => {
            const [
                orgData,
                membersData
            ] = data;

            const {
                name: orgName,
                organizationsTotal: totalSubOrg,
                accountsTotal,
                usersTotal
            } = orgData;

            return {
                id: orgId,
                orgName,
                totalSubOrg,
                totalUsers: usersTotal,
                totalAccounts: accountsTotal,
                members: membersData[collectionPath]
            };
        })
        .catch(e => {
            console.log(e);
        });
}

function filterMembers(name, members) {
    return members.filter(
        member => member.name
            .toLowerCase()
            .includes(name.toLowerCase())
    );
}

function shouldPaneOpen(id, members) {
    return !!id && !!members;
}

function getIfPopulated(users, accounts) {
    return !!users ? USERS : !!accounts ? ACCOUNTS : USERS;
}

class MembersPane extends Component {
    constructor(props) {
        super(props);

        const {
            totalUsers,
            totalAccounts
        } = props;

        this.state = {
            id: 0,
            searchQuery: '',
            listBy: USERS,
            isLoading: true
        };

        _.bindAll(
            this,
            'handleListBy',
            'handleLookUp',
            'handleUpdate'
        );
    }

    componentDidMount() {
        const {id} = this.props;
        const {listBy} = this.state;

        this.handleUpdate(id, listBy);
    }

    componentDidUpdate(prevProps, prevState) {
        const {id} = this.props;
        const {listBy} = this.state;

        if (id !== prevProps.id || listBy !== prevState.listBy) {
            this.setState(() => ({ isLoading: true }), () => {
                this.handleUpdate(id, listBy)
            });
        }
    }

    handleListBy(listBy) {
        this.setState(() => ({
            listBy
        }));
    }

    handleLookUp(e) {
        const name = e.target.value;
        const {id, apiURL} = this.props;
        const {listBy} = this.state;
        const fromState = !!name && name.length ?
            filterMembers(name, this.state.members) : membersListCopy;

        if (fromState.length) {
            this.setState(() => ({
                members: fromState
            }))
        } else {
            fetchMembers(apiURL, id, listBy, name)
                .then(({total, users}) => {
                    this.setState(() => {
                        if (!!total) {
                            const fromFetch = filterMembers(name, users);

                            return fromFetch.length ?
                                {members: fromFetch} :
                                {members: membersListCopy}
                        }

                        return {members: membersListCopy};
                    });
                })
        }
    }

    handleUpdate(id, listBy) {
        const {apiURL} = this.props;

        fetchMembers(apiURL, id, listBy)
            .then((data) => {
                this.setState(state => Object.assign(
                    state,
                    data,
                    {
                        isLoading: false
                    }), () => {
                    membersListCopy = this.state.members;
                });
            })
    }

    render() {
        const {
                orgName,
                members,
                totalSubOrg,
                totalUsers,
                totalAccounts,
                listBy,
                id
            } = this.state,
            paneClasses = `pane${(shouldPaneOpen(id, orgName)) ? ' pane-open' : ''}`;

        return (
            <div className={paneClasses}>
                <PaneHeader
                    orgName={orgName}
                    totalSubOrg={totalSubOrg}
                    totalUsers={totalUsers}
                    totalAccounts={totalAccounts}
                    listBy={this.state.listBy}
                    onViewSelected={this.handleListBy}
                    onLookUp={this.handleLookUp}
                    spritemap={this.props.spritemap}
                />

                <MembersList
                    listBy={listBy}
                    members={members}
                    isLoading={this.state.isLoading}
                    spritemap={this.props.spritemap}
                    imagesPath={this.props.imagesPath}
                />
            </div>
        );
    }
}

PropTypes.defaultProps = {
    selectedId: 0,
    apiURL: ''
};

MembersPane.propTypes = {
    apiURL: PropTypes.string,
    selectedId: PropTypes.number
};

export default MembersPane;
