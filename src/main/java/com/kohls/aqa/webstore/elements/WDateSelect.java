package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class WDateSelect extends GenericElement {

    protected abstract void doAssertOption(WebDriver driver);

    protected List<WOptionDate> options;

    protected By.ByXPath locatorOptions;

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (this.options != null) doAssertOption(driver);
        softAssert.assertAll();
    }
}
