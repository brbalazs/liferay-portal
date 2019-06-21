import React, {Component} from 'react';
import Icon from './Icon';
import PropTypes from 'prop-types';

class PaneSearchBar extends Component {
    constructor(props) {
        super(props);

        this.onSubmit.bind(this);
    }

    onSubmit(e) {
        e.preventDefault();

        alert('hai fatto INVIOOOOHHH!!11one')
    }

    render() {
        const {
            onLookUp,
            spritemap
        } = this.props;

        return (
            <div className='pane-search-bar'>
                <form name='searchUser' onSubmit={this.onSubmit}>
					<span>
						<input autoComplete={'off'}
                               tabIndex='4' type='text'
                               onChange={onLookUp}
                               name='search-user'
                               placeholder='Search user...'
                        />
					</span>
                    <span>
						<button tabIndex='5' type='submit'>
							<Icon
                                symbol={'search'}
                                spritemap={spritemap}
                            />
						</button>
					</span>
                </form>
            </div>
        );
    }
}

PaneSearchBar.defaultProps = {};

PaneSearchBar.propTypes = {};

export default PaneSearchBar;
