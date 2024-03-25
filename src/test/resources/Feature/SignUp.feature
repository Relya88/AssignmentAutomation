Feature: Registration

  As a supporter of BasketballEngland
  I want to register my information as user
  So that I can be a member

  Scenario Outline: Register as user and become a member

    Given I navigate to page BasketballEngland using "<Browser>"
    When I enter my "<dateOfBirth>"
    And I enter "<First Name>" and "<Last Name>"
    And I enter my email and confirmed email in both fields
    And I enter my Password "<Password>" and retype it "<RetypePassword>"
    And I tick the <TermsAndConditions> checkbox is ticked
    And I tick boxes for PR and CoC are ticked
    And I click on the Confirm and join button
    Then my user account should be created giving the "<Result>"

    Examples:
      | Browser | dateOfBirth | First Name | Last Name | Password   | RetypePassword | TermsAndConditions | Result                                                                           |
      | Chrome  | 10/10/2000  | Siri       | Walker    | UseRandom  | UseRandom      | ticked             | THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND                        |
      | Edge    | 10/10/2000  | Siri       |           | UseRandom  | UseRandom      | ticked             | error: Last Name is required                                                     |
      | Chrome  | 10/10/2000  | Siri       | Walker    | password98 | password58     | ticked             | error: Password did not match                                                    |
      | Edge    | 10/10/2000  | Siri       | Walker    | UseRandom  | UseRandom      | not ticked         | error: You must confirm that you have read and accepted our Terms and Conditions |
