package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebElement;

import java.util.List;

public class WOptionDate extends WOption {

    private Integer valueFirst;

    private Integer valueLast;

    public Integer getValueFirst() {
        return valueFirst;
    }

    public Integer getValueLast() {
        return valueLast;
    }

    protected void assertUniqueOption(List<WebElement> actualOptions) {
        WebElement uniqueOpt = actualOptions.get(0);
        if (this.value != null) {
            softAssert.assertEquals(uniqueOpt.getAttribute("value"), this.value,
                    "Value of unique option");
        }
        if (this.selected != null) {
            softAssert.assertEquals(uniqueOpt.isSelected(), this.selected.booleanValue(), "Option selected");
        }
        if (this.text != null) {
            regExPatternMatchAssert(uniqueOpt.getText().trim(), this.text, "Text of unique option");
        }
    }
}

