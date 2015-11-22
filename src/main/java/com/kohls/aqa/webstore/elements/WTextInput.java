package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

import static com.kohls.aqa.webstore.elements.ElementType.TEXT_INPUT;

public class WTextInput extends GenericElement {

    private static final String VALUE_ATTR = "value";

    private String value;

    private String name;

    private String pattern;

    private String placeholder;

    private String autocomplete;

    private Boolean isReadonly;

    private Integer maxLength;

    @Override
    public ElementType getElementType() {
        return TEXT_INPUT;
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (this.value != null) {
            this.softAssert.assertEquals(element.getAttribute(VALUE_ATTR), this.value, "VALUE");
        }
        if (this.isReadonly != null) { // check that attribute validated correctly on existence
            this.softAssert.assertEquals(element.getAttribute("readonly"), String.valueOf(this.isReadonly), "READONLY");
        }
        if (this.placeholder != null) {
            this.softAssert.assertEquals(element.getAttribute("placeholder"), this.placeholder, "PLACEHOLDER");
        }
        if (this.name != null) {
            this.softAssert.assertEquals(element.getAttribute("name"), this.name, "NAME");
        }
        if (this.maxLength != null) {
            this.softAssert.assertEquals(element.getAttribute("maxlength"), String.valueOf(this.maxLength), "MAXLENGTH");
        }
        if (this.pattern != null) {
            this.softAssert.assertEquals(element.getAttribute("pattern"), this.pattern, "PATTERN");
        }
        if (this.autocomplete != null) {
            this.softAssert.assertEquals(element.getAttribute("autocomplete"), this.autocomplete, "AUTOCOMPLETE");
        }
        this.softAssert.assertAll();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setIsReadonly(Boolean isReadonly) {
        this.isReadonly = isReadonly;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
    }
}
