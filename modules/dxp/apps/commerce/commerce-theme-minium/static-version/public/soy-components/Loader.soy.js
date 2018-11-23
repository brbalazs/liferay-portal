/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from Loader.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace Loader.
 * @hassoydeltemplate {Loader.Content.idom}
 * @hassoydelcall {Loader.Content.idom}
 * @public
 */

goog.module('Loader.incrementaldom');

goog.require('goog.soy.data.SanitizedContent');
var incrementalDom = goog.require('incrementaldom');
goog.require('soy');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');

var $templateAlias1 = Soy.getTemplate('Price.incrementaldom', 'render');


/**
 * @param {{
 *  content: (?),
 *  isUpdating: (boolean|null|undefined),
 *  direction: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  inverted: (boolean|null|undefined),
 *  loaderType: (boolean|null|undefined),
 *  type: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $render(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {?} */
  var content = opt_data.content;
  /** @type {boolean|null|undefined} */
  var isUpdating = soy.asserts.assertType(opt_data.isUpdating == null || (goog.isBoolean(opt_data.isUpdating) || opt_data.isUpdating === 1 || opt_data.isUpdating === 0), 'isUpdating', opt_data.isUpdating, 'boolean|null|undefined');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var direction = soy.asserts.assertType(opt_data.direction == null || (goog.isString(opt_data.direction) || opt_data.direction instanceof goog.soy.data.SanitizedContent), 'direction', opt_data.direction, '!goog.soy.data.SanitizedContent|null|string|undefined');
  /** @type {boolean|null|undefined} */
  var inverted = soy.asserts.assertType(opt_data.inverted == null || (goog.isBoolean(opt_data.inverted) || opt_data.inverted === 1 || opt_data.inverted === 0), 'inverted', opt_data.inverted, 'boolean|null|undefined');
  /** @type {boolean|null|undefined} */
  var loaderType = soy.asserts.assertType(opt_data.loaderType == null || (goog.isBoolean(opt_data.loaderType) || opt_data.loaderType === 1 || opt_data.loaderType === 0), 'loaderType', opt_data.loaderType, 'boolean|null|undefined');
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var type = soy.asserts.assertType(opt_data.type == null || (goog.isString(opt_data.type) || opt_data.type instanceof goog.soy.data.SanitizedContent), 'type', opt_data.type, '!goog.soy.data.SanitizedContent|null|string|undefined');
  var pendingClasses__soy747 = '';
  pendingClasses__soy747 += isUpdating ? ' is-loading' : '';
  incrementalDom.elementOpenStart('span');
      incrementalDom.attr('class', 'minium-loader-container' + pendingClasses__soy747);
  incrementalDom.elementOpenEnd();
    if (!loaderType || loaderType == 'horizontal') {
      $horizontalLoader(opt_data, null, opt_ijData);
    }
    incrementalDom.elementOpenStart('span');
        incrementalDom.attr('class', 'minium-loader-container__content');
    incrementalDom.elementOpenEnd();
      soy.$$getDelegateFn(soy.$$getDelTemplateId('Loader.Content.idom'), type, false)(opt_data, null, opt_ijData);
    incrementalDom.elementClose('span');
  incrementalDom.elementClose('span');
}
exports.render = $render;
/**
 * @typedef {{
 *  content: (?),
 *  isUpdating: (boolean|null|undefined),
 *  direction: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  inverted: (boolean|null|undefined),
 *  loaderType: (boolean|null|undefined),
 *  type: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'Loader.render';
}


/**
 * @param {{
 *  content: (?)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function __deltemplate_s767_687fe695(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {?} */
  var content = opt_data.content;
  soyIdom.print(content);
}
exports.__deltemplate_s767_687fe695 = __deltemplate_s767_687fe695;
/**
 * @typedef {{
 *  content: (?)
 * }}
 */
__deltemplate_s767_687fe695.Params;
if (goog.DEBUG) {
  __deltemplate_s767_687fe695.soyTemplateName = 'Loader.__deltemplate_s767_687fe695';
}
soy.$$registerDelegateFn(soy.$$getDelTemplateId('Loader.Content.idom'), 'default', 0, __deltemplate_s767_687fe695);


/**
 * @param {{
 *  content: (?)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function __deltemplate_s772_2c54a775(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {?} */
  var content = opt_data.content;
  $templateAlias1({price: content}, null, opt_ijData);
}
exports.__deltemplate_s772_2c54a775 = __deltemplate_s772_2c54a775;
/**
 * @typedef {{
 *  content: (?)
 * }}
 */
__deltemplate_s772_2c54a775.Params;
if (goog.DEBUG) {
  __deltemplate_s772_2c54a775.soyTemplateName = 'Loader.__deltemplate_s772_2c54a775';
}
soy.$$registerDelegateFn(soy.$$getDelTemplateId('Loader.Content.idom'), 'price', 0, __deltemplate_s772_2c54a775);


/**
 * @param {{
 *  direction: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  inverted: (boolean|null|undefined)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $horizontalLoader(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  opt_data = opt_data || {};
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var direction = soy.asserts.assertType(opt_data.direction == null || (goog.isString(opt_data.direction) || opt_data.direction instanceof goog.soy.data.SanitizedContent), 'direction', opt_data.direction, '!goog.soy.data.SanitizedContent|null|string|undefined');
  /** @type {boolean|null|undefined} */
  var inverted = soy.asserts.assertType(opt_data.inverted == null || (goog.isBoolean(opt_data.inverted) || opt_data.inverted === 1 || opt_data.inverted === 0), 'inverted', opt_data.inverted, 'boolean|null|undefined');
  var loaderClasses__soy782 = '';
  loaderClasses__soy782 += 'loader-dots';
  loaderClasses__soy782 += (direction != null) ? ' loader-dots--' + direction : '';
  loaderClasses__soy782 += inverted ? ' loader-dots--inverted' : '';
  incrementalDom.elementOpenStart('span');
      incrementalDom.attr('class', 'minium-loader-container__loader');
  incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', loaderClasses__soy782);
    incrementalDom.elementOpenEnd();
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'loader-dots__dot');
      incrementalDom.elementOpenEnd();
      incrementalDom.elementClose('div');
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'loader-dots__dot');
      incrementalDom.elementOpenEnd();
      incrementalDom.elementClose('div');
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'loader-dots__dot');
      incrementalDom.elementOpenEnd();
      incrementalDom.elementClose('div');
    incrementalDom.elementClose('div');
  incrementalDom.elementClose('span');
}
exports.horizontalLoader = $horizontalLoader;
/**
 * @typedef {{
 *  direction: (!goog.soy.data.SanitizedContent|null|string|undefined),
 *  inverted: (boolean|null|undefined)
 * }}
 */
$horizontalLoader.Params;
if (goog.DEBUG) {
  $horizontalLoader.soyTemplateName = 'Loader.horizontalLoader';
}

exports.render.params = ["content","isUpdating","direction","inverted","loaderType","type"];
exports.render.types = {"content":"?","isUpdating":"bool","direction":"string","inverted":"bool","loaderType":"bool","type":"string"};
exports.horizontalLoader.params = ["direction","inverted"];
exports.horizontalLoader.types = {"direction":"string","inverted":"bool"};
templates = exports;
return exports;

});

class Loader extends Component {}
Soy.register(Loader, templates);
export { Loader, templates };
export default templates;
/* jshint ignore:end */
