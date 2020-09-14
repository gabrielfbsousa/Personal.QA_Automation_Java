package StepDefinitions;

import java.util.ArrayList;
import java.util.List;

import BrowsingSettings.Setup;
import Pages.AmazonPage;
import Pages.GooglePage;
import Pages.WebPage;
import Support.RestRequests;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class StepDefinitions {

    private WebDriver browser;
    private WebPage pom;

    @Before
    public void setUp() throws Exception {
        Setup chromeSetup = new Setup();
        browser = chromeSetup.getBrowser();
    }

    @After
    public void finishTest() throws Exception{
        pom.closeBrowser();
    }

    /**
     * For Step: Open browser on "www.google.com"
     *
     * A little bit generic, so that can be reused for other sites as well
     * In case you get a new possible site, you can have a Page class specific for it, inheriting from WebPage
     * This way, the common methods are shared, while the specific method are kept for each Page class
     * @param url that you want to navigate to
     * @throws Throwable
     */
    @Given("^I open the browser at (.*?)$")
    public void browseToWebsite(String url) throws Throwable {
        switch (url) {
            case "Google":
                pom = new GooglePage(browser);
                break;
            case "Amazon":
                pom = new AmazonPage(browser);
                break;
            default :
                pom = new WebPage(browser);
                pom.setUrl("https://" + url);
        }
        pom.navigateToPage();
    }

    /**
     * For step: Search for "Amazon Brasil" and Search
     *
     * Also generic, to accept other possible searches
     * It depends that the last step had been a navigation to Google page.
     * Because of that, confirms if the pom is indeed an instance from GooglePage
     * This method will fill Google's search bar with "Amazon Brasil" and submit.
     * It will also wait for the results page to load
     * @param contentToSearch
     * @throws Throwable
     */
    @When("^I search for \"([^\"]*)\"$")
    public void searchFor(String contentToSearch) throws Throwable {
        if(pom instanceof GooglePage == false){
            throw new Exception("You need first to navigate to Google");
        }

        GooglePage googlePage = (GooglePage) pom;
        googlePage.selectSearchBar();
        googlePage.writePhraseToSearch(contentToSearch);

        Boolean hasResults = googlePage.confirmSearch();
        if(!hasResults){
            throw new Exception("Your search has not loaded any results");
        }
    }

    /**
     * For Step: Navigate to "www.amazon.com.br" Through The Search Page
     *
     * Method that will select one URL that must be present from at Google results page
     * A little adaptation is done, to add the https, since it's not presented like this at the google page
     * The validation if the correct page was loaded is done through the URL at the browser
     * @param contentToSearch
     * @throws Throwable
     */
    @Then("^I select the result of \"([^\"]*)\"$")
    public void selectGoogleResult(String contentToSearch) throws Throwable {
        if(pom instanceof GooglePage == false){
            throw new Exception("You need first to navigate to Google");
        }

        GooglePage googlePage = (GooglePage) pom;
        List<WebElement> googleResults = googlePage.getResults();

        WebElement chosenResult = null;
        contentToSearch = "https://" + contentToSearch;
        for (WebElement result : googleResults)
        {
            if(result.getAttribute("href").contains(contentToSearch)){
                chosenResult = result;
                break;
            }
        }

        googlePage.clickOnResult(chosenResult);
        Boolean isValid = false;
        if(googlePage.getCurrentUrl().contains(contentToSearch)){
            isValid = true;
        }

        //googlePage.closeBrowser();
        assertTrue(isValid);
    }

    /**
     * For step: Search for "Iphone" Using the Search Bar
     *
     * Method to fill Amazon's search bar with desired phrase
     * It checks if we are actually at AmazonPage, and then fills our string and submits     *
     * @param product
     * @throws Throwable
     */
    @Then("^I search the product \"([^\"]*)\"$")
    public void searchProduct(String product) throws Throwable{
        if(pom instanceof AmazonPage == false){
            throw new Exception("You need first to navigate to Amazon");
        }

        AmazonPage amazonPage = (AmazonPage) pom;
        amazonPage.searchProduct(product);
        amazonPage.submit();
    }

    /**
     * For Step: Count the Total List of Found Products
     *
     * Method that will count the quantity of products that are shown at Amazon's search result page
     * If the quantity of products is zero, an exception is raised. Otherwise, the number of results is printed
     * @throws Throwable
     */
    @Then("^I count the quantity of products found$")
    public void countProducts() throws Throwable{
        if(pom instanceof AmazonPage == false){
            throw new Exception("You need first to navigate to Amazon");
        }

        AmazonPage amazonPage = (AmazonPage) pom;
        List<WebElement> allResults = amazonPage.getAllProductsFound();

        if(allResults.size() == 0){
            throw new Exception("The current page is not that one that should be selected");
        } else {
            String quantityOfResults = String.valueOf(allResults.size());
            System.out.println("We were able to find " + quantityOfResults + " Results");
        }
    }

    /**
     * For Step: Count the Total of "Iphone" Items Found
     *
     * This method counts how many of the results names have "Iphone" on its name
     * It accepts other inputs, in case we want to search for other things, and not exactly for Iphone
     * Notice that it takes into account any product with "Iphone" on its name, even if it's a case and not
     * exacly and Iphone
     * @param phrase
     * @throws Throwable
     */
    @Then("^I count the quantity of \"([^\"]*)\" found$")
    public void countTerms(String phrase) throws Throwable{
        if(pom instanceof AmazonPage == false){
            throw new Exception("You need first to navigate to Amazon");
        }

        AmazonPage amazonPage = (AmazonPage) pom;
        List<String> allResults = amazonPage.getAllProductNames();

        int counter = 0;
        for(String productName : allResults){
            if(productName.toLowerCase().contains(phrase.toLowerCase())){
                counter++;
            }
        }

        if(counter == 0){
            throw new Exception("No products found match your search");
        } else {
            String quantityOfResults = String.valueOf(counter);
            System.out.println("We were able to find " + counter + " matching phrases for " + phrase);
        }
    }

    /**
     * For Step: Make sure all found products are cheaper than the cheapest "Iphone"
     *
     * This method only deals with real Iphone results, with the criteria explained in steps above with GB and 4G
     * The percentage of results is configurable at the Feature. The original 80% always fails, because the
     * actual number of phones is always lower than that
     * @param phrase
     * @throws Throwable
     */
    @Then("^I check if \"([^\"]*)\"% or more than the result are actual \"([^\"]*)\"$")
    public void countActualIphones(int percentage, String phrase) throws Throwable{
        if(pom instanceof AmazonPage == false){
            throw new Exception("You need first to navigate to Amazon");
        }

        AmazonPage amazonPage = (AmazonPage) pom;
        List<String> allResults = amazonPage.getAllProductNames();

        int counter = 0;
        for(String productName : allResults){
            if(productName.toLowerCase().contains(phrase.toLowerCase())
                && (productName.toLowerCase().contains("gb") ||
                    productName.toLowerCase().contains("4g"))){
                counter++;
            }
        }

        int searchedPercentage = (allResults.size() * percentage)/100;
        System.out.println(percentage + "% quantity is: " + searchedPercentage);
        System.out.println("There were found: " + counter);
        Boolean isValid = false;
        if(counter >= searchedPercentage){
            isValid = true;
        }

        //amazonPage.closeBrowser();
        assertTrue(isValid);
    }


    @Then("^Check if the Cheapest \"([^\"]*)\" is more expensive than the other products$")
    public void findProductsCheaperThanIphone(String phrase) throws Throwable{
        if(pom instanceof AmazonPage == false){
            throw new Exception("You need first to navigate to Amazon");
        }

        AmazonPage amazonPage = (AmazonPage) pom;
        List<WebElement> allResults = amazonPage.getAllProductsFound();
        List<String> allProductsName = amazonPage.getAllProductNames();

        double cheapestIphonePrice = 50000000000000.0;
        List<WebElement> nonIphoneElements = new ArrayList<>();

        for(int i = 0; i < allResults.size(); i++){
            String productName = allProductsName.get(i);
            WebElement product = allResults.get(i);

            if(productName.toLowerCase().contains(phrase.toLowerCase())
                    && (productName.toLowerCase().contains("gb") ||
                    productName.toLowerCase().contains("4g"))){

                try {
                    Double price = amazonPage.getPrice(product);

                    if (price < cheapestIphonePrice) {
                        cheapestIphonePrice = price;
                        System.out.println("Cheapest Iphone is now: " + String.valueOf(price));
                        System.out.println("Cheapest Iphone is now: " + productName);
                        System.out.println("-------------------------");
                    }

                } catch (org.openqa.selenium.NoSuchElementException exception) {
                    System.out.println("Got one element without price");
                }

            } else {
                nonIphoneElements.add(product);
            }
        }

        int totalProductsCheaper = 0;
        for(WebElement notIphoneProduct : nonIphoneElements){

            try {
                Double productPrice = amazonPage.getPrice(notIphoneProduct);
                if(productPrice > cheapestIphonePrice){
                    throw new Exception("Found one product more than expensive then the cheapest " + phrase + " at R$" + productPrice);
                } else {
                    totalProductsCheaper++;
                }
            }
            catch (org.openqa.selenium.NoSuchElementException exception) {
                System.out.println("Got one element without price");
                totalProductsCheaper++;
            }
        }

        System.out.println("Price of the cheapest iphone: " + cheapestIphonePrice);
        System.out.println("Number of NonIphone elements: " + String.valueOf(nonIphoneElements.size()));
        System.out.println("Number of Elements cheaper then the cheapest iphone or without price: " + totalProductsCheaper);

        Boolean isValid = false;
        if(nonIphoneElements.size() == totalProductsCheaper){
            isValid = true;
        }

        //amazonPage.closeBrowser();
        assertTrue(isValid);
    }

    /**
     * For Steps: Find the most expensive Iphone, Convert its value to USD and
     * make sure the converted value is not greater than 2000 USD
     *
     * This method only deals with real Iphone results, with the criteria explained in steps above with GB and 4G
     * The most expensive price is compared only with those results, disregarding results that show no price
     * The API is later called for converting the value to USD. Its result is compared
     * @param phrase
     * @throws Throwable
     */
    @Then("^Check if the most expensive \"([^\"]*)\" costs less than USD\"([^\"]*)\"$")
    public void checkMostExpensiveIPhoneOverDollar(String phrase, Double comparingPrice) throws Throwable {
        if(pom instanceof AmazonPage == false){
            throw new Exception("You need first to navigate to Amazon");
        }

        AmazonPage amazonPage = (AmazonPage) pom;
        List<WebElement> allResults = amazonPage.getAllProductsFound();
        List<String> allProductsName = amazonPage.getAllProductNames();

        double mostExpensivePrice = 0.0;

        for(int i = 0; i < allResults.size(); i++){
            String productName = allProductsName.get(i);
            WebElement product = allResults.get(i);

            if(productName.toLowerCase().contains(phrase.toLowerCase())
                    && (productName.toLowerCase().contains("gb") ||
                    productName.toLowerCase().contains("4g"))){

                try {
                    Double price = amazonPage.getPrice(product);

                    if (price > mostExpensivePrice) {
                        mostExpensivePrice = price;
                        System.out.println("Most Expensive Iphone is now: " + String.valueOf(price));
                        System.out.println("Most Expensive Iphone is now: " + productName);
                        System.out.println("-------------------------");
                    }

                } catch (org.openqa.selenium.NoSuchElementException exception) {
                    System.out.println("Got one element without price");
                }

            }
        }

        RestRequests rest = new RestRequests();
        Double convertedValue = rest.convertPriceToDollar(mostExpensivePrice);
        System.out.println("O valor mais caro em real é: " + mostExpensivePrice);
        System.out.println("O valor convertido pra Dolar é: " + convertedValue);
        System.out.println("O valor comparado foi " + comparingPrice);

        Boolean isValid = false;
        if(convertedValue < comparingPrice){
            isValid = true;
        }

        //amazonPage.closeBrowser();
        assertTrue(isValid);
    }

}
