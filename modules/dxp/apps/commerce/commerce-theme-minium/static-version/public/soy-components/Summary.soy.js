/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from Summary.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace Summary.
 * @public
 */

goog.module('Summary.incrementaldom');

goog.require('goog.soy.data.SanitizedContent');
var incrementalDom = goog.require('incrementaldom');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');

var $templateAlias1 = Soy.getTemplate('Loader.incrementaldom', 'render');


/**
 * @param {{
 *  localization: (?),
 *  checkoutUrl: (!goog.soy.data.SanitizedContent|string),
 *  subtotal: (!goog.soy.data.SanitizedContent|string),
 *  grandTotal: (!goog.soy.data.SanitizedContent|string),
 *  discount: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  totalUnits: number,
 *  productsNumber: number,
 *  isLoading: (boolean|null|undefined)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $render(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {?} */
  var localization = opt_data.localization;
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var checkoutUrl = soy.asserts.assertType(goog.isString(opt_data.checkoutUrl) || opt_data.checkoutUrl instanceof goog.soy.data.SanitizedContent, 'checkoutUrl', opt_data.checkoutUrl, '!goog.soy.data.SanitizedContent|string');
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var subtotal = soy.asserts.assertType(goog.isString(opt_data.subtotal) || opt_data.subtotal instanceof goog.soy.data.SanitizedContent, 'subtotal', opt_data.subtotal, '!goog.soy.data.SanitizedContent|string');
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var grandTotal = soy.asserts.assertType(goog.isString(opt_data.grandTotal) || opt_data.grandTotal instanceof goog.soy.data.SanitizedContent, 'grandTotal', opt_data.grandTotal, '!goog.soy.data.SanitizedContent|string');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var discount = soy.asserts.assertType(opt_data.discount == null || (goog.isString(opt_data.discount) || opt_data.discount instanceof goog.soy.data.SanitizedContent), 'discount', opt_data.discount, '!goog.soy.data.SanitizedContent|null|string|undefined');
  /** @type {number} */
  var totalUnits = soy.asserts.assertType(goog.isNumber(opt_data.totalUnits), 'totalUnits', opt_data.totalUnits, 'number');
  /** @type {number} */
  var productsNumber = soy.asserts.assertType(goog.isNumber(opt_data.productsNumber), 'productsNumber', opt_data.productsNumber, 'number');
  /** @type {boolean|null|undefined} */
  var isLoading = soy.asserts.assertType(opt_data.isLoading == null || (goog.isBoolean(opt_data.isLoading) || opt_data.isLoading === 1 || opt_data.isLoading === 0), 'isLoading', opt_data.isLoading, 'boolean|null|undefined');
  var submitModifiers__soy1085 = '';
  submitModifiers__soy1085 += isLoading || productsNumber == 0 ? ' minium-button--disabled' : '';
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', 'minium-cart__footer');
  incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('dl');
        incrementalDom.attr('class', 'minium-cart__totals');
    incrementalDom.elementOpenEnd();
      incrementalDom.elementOpen('dt');
        soyIdom.print(localization.Units);
      incrementalDom.elementClose('dt');
      incrementalDom.elementOpen('dd');
        soyIdom.print(totalUnits);
        incrementalDom.text(' ');
        soyIdom.print(localization.of);
        incrementalDom.text(' ');
        soyIdom.print(productsNumber);
        incrementalDom.text(' ');
        soyIdom.print(localization.Items);
      incrementalDom.elementClose('dd');
      incrementalDom.elementOpen('dt');
        soyIdom.print(localization.Subtotal);
      incrementalDom.elementClose('dt');
      incrementalDom.elementOpen('dd');
        var param1106 = function() {
          soyIdom.print(subtotal);
        };
        $templateAlias1({isUpdating: isLoading, content: param1106, direction: 'right', inverted: true}, null, opt_ijData);
      incrementalDom.elementClose('dd');
      if (discount) {
        incrementalDom.elementOpen('dt');
          soyIdom.print(localization.Discount);
        incrementalDom.elementClose('dt');
        incrementalDom.elementOpen('dd');
          soyIdom.print(discount);
        incrementalDom.elementClose('dd');
      }
      incrementalDom.elementOpen('dt');
        soyIdom.print(localization.GrandTotal);
      incrementalDom.elementClose('dt');
      incrementalDom.elementOpen('dd');
        var param1123 = function() {
          incrementalDom.elementOpen('big');
            soyIdom.print(grandTotal);
          incrementalDom.elementClose('big');
        };
        $templateAlias1({isUpdating: isLoading, content: param1123, direction: 'right', inverted: true}, null, opt_ijData);
      incrementalDom.elementClose('dd');
    incrementalDom.elementClose('dl');
    incrementalDom.elementOpenStart('a');
        incrementalDom.attr('href', checkoutUrl);
        incrementalDom.attr('class', 'minium-button minium-button--block' + submitModifiers__soy1085);
    incrementalDom.elementOpenEnd();
      soyIdom.print(localization.Submit);
    incrementalDom.elementClose('a');
  incrementalDom.elementClose('div');
}
exports.render = $render;
/**
 * @typedef {{
 *  localization: (?),
 *  checkoutUrl: (!goog.soy.data.SanitizedContent|string),
 *  subtotal: (!goog.soy.data.SanitizedContent|string),
 *  grandTotal: (!goog.soy.data.SanitizedContent|string),
 *  discount: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  totalUnits: number,
 *  productsNumber: number,
 *  isLoading: (boolean|null|undefined)
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'Summary.render';
}

exports.render.params = ["localization","checkoutUrl","subtotal","grandTotal","discount","totalUnits","productsNumber","isLoading"];
exports.render.types = {"localization":"?","checkoutUrl":"string","subtotal":"string","grandTotal":"string","discount":"string","totalUnits":"int","productsNumber":"int","isLoading":"bool"};
templates = exports;
return exports;

});

class Summary extends Component {}
Soy.register(Summary, templates);
export { Summary, templates };
export default templates;
/* jshint ignore:end */
