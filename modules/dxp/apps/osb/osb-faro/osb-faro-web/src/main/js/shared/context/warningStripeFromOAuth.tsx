import React from 'react';

export const WarningStripeFromOAuthContext = React.createContext<{
	showWarningStripe: boolean;
	setShowWarningStripe?:(value: boolean) => void;
}>(null);
