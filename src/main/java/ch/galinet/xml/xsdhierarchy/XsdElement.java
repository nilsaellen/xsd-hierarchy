package ch.galinet.xml.xsdhierarchy;

public class XsdElement {
	private String namespace;
	private String name;

	public XsdElement() {
	}

	public XsdElement(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("{%s}:%s", this.getNamespace(), this.getName());
	}
}
