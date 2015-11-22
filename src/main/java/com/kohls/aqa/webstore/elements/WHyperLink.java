package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

import static com.kohls.aqa.webstore.elements.ElementType.HYPER_LINK;

public class WHyperLink extends GenericElement {

    private String value;
    private String valueRegEx;
    private String exHref;
    private String dataHref;

    @Override
    public ElementType getElementType() {
        return HYPER_LINK;
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        String actualHref = element.getAttribute("href");
        if (this.value != null) {
            this.softAssert.assertEquals(actualHref, this.value, "link value");
        }
        if (this.valueRegEx != null) {
            regExPatternMatchAssert(actualHref, this.valueRegEx, "Link regEx");
        }
        if (this.exHref != null) {
            this.softAssert.assertEquals(element.getAttribute("ex-href"), this.exHref, "link ex-href");
        }
        if (this.dataHref != null) {
            this.softAssert.assertEquals(element.getAttribute("data-href"), this.dataHref, "link data-href");
        }
        this.softAssert.assertAll();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueRegEx(String valueRegEx) {
        this.valueRegEx = valueRegEx;
    }

    public void setExHref(String exHref) {
        this.exHref = exHref;
    }

    public void setDataHref(String dataHref) {
        this.dataHref = dataHref;
    }
}
