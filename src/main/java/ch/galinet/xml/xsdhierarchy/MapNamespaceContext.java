package ch.galinet.xml.xsdhierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.xml.namespace.NamespaceContext;

public class MapNamespaceContext implements NamespaceContext {
	private final HashMap<String, String> map = new HashMap<>();

	public void register(final String prefix, final String namespace) {
		map.put(prefix, namespace);
	}

	@Override
	public String getNamespaceURI(final String prefix) {
		return map.get(prefix);
	}

	@Override
	public String getPrefix(final String namespaceURI) {
		Optional<Map.Entry<String, String>> optElement = map.entrySet().stream().filter(e -> e.getValue().equals(namespaceURI)).findFirst();
		return optElement.map(Map.Entry::getKey).orElse(null);
	}

	@Override
	public Iterator getPrefixes(final String namespaceURI) {
		return map.keySet().iterator();
	}
}