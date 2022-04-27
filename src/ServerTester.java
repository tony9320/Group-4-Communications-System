import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

public class ServerTester {
	@Test
	public void testUIConstructor() {
		 Server server = new Server();
		 assertNotNull(server);
	}

}
