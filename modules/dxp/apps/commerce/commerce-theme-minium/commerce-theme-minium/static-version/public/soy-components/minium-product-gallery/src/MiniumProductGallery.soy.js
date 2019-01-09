/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from MiniumProductGallery.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace MiniumProductGallery.
 * @public
 */

goog.module('MiniumProductGallery.incrementaldom');

var incrementalDom = goog.require('incrementaldom');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');


/**
 * @param {$render.Params} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes|uselessCode}
 */
var $render = function(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {boolean} */
  var fullscreen = soy.asserts.assertType(goog.isBoolean(opt_data.fullscreen) || opt_data.fullscreen === 1 || opt_data.fullscreen === 0, 'fullscreen', opt_data.fullscreen, 'boolean');
  /** @type {?} */
  var images = opt_data.images;
  /** @type {number} */
  var selected = soy.asserts.assertType(goog.isNumber(opt_data.selected), 'selected', opt_data.selected, 'number');
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', 'minium-product-gallery');
  incrementalDom.elementOpenEnd();
  incrementalDom.elementOpenStart('figure', images[selected].preview);
      incrementalDom.attr('class', 'minium-product-gallery__main');
      incrementalDom.attr('data-onclick', 'toggleFullscreen');
      incrementalDom.attr('key', images[selected].preview);
  incrementalDom.elementOpenEnd();
  incrementalDom.elementOpenStart('img');
      incrementalDom.attr('src', images[selected].preview);
      incrementalDom.attr('alt', images[selected].description);
  incrementalDom.elementOpenEnd();
  incrementalDom.elementClose('img');
  incrementalDom.elementClose('figure');
  if (fullscreen) {
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', 'minium-product-gallery__fullscreen');
        incrementalDom.attr('data-onclick', 'toggleFullscreen');
    incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('img');
        incrementalDom.attr('src', images[selected].full);
        incrementalDom.attr('alt', images[selected].description);
    incrementalDom.elementOpenEnd();
    incrementalDom.elementClose('img');
    incrementalDom.elementClose('div');
  }
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', 'minium-product-gallery__thumbs');
  incrementalDom.elementOpenEnd();
  var image22List = images;
  var image22ListLen = image22List.length;
  for (var image22Index = 0; image22Index < image22ListLen; image22Index++) {
    var image22Data = image22List[image22Index];
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', 'minium-product-gallery__thumb ' + (image22Index == selected ? 'is-active' : ''));
        incrementalDom.attr('data-index', image22Index);
        incrementalDom.attr('data-onclick', 'handleThumbClick');
    incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('img');
        incrementalDom.attr('src', image22Data.thumb);
        incrementalDom.attr('alt', image22Data.description);
    incrementalDom.elementOpenEnd();
    incrementalDom.elementClose('img');
    incrementalDom.elementClose('div');
  }
  incrementalDom.elementClose('div');
  incrementalDom.elementClose('div');
};
exports.render = $render;
/**
 * @typedef {{
 *  fullscreen: boolean,
 *  images: ?,
 *  selected: number,
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'MiniumProductGallery.render';
}

exports.render.params = ["fullscreen","images","selected"];
exports.render.types = {"fullscreen":"bool ","images":"? ","selected":"int "};
templates = exports;
return exports;

});

class MiniumProductGallery extends Component {}
Soy.register(MiniumProductGallery, templates);
export { MiniumProductGallery, templates };
export default templates;
/* jshint ignore:end */
