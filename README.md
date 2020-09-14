![College](https://img.shields.io/badge/Objective-Personal-yellow)
![Language](https://img.shields.io/badge/Language-Java_and_Selenium_and_Cucumber-orange)
![IDE](https://img.shields.io/badge/IDE-IntelliJ-darkblue)


# Personal Project :: QA Automation Java
Projects developed using Java, Cucumber and finally uploaded to Git for versioning

#### Project Information
- Project: ``QA Coding``
&nbsp;

- Objective: ``Personal``
&nbsp;

- Status: ``Completed``
&nbsp;

- Technology: ``Java``
&nbsp;

- Date: ``Sep 2020``
&nbsp;

## Project Detailing
### Details
Four scenarios are automated in this code, running through Java and Selenium. Cucumber is the forefront layer, where we can configure the parameters for their execution. Those scenarios will be presented further below.

### Setup and Execution
Since this project is about automating web interfaces, using Selenium, it requires a Driver. Instead of importing a jar to the project, a dependency was set at Maven for Boni Garcia's WebDriverManager ``(https://github.com/bonigarcia/webdrivermanager)`` , that is responsible for doing this management for us in Runtime. The configuration in-code is set for ChromeDriver, in a way that it will match the Chrome version you are using with the correct Driver.

Cucumber and JUnit dependencies were also set into the pom.xml file as dependencies, meaning you need to run some maven commands for it, at least ``mvn clean test`` and ``mvn install`` were ran locally.

This project was entirely built using IntelliJ, with ItelliJ's Cucumber For Java plugin ``(https://plugins.jetbrains.com/plugin/7212-cucumber-for-java)``. Because of that, the way that it was executed is through the IDE, right-clicking the .feature file and select Run. Simple as that. Going inside the feature, it's also possible to run each scenario at the time. At the lines where Scenario starts, the plugin marks right beside the line number a Run button. This happens at the lines 4, 10, 18 and 24 of the Feature

Results, possible failures and prints of what was found at each scenario are all shown into the Terminal. This is important to validate that everything is indeed working. Failed scenarios, of course, will be shown as failed at the end.

--- 
#### Scenario @ID-0001 - Test Setup
In this scenario, we open the browser and navigate to Google's search page, initially empty. It's required that we fill it with a searching phrase, in this case, "Amazon Brasil". By submitting, a list of results should appear, and the one with "www.amazon.com.br" should be clicked.

At the code, the list of results is analyzed, and the one that has a link matching our parameter from the feature is the one that is selected. A validation is made checking if the URL the browser currently is indeed corresponds to the one we asked to click.

--- 
#### Scenario @ID-0002 - 80% Of Shown Products Should Be Exclusively The Searched Product
For this scenario, we already browse to Amazon's webpage, in order to make the execution faster, instead of going all the way through Google. There, we locate the Amazon's search bar and type something we want to search, in this case, "iPhone", and submit. A page with the found results should be shown, and the code waits until it's loaded in order to move on. Specifically, it waits for the lateral bar, with its filters, to show up, indicating everything is good to go.

After that, we count how many products were returned by Amazon's search. It's only a counting from elements, not doing any filtering

Then, we count how many "iPhone" were found. In order to better filter this, we not only searched for the name "iPhone" at the results, but also for things such as "GB" and "4G". This is due to the fact that some cases and accessories were showing up at the results, and they are not the iPhones we want to search (not the phone itself). Using this criteria, we can trim further down, make the counting and print at the terminal.

Finally, we check if at least more than 80% of the results are indeed the iPhone. Again, we use the same criteria above, count the quantity of results, and compare it over the total to see if it's more or less than 80%. Please notice that the results often shown are much below 80%, and thus this scenario fails every time (it's a correct behaviour. It really should fail, since we are really finding less results than 80%). However, if changing the parameter at the feature to some levels below (like say, 50%), it passes, since we really have at least this value.

--- 
#### Scenario @ID-0003 - The Higher Price In The First Page Can't Be Greater Than U$2000
In order to create this scenario, we again navigate to Amazon and search for "Iphone". This time, using the same criteria as before to filter what is really an Iphone from the results, we make comparisons, checking one by one, to see if each iPhone is more expensive than the current most expensive one. 

After finding the most expensive from all of the results, we then call an external API from Exchange Rate ``(https://exchangeratesapi.io/)`` and get the rate for BRL-USD. With this ratio found, we pass this value in Real as a parameter and receive the equivalent value in Dollar.

Finally, we check if this value in dollar is lesser than 2000 USD.

--- 
#### Scenario @ID-0004 - Products Different Than The Searched Product Should Be Cheaper Than The Searched Product
Finishing our scenarios, we go again for Amazon and search for Iphone. For each results, we are again checking if it is an Iphone or not. However, now:

- 1. If it is an Iphone, we check if its price is lesser than the current least expensive price
- 2. If it's not an Iphone, we add this product to a List

After all results are analyzed, then we check if every product from the list has its price lesser than the least expensive Iphone. All of them must be cheaper than the cheapest Iphone for this scenario to pass.

--- 
Made by Gabriel Ferreira :computer: [Find me at Linkedin!](https://www.linkedin.com/in/gabriel-f-sousa/)
