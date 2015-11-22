package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

import static com.kohls.aqa.webstore.elements.ElementType.TEXT;

public class WText extends GenericElement {

    @Override
    public ElementType getElementType() {
        return TEXT;
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        softAssert.assertAll();
    }
}
