package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.kohls.aqa.webstore.elements.ElementType.LIST_OF_LINKS;

public class WListOfLinks extends GenericElement {

    protected final By.ByXPath LIST_ITEMS_LOCATOR = new By.ByXPath("./li/a");
    protected List<WHyperLink> listItems;

    @Override
    public ElementType getElementType() {
        return LIST_OF_LINKS;
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (this.listItems != null) doAssertListItems(driver);
        softAssert.assertAll();
    }

    public List<WHyperLink> getListItems() {
        return listItems;
    }

    public void setListItems(List<WHyperLink> listItems) {
        this.listItems = listItems;
    }

    private void doAssertListItems(WebDriver driver) {
        List<WebElement> actualItems = element.findElements(LIST_ITEMS_LOCATOR);
        if (this.listItems.size() != actualItems.size()) {
            softAssert.assertEquals(actualItems.size(), this.listItems.size(), "Amount of ListItems");
            return;
        }
        for (int i = 0; i < actualItems.size(); i++) {
            WHyperLink itemExp = this.listItems.get(i);
            itemExp.element = actualItems.get(i);
            itemExp.doAssert(driver);
        }
    }
}
