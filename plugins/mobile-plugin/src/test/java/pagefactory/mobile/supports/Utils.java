package pagefactory.mobile.supports;

import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class Utils {
    
    public static WebElement getRandomItem(String id) {
	Random random = new Random();
	List<WebElement> list = Environment.getDriverService().getDriver().findElements(By.id(id));
	int index = 1 + random.nextInt(list.size() - 1);
	
	return list.get(index);
    }
}
