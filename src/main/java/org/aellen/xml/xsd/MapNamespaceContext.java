package org.aellen.xml.xsd;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by aellenn on 28.04.2017.
 */
public class MapNamespaceContext implements NamespaceContext {
    private final HashMap<String, String> map = new HashMap();

    public void register(String prefix, String namespace) {
        map.put(prefix, namespace);
    }

    @Override
    public String getNamespaceURI(String prefix) {
        return map.get(prefix);
    }

    @Override
    public String getPrefix(String namespaceURI) {
        Optional<Map.Entry<String, String>> optElement = map.entrySet().stream().filter(e -> e.getValue().equals(namespaceURI)).findFirst();
        if (optElement.isPresent()) {
            return optElement.get().getKey();
        } else {
            return null;
        }
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        return map.keySet().iterator();
    }
}
