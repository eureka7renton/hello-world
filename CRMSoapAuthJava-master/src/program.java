import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class program {

	public static void main(String[] args) throws Exception {

		CrmAuth auth = new CrmAuth();

		// CRM Online
		String url = "https://meitec-dev.crm7.dynamics.com/";
		String username ="dynamics-admin@meitecgrp.onmicrosoft.com";
		String password = "dyna-Adm@";
		CrmAuthenticationHeader authHeader = auth.GetHeaderOnline(username,
		password, url);
		// End CRM Online

		String id = CrmWhoAmI(authHeader, url);
		if (id == null)
			return;

		String name = CrmGetUserName(authHeader, id, url);
		System.out.println(name);

		// String testName = "Java";
		// CrmCreateTest(authHeader,testName,url);

		NewTestParent testObj = new NewTestParent();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		testObj.new_emp_name = "Java";
		testObj.new_sex = false;
		testObj.new_contact_start_date = sdf.parse("19871215");
		testObj.new_select_cutomized = new CrmOptionSet("100000001");
		testObj.new_emp_age = 29;
		testObj.new_float = 2.71;
		testObj.new_multi = "あいうえお\r\nかきくけこ";
		testObj.new_money = new CrmMoney(5000);
		testObj.new_lookup = new CrmReference("new_mago","a1","a2241c5e-29bb-e511-80f7-fc15b428ddc0");
		CrmCreateTest2(authHeader,testObj,url);

	}

	public static void CrmCreateTest2(CrmAuthenticationHeader authHeader,NewTestParent ent,String url) throws IOException,SAXException,ParserConfigurationException{

		StringBuilder xml = CreateXml(ent);

		Document xDoc = CrmExecuteSoap.ExecuteSoapRequest(authHeader,xml.toString(),url);
		if (xDoc == null) {
			return;
		}
	}

	public static StringBuilder CreateXml(Object ent){

		StringBuilder xml = new StringBuilder();
		xml.append(CrmXmlConst.creatHead);
		Field[] field = ent.getClass().getDeclaredFields();
		for (int i = 0;i<field.length ;i++ ) {
			xml.append(fieldXml(ent,field[i]));
		}
		xml.append(CrmXmlConst.creatTail);

		return xml;
	}

	public static StringBuilder fieldXml(Object entObj,Field entField){
		try {
			StringBuilder xml = new StringBuilder();
			String fieldType = entField.getGenericType().toString();
			String fieldTypeName = fieldType.lastIndexOf(".") == -1 ? fieldType.substring(fieldType.lastIndexOf(" ")+1): fieldType.substring(fieldType.lastIndexOf(".")+1);
			String fieldName = entField.getName();

			switch(fieldTypeName){
				case "String":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>" + fieldName + "</b:key>");
					xml.append("<b:value i:type=\"c:string\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ entField.get(entObj) +"</b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				case "Boolean":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>" + fieldName + "</b:key>");
					xml.append("<b:value i:type=\"c:boolean\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ entField.get(entObj) +"</b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				case "Date":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>" + fieldName + "</b:key>");
					xml.append("<b:value i:type=\"c:dateTime\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ new SimpleDateFormat("yyyy-MM-dd").format(entField.get(entObj)) +"</b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				case "int":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>" + fieldName + "</b:key>");
					xml.append("<b:value i:type=\"c:int\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ entField.get(entObj) +"</b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				case "double":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>" + fieldName + "</b:key>");
					xml.append("<b:value i:type=\"c:double\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ entField.get(entObj) +"</b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				case "CrmOptionSet":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>" + fieldName + "</b:key>");
					xml.append("<b:value i:type=\"a:OptionSetValue\" ><a:Value>"+ ((CrmOptionSet)entField.get(entObj)).getValue() + "</a:Value></b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				case "CrmMoney":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>"+fieldName+"</b:key>");
					xml.append("<b:value i:type=\"a:Money\" ><a:Value>"+ ((CrmMoney)entField.get(entObj)).getValue() +"</a:Value></b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				case "CrmReference":
					xml.append("<a:KeyValuePairOfstringanyType>");
					xml.append("<b:key>"+fieldName+"</b:key>");
					xml.append("<b:value i:type=\"a:EntityReference\">");
			        xml.append("<a:Id>"+((CrmReference)entField.get(entObj)).getGuid()+"</a:Id>");
			        xml.append("<a:KeyAttributes xmlns:c=\"http://schemas.microsoft.com/xrm/7.1/Contracts\" />");
			        xml.append("<a:LogicalName>"+((CrmReference)entField.get(entObj)).getLogicalName()+"</a:LogicalName>");
			        xml.append("<a:Name i:nil=\"true\" />");
			        xml.append("<a:RowVersion i:nil=\"true\" />");
			        xml.append("</b:value>");
					xml.append("</a:KeyValuePairOfstringanyType>");
					break;
				default:
					break;
			}

			return xml;
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	public static String CrmWhoAmI(CrmAuthenticationHeader authHeader,String url) throws IOException, SAXException,ParserConfigurationException {
		StringBuilder xml = new StringBuilder();
		xml.append("<s:Body>");
		xml.append("<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\">");
		xml.append("<request i:type=\"c:WhoAmIRequest\" xmlns:b=\"http://schemas.microsoft.com/xrm/2011/Contracts\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:c=\"http://schemas.microsoft.com/crm/2011/Contracts\">");
		xml.append("<b:Parameters xmlns:d=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"/>");
		xml.append("<b:RequestId i:nil=\"true\"/>");
		xml.append("<b:RequestName>WhoAmI</b:RequestName>");
		xml.append("</request>");
		xml.append("</Execute>");
		xml.append("</s:Body>");

		Document xDoc = CrmExecuteSoap.ExecuteSoapRequest(authHeader,
				xml.toString(), url);
		if (xDoc == null)
			return null;

		NodeList nodes = xDoc
				.getElementsByTagName("b:KeyValuePairOfstringanyType");
		for (int i = 0; i < nodes.getLength(); i++) {
			if ((nodes.item(i).getFirstChild().getTextContent())
					.equals("UserId")) {
				return nodes.item(i).getLastChild().getTextContent();
			}
		}

		return null;
	}

	public static String CrmGetUserName(CrmAuthenticationHeader authHeader,String id, String url) throws IOException, SAXException,ParserConfigurationException {
		StringBuilder xml = new StringBuilder();
		xml.append("<s:Body>");
		xml.append("<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">");
		xml.append("<request i:type=\"a:RetrieveRequest\" xmlns:a=\"http://schemas.microsoft.com/xrm/2011/Contracts\">");
		xml.append("<a:Parameters xmlns:b=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\">");
		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>Target</b:key>");
		xml.append("<b:value i:type=\"a:EntityReference\">");
		xml.append("<a:Id>" + id + "</a:Id>");
		xml.append("<a:LogicalName>systemuser</a:LogicalName>");
		xml.append("<a:Name i:nil=\"true\" />");
		xml.append("</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");
		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>ColumnSet</b:key>");
		xml.append("<b:value i:type=\"a:ColumnSet\">");
		xml.append("<a:AllColumns>false</a:AllColumns>");
		xml.append("<a:Columns xmlns:c=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">");
		xml.append("<c:string>firstname</c:string>");
		xml.append("<c:string>lastname</c:string>");
		xml.append("</a:Columns>");
		xml.append("</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");
		xml.append("</a:Parameters>");
		xml.append("<a:RequestId i:nil=\"true\" />");
		xml.append("<a:RequestName>Retrieve</a:RequestName>");
		xml.append("</request>");
		xml.append("</Execute>");
		xml.append("</s:Body>");

		Document xDoc = CrmExecuteSoap.ExecuteSoapRequest(authHeader,
				xml.toString(), url);
		if (xDoc == null)
			return null;

		String firstname = "";
		String lastname = "";

		NodeList nodes = xDoc
				.getElementsByTagName("b:KeyValuePairOfstringanyType");
		for (int i = 0; i < nodes.getLength(); i++) {
			if ((nodes.item(i).getFirstChild().getTextContent())
					.equals("firstname")) {
				firstname = nodes.item(i).getLastChild().getTextContent();
			}
			if ((nodes.item(i).getFirstChild().getTextContent())
					.equals("lastname")) {
				lastname = nodes.item(i).getLastChild().getTextContent();
			}
		}

		return firstname + " " + lastname;
	}

	public static void CrmCreateTest(CrmAuthenticationHeader authHeader,String fieldValue,String url) throws IOException,SAXException,ParserConfigurationException{
		StringBuilder xml = new StringBuilder();
		// xml.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope\">");
		xml.append("<s:Body>");
		xml.append("<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">");
		xml.append("<request i:type=\"a:CreateRequest\" xmlns:a=\"http://schemas.microsoft.com/xrm/2011/Contracts\">");
		xml.append("<a:Parameters xmlns:b=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\">");
		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>Target</b:key>");
		xml.append("<b:value i:type=\"a:Entity\">");
		xml.append("<a:Attributes>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_emp_name</b:key>");
		xml.append("<b:value i:type=\"c:string\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ fieldValue +"</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_emp_age</b:key>");
		xml.append("<b:value i:type=\"c:int\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ 30 +"</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_sex</b:key>");
		xml.append("<b:value i:type=\"c:boolean\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ true +"</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_contact_start_date</b:key>");
		xml.append("<b:value i:type=\"c:dateTime\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ "1987-12-12T00:00:00" +"</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_select_cutomized</b:key>");
		xml.append("<b:value i:type=\"a:OptionSetValue\" ><a:Value>"+ "100000001" +"</a:Value></b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_float</b:key>");
		xml.append("<b:value i:type=\"c:double\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ "3.14" +"</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_money</b:key>");
		xml.append("<b:value i:type=\"a:Money\" ><a:Value>"+ "7000" +"</a:Value></b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_lookup</b:key>");
		xml.append("<b:value i:type=\"a:EntityReference\">");
        xml.append("<a:Id>a2241c5e-29bb-e511-80f7-fc15b428ddc0</a:Id>");
        xml.append("<a:KeyAttributes xmlns:c=\"http://schemas.microsoft.com/xrm/7.1/Contracts\" />");
        xml.append("<a:LogicalName>new_mago</a:LogicalName>");
        xml.append("<a:Name i:nil=\"true\" />");
        xml.append("<a:RowVersion i:nil=\"true\" />");
        xml.append("</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("<a:KeyValuePairOfstringanyType>");
		xml.append("<b:key>new_multi</b:key>");
		xml.append("<b:value i:type=\"c:string\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">"+ "Wang\r\nLi\r\nMing" +"</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");

		xml.append("</a:Attributes>");
		xml.append("<a:EntityState i:nil=\"true\" />");
		xml.append("<a:FormattedValues />");
		xml.append("<a:Id>00000000-0000-0000-0000-000000000000</a:Id>");
		xml.append("<a:KeyAttributes xmlns:c=\"http://schemas.microsoft.com/xrm/7.1/Contracts\" />");
		xml.append("<a:LogicalName>new_test_parent</a:LogicalName>");
		xml.append("<a:RelatedEntities />");
		xml.append("<a:RowVersion i:nil=\"true\" />");
		xml.append("</b:value>");
		xml.append("</a:KeyValuePairOfstringanyType>");
		xml.append("</a:Parameters>");
		xml.append("<a:RequestId i:nil=\"true\" />");
		xml.append("<a:RequestName>Create</a:RequestName>");
		xml.append("</request>");
		xml.append("</Execute>");
		xml.append("</s:Body>");
		// xml.append("</s:Envelope>");

		Document xDoc = CrmExecuteSoap.ExecuteSoapRequest(authHeader,xml.toString(),url);
		if (xDoc == null) {
			return;
		}
	}
}
