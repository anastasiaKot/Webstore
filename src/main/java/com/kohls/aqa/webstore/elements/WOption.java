package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

public class WOption extends GenericElement {

    protected String value;
    protected Boolean selected;

    public void setValue(String value) {
        this.value = value;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (this.value != null) {
            softAssert.assertEquals(element.getAttribute("value"), this.value, "Option value");
        }
        if (this.selected != null) {
            softAssert.assertEquals(element.isSelected(), this.selected.booleanValue(), "Option selected");
        }
    }

    @Override
    public ElementType getElementType() {
        return null;
    }
}
