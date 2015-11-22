package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.kohls.aqa.webstore.elements.ElementType.YEAR_DAY_SELECT;

public class WDayYearSelect extends WDateSelect {

    @Override
    public ElementType getElementType() {
        return YEAR_DAY_SELECT;
    }

    @Override
    protected void doAssertOption(WebDriver driver) {

        List<WebElement> actualOptions = element.findElements(locatorOptions);

        this.options.get(0).assertUniqueOption(actualOptions);

        WOptionDate optExp = this.options.get(1);

        Integer expect = optExp.getValueFirst();

        Integer valueLast = this.options.get(2).getValueLast();

        for (int i = 1; i < actualOptions.size() && expect <= valueLast; i++, expect++) {
            WebElement optAct = actualOptions.get(i);
            softAssert.assertEquals(optAct.getAttribute("value"), expect.toString(), "Option value");
            regExPatternMatchAssert(optAct.getText().trim(), expect.toString(), "Option text");
        }
    }
}