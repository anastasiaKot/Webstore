package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

import static com.kohls.aqa.webstore.elements.ElementType.BUTTON;

public class WButton extends GenericElement {

    private String value;
    private String name;

    @Override
    public ElementType getElementType() {
        return BUTTON;
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (this.value != null) {
            this.softAssert.assertEquals(element.getAttribute("value"), this.value);
        }
        if (this.name != null) {
            this.softAssert.assertEquals(element.getAttribute("name"), this.name);
        }
        this.softAssert.assertAll();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
