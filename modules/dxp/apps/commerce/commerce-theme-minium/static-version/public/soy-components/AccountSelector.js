"use strict";

import { debounce } from "debounce";

import template from "./AccountSelector.soy.js";
import Component from "metal-component";
import Soy from "metal-soy";

class AccountSelector extends Component {
created() {
	return (this.filterAccounts = debounce(this.filterAccounts, 500));
}

attached() {
	return (this.filteredAccounts = this.accounts);
}

toggleAccountSelector() {
	if (this.openingState === "closed") {
	  return (this.openingState = "open");
	}
	if (this.openingState === "open") {
	  this.openingState = "closing";
	  return setTimeout(() => {
		this.openingState = "closed";
	  }, 200);
	}
}

handleFilterChange(evt) {
	this.accountFilter = evt.target.value;
	return this.filterAccounts();
}

filterAccounts() {
	if (!this.accountFilter.length) {
	  return (this.filteredAccounts = this.accounts);
	}
	return (this.filteredAccounts = this.accounts.filter(
	  account =>
		account.name.toLowerCase().indexOf(this.accountFilter.toLowerCase()) >
		-1
	));
}
}

Soy.register(AccountSelector, template);

AccountSelector.STATE = {
accountFilter: {
	value: ""
},
openingState: {
	value: "closed"
},
accounts: {
	value: []
},
filteredAccounts: {
	value: []
}
};

export { AccountSelector };
export default AccountSelector;