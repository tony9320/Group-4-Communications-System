import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

public class ServerTest {
	@Test
	public void testUIConstructor() {
		 Server server = new Server();
		 assertNotNull(server);
	}

}
