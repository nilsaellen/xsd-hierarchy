package org.aellen.xml.xsd;

import com.sun.xml.xsom.impl.ComplexTypeImpl;
import com.sun.xml.xsom.impl.ElementDecl;
import com.sun.xml.xsom.impl.ModelGroupImpl;
import com.sun.xml.xsom.impl.ParticleImpl;
import com.sun.xml.xsom.parser.XSOMParser;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aellenn on 28.04.2017.
 */
public class XsdParser {

    private XSOMParser parser;

    public XsdParser(InputStream xsd) throws SAXException {
        parser = new XSOMParser(SAXParserFactory.newInstance());
        parser.parse(xsd);
    }

    public XsdElement getRoot() throws SAXException {
        ElementDecl c = (ElementDecl) parser.getResult().selectSingle("*", new MapNamespaceContext());

        XsdElement result = new XsdElement();

        result.setNamespace(c.getTargetNamespace());
        result.setName(c.getName());

        return result;
    }

    public List<XsdElement> getChildren(String scd, NamespaceContext ctx) throws SAXException {
        ElementDecl c = (ElementDecl) parser.getResult().selectSingle(scd, ctx);

        return getChildren(c);
    }

    private List<XsdElement> getChildren(ElementDecl ed) {
        List<XsdElement> result = new ArrayList<>();

        ComplexTypeImpl type = (ComplexTypeImpl) ed.getType();
        ParticleImpl ct = (ParticleImpl) type.getContentType();
        ModelGroupImpl term = (ModelGroupImpl) ct.getTerm();

        recurse(result, term);

        return result;
    }

    private void recurse(List<XsdElement> result, ModelGroupImpl term) {
        for (ParticleImpl child : term.getChildren()) {
            if (child.getTerm() instanceof ModelGroupImpl) {
                recurse(result, ((ModelGroupImpl) child.getTerm()));
            } else if (child.getTerm() instanceof ElementDecl) {
                ElementDecl chTerm = (ElementDecl) child.getTerm();
                result.add(new XsdElement(chTerm.getTargetNamespace(), chTerm.getName()));
            } else {
                throw new RuntimeException(String.format("Type %s not implemented!", child.getTerm().getClass()));
            }
        }
    }
}
