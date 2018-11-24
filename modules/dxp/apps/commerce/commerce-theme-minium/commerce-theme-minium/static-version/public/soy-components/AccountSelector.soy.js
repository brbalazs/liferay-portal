/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from AccountSelector.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace AccountSelector.
 * @public
 */

goog.module('AccountSelector.incrementaldom');

goog.require('goog.soy.data.SanitizedContent');
var incrementalDom = goog.require('incrementaldom');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');


/**
 * @param {{
 *  openingState: (!goog.soy.data.SanitizedContent|string),
 *  filteredAccounts: (?)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $render(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var openingState = soy.asserts.assertType(goog.isString(opt_data.openingState) || opt_data.openingState instanceof goog.soy.data.SanitizedContent, 'openingState', opt_data.openingState, '!goog.soy.data.SanitizedContent|string');
  /** @type {?} */
  var filteredAccounts = opt_data.filteredAccounts;
  var curtainClasses__soy6 = '';
  curtainClasses__soy6 += openingState == 'open' ? ' is-open' : '';
  curtainClasses__soy6 += openingState == 'closing' ? ' is-closing' : '';
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', 'minium-dropdown');
  incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('a');
        incrementalDom.attr('href', '#');
        incrementalDom.attr('class', 'minium-topbar__button');
        incrementalDom.attr('data-onclick', 'toggleAccountSelector');
    incrementalDom.elementOpenEnd();
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'account-selector');
      incrementalDom.elementOpenEnd();
        incrementalDom.elementOpenStart('div');
            incrementalDom.attr('class', 'account-selector__image');
        incrementalDom.elementOpenEnd();
          incrementalDom.elementOpenStart('img');
              incrementalDom.attr('src', '/avatar.jpg');
              incrementalDom.attr('alt', '');
          incrementalDom.elementOpenEnd();
          incrementalDom.elementClose('img');
        incrementalDom.elementClose('div');
        incrementalDom.elementOpenStart('div');
            incrementalDom.attr('class', 'account-selector__title');
        incrementalDom.elementOpenEnd();
          incrementalDom.text('Select Account & Order');
        incrementalDom.elementClose('div');
        incrementalDom.elementOpenStart('div');
            incrementalDom.attr('class', 'account-selector__info');
        incrementalDom.elementOpenEnd();
          incrementalDom.text('No order selected');
        incrementalDom.elementClose('div');
      incrementalDom.elementClose('div');
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
        incrementalDom.attr('class', 'minium-dropdown__curtain' + curtainClasses__soy6);
    incrementalDom.elementOpenEnd();
      incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'account-switcher is-visible');
      incrementalDom.elementOpenEnd();
        incrementalDom.elementOpenStart('div');
            incrementalDom.attr('class', 'account-switcher__section');
        incrementalDom.elementOpenEnd();
          incrementalDom.elementOpenStart('div');
              incrementalDom.attr('class', 'minium-search');
          incrementalDom.elementOpenEnd();
            incrementalDom.elementOpenStart('div');
                incrementalDom.attr('class', 'minium-search__input');
            incrementalDom.elementOpenEnd();
              incrementalDom.elementOpenStart('input');
                  incrementalDom.attr('type', 'text');
                  incrementalDom.attr('placeholder', 'Search Acconts\u2026');
                  incrementalDom.attr('data-onkeyup', 'handleFilterChange');
              incrementalDom.elementOpenEnd();
              incrementalDom.elementClose('input');
            incrementalDom.elementClose('div');
            incrementalDom.elementOpenStart('a');
                incrementalDom.attr('href', '#');
                incrementalDom.attr('class', 'minium-search__button');
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
          incrementalDom.elementClose('div');
        incrementalDom.elementClose('div');
        incrementalDom.elementOpenStart('div');
            incrementalDom.attr('class', 'account-switcher__section account-switcher__section--fill');
        incrementalDom.elementOpenEnd();
          incrementalDom.elementOpenStart('div');
              incrementalDom.attr('class', 'account-list');
          incrementalDom.elementOpenEnd();
            incrementalDom.elementOpenStart('div');
                incrementalDom.attr('class', 'account-list__title');
            incrementalDom.elementOpenEnd();
              incrementalDom.text('Select Account...');
            incrementalDom.elementClose('div');
            var account26List = filteredAccounts;
            var account26ListLen = account26List.length;
            for (var account26Index = 0; account26Index < account26ListLen; account26Index++) {
                var account26Data = account26List[account26Index];
                $item({id: account26Data.id, thumbnail: account26Data.thumbnail, name: account26Data.name, link: account26Data.link}, null, opt_ijData);
              }
          incrementalDom.elementClose('div');
        incrementalDom.elementClose('div');
      incrementalDom.elementClose('div');
    incrementalDom.elementClose('div');
  incrementalDom.elementClose('div');
}
exports.render = $render;
/**
 * @typedef {{
 *  openingState: (!goog.soy.data.SanitizedContent|string),
 *  filteredAccounts: (?)
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'AccountSelector.render';
}


/**
 * @param {{
 *  thumbnail: (!goog.soy.data.SanitizedContent|string),
 *  name: (!goog.soy.data.SanitizedContent|string)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $item(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var thumbnail = soy.asserts.assertType(goog.isString(opt_data.thumbnail) || opt_data.thumbnail instanceof goog.soy.data.SanitizedContent, 'thumbnail', opt_data.thumbnail, '!goog.soy.data.SanitizedContent|string');
  /** @type {!goog.soy.data.SanitizedContent|string} */
  var name = soy.asserts.assertType(goog.isString(opt_data.name) || opt_data.name instanceof goog.soy.data.SanitizedContent, 'name', opt_data.name, '!goog.soy.data.SanitizedContent|string');
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('class', 'minium-dropdown');
  incrementalDom.elementOpenEnd();
    incrementalDom.elementOpenStart('a');
        incrementalDom.attr('href', '/' + name);
        incrementalDom.attr('class', 'account-list__item u-hoverable');
    incrementalDom.elementOpenEnd();
      incrementalDom.elementOpenStart('img');
          incrementalDom.attr('src', thumbnail);
          incrementalDom.attr('alt', name);
      incrementalDom.elementOpenEnd();
      incrementalDom.elementClose('img');
      incrementalDom.elementOpen('span');
        soyIdom.print(name);
      incrementalDom.elementClose('span');
    incrementalDom.elementClose('a');
  incrementalDom.elementClose('div');
}
exports.item = $item;
/**
 * @typedef {{
 *  thumbnail: (!goog.soy.data.SanitizedContent|string),
 *  name: (!goog.soy.data.SanitizedContent|string)
 * }}
 */
$item.Params;
if (goog.DEBUG) {
  $item.soyTemplateName = 'AccountSelector.item';
}

exports.render.params = ["openingState","filteredAccounts"];
exports.render.types = {"openingState":"string","filteredAccounts":"?"};
exports.item.params = ["thumbnail","name"];
exports.item.types = {"thumbnail":"string ","name":"string "};
templates = exports;
return exports;

});

class AccountSelector extends Component {}
Soy.register(AccountSelector, templates);
export { AccountSelector, templates };
export default templates;
/* jshint ignore:end */
