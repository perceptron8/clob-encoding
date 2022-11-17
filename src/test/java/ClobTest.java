import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ClobTest {
	@Test
	public void executeUpdate() throws Exception {
		try (Connection connection = DriverManager.getConnection("jdbc:tc:mysql:8.0.31:///test", "test", "test")) {
			try (PreparedStatement create = connection.prepareStatement("CREATE TABLE test (test LONGTEXT)")) {
				create.execute();
			}
			try (PreparedStatement insert = connection.prepareStatement("INSERT INTO test(test) VALUES (?)")) {
				insert.setClob(1, new StringReader("Hello world, Καλημέρα κόσμε, コンニチハ"));
				insert.executeUpdate();
			}
			try (
				PreparedStatement select = connection.prepareStatement("SELECT test FROM test");
				ResultSet result = select.executeQuery();
			) {
				assertTrue(result.next());
				assertEquals("Hello world, Καλημέρα κόσμε, コンニチハ", result.getString("test"));
				assertFalse(result.next());
			}
		}
	}
}
