package ch.galinet.xml.xsdhierarchy;

import org.apache.commons.lang3.tuple.Pair;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aellenn on 04.05.2017.
 */
public class XsdHelper {

    public static Pair<String, MapNamespaceContext> generateScdPath(Element element) {
        List<String> path = new ArrayList<>();
        final MapNamespaceContext ctx = new MapNamespaceContext();

        while (element.getParent() instanceof Element) {
            path.add(0, !element.getNamespacePrefix().equals("") ? element.getNamespacePrefix() + ":" : "" + element.getName());
            ctx.register(element.getNamespacePrefix(), element.getNamespace().getURI());
            element = element.getParentElement();
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i == 0) sb.append("/");
            sb.append(path.get(i));
            if (i < path.size() - 1) sb.append("//");
        }
        return Pair.of(sb.toString(), ctx);
    }
}
