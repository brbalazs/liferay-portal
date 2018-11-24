/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from Cart.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace Cart.
 * @public
 */

goog.module('Cart.incrementaldom');

goog.require('goog.soy.data.SanitizedContent');
var incrementalDom = goog.require('incrementaldom');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');

var $templateAlias1 = Soy.getTemplate('CartProduct.incrementaldom', 'render');

var $templateAlias2 = Soy.getTemplate('Summary.incrementaldom', 'render');


/**
 * @param {{
 *  isOpen: boolean,
 *  isDisabled: boolean,
 *  localization: (?),
 *  products: (?),
 *  detailsUrl: (!goog.soy.data.SanitizedContent|string),
 *  productsAmount: number,
 *  summary: (?),
 *  isLoading: (?),
 *  handleSubmitQuantity: (?),
 *  handleDeleteItem: (?),
 *  handleCancelItemDeletion: (?)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $render(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {boolean} */
  var isOpen = soy.asserts.assertType(goog.isBoolean(opt_data.isOpen) || opt_data.isOpen === 1 || opt_data.isOpen === 0, 'isOpen', opt_data.isOpen, 'boolean');
  /** @type {boolean} */
  var isDisabled = soy.asserts.assertType(goog.isBoolean(opt_data.isDisabled) || opt_data.isDisabled === 1 || opt_data.isDisabled === 0, 'isDisabled', opt_data.isDisabled, 'boolean');
  /** @type {?} */
  var localization = opt_data.localization;
  /** @type {?} */
  var products = opt_data.products;
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var detailsUrl = soy.asserts.assertType(goog.isString(opt_data.detailsUrl) || opt_data.detailsUrl instanceof goog.soy.data.SanitizedContent, 'detailsUrl', opt_data.detailsUrl, '!goog.soy.data.SanitizedContent|string');
  /** @type {number} */
  var productsAmount = soy.asserts.assertType(goog.isNumber(opt_data.productsAmount), 'productsAmount', opt_data.productsAmount, 'number');
  /** @type {?} */
  var summary = opt_data.summary;
  /** @type {?} */
  var isLoading = opt_data.isLoading;
  /** @type {?} */
  var handleSubmitQuantity = opt_data.handleSubmitQuantity;
  /** @type {?} */
  var handleDeleteItem = opt_data.handleDeleteItem;
  /** @type {?} */
  var handleCancelItemDeletion = opt_data.handleCancelItemDeletion;
  var openCartButtonModifiers__soy348 = '';
  openCartButtonModifiers__soy348 += isDisabled ? ' is-disabled' : '';
  var cartModifiers__soy355 = '';
  cartModifiers__soy355 += isOpen ? ' is-open' : '';
  incrementalDom.elementOpen('div');
    incrementalDom.elementOpenStart('a');
        incrementalDom.attr('href', '#');
        incrementalDom.attr('class', 'minium-topbar__button' + openCartButtonModifiers__soy348);
        incrementalDom.attr('data-onclick', 'toggleCart');
    incrementalDom.elementOpenEnd();
      incrementalDom.elementOpenStart('svg');
          incrementalDom.attr('xmlns', 'http://www.w3.org/2000/svg');
          incrementalDom.attr('viewBox', '0 0 100 100');
          incrementalDom.attr('class', 'minium-icon');
      incrementalDom.elementOpenEnd();
        incrementalDom.elementOpenStart('rect');
            incrementalDom.attr('fill', 'currentColor');
            incrementalDom.attr('x', '5');
            incrementalDom.attr('y', '5');
            incrementalDom.attr('width', '90');
            incrementalDom.attr('height', '90');
            incrementalDom.attr('rx', '10');
            incrementalDom.attr('ry', '10');
        incrementalDom.elementOpenEnd();
        incrementalDom.elementClose('rect');
      incrementalDom.elementClose('svg');
    incrementalDom.elementClose('a');
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', 'minium-cart' + cartModifiers__soy355);
    incrementalDom.elementOpenEnd();
      incrementalDom.elementOpenStart('a');
          incrementalDom.attr('href', '#');
          incrementalDom.attr('class', 'minium-topbar__button minium-cart__close');
          incrementalDom.attr('data-onclick', 'toggleCart');
      incrementalDom.elementOpenEnd();
        incrementalDom.elementOpenStart('svg');
            incrementalDom.attr('xmlns', 'http://www.w3.org/2000/svg');
            incrementalDom.attr('viewBox', '0 0 100 100');
            incrementalDom.attr('class', 'minium-icon');
        incrementalDom.elementOpenEnd();
          incrementalDom.elementOpenStart('rect');
              incrementalDom.attr('fill', 'currentColor');
              incrementalDom.attr('x', '5');
              incrementalDom.attr('y', '5');
              incrementalDom.attr('width', '90');
              incrementalDom.attr('height', '90');
              incrementalDom.attr('rx', '10');
              incrementalDom.attr('ry', '10');
          incrementalDom.elementOpenEnd();
          incrementalDom.elementClose('rect');
        incrementalDom.elementClose('svg');
      incrementalDom.elementClose('a');
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'minium-cart__top');
      incrementalDom.elementOpenEnd();
        incrementalDom.elementOpen('div');
          incrementalDom.elementOpen('strong');
            soyIdom.print(productsAmount);
          incrementalDom.elementClose('strong');
          incrementalDom.text(' ');
          soyIdom.print(localization.Products);
        incrementalDom.elementClose('div');
        incrementalDom.elementOpenStart('a');
            incrementalDom.attr('href', detailsUrl);
            incrementalDom.attr('class', 'minium-link');
        incrementalDom.elementOpenEnd();
          soyIdom.print(localization.ViewDetails);
        incrementalDom.elementClose('a');
      incrementalDom.elementClose('div');
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'minium-cart__content');
      incrementalDom.elementOpenEnd();
        var product391List = products;
        var product391ListLen = product391List.length;
        for (var product391Index = 0; product391Index < product391ListLen; product391Index++) {
            var product391Data = product391List[product391Index];
            $templateAlias1({events: {submitQuantity: handleSubmitQuantity, deleteItem: handleDeleteItem, cancelItemDeletion: handleCancelItemDeletion}, id: product391Data.id, name: product391Data.name, price: product391Data.price, sku: product391Data.sku, quantity: product391Data.quantity, thumbnail: product391Data.thumbnail, settings: product391Data.settings, error: product391Data.error, isDeleting: product391Data.isDeleting, isCollapsed: product391Data.isCollapsed, isUpdating: product391Data.isUpdating, isDeleteDisabled: product391Data.isDeleteDisabled, localization: localization}, null, opt_ijData);
          }
      incrementalDom.elementClose('div');
      $templateAlias2({localization: localization, checkoutUrl: summary.checkoutUrl, subtotal: summary.subtotal, grandTotal: summary.grandTotal, discount: summary.discount, totalUnits: summary.totalUnits, productsNumber: productsAmount, isLoading: isLoading, dats: summary}, null, opt_ijData);
    incrementalDom.elementClose('div');
  incrementalDom.elementClose('div');
}
exports.render = $render;
/**
 * @typedef {{
 *  isOpen: boolean,
 *  isDisabled: boolean,
 *  localization: (?),
 *  products: (?),
 *  detailsUrl: (!goog.soy.data.SanitizedContent|string),
 *  productsAmount: number,
 *  summary: (?),
 *  isLoading: (?),
 *  handleSubmitQuantity: (?),
 *  handleDeleteItem: (?),
 *  handleCancelItemDeletion: (?)
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'Cart.render';
}

exports.render.params = ["isOpen","isDisabled","localization","products","detailsUrl","productsAmount","summary","isLoading","handleSubmitQuantity","handleDeleteItem","handleCancelItemDeletion"];
exports.render.types = {"isOpen":"bool","isDisabled":"bool","localization":"?","products":"?","detailsUrl":"string","productsAmount":"int","summary":"?","isLoading":"?","handleSubmitQuantity":"?","handleDeleteItem":"?","handleCancelItemDeletion":"?"};
templates = exports;
return exports;

});

class Cart extends Component {}
Soy.register(Cart, templates);
export { Cart, templates };
export default templates;
/* jshint ignore:end */
