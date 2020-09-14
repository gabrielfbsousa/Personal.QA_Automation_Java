package Support;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/features/",glue={"src/java/StepDefinitions"}, strict = true)
public class Runner {
}
