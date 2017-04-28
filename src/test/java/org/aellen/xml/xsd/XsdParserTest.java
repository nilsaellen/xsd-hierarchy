package org.aellen.xml.xsd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by aellenn on 28.04.2017.
 */
public class XsdParserTest {

    XsdParser parser;
    private final String namespace = "http://www.aellen.org/xsd";

    @Before
    public void setUp() throws Exception {
        InputStream is = XsdParserTest.class.getClassLoader().getResourceAsStream("test.xsd");
        parser = new XsdParser(is);
    }

    @Test
    public void getRoot() throws Exception {
        XsdElement root = parser.getRoot();

        Assert.assertNotNull(root);
        Assert.assertEquals(root.getName(), "root");
        Assert.assertEquals(root.getNamespace(), namespace);
    }

    @Test
    public void getChildren() throws Exception {

        MapNamespaceContext ctx = new MapNamespaceContext();
        ctx.register("", namespace);
        List<XsdElement> children = parser.getChildren("/root", ctx);

        Assert.assertNotNull(children);
        Assert.assertEquals(2, children.size());
        Assert.assertEquals("child1", children.get(0).getName());
        Assert.assertEquals(namespace, children.get(0).getNamespace());
        Assert.assertEquals("child2", children.get(1).getName());
        Assert.assertEquals(namespace, children.get(1).getNamespace());

        children = parser.getChildren("/root//child1", ctx);
        Assert.assertNotNull(children);
        Assert.assertEquals(3, children.size());
        Assert.assertEquals("element1", children.get(0).getName());
        Assert.assertEquals(namespace, children.get(0).getNamespace());
        Assert.assertEquals("element2", children.get(1).getName());
        Assert.assertEquals(namespace, children.get(1).getNamespace());
        Assert.assertEquals("element3", children.get(2).getName());
        Assert.assertEquals(namespace, children.get(2).getNamespace());

        children = parser.getChildren("/root//child2", ctx);
        Assert.assertNotNull(children);
        Assert.assertEquals(4, children.size());
        Assert.assertEquals("element1", children.get(0).getName());
        Assert.assertEquals(namespace, children.get(0).getNamespace());
        Assert.assertEquals("element2", children.get(1).getName());
        Assert.assertEquals(namespace, children.get(1).getNamespace());
        Assert.assertEquals("element3", children.get(2).getName());
        Assert.assertEquals(namespace, children.get(2).getNamespace());
        Assert.assertEquals("element4", children.get(3).getName());
        Assert.assertEquals(namespace, children.get(3).getNamespace());
    }

    @Test
    public void getChildrenWithPrefix() throws Exception {

        MapNamespaceContext ctx = new MapNamespaceContext();
        ctx.register("prefix", namespace);

        List<XsdElement> children = parser.getChildren("/prefix:root//prefix:child1", ctx);

        Assert.assertEquals(3, children.size());
        Assert.assertEquals("element1", children.get(0).getName());
        Assert.assertEquals(namespace, children.get(0).getNamespace());
        Assert.assertEquals("element2", children.get(1).getName());
        Assert.assertEquals(namespace, children.get(1).getNamespace());
        Assert.assertEquals("element3", children.get(2).getName());
        Assert.assertEquals(namespace, children.get(2).getNamespace());
    }
}