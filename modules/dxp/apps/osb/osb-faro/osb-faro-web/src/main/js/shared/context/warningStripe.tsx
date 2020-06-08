import React from 'react';

export const WarningStripeContext = React.createContext<{
	showWarningStripe: boolean;
	setShowWarningStripe?:(value: boolean) => void;
}>({showWarningStripe: true});
