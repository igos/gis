package pl.edu.pw.gis.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import pl.edu.pw.gis.Cli;
import pl.edu.pw.gis.Settings;

public class CliTest {

	@Test
	public void testCmdParams() {
		Settings s = null;
		String[] cmd = new String[0];
		s = Cli.parseCliArgs(cmd);
		assertNull("should not be parsed: " + Arrays.toString(cmd), s);

		cmd = new String[] { "-v" };
		s = Cli.parseCliArgs(cmd);
		assertNull("should not be parsed: " + Arrays.toString(cmd), s);

		cmd = new String[] { "-r", "200" };
		s = Cli.parseCliArgs(cmd);
		assertNull("should not be parsed: " + Arrays.toString(cmd), s);

		cmd = new String[] { "-f", "blahblah" };
		s = Cli.parseCliArgs(cmd);
		assertNull("should not be parsed: " + Arrays.toString(cmd), s);

		cmd = new String[] { "-b", "-r", "200", "-f", "blahblah" };
		s = Cli.parseCliArgs(cmd);
		assertNull("should not be parsed: " + Arrays.toString(cmd), s);

		cmd = new String[] { "-r", "200", "-f", "blahblah" };
		s = Cli.parseCliArgs(cmd);
		assertNotNull("should be parsed: " + Arrays.toString(cmd), s);
		assertEquals(200, s.radius);
		assertEquals("blahblah", s.filePath);
		assertFalse(s.verbose);
		assertFalse(s.graphx);

		cmd = new String[] { "-r", "200", "-f", "blahblah", "-v", "-x" };
		s = Cli.parseCliArgs(cmd);
		assertNotNull("should be parsed: " + Arrays.toString(cmd), s);
		assertEquals(200, s.radius);
		assertEquals("blahblah", s.filePath);
		assertTrue(s.verbose);
		assertTrue(s.graphx);
		assertEquals(-1, s.limit);

		cmd = new String[] { "-r", "200", "-f", "-l" };
		s = Cli.parseCliArgs(cmd);
		assertNull("should not be parsed: " + Arrays.toString(cmd), s);

		cmd = new String[] { "-r", "www", "-f", "l" };
		s = Cli.parseCliArgs(cmd);
		assertNull("should not be parsed: " + Arrays.toString(cmd), s);

		cmd = new String[] { "-r", "200", "-f", "blahblah", "-l", "201" };
		s = Cli.parseCliArgs(cmd);
		assertNotNull("should be parsed: " + Arrays.toString(cmd), s);
		assertEquals(201, s.limit);

	}

}
