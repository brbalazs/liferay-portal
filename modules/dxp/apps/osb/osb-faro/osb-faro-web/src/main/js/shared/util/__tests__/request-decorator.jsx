import React from 'react';
import {autoCancel, autoCancelWith, hasRequest} from '../request-decorator';
import {shallow} from 'enzyme';

describe('request-decorator', () => {
	describe('autoCancel', () => {
		it('should cancel the request if the same request was made again', () => {
			expect.assertions(2);

			const cancel = jest.fn();

			class TestAutoCancel extends React.Component {
				@autoCancel
				foo() {
					return {cancel};
				}

				render() {
					return null;
				}
			}

			const testAutoCancel = shallow(<TestAutoCancel />);

			testAutoCancel.instance().foo();

			expect(cancel).not.toBeCalled();

			testAutoCancel.instance().foo();

			expect(cancel).toBeCalled();
		});

		it('should not cancel the request if cancel is false', () => {
			expect.assertions(2);

			const cancel = jest.fn();

			class TestAutoCancel extends React.Component {
				@autoCancelWith(false)
				foo() {
					return {cancel};
				}

				render() {
					return null;
				}
			}

			const testAutoCancel = shallow(<TestAutoCancel />);

			testAutoCancel.instance().foo();

			expect(cancel).not.toBeCalled();

			testAutoCancel.instance().foo();

			expect(cancel).not.toBeCalled();
		});
	});

	describe('hasRequest', () => {
		it('should cancel the requests on the disposal of the component', () => {
			expect.assertions(2);

			const cancel = jest.fn();

			@hasRequest
			class TestAutoCancel extends React.Component {
				@autoCancel
				foo() {
					return {cancel};
				}

				render() {
					return null;
				}
			}

			const testAutoCancel = shallow(<TestAutoCancel />);

			testAutoCancel.instance().foo();

			expect(cancel).not.toBeCalled();

			testAutoCancel.unmount();

			expect(cancel).toBeCalled();
		});
	});
});
