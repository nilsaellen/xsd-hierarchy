package ch.galinet.xml.xsdhierarchy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.sun.xml.xsom.impl.ComplexTypeImpl;
import com.sun.xml.xsom.impl.ElementDecl;
import com.sun.xml.xsom.impl.ModelGroupImpl;
import com.sun.xml.xsom.impl.ParticleImpl;
import com.sun.xml.xsom.parser.XSOMParser;

public class XsdParser {

	private final XSOMParser parser;

	public XsdParser(final InputStream xsd) throws SAXException {
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

	public List<XsdElement> getChildren(final String scd, final NamespaceContext ctx) throws SAXException {
		ElementDecl c = (ElementDecl) parser.getResult().selectSingle(scd, ctx);

		if (c != null) {
			List<XsdElement> result = new ArrayList<>();

			ComplexTypeImpl type = (ComplexTypeImpl) c.getType();
			ParticleImpl ct = (ParticleImpl) type.getContentType();
			ModelGroupImpl term = (ModelGroupImpl) ct.getTerm();

			recurse(result, term);

			return result;
		} else {
			return null;
		}
	}

	private void recurse(final List<XsdElement> result, final ModelGroupImpl term) {
		for (ParticleImpl child : term.getChildren()) {
			if (child.getTerm() instanceof ModelGroupImpl modelGroupImpl) {
				recurse(result, modelGroupImpl);
			} else if (child.getTerm() instanceof ElementDecl elementDecl) {
				result.add(new XsdElement(elementDecl.getTargetNamespace(), elementDecl.getName()));
			} else {
				throw new IllegalArgumentException(String.format("Type %s not implemented!", child.getTerm().getClass()));
			}
		}
	}
}
