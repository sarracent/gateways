import { element, by, ElementFinder } from 'protractor';

export class GatewayComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-gateway div table .btn-danger'));
    title = element.all(by.css('jhi-gateway div h2#page-heading span')).first();

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

export class GatewayUpdatePage {
    pageTitle = element(by.id('jhi-gateway-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    serialNumberInput = element(by.id('field_serialNumber'));
    humanRadableNameInput = element(by.id('field_humanRadableName'));
    iPVFourInput = element(by.id('field_iPVFour'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setSerialNumberInput(serialNumber) {
        await this.serialNumberInput.sendKeys(serialNumber);
    }

    async getSerialNumberInput() {
        return this.serialNumberInput.getAttribute('value');
    }

    async setHumanRadableNameInput(humanRadableName) {
        await this.humanRadableNameInput.sendKeys(humanRadableName);
    }

    async getHumanRadableNameInput() {
        return this.humanRadableNameInput.getAttribute('value');
    }

    async setIPVFourInput(iPVFour) {
        await this.iPVFourInput.sendKeys(iPVFour);
    }

    async getIPVFourInput() {
        return this.iPVFourInput.getAttribute('value');
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

export class GatewayDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-gateway-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-gateway'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
