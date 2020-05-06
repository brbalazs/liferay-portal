@spira_Data_Source @Data_Source @DXP @Token @team_FARO @priority_4 @prototype
Feature: Sync Contacts by User Group using Auth Token
	As a Business User, I should be able to sync contacts by User Group using Token Authentication

	Background: [Setup] Navigate to the Data Source Page and Click to Add a Data Source
		* I go to the "Home" page
		* I login as "test@faro.io:test"
		* I should see the "Sites" page
		* I go to the "Data Source" page
		* I click the "Add Data Source" button
		* I click the "Liferay DXP" button
		* I click the "Connect with Token" button
		* I copy the DXP Authentication Token
		* I set up the local DXP instance
		* I connect Analytics Cloud to DXP using the Authentication Token

	Scenario: Sync Contacts by User Group - Auth Token
		Given I go to the "Synced Contacts" DXP Page
		And I sync the Data Source Contacts by the following User Groups:
			| Irvine |
		And I go to the "Individuals" page
		And I click the "Known Individuals" tab
		When I search for "ryan.weng@test.com"
		Then I should see an item named "Ryan Weng" in the table
		When I search for "nack.ho@test.com"
		Then I should see an item named "Nack Ho" in the table
		When I search for "shrall.tang@test.com"
		Then I should not see an item named "Shrall Tang" in the table