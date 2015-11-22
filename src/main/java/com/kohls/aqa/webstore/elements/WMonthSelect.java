package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class WMonthSelect extends WDateSelect {

    @Override
    public ElementType getElementType() {
        return ElementType.MONTH_SELECT;
    }

    @Override
    protected void doAssertOption(WebDriver driver) {

        List<WebElement> actualOptions = element.findElements(locatorOptions);

        List<String> monthExp = new ArrayList<>();
        monthExp.add(0, "January");
        monthExp.add(1, "February");
        monthExp.add(2, "March");
        monthExp.add(3, "April");
        monthExp.add(4, "May");
        monthExp.add(5, "June");
        monthExp.add(6, "July");
        monthExp.add(7, "August");
        monthExp.add(8, "September");
        monthExp.add(9, "October");
        monthExp.add(10, "November");
        monthExp.add(11, "December");

        int expMonthListSize = monthExp.size() + 1;

        if (expMonthListSize != actualOptions.size()) {
            softAssert.assertEquals(actualOptions.size(), expMonthListSize, "Amount of options");
            return;
        }

        this.options.get(0).assertUniqueOption(actualOptions);

        WOptionDate optExp = this.options.get(1);

        Integer expect = optExp.getValueFirst();

        Integer valueLast = this.options.get(2).getValueLast();

        for (int i = 1; i < actualOptions.size() && expect <= valueLast; i++, expect++) {
            WebElement optAct = actualOptions.get(i);
            softAssert.assertEquals(optAct.getAttribute("value"), expect.toString(), "Option value");
            regExPatternMatchAssert(optAct.getText().trim(), monthExp.get(i - 1), "Option text");
        }
    }
}
