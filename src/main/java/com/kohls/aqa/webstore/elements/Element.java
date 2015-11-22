package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

public interface Element {

    ElementType getElementType();

    void doAssert(WebDriver driver);
}
