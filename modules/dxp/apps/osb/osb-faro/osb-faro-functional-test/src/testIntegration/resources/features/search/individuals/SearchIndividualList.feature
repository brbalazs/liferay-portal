@spira_Search @Search @Individuals @List @team_FARO @priority_3
Feature: Search for an Individual in the Individual's List
	As an Business User, I should be able to search for an individual in the individual's list by name

	Background: [Setup]
		* I go to the "Home" page
		* I login as "test@faro.io:test"
		* I should see the "Sites" page
		* I click "Individuals" in the sidebar
		* I click the "Known Individuals" tab

	Scenario: Search for an Individual
#	Scenario: Search for an Individual Using a First Name
		When I search for "Macy"
		Then I should only see Individuals named "Macy" in the table

#	Scenario: Search for an Individual Using a Last Name
		When I search for "Wuckert"
		Then I should only see Individuals named "Wuckert" in the table

#	Scenario: Search for an Individual Using a Full Name
		When I search for "Macy Wuckert"
		Then I should only see Individuals named "Macy Wuckert" in the table

#	Scenario: Search for an Individual Using an Email
		When I search for "Kym.Hand@hotmail.com"
		Then I should only see Individuals named "Kym Hand" in the table

#	Scenario: Search for an Individual Using a Job Title
		When I search for "Direct Tactics Manager"
		Then I should only see Individuals named "Jc Luettgen" in the table

#	Scenario: Search for an Individual Using the "worksFor" value
		When I search for "O'Keefe-Waelchi"
		Then I should only see Individuals named "An Padberg" in the table
