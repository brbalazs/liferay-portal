/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from CartProduct.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace CartProduct.
 * @public
 */

goog.module('CartProduct.incrementaldom');

goog.require('goog.soy.data.SanitizedContent');
var incrementalDom = goog.require('incrementaldom');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');

var $templateAlias1 = Soy.getTemplate('Loader.incrementaldom', 'render');

var $templateAlias2 = Soy.getTemplate('QuantitySelector.incrementaldom', 'render');


/**
 * @param {{
 *  thumbnail: (!goog.soy.data.SanitizedContent|string),
 *  name: (!goog.soy.data.SanitizedContent|string),
 *  sku: (!goog.soy.data.SanitizedContent|string),
 *  quantity: number,
 *  error: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  settings: (?),
 *  price: (?),
 *  localization: (?),
 *  isDeleting: (boolean|null|undefined),
 *  isCollapsed: (boolean|null|undefined),
 *  isUpdating: (boolean|null|undefined),
 *  isDeleteDisabled: (boolean|null|undefined),
 *  updateQuantity: (?)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $render(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var thumbnail = soy.asserts.assertType(goog.isString(opt_data.thumbnail) || opt_data.thumbnail instanceof goog.soy.data.SanitizedContent, 'thumbnail', opt_data.thumbnail, '!goog.soy.data.SanitizedContent|string');
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var name = soy.asserts.assertType(goog.isString(opt_data.name) || opt_data.name instanceof goog.soy.data.SanitizedContent, 'name', opt_data.name, '!goog.soy.data.SanitizedContent|string');
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var sku = soy.asserts.assertType(goog.isString(opt_data.sku) || opt_data.sku instanceof goog.soy.data.SanitizedContent, 'sku', opt_data.sku, '!goog.soy.data.SanitizedContent|string');
  /** @type {number} */
  var quantity = soy.asserts.assertType(goog.isNumber(opt_data.quantity), 'quantity', opt_data.quantity, 'number');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var error = soy.asserts.assertType(opt_data.error == null || (goog.isString(opt_data.error) || opt_data.error instanceof goog.soy.data.SanitizedContent), 'error', opt_data.error, '!goog.soy.data.SanitizedContent|null|string|undefined');
  /** @type {?} */
  var settings = opt_data.settings;
  /** @type {?} */
  var price = opt_data.price;
  /** @type {?} */
  var localization = opt_data.localization;
  /** @type {boolean|null|undefined} */
  var isDeleting = soy.asserts.assertType(opt_data.isDeleting == null || (goog.isBoolean(opt_data.isDeleting) || opt_data.isDeleting === 1 || opt_data.isDeleting === 0), 'isDeleting', opt_data.isDeleting, 'boolean|null|undefined');
  /** @type {boolean|null|undefined} */
  var isCollapsed = soy.asserts.assertType(opt_data.isCollapsed == null || (goog.isBoolean(opt_data.isCollapsed) || opt_data.isCollapsed === 1 || opt_data.isCollapsed === 0), 'isCollapsed', opt_data.isCollapsed, 'boolean|null|undefined');
  /** @type {boolean|null|undefined} */
  var isUpdating = soy.asserts.assertType(opt_data.isUpdating == null || (goog.isBoolean(opt_data.isUpdating) || opt_data.isUpdating === 1 || opt_data.isUpdating === 0), 'isUpdating', opt_data.isUpdating, 'boolean|null|undefined');
  /** @type {boolean|null|undefined} */
  var isDeleteDisabled = soy.asserts.assertType(opt_data.isDeleteDisabled == null || (goog.isBoolean(opt_data.isDeleteDisabled) || opt_data.isDeleteDisabled === 1 || opt_data.isDeleteDisabled === 0), 'isDeleteDisabled', opt_data.isDeleteDisabled, 'boolean|null|undefined');
  /** @type {?} */
  var updateQuantity = opt_data.updateQuantity;
  var miniumCartItemClasses__soy597 = '';
  miniumCartItemClasses__soy597 += 'minium-cart__item minium-item u-hoverable';
  miniumCartItemClasses__soy597 += isDeleting ? ' is-deleting' : '';
  miniumCartItemClasses__soy597 += isCollapsed ? ' is-collapsed' : '';
  miniumCartItemClasses__soy597 += error ? ' is-not-valid' : '';
  var deleteButtonClasses__soy612 = '';
  deleteButtonClasses__soy612 += isDeleteDisabled ? ' is-disabled' : '';
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', miniumCartItemClasses__soy597);
  incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('img');
        incrementalDom.attr('src', thumbnail);
        incrementalDom.attr('alt', name);
        incrementalDom.attr('class', 'minium-item__image');
    incrementalDom.elementOpenEnd();
    incrementalDom.elementClose('img');
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', 'minium-item__content');
    incrementalDom.elementOpenEnd();
      soyIdom.print(sku);
      incrementalDom.elementOpen('br');
      incrementalDom.elementClose('br');
      soyIdom.print(name);
      incrementalDom.elementOpen('br');
      incrementalDom.elementClose('br');
      $templateAlias1({isUpdating: isUpdating, content: price, direction: 'left', type: 'price'}, null, opt_ijData);
    incrementalDom.elementClose('div');
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', 'minium-item__actions');
    incrementalDom.elementOpenEnd();
      $templateAlias2({events: {updateQuantity: updateQuantity}, quantity: quantity, minQuantity: settings.minQuantity, maxQuantity: settings.maxQuantity, multipleQuantities: settings.multipleQuantities, allowedOptions: settings.allowedOptions}, null, opt_ijData);
      incrementalDom.elementOpenStart('a');
          incrementalDom.attr('href', '#');
          incrementalDom.attr('class', 'minium-item__delete' + deleteButtonClasses__soy612);
          incrementalDom.attr('data-onclick', 'deleteItem');
      incrementalDom.elementOpenEnd();
      incrementalDom.elementClose('a');
    incrementalDom.elementClose('div');
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', 'minium-item__cancel-delete');
    incrementalDom.elementOpenEnd();
      soyIdom.print(localization.TheElementHasBeenRemoved);
      incrementalDom.elementOpenStart('span');
          incrementalDom.attr('class', 'minium-button minium-button--inline');
          incrementalDom.attr('data-onclick', 'cancelDeletion');
      incrementalDom.elementOpenEnd();
        soyIdom.print(localization.Cancel);
      incrementalDom.elementClose('span');
    incrementalDom.elementClose('div');
    if ((error != null)) {
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'minium-item__error');
      incrementalDom.elementOpenEnd();
        soyIdom.print(error);
      incrementalDom.elementClose('div');
    }
  incrementalDom.elementClose('div');
}
exports.render = $render;
/**
 * @typedef {{
 *  thumbnail: (!goog.soy.data.SanitizedContent|string),
 *  name: (!goog.soy.data.SanitizedContent|string),
 *  sku: (!goog.soy.data.SanitizedContent|string),
 *  quantity: number,
 *  error: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  settings: (?),
 *  price: (?),
 *  localization: (?),
 *  isDeleting: (boolean|null|undefined),
 *  isCollapsed: (boolean|null|undefined),
 *  isUpdating: (boolean|null|undefined),
 *  isDeleteDisabled: (boolean|null|undefined),
 *  updateQuantity: (?)
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'CartProduct.render';
}

exports.render.params = ["thumbnail","name","sku","quantity","error","settings","price","localization","isDeleting","isCollapsed","isUpdating","isDeleteDisabled","updateQuantity"];
exports.render.types = {"thumbnail":"string ","name":"string ","sku":"string ","quantity":"int ","error":"string ","settings":"? ","price":"? ","localization":"? ","isDeleting":"bool ","isCollapsed":"bool ","isUpdating":"bool ","isDeleteDisabled":"bool ","updateQuantity":"? "};
templates = exports;
return exports;

});

class CartProduct extends Component {}
Soy.register(CartProduct, templates);
export { CartProduct, templates };
export default templates;
/* jshint ignore:end */
