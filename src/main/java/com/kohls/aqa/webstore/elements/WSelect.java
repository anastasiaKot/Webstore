package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kohls.aqa.webstore.elements.ElementType.SELECT;

public class WSelect extends GenericElement {

    private static final String LINK_ANCHOR = "^.*#(.*)$";
    private static final String WHITE_SPACE = "%20";
    private List<WOption> options;
    private Pattern pattern = Pattern.compile(LINK_ANCHOR);
    /**
     * Locator for alternative pseudo-select options, related to SELECT element,
     * if their implementation differs
     */
    private By.ByXPath locatorAOptions;

    @Override
    public ElementType getElementType() {
        return SELECT;
    }

    private void doAssertOptions(WebDriver driver) {
        List<WebElement> actualOptions = element.findElements(new By.ByXPath("./option"));
        if (this.options.size() != actualOptions.size()) {
            softAssert.assertEquals(actualOptions.size(), this.options.size(), "Amount of options");
            return;
        }
        for (int i = 0; i < actualOptions.size(); i++) {
            WOption option = this.options.get(i);
            option.element = actualOptions.get(i);
            option.doAssert(driver);
        }

        if (locatorAOptions != null) {
            List<WebElement> alternativeOptions = element.findElements(locatorAOptions);
            if (alternativeOptions != null) {
                for (int i = 0; i < alternativeOptions.size(); i++) {
                    WebElement aOption = alternativeOptions.get(i);
                    WOption optExp = this.options.get(i);
                    if (optExp.value != null) {
                        Matcher matcher = pattern.matcher(aOption.getAttribute("href"));
                        if (matcher.find()) {
                            String s = matcher.group(1).replaceAll(WHITE_SPACE, " ");
                            softAssert.assertTrue(Pattern.matches(optExp.value, s), "Alternative WOption value");
                        } else {
                            softAssert.assertEquals(aOption.getAttribute("href"), optExp.value, "Alternative WOption value");
                        }
                    }
                    if (optExp.text != null) {
                        // TODO: Add text assert for display:none tags
                        // softAssert.assertTrue(Pattern.matches(optExp.text, aOption.getText()), "Alternative WOption text ");
                    }
                }
            } else {
                softAssert.assertNotNull(alternativeOptions, "Alternative options");
            }
        }
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (this.options != null) doAssertOptions(driver);
        softAssert.assertAll();
    }

    public void setLocatorAOptions(String locatorAOptions) {
        this.locatorAOptions = new By.ByXPath(locatorAOptions);
    }

    public List<WOption> getOptions() {
        return options;
    }

    public void setOptions(List<WOption> options) {
        this.options = options;
    }
}
