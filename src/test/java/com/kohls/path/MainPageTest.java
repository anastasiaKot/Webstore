package com.kohls.path;

import com.kohls.aqa.webstore.util.CommonParams;
import com.kohls.aqa.webstore.util.Config;
import com.kohls.aqa.webstore.elements.Element;
import net.sf.testng.databinding.DataBinding;
import net.sf.testng.databinding.TestInput;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.kohls.aqa.webstore.util.Config.DRIVER_TYPE;
import static com.kohls.aqa.webstore.util.Config.STORE_URL;
import static com.kohls.aqa.webstore.util.DriverFactory.close;
import static com.kohls.aqa.webstore.util.DriverFactory.getDriver;
import static java.lang.System.getProperty;

public class MainPageTest {

    private CommonParams commonParams;

    private String url = Config.getProperty(STORE_URL);

    private WebDriver driver;

    @BeforeClass
    protected void setUp() {
        this.driver = getDriver(getProperty(DRIVER_TYPE));
    }

    @BeforeMethod
    protected void beforeClass(Object[] params) {
        if (this.commonParams == null) {
            this.commonParams = (CommonParams) params[0];
//            driver.get(this.url + commonParams.getPath());
            driver.get(this.url + commonParams.getPath());
            driver.manage().window().maximize();
        }
    }

    @Test(groups = "MainPage")
    @DataBinding
    public void testMainPage(@TestInput(name = "params") CommonParams params,
                             @TestInput(name = "element") Element element) {
        element.doAssert(this.driver);
    }

    @AfterClass(alwaysRun = true)
    protected void tearDown() {
        if (this.driver != null) {
            close(driver);
        }
    }
}
