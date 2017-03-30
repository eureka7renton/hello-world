
public class CrmReference {
	private String guid;
	private String logicalName;
	private String displayValue;

	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getLogicalName() {
		return logicalName;
	}
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	public CrmReference(String logicalName, String displayValue,String guid) {
		super();
		this.logicalName = logicalName;
		this.displayValue = displayValue;
		this.guid = guid;
	}


}
