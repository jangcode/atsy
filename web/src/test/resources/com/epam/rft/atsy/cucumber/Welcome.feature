Feature: Welcome

  Background:
    Given The user signed in
    Given The following application positions exist for candidates:
      | Candidate name | Candidate ID | Position  |
      | Candidate A    | 1            | Fejlesztő |
      | Candidate B    | 2            |           |
      | Candidate C    | 3            | Fejlszető |
    Given The applications of Candidate A are deleted.

  Scenario: the Candidates page appears
    Given the user clicks on the logo
    Then the Candidates page appears
    And the list of candidates appears with the columns: Név, E-Mail, Telefonszám, Pályázott pozíciók

  Scenario: the candidates are sorted by default name ascending
    Given there are existing candidates:
      | name        | email                | phone        | positions |
      | Candidate A | candidate.a@atsy.com | +36105555555 |           |
      | Candidate B | candidate.b@atsy.com | +36106666666 |           |
      | Candidate C | candidate.c@atsy.com | +36107777777 | Fejlesztő |
    When the user clicks on the logo
    Then the Candidates page appears
    And the list of candidates shown in order
      | name        | email                | phone        | positions |
      | Candidate A | candidate.a@atsy.com | +36105555555 |           |
      | Candidate B | candidate.b@atsy.com | +36106666666 |           |
      | Candidate C | candidate.c@atsy.com | +36107777777 | Fejlesztő |

  Scenario Outline: the candidates list sorting field can be changed
    Given there are existing candidates:
      | name        | email                | phone        | positions |
      | Candidate A | candidate.a@atsy.com | +36105555555 |           |
      | Candidate B | candidate.b@atsy.com | +36106666666 |           |
      | Candidate C | candidate.c@atsy.com | +36107777777 | Fejlesztő |
    When the user clicks on the logo
    Then the Candidates page appears
    When the user changes the order field to <field>, <order>
    And the list of candidates shown ordered by <field> as <order>
    Examples:
      | field     | order |
      | name      | asc   |
      | email     | asc   |
      | phone     | asc   |
      | positions | asc   |
      | name      | desc  |
      | email     | desc  |
      | phone     | desc  |
      | positions | desc  |