@spira_Pagination @Pagination @Individuals @List @team_FARO @priority_3
Feature: Individuals Segment list has pagination
	As a Business User, I should be able to paginate to the second page of individuals segment list

	Background: [Setup] Create 4 Static Segments
		* I go to the "Home" page
		* I login as "test@faro.io:test"
		* I should see the "Sites" page
		* I go to the "Segments" page
		* I click the "Create Segment" button
		* I click the "Static Segment" dropdown option
		* I click the "Add Members" button
		* I search for "Bao Raynor"
		* I click the checkbox on the table row containing "Bao Raynor"
		* I click the "Add" button
		* I name the Static segment "CreateStaticIndividualsSegment1 - ${Random.1}" and save it
		* I go to the "Segments" page
		* I click the "Create Segment" button
		* I click the "Static Segment" dropdown option
		* I click the "Add Members" button
		* I search for "Bao Raynor"
		* I click the checkbox on the table row containing "Bao Raynor"
		* I click the "Add" button
		* I name the Static segment "CreateStaticIndividualsSegment2 - ${Random.1}" and save it
		* I go to the "Segments" page
		* I click the "Create Segment" button
		* I click the "Static Segment" dropdown option
		* I click the "Add Members" button
		* I search for "Bao Raynor"
		* I click the checkbox on the table row containing "Bao Raynor"
		* I click the "Add" button
		* I name the Static segment "CreateStaticIndividualsSegment3 - ${Random.1}" and save it
		* I go to the "Segments" page
		* I click the "Create Segment" button
		* I click the "Static Segment" dropdown option
		* I click the "Add Members" button
		* I search for "Bao Raynor"
		* I click the checkbox on the table row containing "Bao Raynor"
		* I click the "Add" button
		* I name the Static segment "CreateStaticIndividualsSegment4 - ${Random.1}" and save it

	Scenario: Paginate to the second page of individuals segment
		Given I go to the "Individuals" page
		And I click the "Known Individuals" tab
		And I search for "Bao Raynor"
		And I click "Bao Raynor" in the table
		And I click the "Segments" tab
		And I set the pagination delta to "5"
		When I go to page "2" in the table
		Then I should be on page "2" in the table
		And I should see a segment named "everybody" in the table