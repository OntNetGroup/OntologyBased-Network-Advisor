package provisioner.domain;

public class IntBinds {
	Interface interfaceFrom, interfaceTo;
	
	public IntBinds(Interface interfaceFrom, Interface interfaceTo) {
		this.interfaceFrom = interfaceFrom;
		this.interfaceTo = interfaceTo;
	}
	
	public Interface getInterfaceFrom() {
		return interfaceFrom;
	}
	
	public Interface getInterfaceTo() {
		return interfaceTo;
	}
}
