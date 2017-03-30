
public class CrmXmlConst {

	public static final String creatHead = "<s:Body><Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><request i:type=\"a:CreateRequest\" xmlns:a=\"http://schemas.microsoft.com/xrm/2011/Contracts\"><a:Parameters xmlns:b=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"><a:KeyValuePairOfstringanyType><b:key>Target</b:key><b:value i:type=\"a:Entity\"><a:Attributes>";

	public static final String creatTail = "</a:Attributes><a:EntityState i:nil=\"true\" /><a:FormattedValues /><a:Id>00000000-0000-0000-0000-000000000000</a:Id><a:KeyAttributes xmlns:c=\"http://schemas.microsoft.com/xrm/7.1/Contracts\" /><a:LogicalName>" + "new_test_parent" + "</a:LogicalName><a:RelatedEntities /><a:RowVersion i:nil=\"true\" /></b:value></a:KeyValuePairOfstringanyType></a:Parameters><a:RequestId i:nil=\"true\" /><a:RequestName>Create</a:RequestName></request></Execute></s:Body>";

	public static final String updateHead = "ToDo";
	public static final String updateTail = "ToDo";
}
