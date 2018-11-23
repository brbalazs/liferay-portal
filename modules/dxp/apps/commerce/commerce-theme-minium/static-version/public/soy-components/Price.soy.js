/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from Price.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace Price.
 * @public
 */

goog.module('Price.incrementaldom');

goog.require('goog.soy.data.SanitizedContent');
var incrementalDom = goog.require('incrementaldom');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');


/**
 * @param {{
 *  price: (?),
 *  additionalDiscountedClasses: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  additionalOldPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  additionalPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $render(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {?} */
  var price = opt_data.price;
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var additionalDiscountedClasses = soy.asserts.assertType(opt_data.additionalDiscountedClasses == null || (goog.isString(opt_data.additionalDiscountedClasses) || opt_data.additionalDiscountedClasses instanceof goog.soy.data.SanitizedContent), 'additionalDiscountedClasses', opt_data.additionalDiscountedClasses, '!goog.soy.data.SanitizedContent|null|string|undefined');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var additionalOldPriceClasses = soy.asserts.assertType(opt_data.additionalOldPriceClasses == null || (goog.isString(opt_data.additionalOldPriceClasses) || opt_data.additionalOldPriceClasses instanceof goog.soy.data.SanitizedContent), 'additionalOldPriceClasses', opt_data.additionalOldPriceClasses, '!goog.soy.data.SanitizedContent|null|string|undefined');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var additionalPriceClasses = soy.asserts.assertType(opt_data.additionalPriceClasses == null || (goog.isString(opt_data.additionalPriceClasses) || opt_data.additionalPriceClasses instanceof goog.soy.data.SanitizedContent), 'additionalPriceClasses', opt_data.additionalPriceClasses, '!goog.soy.data.SanitizedContent|null|string|undefined');
  if ((price.formattedPromoPrice != null)) {
    $promoContent(opt_data, null, opt_ijData);
  } else {
    $content(opt_data, null, opt_ijData);
  }
}
exports.render = $render;
/**
 * @typedef {{
 *  price: (?),
 *  additionalDiscountedClasses: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  additionalOldPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  additionalPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'Price.render';
}


/**
 * @param {{
 *  price: {formattedPrice: (!goog.soy.data.SanitizedContent|string), formattedPromoPrice: (!goog.soy.data.SanitizedContent|string)},
 *  additionalDiscountedClasses: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  additionalOldPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $promoContent(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {{formattedPrice: (!goog.soy.data.SanitizedContent|string), formattedPromoPrice: (!goog.soy.data.SanitizedContent|string)}} */
  var price = soy.asserts.assertType(goog.isObject(opt_data.price), 'price', opt_data.price, '{formattedPrice: (!goog.soy.data.SanitizedContent|string), formattedPromoPrice: (!goog.soy.data.SanitizedContent|string)}');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var additionalDiscountedClasses = soy.asserts.assertType(opt_data.additionalDiscountedClasses == null || (goog.isString(opt_data.additionalDiscountedClasses) || opt_data.additionalDiscountedClasses instanceof goog.soy.data.SanitizedContent), 'additionalDiscountedClasses', opt_data.additionalDiscountedClasses, '!goog.soy.data.SanitizedContent|null|string|undefined');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var additionalOldPriceClasses = soy.asserts.assertType(opt_data.additionalOldPriceClasses == null || (goog.isString(opt_data.additionalOldPriceClasses) || opt_data.additionalOldPriceClasses instanceof goog.soy.data.SanitizedContent), 'additionalOldPriceClasses', opt_data.additionalOldPriceClasses, '!goog.soy.data.SanitizedContent|null|string|undefined');
  incrementalDom.elementOpenStart('span');
      incrementalDom.attr('class', 'price price--discounted' + additionalDiscountedClasses);
  incrementalDom.elementOpenEnd();
    soyIdom.print(price.formattedPromoPrice);
  incrementalDom.elementClose('span');
  incrementalDom.elementOpenStart('span');
      incrementalDom.attr('class', 'price price--old' + additionalOldPriceClasses);
  incrementalDom.elementOpenEnd();
    soyIdom.print(price.formattedPrice);
  incrementalDom.elementClose('span');
}
exports.promoContent = $promoContent;
/**
 * @typedef {{
 *  price: {formattedPrice: (!goog.soy.data.SanitizedContent|string), formattedPromoPrice: (!goog.soy.data.SanitizedContent|string)},
 *  additionalDiscountedClasses: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  additionalOldPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }}
 */
$promoContent.Params;
if (goog.DEBUG) {
  $promoContent.soyTemplateName = 'Price.promoContent';
}


/**
 * @param {{
 *  price: {formattedPrice: (!goog.soy.data.SanitizedContent|string)},
 *  additionalPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $content(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {{formattedPrice: (!goog.soy.data.SanitizedContent|string)}} */
  var price = soy.asserts.assertType(goog.isObject(opt_data.price), 'price', opt_data.price, '{formattedPrice: (!goog.soy.data.SanitizedContent|string)}');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var additionalPriceClasses = soy.asserts.assertType(opt_data.additionalPriceClasses == null || (goog.isString(opt_data.additionalPriceClasses) || opt_data.additionalPriceClasses instanceof goog.soy.data.SanitizedContent), 'additionalPriceClasses', opt_data.additionalPriceClasses, '!goog.soy.data.SanitizedContent|null|string|undefined');
  incrementalDom.elementOpenStart('span');
      incrementalDom.attr('class', 'price' + additionalPriceClasses);
  incrementalDom.elementOpenEnd();
    soyIdom.print(price.formattedPrice);
  incrementalDom.elementClose('span');
}
exports.content = $content;
/**
 * @typedef {{
 *  price: {formattedPrice: (!goog.soy.data.SanitizedContent|string)},
 *  additionalPriceClasses: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }}
 */
$content.Params;
if (goog.DEBUG) {
  $content.soyTemplateName = 'Price.content';
}

exports.render.params = ["price","additionalDiscountedClasses","additionalOldPriceClasses","additionalPriceClasses"];
exports.render.types = {"price":"?","additionalDiscountedClasses":"string","additionalOldPriceClasses":"string","additionalPriceClasses":"string"};
exports.promoContent.params = ["price","additionalDiscountedClasses","additionalOldPriceClasses"];
exports.promoContent.types = {"price":"[\n        formattedPromoPrice: string,\n        formattedPrice: string\n    ]","additionalDiscountedClasses":"string","additionalOldPriceClasses":"string"};
exports.content.params = ["price","additionalPriceClasses"];
exports.content.types = {"price":"[\n        formattedPrice: string\n    ]","additionalPriceClasses":"string"};
templates = exports;
return exports;

});

class Price extends Component {}
Soy.register(Price, templates);
export { Price, templates };
export default templates;
/* jshint ignore:end */
