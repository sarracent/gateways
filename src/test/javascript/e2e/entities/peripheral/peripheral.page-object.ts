import { element, by, ElementFinder } from 'protractor';

export class PeripheralComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-peripheral div table .btn-danger'));
    title = element.all(by.css('jhi-peripheral div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PeripheralUpdatePage {
    pageTitle = element(by.id('jhi-peripheral-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    uIDInput = element(by.id('field_uID'));
    vendorInput = element(by.id('field_vendor'));
    dateCreatedInput = element(by.id('field_dateCreated'));
    statusSelect = element(by.id('field_status'));
    gatewaySelect = element(by.id('field_gateway'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setUIDInput(uID) {
        await this.uIDInput.sendKeys(uID);
    }

    async getUIDInput() {
        return this.uIDInput.getAttribute('value');
    }

    async setVendorInput(vendor) {
        await this.vendorInput.sendKeys(vendor);
    }

    async getVendorInput() {
        return this.vendorInput.getAttribute('value');
    }

    async setDateCreatedInput(dateCreated) {
        await this.dateCreatedInput.sendKeys(dateCreated);
    }

    async getDateCreatedInput() {
        return this.dateCreatedInput.getAttribute('value');
    }

    async setStatusSelect(status) {
        await this.statusSelect.sendKeys(status);
    }

    async getStatusSelect() {
        return this.statusSelect.element(by.css('option:checked')).getText();
    }

    async statusSelectLastOption() {
        await this.statusSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async gatewaySelectLastOption() {
        await this.gatewaySelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async gatewaySelectOption(option) {
        await this.gatewaySelect.sendKeys(option);
    }

    getGatewaySelect(): ElementFinder {
        return this.gatewaySelect;
    }

    async getGatewaySelectedOption() {
        return this.gatewaySelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class PeripheralDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-peripheral-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-peripheral'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
