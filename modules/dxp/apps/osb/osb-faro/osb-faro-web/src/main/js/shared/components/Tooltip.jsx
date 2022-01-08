import autobind from 'autobind-decorator';
import dom from 'metal-dom';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {Align} from 'metal-position';
import {CSSTransition, TransitionGroup} from 'react-transition-group';
import {EventHandler} from 'metal-events';
import {PropTypes} from 'prop-types';

const ALIGNMENTS = [
	'top',
	'top-right',
	'right',
	'bottom-right',
	'bottom',
	'bottom-left',
	'left',
	'top-left'
];

const ALIGNMENTS_MAP = {
	bottom: Align.Bottom,
	'bottom-left': Align.BottomLeft,
	'bottom-right': Align.BottomRight,
	left: Align.Left,
	right: Align.Right,
	top: Align.Top,
	'top-left': Align.TopLeft,
	'top-right': Align.TopRight
};

class Tooltip extends React.Component {
	static propTypes = {
		initialAlignment: PropTypes.oneOf(ALIGNMENTS),
		message: PropTypes.string,
		target: PropTypes.object
	};

	state = {
		alignment: 'top'
	};

	constructor(props) {
		super(props);
		const {initialAlignment} = this.props;

		if (initialAlignment) {
			this.state = {
				...this.state,
				alignment: initialAlignment
			};
		}

		this._elementRef = React.createRef();
	}

	componentDidMount() {
		window.addEventListener('resize', this.alignOverlay);
		window.addEventListener('scroll', this.alignOverlay);

		this.alignOverlay();
	}

	componentDidUpdate() {
		const {target} = this.props;

		if (!target) return;

		const {x, y} = target.getBoundingClientRect();

		if (!!x && !!y) {
			this.alignOverlay();
		}
	}

	componentWillUnmount() {
		window.removeEventListener('resize', this.alignOverlay);
		window.removeEventListener('scroll', this.alignOverlay);
	}

	@autobind
	alignOverlay() {
		const {
			props: {target},
			state: {alignment}
		} = this;

		const newAlignment =
			ALIGNMENTS[
				Align.align(
					this._elementRef.current,
					target,
					ALIGNMENTS_MAP[alignment]
				)
			];

		if (newAlignment !== alignment) {
			this.setState({
				alignment: newAlignment
			});
		}
	}

	render() {
		const {
			props: {className, message, ...otherProps},
			state: {alignment}
		} = this;

		const classes = getCN('show', 'tooltip', 'tooltip-root', className, {
			[`clay-tooltip-${alignment}`]: alignment
		});

		return (
			<div
				{...omitDefinedProps(otherProps, Tooltip.propTypes)}
				className={classes}
				ref={this._elementRef}
				role='tooltip'
			>
				<div className='arrow' />

				<div className='tooltip-inner'>{message}</div>
			</div>
		);
	}
}

class TooltipBase extends React.Component {
	state = {
		align: PropTypes.oneOf(ALIGNMENTS),
		message: '',
		show: false,
		target: {}
	};

	constructor(props) {
		super(props);

		this._eventHandler = new EventHandler();
	}

	componentDidMount() {
		this.setTriggers();
	}

	componentWillUnmount() {
		if (this._delay) {
			clearTimeout(this._delay);
		}

		this._eventHandler.removeAllListeners();
	}

	@autobind
	handleClick(event) {
		if (this._responseMessage) {
			this.setState({
				message: this._responseMessage,
				show: true
			});
		} else {
			this.handleHide(event);
		}
	}

	@autobind
	handleHide({target}) {
		const dataTitle = target && target.getAttribute('data-title');

		if (dataTitle) {
			target.removeEventListener('click', this.handleClick);

			target.setAttribute('title', dataTitle);

			target.removeAttribute('data-title');

			this.setState({
				show: false
			});
		} else {
			this.setState({
				show: false
			});
		}

		clearTimeout(this._delay);
	}

	@autobind
	handleShow({target}) {
		this._responseMessage = target.getAttribute('data-tooltip-response');

		const align = target.getAttribute('data-tooltip-align');

		target.addEventListener('click', this.handleClick);

		const message = target.getAttribute('title');

		if (message) {
			this.setState({
				align: align ? align : ALIGNMENTS[0],
				message,
				target
			});

			target.setAttribute('data-title', message);
			target.removeAttribute('title');

			if (
				target.hasAttribute('data-tooltip') ||
				target.hasAttribute('data-tooltip-response')
			) {
				this._delay = setTimeout(() => {
					this.setState({
						show: true
					});
				}, 600);
			}
		}
	}

	setTriggers() {
		this._eventHandler.removeAllListeners();

		this._eventHandler.add(
			dom.delegate(document, 'mouseenter', '[title]', this.handleShow),
			dom.delegate(
				document,
				'mouseleave',
				'[data-title]',
				this.handleHide
			)
		);
	}

	render() {
		const {align, message, show, target} = this.state;

		return (
			<TransitionGroup>
				{show && (
					<CSSTransition
						appear
						classNames='transition-fade-in-out'
						timeout={{enter: 150, exit: 150}}
					>
						<Tooltip
							initialAlignment={align}
							message={message}
							target={target}
						/>
					</CSSTransition>
				)}
			</TransitionGroup>
		);
	}
}

TooltipBase.ALIGNMENTS = ALIGNMENTS;
export default TooltipBase;
