package ch.galinet.xml.xsdhierarchy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by aellenn on 28.04.2017.
 */
class XsdParserTest {

	private static XsdParser parser;
	private final String namespace = "http://www.aellen.org/xsd";

	@BeforeAll
	static void setUp() throws Exception {
		InputStream is = XsdParserTest.class.getClassLoader().getResourceAsStream("test.xsd");
		parser = new XsdParser(is);
	}

	@Test
	void getRoot() throws Exception {
		XsdElement root = parser.getRoot();

		assertNotNull(root);
		assertEquals("root", root.getName());
		assertEquals(namespace, root.getNamespace());
	}

	@Test
	void getChildren() throws Exception {

		MapNamespaceContext ctx = new MapNamespaceContext();
		ctx.register("", namespace);
		List<XsdElement> children = parser.getChildren("/root", ctx);

		assertNotNull(children);
		assertEquals(2, children.size());
		assertEquals("child1", children.get(0).getName());
		assertEquals(namespace, children.get(0).getNamespace());
		assertEquals("child2", children.get(1).getName());
		assertEquals(namespace, children.get(1).getNamespace());

		children = parser.getChildren("/root//child1", ctx);
		assertNotNull(children);
		assertEquals(3, children.size());
		assertEquals("element1", children.get(0).getName());
		assertEquals(namespace, children.get(0).getNamespace());
		assertEquals("element2", children.get(1).getName());
		assertEquals(namespace, children.get(1).getNamespace());
		assertEquals("element3", children.get(2).getName());
		assertEquals(namespace, children.get(2).getNamespace());

		children = parser.getChildren("/root//child2", ctx);
		assertNotNull(children);
		assertEquals(4, children.size());
		assertEquals("element1", children.get(0).getName());
		assertEquals(namespace, children.get(0).getNamespace());
		assertEquals("element2", children.get(1).getName());
		assertEquals(namespace, children.get(1).getNamespace());
		assertEquals("element3", children.get(2).getName());
		assertEquals(namespace, children.get(2).getNamespace());
		assertEquals("element4", children.get(3).getName());
		assertEquals(namespace, children.get(3).getNamespace());
	}

	@Test
	void getChildrenWithPrefix() throws Exception {

		MapNamespaceContext ctx = new MapNamespaceContext();
		ctx.register("prefix", namespace);

		List<XsdElement> children = parser.getChildren("/prefix:root//prefix:child1", ctx);

		assertEquals(3, children.size());
		assertEquals("element1", children.get(0).getName());
		assertEquals(namespace, children.get(0).getNamespace());
		assertEquals("element2", children.get(1).getName());
		assertEquals(namespace, children.get(1).getNamespace());
		assertEquals("element3", children.get(2).getName());
		assertEquals(namespace, children.get(2).getNamespace());
	}

	@Test
	void testUnknownSCD() throws Exception {
		MapNamespaceContext ctx = new MapNamespaceContext();
		ctx.register("", namespace);

		List<XsdElement> children = parser.getChildren("/somethingElse//other", ctx);

		assertNull(children);
	}
}