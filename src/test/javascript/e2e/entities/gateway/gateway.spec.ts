/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GatewayComponentsPage, GatewayDeleteDialog, GatewayUpdatePage } from './gateway.page-object';

const expect = chai.expect;

describe('Gateway e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let gatewayUpdatePage: GatewayUpdatePage;
    let gatewayComponentsPage: GatewayComponentsPage;
    let gatewayDeleteDialog: GatewayDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Gateways', async () => {
        await navBarPage.goToEntity('gateway');
        gatewayComponentsPage = new GatewayComponentsPage();
        expect(await gatewayComponentsPage.getTitle()).to.eq('gatewaysApp.gateway.home.title');
    });

    it('should load create Gateway page', async () => {
        await gatewayComponentsPage.clickOnCreateButton();
        gatewayUpdatePage = new GatewayUpdatePage();
        expect(await gatewayUpdatePage.getPageTitle()).to.eq('gatewaysApp.gateway.home.createOrEditLabel');
        await gatewayUpdatePage.cancel();
    });

    it('should create and save Gateways', async () => {
        const nbButtonsBeforeCreate = await gatewayComponentsPage.countDeleteButtons();

        await gatewayComponentsPage.clickOnCreateButton();
        await promise.all([
            gatewayUpdatePage.setSerialNumberInput('serialNumber'),
            gatewayUpdatePage.setHumanRadableNameInput('humanRadableName'),
            gatewayUpdatePage.setIPVFourInput('iPVFour')
        ]);
        expect(await gatewayUpdatePage.getSerialNumberInput()).to.eq('serialNumber');
        expect(await gatewayUpdatePage.getHumanRadableNameInput()).to.eq('humanRadableName');
        expect(await gatewayUpdatePage.getIPVFourInput()).to.eq('iPVFour');
        await gatewayUpdatePage.save();
        expect(await gatewayUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await gatewayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Gateway', async () => {
        const nbButtonsBeforeDelete = await gatewayComponentsPage.countDeleteButtons();
        await gatewayComponentsPage.clickOnLastDeleteButton();

        gatewayDeleteDialog = new GatewayDeleteDialog();
        expect(await gatewayDeleteDialog.getDialogTitle()).to.eq('gatewaysApp.gateway.delete.question');
        await gatewayDeleteDialog.clickOnConfirmButton();

        expect(await gatewayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
