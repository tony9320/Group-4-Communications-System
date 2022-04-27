

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({
	ServerTester.class,
	SupervisorTester.class,
	UserTester.class,
	ChatroomTester.class
})
public class AllTests {

}
