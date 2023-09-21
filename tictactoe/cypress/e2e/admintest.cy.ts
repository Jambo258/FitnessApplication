describe('Admin profile tests', () => {
  it('Register dummy user succeessfully for admin to delete', () => {
    cy.visit('/register');
    cy.get('input[placeholder="Username"]').type('dummydude');
    cy.get('input[placeholder="Email"]').type('dummydude@gmail.com');
    cy.get('input[placeholder="Password"]').type('password');
    cy.get('input[placeholder="ReType Password"').type('password');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    cy.contains('Logout').should('be.visible');
  });
  it('Login as admin and delete dummy user', () => {
    cy.visit('/login');
    cy.get('input[placeholder="Email"]').type('admin@gmail.com');
    cy.get('input[placeholder="Password"]').type('admin');
    cy.get('button[type="submit"]').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/home');
    cy.contains('Logout').should('be.visible');
    cy.get('a[href="/adminpage"]').click();
    cy.url().should('include', '/adminpage');
    cy.contains('button', 'Delete dummydude').click();
    cy.contains('Confirm Deletion').should('be.visible');
    cy.get('#delete-button').click();
    cy.contains('dummydude').should('not.exist');
  });
});
