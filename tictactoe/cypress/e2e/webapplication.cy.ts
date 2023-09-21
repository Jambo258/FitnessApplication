describe('Redirect routes', () => {

  it('Visits the app / url and redirect to /home', () => {
    cy.visit('/');
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
  });
  it('Visits the app with route that doesnt exist', () => {
    cy.visit('/abcdef');
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
  });
});

describe('Register user', () => {
  it('Visit homepage and click register in nav bar', () => {
    cy.visit('/');
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    cy.get('a[href="/register"]').click();
    cy.url().should('include', '/register');
  });
  it('Try to register user with existing email', () => {
    cy.visit('/register');
    cy.get('input[placeholder="Username"]').type('testguy');
    cy.get('input[placeholder="Email"]').type('admin@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('input[placeholder="ReType Password"').type('password');
    cy.get('button[type="submit"]').click();
    cy.contains('email exists').should(
      'be.visible'
    );
  });

  it('Try to register user with false inputs', () => {
    cy.visit('/register');
    cy.get('input[placeholder="Username"]').type('test');
    cy.get('input[placeholder="Email"]').type('testguy');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('input[placeholder="ReType Password"').type('password1');
    cy.get('input[placeholder="ReType Password"').blur();
    cy.contains('Minimum length for username is 5 characters.').should(
      'be.visible'
    );
    cy.contains('Please enter a valid email address.').should('be.visible');
    cy.contains('Passwords do not match.').should('be.visible');
    cy.get('button[type="submit"]').should('have.attr', 'disabled');
    cy.get('button[type="submit"]').click({ force: true });

  });

  it('Register user succeessfully', () => {
    cy.visit('/register');
    cy.get('input[placeholder="Username"]').type('testguy');
    cy.get('input[placeholder="Email"]').type('testguy@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('input[placeholder="ReType Password"').type('password');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    //cy.contains('.mat-button', 'Logout').should('exist');
    cy.contains('Logout').should('be.visible');
  });
});

describe('Login user', () => {
  it('Login with with false input', () => {
    cy.visit('/login');
    cy.get('input[placeholder="Email"]').type('testguy');
    cy.get('input[placeholder="Password"]').type('pass');
    cy.get('input[placeholder="Password"').blur();
    cy.contains('Minimum length for password is 5 characters.').should(
      'be.visible'
    );
    cy.contains('Please enter a valid email address.').should('be.visible');
    cy.get('button[type="submit"]').should('have.attr', 'disabled');
  });

  it('Login with user that doesnt exist', () => {
    cy.visit('/login');
    cy.get('input[placeholder="Email"]').type('testguy123@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('button[type="submit"]').click();
    cy.contains('Invalid email / password').should('be.visible');
  });



  it('Login succeessfully', () => {
    cy.visit('/login');
    cy.get('input[placeholder="Email"]').type('testguy@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    //cy.contains('.mat-button', 'Logout').should('exist');
    cy.contains('Logout').should('be.visible');
  });
});

describe('Logged in user tests', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.get('input[placeholder="Email"]').type('testguy@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    cy.contains('Logout').should('be.visible');
  });

  it('Change username with false input', () => {
    cy.get('a[href="/profilepage"]').click();
    cy.url().should('include', '/profilepage');
    cy.contains('Edit Username').click();
    cy.get('input[placeholder="Username"]').clear();
    cy.contains('button', 'Save').should('be.visible').click();
    cy.contains('Username is required.').should('be.visible');
  });



  it('Change password with false input', () => {
    cy.get('a[href="/profilepage"]').click();
    cy.url().should('include', '/profilepage');
    cy.contains('Edit Password').click();
    cy.get('input[placeholder="New Password"]').clear();
    cy.get('input[placeholder="Confirm New Password"]').clear();
    cy.contains('button', 'Save').should('be.visible').click();
    cy.contains('Password is required.').should('be.visible');
    cy.contains('Confirm Password is required.').should('be.visible');
  });

  it('Change password without giving same passwords', () => {
    cy.get('a[href="/profilepage"]').click();
    cy.url().should('include', '/profilepage');
    cy.contains('Edit Password').click();
    cy.get('input[placeholder="New Password"]').type('password1');
    cy.get('input[placeholder="Confirm New Password"]').type('password2');
    cy.contains('button', 'Save').should('be.visible').click();
    cy.contains('Passwords do not match.').should('be.visible');
  });

  it('Change email with false input', () => {
    cy.get('a[href="/profilepage"]').click();
    cy.url().should('include', '/profilepage');
    cy.contains('Edit Email').click();
    cy.get('input[placeholder="Email"]').clear();
    cy.contains('button', 'Save').should('be.visible').click();
    cy.contains('Email is required.').should('be.visible');
  });



  it('Delete account and on confirm press cancel', () => {
    cy.get('a[href="/profilepage"]').click();
    cy.url().should('include', '/profilepage');
    cy.contains('Delete Account').click();
    cy.contains('Confirm Deletion').should('be.visible');
    cy.contains('button', 'Cancel').should('be.visible').click();
  });



  it('Change username successfully', () => {
    cy.get('a[href="/profilepage"]').click();
    cy.url().should('include', '/profilepage');
    cy.contains('Edit Username').click();
    cy.get('input[placeholder="Username"]').clear();
    cy.get('input[placeholder="Username"]').type('newusername');
    cy.contains('button', 'Save').should('be.visible').click();
    cy.contains('mat-card-content', 'newusername').should('exist');
  });

  it('Change password and email successfully', () => {
    cy.get('a[href="/profilepage"]').click();
    cy.url().should('include', '/profilepage');
    cy.contains('Edit Password').click();
    cy.get('input[placeholder="New Password"]').type('password1');
    cy.get('input[placeholder="Confirm New Password"]').type('password1');
    cy.contains('button', 'Save').should('be.visible').click();
    cy.contains('Edit Email').click();
    cy.get('input[placeholder="Email"]').clear();
    cy.get('input[placeholder="Email"]').type('newusername@gmail.com');
    cy.contains('button', 'Save').should('be.visible').click();
    cy.contains('mat-card-content', 'newusername@gmail.com').should('exist');
  });
});

describe('Logged in user tests after changing password and email', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.get('input[placeholder="Email"]').type('newusername@gmail.com');
    cy.get('input[placeholder="Password"]').type('password1');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    cy.contains('Logout').should('be.visible');
  });

  it('Delete user succeessfully', () => {
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
