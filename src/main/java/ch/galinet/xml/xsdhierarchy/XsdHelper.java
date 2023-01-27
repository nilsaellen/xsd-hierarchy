package ch.galinet.xml.xsdhierarchy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jdom2.Element;

public class XsdHelper {

	private XsdHelper() {
		//Intentionally left blank
	}

	public static Pair<String, MapNamespaceContext> generateScdPath(final Element element) {
		List<String> path = new ArrayList<>();
		final MapNamespaceContext ctx = new MapNamespaceContext();

		Element el = element;
		while (el.getParent() instanceof Element) {
			path.add(0, !el.getNamespacePrefix().equals("") ? el.getNamespacePrefix() + ":" : "" + el.getName());
			ctx.register(el.getNamespacePrefix(), el.getNamespace().getURI());
			el = el.getParentElement();
		}

		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < path.size(); i++) {
			if (i == 0) {
				sb.append("/");
			}
			sb.append(path.get(i));
			if (i < path.size() - 1) {
				sb.append("//");
			}
		}
		return Pair.of(sb.toString(), ctx);
	}
}
