package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.regex.Pattern;

public abstract class GenericElement implements Element {

    protected Boolean isDisplayed;
    protected Boolean isEnabled;
    protected By.ByXPath locator;
    protected String text;
    protected SoftAssert softAssert = new SoftAssert();
    protected String style;
    protected WebElement element;
    protected String title;
    protected String description;
    protected Integer coordinateX;
    protected Integer coordinateY;
    protected static final Integer OFFSET = 5;
    protected List<CSSRecord> css;

    public void doAssert(WebDriver driver) {
        if (element == null) {
            element = driver.findElement(locator);
        }
        if (isEnabled != null) {
            this.softAssert.assertEquals(element.isEnabled(), isEnabled.booleanValue(), "Enabled ");
        }
        if (isDisplayed != null) {
            this.softAssert.assertEquals(element.isDisplayed(), isDisplayed.booleanValue(), "Displayed, located" + this.locator);
        }
        if (style != null) {
            regExPatternMatchAssert(element.getAttribute("style"), style, "CSS style");
        }
        if (text != null) {
            regExPatternMatchAssert(element.getText(), text, "WText content");
        }
        if (this.title != null) {
            this.softAssert.assertEquals(element.getAttribute("title"), title, "Tooltip");
        }
        if (this.coordinateX != null) {
            this.softAssert.assertEquals(element.getLocation().getX(), coordinateX, OFFSET, "Coordinate X isn't match");
        }
        if (this.coordinateY != null) {
            this.softAssert.assertEquals(element.getLocation().getY(), coordinateY, OFFSET, "Coordinate Y isn't match");
        }
        if (this.css != null) {
            assertCSS();
        }
    }

    private void assertCSS() {
        for (CSSRecord style : css) {
            String message = String.format("%s CSS: %s", this.description, style.name);
            if (style.value != null) {
                this.softAssert.assertEquals(element.getCssValue(style.name), style.value, message);
            }
            if (style.valueRegEx != null) {
                regExPatternMatchAssert(element.getCssValue(style.name), style.valueRegEx, message);
            }
        }
    }

    public void setIsDisplayed(Boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setLocator(String xpath) {
        this.locator = new By.ByXPath(xpath);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoordinateX(Integer coordinateX) {
        this.coordinateX = coordinateX;
    }

    public void setCoordinateY(Integer coordinateY) {
        this.coordinateY = coordinateY;
    }

    public void setCss(List<CSSRecord> css) {
        this.css = css;
    }

    protected void regExPatternMatchAssert(String actualValue, String expectedPattern, String propertyName) {
        String msg = String.format("%s pattern match failed for element \"%s\":\n" +
                "Expected pattern: %s\nActual value: %s\n", propertyName, this.description, expectedPattern, actualValue);
        this.softAssert.assertTrue(Pattern.matches(expectedPattern, actualValue), msg);
    }

    public void setElement(WebElement element) {
        this.element = element;
    }
}

