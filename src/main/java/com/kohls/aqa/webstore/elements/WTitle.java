package com.kohls.aqa.webstore.elements;

import com.google.common.base.Objects;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import static com.kohls.aqa.webstore.elements.ElementType.TITLE;

public class WTitle implements Element {

    private SoftAssert softAssert = new SoftAssert();

    private String value;

    @Override
    public ElementType getElementType() {
        return TITLE;
    }

    @Override
    public void doAssert(WebDriver driver) {
        if (this.value != null) {
            String actualTitle = driver.getTitle();
            this.softAssert.assertEquals(actualTitle, this.value, "title value");
        }
        this.softAssert.assertAll();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("type", this.getElementType())
                .add("value", this.value)
                .omitNullValues()
                .toString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
