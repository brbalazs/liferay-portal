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
  /** @type {number} */
  var selected = soy.asserts.assertType(goog.isNumber(opt_data.selected), 'selected', opt_data.selected, 'number');
  /** @type {?} */
  var images = opt_data.images;
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', 'minium-product-gallery');
  incrementalDom.elementOpenEnd();
  incrementalDom.elementOpenStart('figure');
      incrementalDom.attr('class', 'minium-product-gallery__main');
  incrementalDom.elementOpenEnd();
  incrementalDom.elementOpenStart('img');
      incrementalDom.attr('src', images[selected].preview);
      incrementalDom.attr('alt', images[selected].description);
  incrementalDom.elementOpenEnd();
  incrementalDom.elementClose('img');
  incrementalDom.elementClose('figure');
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', 'minium-product-gallery__thumbs');
  incrementalDom.elementOpenEnd();
  var image11List = images;
  var image11ListLen = image11List.length;
  for (var image11Index = 0; image11Index < image11ListLen; image11Index++) {
    var image11Data = image11List[image11Index];
    incrementalDom.elementOpenStart('div');
        incrementalDom.attr('class', 'minium-product-gallery__thumb ' + (image11Index == selected ? 'is-active' : ''));
        incrementalDom.attr('data-index', image11Index);
        incrementalDom.attr('data-onclick', 'handleThumbClick');
    incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('img');
        incrementalDom.attr('src', image11Data.thumb);
        incrementalDom.attr('alt', image11Data.description);
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
 *  selected: number,
 *  images: ?,
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'MiniumProductGallery.render';
}

exports.render.params = ["selected","images"];
exports.render.types = {"selected":"int ","images":"? "};
templates = exports;
return exports;

});

class MiniumProductGallery extends Component {}
Soy.register(MiniumProductGallery, templates);
export { MiniumProductGallery, templates };
export default templates;
/* jshint ignore:end */
