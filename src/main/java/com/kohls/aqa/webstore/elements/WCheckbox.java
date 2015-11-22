package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

public class WCheckbox extends GenericElement {

    private String value;

    private String name;

    @Override
    public ElementType getElementType() {
        return ElementType.CHECKBOX;
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (this.value != null) {
            this.softAssert.assertEquals(element.getAttribute("value"), this.value, "Ckeckbox value");
        }
        if (this.name != null) {
            this.softAssert.assertEquals(element.getAttribute("name"), this.name, "Ckeckbox name");
        }
        softAssert.assertAll();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
