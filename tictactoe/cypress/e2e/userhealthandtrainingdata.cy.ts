describe('User Health Status tests user creation', () => {

  it('Register user succeessfully', () => {
    cy.visit('/register');
    cy.get('input[placeholder="Username"]').type('testdude');
    cy.get('input[placeholder="Email"]').type('testdude@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('input[placeholder="ReType Password"').type('password');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    cy.contains('Logout').should('be.visible');
  });
});

describe('User Health Status tests', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.get('input[placeholder="Email"]').type('testdude@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    cy.contains('Logout').should('be.visible');
  });

  it('View leaderboard when no avaible userDatas', () => {
    cy.get('a[href="/leaderboard"]').click();
    cy.url().should('include', '/leaderboard');
    cy.contains('No leaderboard data').should('be.visible');
  });

  it('Set up user healthData with invalid value types', () => {
    cy.get('a[href="/healthdata"]').click();
    cy.url().should('include', '/healthdata');
    cy.get('input[placeholder="Height (m)"]').type('a');
    cy.get('input[placeholder="Weight (kg)"]').type('b');
    cy.get('input[placeholder="Target Weight (kg)"]').type('c');
    cy.get('input[placeholder="Target Calories (kCal/Day)"').type('d');
    cy.get('input[placeholder="Target Steps (Day)"').type('e');
    cy.get('input[placeholder="Target Steps (Day)"').blur();
    cy.contains('Height is required.').should('be.visible');
    cy.contains('Weight is required.').should('be.visible');
    cy.contains('Target Weight is required.').should('be.visible');
    cy.contains('Target Calories is required.').should('be.visible');
    cy.contains('Target Steps is required.').should('be.visible');
    cy.get('button[type="submit"]').should('have.attr', 'disabled');
  });

  it('Set up user healthData with invalid values', () => {
    cy.get('a[href="/healthdata"]').click();
    cy.url().should('include', '/healthdata');
    cy.get('input[placeholder="Height (m)"]').type('-0.8');
    cy.get('input[placeholder="Weight (kg)"]').type('-10.5');
    cy.get('input[placeholder="Target Weight (kg)"]').type('-10');
    cy.get('input[placeholder="Target Calories (kCal/Day)"').type('-1000');
    cy.get('input[placeholder="Target Steps (Day)"').type('-100');
    cy.get('input[placeholder="Target Steps (Day)"').blur();
    cy.contains('Height must be greater than or equal to 1.0(m).').should(
      'be.visible'
    );
    cy.contains('Weight must be greater than or equal to 1(kg).').should(
      'be.visible'
    );
    cy.contains(
      'Target Weight must be greater than or equal to 1(kg).'
    ).should('be.visible');
    cy.contains(
      'Target Calories must be greater than or equal to 1kCal.'
    ).should('be.visible');
    cy.contains('Target Steps must be greater than or equal to 0.').should(
      'be.visible'
    );
    cy.get('button[type="submit"]').should('have.attr', 'disabled');
  });

  it('Set up user healthData with decimal values when integer only allowed on targetCalories and targetSteps', () => {
    cy.get('a[href="/healthdata"]').click();
    cy.url().should('include', '/healthdata');
    cy.get('input[placeholder="Height (m)"]').type('1.9');
    cy.get('input[placeholder="Weight (kg)"]').type('100');
    cy.get('input[placeholder="Target Weight (kg)"]').type('90');
    cy.get('input[placeholder="Target Calories (kCal/Day)"').type('2036.5');
    cy.get('input[placeholder="Target Steps (Day)"').type('3350.5');
    cy.get('button[type="submit"]').click();
    cy.contains('mat-card-content', 'Invalid request body fields').should(
      'exist'
    );
    cy.contains('mat-card-content', 'targetCalories must be an integer').should(
      'exist'
    );
    cy.contains('mat-card-content', 'targetSteps must be an integer').should(
      'exist'
    );
  });

  it('Set up user healthData succeessfully', () => {
    cy.get('a[href="/healthdata"]').click();
    cy.url().should('include', '/healthdata');
    cy.get('input[placeholder="Height (m)"]').type('1.9');
    cy.get('input[placeholder="Weight (kg)"]').type('100');
    cy.get('input[placeholder="Target Weight (kg)"]').type('90');
    cy.get('input[placeholder="Target Calories (kCal/Day)"').type('2036');
    cy.get('input[placeholder="Target Steps (Day)"').type('3350');
    cy.contains('mat-card-content', '2036 kCal / Day').should('exist');
    cy.get('button[type="submit"]').click();
  });

  it('Set up user healthData once it has been already given', () => {
    cy.get('a[href="/healthdata"]').click();
    cy.url().should('include', '/healthdata');
    cy.get('input[placeholder="Height (m)"]').type('1.9');
    cy.get('input[placeholder="Weight (kg)"]').type('100');
    cy.get('input[placeholder="Target Weight (kg)"]').type('90');
    cy.get('input[placeholder="Target Calories (kCal/Day)"').type('2036');
    cy.get('input[placeholder="Target Steps (Day)"').type('3350');
    cy.get('button[type="submit"]').click();
    cy.contains('mat-card-content', 'User already has a health status').should(
      'exist'
    );
  });

  it('Set up daily training data with invalid value types', () => {
    cy.get('a[href="/dailytraining"]').click();
    cy.url().should('include', '/dailytraining');
    cy.get('input[placeholder="Daily Calories"]').type('a');
    cy.get('input[placeholder="Daily Steps"').type('b');
    cy.get('input[placeholder="Current Weight (kg)"').type('c');
    cy.get('input[placeholder="Current Weight (kg)"').blur();
    cy.contains('Daily Calories is required.').should('be.visible');
    cy.contains('Daily Steps is required.').should('be.visible');
    cy.contains('Current Weight is required.').should('be.visible');
    cy.get('#submit-button').should('have.attr', 'disabled');
  });

  it('Set up daily training data with invalid values', () => {
    cy.get('a[href="/dailytraining"]').click();
    cy.url().should('include', '/dailytraining');
    cy.get('input[placeholder="Daily Calories"]').type('-1');
    cy.get('input[placeholder="Daily Steps"').type('-1');
    cy.get('input[placeholder="Current Weight (kg)"').type('-1');
    cy.get('input[placeholder="Current Weight (kg)"').blur();
    cy.contains(
      'Daily Calories must be greater than or equal to 1kCal.'
    ).should('be.visible');
    cy.contains('Daily Steps must be greater than or equal to 0.').should(
      'be.visible'
    );
    cy.contains(
      'Current Weight must be greater than or equal to 1(kg).'
    ).should('be.visible');
    cy.get('#submit-button').should('have.attr', 'disabled');
  });

  it('Set up daily training data succeessfully', () => {
    cy.get('a[href="/dailytraining"]').click();
    cy.url().should('include', '/dailytraining');
    cy.get('input[placeholder="Daily Calories"]').type('1800');
    cy.get('input[placeholder="Daily Steps"').type('3000');
    cy.get('input[placeholder="Current Weight (kg)"').type('99');
    cy.get('#submit-button').click();
  });

  it('Try to set up daily training data before 24h has passed', () => {
    cy.get('a[href="/dailytraining"]').click();
    cy.url().should('include', '/dailytraining');
    cy.get('input[placeholder="Daily Calories"]').type('1800');
    cy.get('input[placeholder="Daily Steps"').type('3000');
    cy.get('input[placeholder="Current Weight (kg)"').type('99');
    cy.get('#submit-button').click();
    cy.contains('You can make another request in 23 hours.').should(
      'be.visible'
    );
  });

  it('View leaderboard when there is data avaible', () => {
    cy.get('a[href="/leaderboard"]').click();
    cy.url().should('include', '/leaderboard');
    cy.contains('testdude').should(
      'exist'
    );
  });

  it('View user progress chart', () => {
    cy.get('a[href="/chart"]').click();
    cy.url().should('include', '/chart');
    cy.get('#MyChart1').should('exist');
    cy.get('#MyChart2').should('exist');
    cy.get('#MyChart3').should('exist');
  });

   it('Delete user succeessfully to maintain test quality', () => {
     cy.get('a[href="/profilepage"]').click();
     cy.url().should('include', '/profilepage');
     cy.contains('Delete Account').click();
     cy.contains('Confirm Deletion').should('be.visible');
     cy.get('#delete-button').click();
     cy.url().should('eq', Cypress.config().baseUrl + '/home');
     cy.contains('Login').should('be.visible');
     cy.contains('Register').should('be.visible');
   });

});