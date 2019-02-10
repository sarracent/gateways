/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PeripheralComponentsPage, PeripheralDeleteDialog, PeripheralUpdatePage } from './peripheral.page-object';

const expect = chai.expect;

describe('Peripheral e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let peripheralUpdatePage: PeripheralUpdatePage;
    let peripheralComponentsPage: PeripheralComponentsPage;
    /*let peripheralDeleteDialog: PeripheralDeleteDialog;*/

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Peripherals', async () => {
        await navBarPage.goToEntity('peripheral');
        peripheralComponentsPage = new PeripheralComponentsPage();
        expect(await peripheralComponentsPage.getTitle()).to.eq('gatewaysApp.peripheral.home.title');
    });

    it('should load create Peripheral page', async () => {
        await peripheralComponentsPage.clickOnCreateButton();
        peripheralUpdatePage = new PeripheralUpdatePage();
        expect(await peripheralUpdatePage.getPageTitle()).to.eq('gatewaysApp.peripheral.home.createOrEditLabel');
        await peripheralUpdatePage.cancel();
    });

    /* it('should create and save Peripherals', async () => {
        const nbButtonsBeforeCreate = await peripheralComponentsPage.countDeleteButtons();

        await peripheralComponentsPage.clickOnCreateButton();
        await promise.all([
            peripheralUpdatePage.setUIDInput('5'),
            peripheralUpdatePage.setVendorInput('vendor'),
            peripheralUpdatePage.setDateCreatedInput('2000-12-31'),
            peripheralUpdatePage.statusSelectLastOption(),
            peripheralUpdatePage.gatewaySelectLastOption(),
        ]);
        expect(await peripheralUpdatePage.getUIDInput()).to.eq('5');
        expect(await peripheralUpdatePage.getVendorInput()).to.eq('vendor');
        expect(await peripheralUpdatePage.getDateCreatedInput()).to.eq('2000-12-31');
        await peripheralUpdatePage.save();
        expect(await peripheralUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await peripheralComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

    /* it('should delete last Peripheral', async () => {
        const nbButtonsBeforeDelete = await peripheralComponentsPage.countDeleteButtons();
        await peripheralComponentsPage.clickOnLastDeleteButton();

        peripheralDeleteDialog = new PeripheralDeleteDialog();
        expect(await peripheralDeleteDialog.getDialogTitle())
            .to.eq('gatewaysApp.peripheral.delete.question');
        await peripheralDeleteDialog.clickOnConfirmButton();

        expect(await peripheralComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
