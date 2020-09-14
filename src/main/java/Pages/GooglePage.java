package Pages;

import Support.Paths;
import Tests.GoogleTests;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GooglePage extends WebPage{

    private GoogleTests test;
    private Paths paths;
    private WebElement element;

    public GooglePage(WebDriver browser){
        super(browser);
        super.setUrl("https://www.google.com");
        test = new GoogleTests(browser);
        paths = new Paths();
    }

    /**
     * Method to select the search bar from Google page
     */
    public void selectSearchBar(){
        element = test.searchElement(paths.nameGoogleSearchBar(), "NAME");
        test.clickElement(element);
    }

    /**
     * Method to write something at the search bar of Google page
     * @param searchedPhrase is the frase written into Google Searchbar
     */
    public void writePhraseToSearch(String searchedPhrase){
        test.write(element, searchedPhrase);
    }

    /**
     * Method to submit the phrase you wrote into search bar and wait until the results show up
     * @return
     */
    public Boolean confirmSearch(){
        test.submit(element);
        element = test.searchElement(paths.idGoogleResults(), "ID");
        Boolean hasFoundResults = test.waitElement(element);
        return hasFoundResults;
    }

    /**
     * Method to get all the results from Google Results page, after you search for something
     * @return
     */
    public List<WebElement> getResults(){
        List<WebElement> allResults = test.searchElements(paths.xpathGoogleEachResult(), "XPATH");
        return allResults;
    }

    /**
     * Method to click in an element into Google Results page
     * @param element that should be clicked
     */
    public void clickOnResult(WebElement element){
        test.clickElement(element);
    }
}
