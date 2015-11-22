package com.kohls.aqa.webstore.elements;

import org.openqa.selenium.WebDriver;

import static com.kohls.aqa.webstore.elements.ElementType.IMAGE;

public class WImage extends GenericElement {

    private String src;
    private String alt;
    private String height;
    private String width;
    private String widthCSS;
    private String heightCSS;

    @Override
    public ElementType getElementType() {
        return IMAGE;
    }

    @Override
    public void doAssert(WebDriver driver) {
        super.doAssert(driver);
        if (src != null) {
            regExPatternMatchAssert(element.getAttribute("src"), src, "Image source");
        }
        if (alt != null) {
            softAssert.assertEquals(element.getAttribute("alt"), alt, "Alternative text");
        }
        if (height != null) {
            softAssert.assertEquals(element.getAttribute("height"), height, "Image height");
        }
        if (width != null) {
            softAssert.assertEquals(element.getAttribute("width"), width, "Image width");
        }
        if (widthCSS != null) {
            softAssert.assertEquals(element.getCssValue("width"), widthCSS, "Image CSS width");
        }
        if (heightCSS != null) {
            softAssert.assertEquals(element.getCssValue("height"), heightCSS, "Image CSS height");
        }
        softAssert.assertAll();
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setWidthCSS(String widthCSS) {
        this.widthCSS = widthCSS;
    }

    public void setHeightCSS(String heightCSS) {
        this.heightCSS = heightCSS;
    }
}
