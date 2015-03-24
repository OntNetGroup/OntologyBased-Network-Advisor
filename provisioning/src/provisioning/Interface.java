package provisioning;

public class Interface {
	private String interfaceURI;
	private String equipmentURI;
	
	public Interface(String interfaceURI, String equipmentURI) {
		this.interfaceURI = interfaceURI;
		this.equipmentURI = equipmentURI;
	}

	public String getInterfaceURI() {
		return interfaceURI;
	}

	public String getEquipmentURI() {
		return equipmentURI;
	}
	
	@Override
	public String toString() {
		String interfaceURI;
		String[] interfaceURISplit = this.interfaceURI.split("#");
		if(interfaceURISplit.length > 1){
			interfaceURI = interfaceURISplit[1];
		}else{
			interfaceURI = this.interfaceURI;
		}
		
		String equipmentURI;
		String[] equipmentURISplit = this.equipmentURI.split("#");
		if(equipmentURISplit.length > 1){
			equipmentURI = equipmentURISplit[1];
		}else{
			equipmentURI = this.equipmentURI;
		}
		
		String ret = interfaceURI + " [from: " + equipmentURI + "]";
		return ret;
	}
}
