/**
 * @author Administrator
 * <p>Copyright: Copyright (c) 2015 Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>Developed By: Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>最新版本的SDK</p>
 */
package cn.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketImpl;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.service.bean.AssessInfoType;
import cn.service.bean.BannerInfoType;
import cn.service.bean.CarInfo;
import cn.service.bean.CityInfo;
import cn.service.bean.CouponInfo;
import cn.service.bean.DecoOrderInfo;
import cn.service.bean.OrderStateType;
import cn.service.bean.DecoOrderInfo.DecoOrderNumber;
import cn.service.bean.DecorationInfo;
import cn.service.bean.GoodInfo;
import cn.service.bean.MetaOrderInfo;
import cn.service.bean.MetaOrderInfo.MetaOrderImg;
import cn.service.bean.MetaOrderInfo.MetaOrderNumber;
import cn.service.bean.MetalplateInfo;
import cn.service.bean.OilInfo;
import cn.service.bean.OilOrderInfo;
import cn.service.bean.OilOrderInfo.OilOrderNumberInfo;
import cn.service.bean.PromotionInfoType;
import cn.service.bean.ShopInfo;
import cn.service.bean.UserInfo;



import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WSConnector {
	private static String wsUrl = "";
	private static String IP1 = "192.168.2.173";
	private static String portStr = "9000";
	private static final String REQUEST_HEAD = "http://";
	private static WSConnector instance = new WSConnector();
	private Map<String, String> userMap;
    private IWSErrorCodeListener listener;
	private WSConnector() {
		this.userMap = new LinkedHashMap<String, String>();
		wsUrl = REQUEST_HEAD + IP1 + ":" + portStr + "/car/services/carwsapi/";
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public String getIP1() {
		return IP1;
	}

	public static WSConnector getInstance() {
		return instance;
	}

	public static WSConnector getInstance(String ip, String port,
			boolean isHttps) {
		instance.IP1 = ip;
		instance.portStr = port;
		wsUrl = REQUEST_HEAD + IP1 + ":" + portStr +  "/car/services/carwsapi/";
		return instance;
	}

	private InputStream request(String service) throws WSException {
		String path = service;
		InputStream is = null;
		HttpURLConnection uc = null;
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("GET");
			// uc.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
			// uc.setRequestProperty("Accept-Language",
			// "en,zh-cn;q=0.8,zh;q=0.5,en-us;q=0.3");
			// uc.setRequestProperty("Cache-Control", "max-age=0");
			// uc.setRequestProperty("Connection", "keep-alive");
			// uc.setRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; rv:28.0) Gecko/20100101 Firefox/28.0");
		} catch (IOException e) {
			e.printStackTrace();
		}
		uc.setConnectTimeout(Util.REQ_TIME_OUT);
		uc.setReadTimeout(Util.READ_TIME_OUT);
		uc.setDoOutput(true);
		uc.setDoInput(true);
		try {
			uc.connect();
		} catch (Exception e1) {
			throw new WSException(ErrorCode.NET_WORK_TIME_OUT);
		}
		try {
			System.out.println("rspCode=========================:"
					+ uc.getResponseCode());
			is = uc.getInputStream();
		} catch (Exception e) {
			throw new WSException(ErrorCode.NET_WORK_TIME_OUT);
		}
		return is;
	}


	

	private Element parseInputStreamToDom(InputStream is) {
		Logger.getLogger(this.getClass()).info("inputStream :" + is);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			is = null;
			return null;
		}
		Logger.getLogger(this.getClass()).info("document :" + document);
		Element rootElement = document.getDocumentElement();
		Logger.getLogger(this.getClass()).info(rootElement.toString());
		try {
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootElement;
	}
	public void setWSErrorCodeListener(IWSErrorCodeListener listener){
		this.listener=listener;
	}

	private Element getXMLNode(String service) throws WSException {
		Logger.getLogger(this.getClass()).info(service);

		InputStream is = request(service);

		Logger.getLogger(this.getClass()).info("inputStream :" + is);
		if (is == null) {
			throw new WSException(ErrorCode.CONN_TO_WS_ERR);
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			is = null;
			return null;
		}
		Logger.getLogger(this.getClass()).info("document :" + document);
		Element rootElement = document.getDocumentElement();
		
		Logger.getLogger(this.getClass()).info(rootElement.toString());
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(listener!=null){
			listener.handleErrorCode(getErrorCodeInElement(rootElement));
		}
		
		return rootElement;
	}
	
	private ErrorCode getErrorCodeInElement(Element element){
	
		Element errorCodeNode = element.getElementsByTagName("errorCode") != null ? (Element) element
				.getElementsByTagName("errorCode").item(0) : null;
			if ( errorCodeNode != null){
				int errorCode = Integer.parseInt(errorCodeNode.getFirstChild()
						.getNodeValue());
			    return  ErrorCode.get(errorCode);
			}
			return null;
	}

	/*
	 * ***********************************************************
	 * 汽车美容  webservice 最新版本的接口 * * *
	 * ***********************************************************
	 */

	
	/**
       *  功能描述: 用户登录 简单版本
	   */

	public boolean appUserLogin(String name, String password, int shopId,
			String clientEnv, boolean logoutYN) throws WSException {
		this.userMap.put("password",password);
		Element element = null;
		String service = WSConnector.wsUrl + "appUserLogin?name=" + name
				+ "&password=" + password + "&appId=" + shopId + "&clientEnv="
				+ clientEnv + "&logoutYN=" + logoutYN;
		Logger.getLogger(this.getClass()).info(
				"[appUserLogin] service = " + service);
		element = getXMLNode(service);

		if (element != null) {
			Element errCodeNode = element.getElementsByTagName("errorCode") != null ? (Element) element
					.getElementsByTagName("errorCode").item(0) : null;
			Element userIdNode = element.getElementsByTagName("userId") != null ? (Element) element
					.getElementsByTagName("userId").item(0) : null;
			Element shopIdNode = element.getElementsByTagName("shopId") != null ? (Element) element
							.getElementsByTagName("userId").item(0) : null;
			Element secTokenNode = element.getElementsByTagName("secToken") != null ? (Element) element
					.getElementsByTagName("secToken").item(0) : null;

			if (errCodeNode != null) {
				int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
						.getNodeValue());
				if (errorCode == ErrorCode.ACCEPT.getCode()) {
					this.userMap.put("loginName", name);
					this.userMap.put("password", password);
					if (userIdNode != null) {
						this.userMap.put("userId", userIdNode.getFirstChild()
								.getNodeValue());
					}
					if (shopIdNode != null) {
						this.userMap.put("shopId", shopIdNode.getFirstChild()
								.getNodeValue());
					}
					
					if (secTokenNode != null&&secTokenNode
							.getFirstChild()!=null) {
						this.userMap.put("secToken", secTokenNode
								.getFirstChild().getNodeValue());
					}
					Logger.getLogger(this.getClass()).info(
							"[appUserLogin]  value = "
									+ this.userMap.toString());
					return true;
				} else {
					throw new WSException(ErrorCode.get(errorCode));
				}
			} else {
				throw new WSException(ErrorCode.CONN_TO_WS_ERR);
			}
		} else {
			throw new WSException(ErrorCode.LOGIN_FAILED);
		}
	}

	/**
	 *   功能描述: 注册用户
	 */
	public int createUser(String loginName, String password, String phoneNumber, 
			int type,int shopId,String email, String realName) throws WSException {
		String service = "";
		service = WSConnector.wsUrl + "createUser?senderId=0&secToken=1&shopId=" + shopId
				+ "&loginName=" + loginName + "&password=" + password
				+ "&phone=" + phoneNumber
				+ "&type="+type;
		
		if(email!=null){
			service+="&email="+email;
		}
		if(realName!=null){
			service+="&realName="+realName;
		}
		
		
		Logger.getLogger(this.getClass()).info(
				"[createUser]  ws query = " + service);

		int userId;
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				userId = Integer.parseInt(root.getElementsByTagName("userId")
						.item(0).getFirstChild().getNodeValue());
			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		} else {
			throw new WSException(ErrorCode.REJECT);
		}
		return userId;
	}
    /*
     *  更新用户信息
     */
	
	public boolean updUser(int type,UserInfo userInfo) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "updUser?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken") + "&userId="
				+ this.userMap.get("userId")+"&type="+type;
		if(userInfo!=null){
			if(userInfo.getLoginName()!=null){
				service+="&loginName="+userInfo.getLoginName();
			}
			if(userInfo.getPassword()!=null){
				service+="&password="+userInfo.getPassword();
			}
			if(userInfo.getRealName()!=null){
				service+="&realName="+userInfo.getRealName();
			}
			if(userInfo.getEmail()!=null){
				service+="&email="+userInfo.getEmail();
			}
			if(userInfo.getPhone()!=null){
				service+="&phone="+userInfo.getPhone();
			}
			if(userInfo.getWechatId()!=null){
				service+="&wechatId="+userInfo.getWechatId();
			}
			if(userInfo.getShopId()>0){
				service+="&shopId="+userInfo.getShopId();
			}
		}
	
		Logger.getLogger(this.getClass()).info(
				"[updUser]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
			      return true;
			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		} 
		
        return false;
	}
	
	
	public UserInfo getUserInfoById() throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getUserInfoById?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken") + "&userId="
				+ this.userMap.get("userId");
		Logger.getLogger(this.getClass()).info(
				"[updUser]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				Element loginNameNode = root.getElementsByTagName("loginName") != null ? (Element) root
						.getElementsByTagName("loginName").item(0) : null;
			    Element realNameNode = root.getElementsByTagName("realName") != null ? (Element) root
						.getElementsByTagName("realName").item(0) : null;
				Element emailNode = root.getElementsByTagName("email") != null ? (Element) root
						.getElementsByTagName("email").item(0) : null;
				Element phoneNode = root.getElementsByTagName("phone") != null ? (Element) root
						.getElementsByTagName("phone").item(0) : null;
				Element wechatIdNode = root.getElementsByTagName("wechatId") != null ? (Element) root
						.getElementsByTagName("wechatId").item(0) : null;
				Element regTimeNode = root.getElementsByTagName("regTime") != null ? (Element) root
						.getElementsByTagName("regTime").item(0) : null;													
				Element typeNode = root.getElementsByTagName("type") != null ? (Element) root
						.getElementsByTagName("type").item(0) : null;	
				Element shopIdNode = root.getElementsByTagName("shopId") != null ? (Element) root
						.getElementsByTagName("shopId").item(0) : null;	
				String loginName=null;
				String realName=null;
				String email=null;
				String phone=null;
				String wechatId=null;
				String regTime=null;
				int type=-1;
				int shopId=-1;
				
				if (loginNameNode != null && loginNameNode.getFirstChild() != null) {
					  loginName = loginNameNode.getFirstChild().getNodeValue();
				}	
				if (realNameNode != null && realNameNode.getFirstChild() != null) {
					  realName = realNameNode.getFirstChild().getNodeValue();
				}	
				if (emailNode != null && emailNode.getFirstChild() != null) {
					  email = emailNode.getFirstChild().getNodeValue();
				}	
				if (phoneNode != null && phoneNode.getFirstChild() != null) {
					  phone = phoneNode.getFirstChild().getNodeValue();
				}	
				if (wechatIdNode != null && wechatIdNode.getFirstChild() != null) {
					  wechatId = wechatIdNode.getFirstChild().getNodeValue();
				}	
				if (regTimeNode != null && regTimeNode.getFirstChild() != null) {
					  regTime = regTimeNode.getFirstChild().getNodeValue();
				}	
				if (typeNode != null && typeNode.getFirstChild() != null) {
					  type = Integer.parseInt(typeNode.getFirstChild().getNodeValue());
				}	
				if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
					  shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
				}	
				
				UserInfo userInfo=new UserInfo(-1,loginName, realName, null, email, phone, wechatId, regTime, type, shopId);
				
				return userInfo;

			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		} 
		return null;
	}
	private  ShopInfo  parseXmlToShopInfo(Element element){
		Element shopIdNode = (Element) element.getElementsByTagName(
				"shopId").item(0);
		Element nameNode = (Element) element.getElementsByTagName("name")
				.item(0);
		Element longitudeNode = (Element) element.getElementsByTagName("longitude").item(
				0);
		Element latitudeNode = (Element) element.getElementsByTagName(
				"latitude").item(0);
		Element cityIdNode = (Element) element.getElementsByTagName("cityId")
				.item(0);
		Element descNode = (Element) element.getElementsByTagName(
				"desc").item(0);
		int shopId =-1;
		int cityId=-1;
		
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (cityIdNode != null && cityIdNode.getFirstChild() != null) {
			cityId = Integer.parseInt(cityIdNode.getFirstChild().getNodeValue());
		}
		float longitude = 0,latitude = 0;
		if (longitudeNode != null && longitudeNode.getFirstChild() != null) {
			longitude = Float.parseFloat(longitudeNode.getFirstChild().getNodeValue());
		}
		if (latitudeNode != null && latitudeNode.getFirstChild() != null) {
			latitude = Float.parseFloat(latitudeNode.getFirstChild().getNodeValue());
		}
		
		String name = "";
		String desc = "";
		if (nameNode != null && nameNode.getFirstChild() != null) {
			name = nameNode.getFirstChild().getNodeValue();
		}
		if (descNode != null && descNode.getFirstChild() != null) {
			desc = descNode.getFirstChild().getNodeValue();
		}
		ShopInfo shopInfo=new ShopInfo(shopId, name, longitude, latitude, cityId, desc);
		
		return shopInfo;
	}
	public List<ShopInfo> getShopList() throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getShopList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken");
		Logger.getLogger(this.getClass()).info(
				"[getShopList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("shopList");
				Logger.getLogger(this.getClass()).info(
						"[shopList]   nodeList Size = " + nodeList.getLength());
				List<ShopInfo> shopInfos=new ArrayList<ShopInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					ShopInfo shopInfo = parseXmlToShopInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[ShopInfo]  shopInfo = "
									+ shopInfo.toString());
					shopInfos.add(shopInfo);
				}
				return shopInfos;
			}
		}
		return null;
		
	}
	private  DecorationInfo  parseXmlToDecorationInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element nameNode = (Element) element.getElementsByTagName("name")
				.item(0);
		Element descNode = (Element) element.getElementsByTagName("desc").item(
				0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName("shopId")
				.item(0);
		int shopId =-1;
		int id=-1;

		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		float price = 0 ;
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		String name = "";
		String desc = "";
		if (nameNode != null && nameNode.getFirstChild() != null) {
			name = nameNode.getFirstChild().getNodeValue();
		}
		if (descNode != null && descNode.getFirstChild() != null) {
			desc = descNode.getFirstChild().getNodeValue();
		}

		DecorationInfo decorationInfo=new DecorationInfo(id, name, desc, price, shopId);
		return decorationInfo;
	}
	private  OilInfo  parseXmlToOilInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element nameNode = (Element) element.getElementsByTagName("name")
				.item(0);
		Element descNode = (Element) element.getElementsByTagName("desc").item(
				0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName("shopId")
				.item(0);
		int shopId =-1;
		int id=-1;
		
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		float price = 0 ;
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		String name = "";
		String desc = "";
		if (nameNode != null && nameNode.getFirstChild() != null) {
			name = nameNode.getFirstChild().getNodeValue();
		}
		if (descNode != null && descNode.getFirstChild() != null) {
			desc = descNode.getFirstChild().getNodeValue();
		}
		
		OilInfo oilInfo=new OilInfo(id, name, desc, price, shopId);
		return oilInfo;
	}
	public List<OilInfo> getOilList(int shopId) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getOilList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&shopId="+shopId;
		Logger.getLogger(this.getClass()).info(
				"[getOilList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("oilList");
				Logger.getLogger(this.getClass()).info(
						"[oilList]   nodeList Size = " + nodeList.getLength());
				List<OilInfo> oilInfos=new ArrayList<OilInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					OilInfo oilInfo = parseXmlToOilInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[OilInfo]  oilInfo = "
									+ oilInfo.toString());
					oilInfos.add(oilInfo);
				}
				return oilInfos;
			}
		}
		return null;
	}
	private  MetalplateInfo  parseXmlToMetalplateInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element numberNode = (Element) element.getElementsByTagName(
				"number").item(0);
		Element nameNode = (Element) element.getElementsByTagName("name")
				.item(0);
		Element descNode = (Element) element.getElementsByTagName("desc").item(
				0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName("shopId")
				.item(0);
		int shopId =-1;
		int id=-1;

		

		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		float price = 0 ;
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		String name = "";
		String desc = "";
		String number="";
		if (numberNode != null && numberNode.getFirstChild() != null) {
			number = numberNode.getFirstChild().getNodeValue();
		}
		if (nameNode != null && nameNode.getFirstChild() != null) {
			name = nameNode.getFirstChild().getNodeValue();
		}
		if (descNode != null && descNode.getFirstChild() != null) {
			desc = descNode.getFirstChild().getNodeValue();
		}
		
		MetalplateInfo metalplateInfo=new MetalplateInfo(id, name, desc, price, shopId, number);
		return metalplateInfo;
	}
	public List<MetalplateInfo> getMetalplateList(int shopId) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getMetalplateList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&shopId="+shopId;
		Logger.getLogger(this.getClass()).info(
				"[getMetalplateList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("metalplateList");
				Logger.getLogger(this.getClass()).info(
						"[metalplateList]   nodeList Size = " + nodeList.getLength());
				List<MetalplateInfo> metalplateInfos=new ArrayList<MetalplateInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					MetalplateInfo metalplateInfo = parseXmlToMetalplateInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[MetalplateInfo]  metalplateInfo = "
									+ metalplateInfo.toString());
					metalplateInfos.add(metalplateInfo);
				}
				return metalplateInfos;
			}
		}
		return null;
	}
	public List<DecorationInfo> getDecorationList(int shopId) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getDecorationList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&shopId="+shopId;
		Logger.getLogger(this.getClass()).info(
				"[getDecorationList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("decorationList");
				Logger.getLogger(this.getClass()).info(
						"[decorationList]   nodeList Size = " + nodeList.getLength());
				List<DecorationInfo> decorationInfos=new ArrayList<DecorationInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					DecorationInfo decorationInfo = parseXmlToDecorationInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[DecorationInfo]  decorationInfo = "
									+ decorationInfo.toString());
					decorationInfos.add(decorationInfo);
				}
				return decorationInfos;
			}
		}
		return null;
	}
	private  CouponInfo  parseXmlToCouponInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element numberNode = (Element) element.getElementsByTagName(
				"number").item(0);
		Element nameNode = (Element) element.getElementsByTagName(
				"name").item(0);
		Element descNode = (Element) element.getElementsByTagName(
				"desc").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName(
				"shopId").item(0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element discountNode = (Element) element.getElementsByTagName(
				"discount").item(0);
		Element typeNode = (Element) element.getElementsByTagName(
				"type").item(0);
		Element endTimeNode = (Element) element.getElementsByTagName(
				"endTime").item(0);
		Element createTimeNode = (Element) element.getElementsByTagName(
				"createTime").item(0);
		Element createUserIdNode = (Element) element.getElementsByTagName(
				"createUserId").item(0);
		Element useUserIdNode = (Element) element.getElementsByTagName(
				"useUserId").item(0);
		Element isUsedNode = (Element) element.getElementsByTagName(
				"isUsed").item(0);
		Element orderTypeNode = (Element) element.getElementsByTagName(
				"orderType").item(0);
		int id=-1,shopId=-1,type=-1,createUserId=-1,useUserId=-1,isUsed=-1,orderType=-1;
		float price=0,discount=0;
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		if (discountNode != null && discountNode.getFirstChild() != null) {
			discount = Float.parseFloat(discountNode.getFirstChild().getNodeValue());
		}
		if (typeNode != null && typeNode.getFirstChild() != null) {
			type = Integer.parseInt(typeNode.getFirstChild().getNodeValue());
		}
		if (createUserIdNode != null && createUserIdNode.getFirstChild() != null) {
			createUserId = Integer.parseInt(createUserIdNode.getFirstChild().getNodeValue());
		}
		if (useUserIdNode != null && useUserIdNode.getFirstChild() != null) {
			useUserId = Integer.parseInt(useUserIdNode.getFirstChild().getNodeValue());
		}
		if (isUsedNode != null && isUsedNode.getFirstChild() != null) {
			isUsed = Integer.parseInt(isUsedNode.getFirstChild().getNodeValue());
		}
		if (orderTypeNode != null && orderTypeNode.getFirstChild() != null) {
			orderType = Integer.parseInt(orderTypeNode.getFirstChild().getNodeValue());
		}
		
		
		String number="", name="",desc="",endTime="",createTime="";
		if (numberNode != null && numberNode.getFirstChild() != null) {
			number = numberNode.getFirstChild().getNodeValue();
		}
		if (nameNode != null && nameNode.getFirstChild() != null) {
			name = nameNode.getFirstChild().getNodeValue();
		}
		if (descNode != null && descNode.getFirstChild() != null) {
			desc = descNode.getFirstChild().getNodeValue();
		}
		if (endTimeNode != null && endTimeNode.getFirstChild() != null) {
			endTime = endTimeNode.getFirstChild().getNodeValue();
		}
		if (createTimeNode != null && createTimeNode.getFirstChild() != null) {
			createTime = createTimeNode.getFirstChild().getNodeValue();
		}
		
		CouponInfo couponInfo=new CouponInfo(id, number, name, desc, shopId, price, discount, type, createTime, endTime, createUserId, useUserId, isUsed, orderType);
		
		return couponInfo;
		
	}
	public List<CouponInfo> getCouponList(int shopId) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getCouponList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&shopId="+shopId;
		Logger.getLogger(this.getClass()).info(
				"[getCouponList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("couponList");
				Logger.getLogger(this.getClass()).info(
						"[couponList]   nodeList Size = " + nodeList.getLength());
				List<CouponInfo> couponInfos=new ArrayList<CouponInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					CouponInfo couponInfo =parseXmlToCouponInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[CouponInfo]  couponInfo = "
									+ couponInfo.toString());
					couponInfos.add(couponInfo);
				}
				return couponInfos;
			}
		}
		return null;
	}
	private CityInfo parseXmlToCityInfo(Element element){
		Element cityIdNode = (Element) element.getElementsByTagName(
				"cityId").item(0);
		Element nameNode = (Element) element.getElementsByTagName(
				"name").item(0);
		Element shopNumNode = (Element) element.getElementsByTagName(
				"shopNum").item(0);
		int  cityId=-1,shopNum=0;
		if (cityIdNode != null && cityIdNode.getFirstChild() != null) {
			cityId = Integer.parseInt(cityIdNode.getFirstChild().getNodeValue());
		}
		if (shopNumNode != null && shopNumNode.getFirstChild() != null) {
			shopNum = Integer.parseInt(shopNumNode.getFirstChild().getNodeValue());
		}
		String name="";
		if (nameNode != null && nameNode.getFirstChild() != null) {
			name = nameNode.getFirstChild().getNodeValue();
		}
		CityInfo cityInfo=new CityInfo(cityId, name, shopNum);
		
		return cityInfo;
	}
	public List<CityInfo> getCityList() throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getCityList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken");
		Logger.getLogger(this.getClass()).info(
				"[getCityList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("cityList");
				Logger.getLogger(this.getClass()).info(
						"[cityList]   nodeList Size = " + nodeList.getLength());
				List<CityInfo> cityInfos=new ArrayList<CityInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					CityInfo cityInfo =parseXmlToCityInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[CityInfo]  cityInfo = "
									+ cityInfo.toString());
					cityInfos.add(cityInfo);
				}
				return cityInfos;
			}
		}
		return null;
	}
	
	public boolean createCar(CarInfo carInfo) throws WSException, UnsupportedEncodingException {
		if(carInfo==null){
			return false;
		}
		String service = "";
		service = WSConnector.wsUrl + "createCar?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&userId="+this.userMap.get("userId")
				+"&type="+carInfo.getType()+"&number="+URLEncoder.encode(carInfo.getNumber(), "UTF-8");
		Logger.getLogger(this.getClass()).info(
				"[createCar]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
	public boolean delCar(int id) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "delCar?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&id="+id;
		Logger.getLogger(this.getClass()).info(
				"[delCar]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
	public boolean updCar(CarInfo carInfo) throws WSException, UnsupportedEncodingException {
		String service = "";
		service = WSConnector.wsUrl + "updCar?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&userId="+this.userMap.get("userId")
				+"&id="+carInfo.getId()+"&number="+URLEncoder.encode(carInfo.getNumber(), "UTF-8");
		if(carInfo.getType()>=0){
			service+="&type="+carInfo.getType();
		}
		Logger.getLogger(this.getClass()).info(
				"[updCar]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}	
	private CarInfo parseXmlToCarInfo(Element element) throws UnsupportedEncodingException {
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element numberNode = (Element) element.getElementsByTagName(
				"number").item(0);
		Element typeNode = (Element) element.getElementsByTagName(
				"type").item(0);
		Element userIdNode = (Element) element.getElementsByTagName(
				"userId").item(0);
		int  id=-1,type=-1,userId=-1;
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (typeNode != null && typeNode.getFirstChild() != null) {
			type = Integer.parseInt(typeNode.getFirstChild().getNodeValue());
		}
		if (userIdNode != null && userIdNode.getFirstChild() != null) {
			userId = Integer.parseInt(userIdNode.getFirstChild().getNodeValue());
		}
		String number="";
		if (numberNode != null && numberNode.getFirstChild() != null) {
			number = numberNode.getFirstChild().getNodeValue();
		}
		//


		CarInfo carInfo=new CarInfo(id, type, URLDecoder.decode(number,"UTF-8"), userId);
		return carInfo;
	}
	public List<CarInfo> getCarByUserId() throws WSException, UnsupportedEncodingException {
		String service = "";
		service = WSConnector.wsUrl + "getCarByUserId?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&userId="+this.userMap.get("userId");
	
		Logger.getLogger(this.getClass()).info(
				"[getCarByUserId]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("carList");
				Logger.getLogger(this.getClass()).info(
						"[carList]   nodeList Size = " + nodeList.getLength());
				List<CarInfo> carInfos=new ArrayList<CarInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					CarInfo carInfo =parseXmlToCarInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[CarInfo]  carInfo = "
									+ carInfo.toString());
					carInfos.add(carInfo);
				}
				return carInfos;
			}
		}
		return null;
	}
	private GoodInfo parseXmlToGoodInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element nameNode = (Element) element.getElementsByTagName(
				"name").item(0);
		Element descNode = (Element) element.getElementsByTagName(
				"desc").item(0);
		Element isShowNode = (Element) element.getElementsByTagName(
				"isShow").item(0);
		Element isChangeNode = (Element) element.getElementsByTagName(
				"isChange").item(0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element srcNode = (Element) element.getElementsByTagName(
				"src").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName(
				"shopId").item(0);
		int  id=-1,isShow=-1,isChange=-1,shopId=-1;
		float price=0;
		String name="",desc="",src="";
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (isShowNode != null && isShowNode.getFirstChild() != null) {
			isShow = Integer.parseInt(isShowNode.getFirstChild().getNodeValue());
		}
		if (isChangeNode != null && isChangeNode.getFirstChild() != null) {
			isChange = Integer.parseInt(isChangeNode.getFirstChild().getNodeValue());
		}
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		
		if (nameNode != null && nameNode.getFirstChild() != null) {
			name =nameNode.getFirstChild().getNodeValue();
		}
		if (descNode != null && descNode.getFirstChild() != null) {
			desc =descNode.getFirstChild().getNodeValue();
		}
		if (srcNode != null && srcNode.getFirstChild() != null) {
			src =srcNode.getFirstChild().getNodeValue();
		}
		
		GoodInfo goodInfo=new GoodInfo(id, name, desc, isShow, isChange, price, src, shopId);
		return goodInfo;
	}
	public List<GoodInfo> getGoodsList(int shopId) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getGoodsList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&shopId="+shopId;
	
		Logger.getLogger(this.getClass()).info(
				"[getGoodsList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("goodsList");
				Logger.getLogger(this.getClass()).info(
						"[goodsList]   nodeList Size = " + nodeList.getLength());
				List<GoodInfo> goodInfos=new ArrayList<GoodInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					GoodInfo goodInfo =parseXmlToGoodInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[GoodInfo]  goodInfo = "
									+ goodInfo.toString());
					goodInfos.add(goodInfo);
				}
				return goodInfos;
			}
		}
		return null;
	}
	public OilOrderInfo createOilOrder(OilOrderInfo oilOrderInfo) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "createOilOrder?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&type="+oilOrderInfo.getType()+"&state="+oilOrderInfo.getState()
				+"&payState="+oilOrderInfo.getPayState()+"&userId="+this.userMap.get("userId")+"&carId="+oilOrderInfo.getCarId()
				+"&shopId="+oilOrderInfo.getShopId()+"&stationId="+oilOrderInfo.getStationId()+"&orderTime="+oilOrderInfo.getOrderTime();
	
		if(oilOrderInfo.getPrice()>0){
			service+="&price="+oilOrderInfo.getPrice();
		}
		if(oilOrderInfo.getCouponId()>0){
			service+="&couponId="+oilOrderInfo.getCouponId();
		}
		
		Logger.getLogger(this.getClass()).info(
				"[createOilOrder]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element idNode = root.getElementsByTagName("id") != null ? (Element) root
				.getElementsByTagName("id").item(0) : null;
		Element createTimeNode = root.getElementsByTagName("createTime") != null ? (Element) root
				.getElementsByTagName("createTime").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());

			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				int id = Integer.parseInt(idNode.getFirstChild()
						.getNodeValue());
				String createTime = createTimeNode.getFirstChild()
						.getNodeValue();
				oilOrderInfo.setId(id);
				oilOrderInfo.setCreateTime(createTime);
				return oilOrderInfo;
			}
		}
		return null;
		
	}
	public boolean delOilOrder(int id) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "delOilOrder?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&id="+id;
		Logger.getLogger(this.getClass()).info(
				"[delOilOrder]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
	private OilOrderInfo parseXmlToOilOrderInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element typeNode = (Element) element.getElementsByTagName(
				"type").item(0);
		Element createTimeNode = (Element) element.getElementsByTagName(
				"createTime").item(0);
		Element finishTimeNode = (Element) element.getElementsByTagName(
				"finishTime").item(0);
		Element stateNode = (Element) element.getElementsByTagName(
				"state").item(0);
		Element payStateNode = (Element) element.getElementsByTagName(
				"payState").item(0);
		Element userIdNode = (Element) element.getElementsByTagName(
				"userId").item(0);
		Element carIdNode = (Element) element.getElementsByTagName(
				"carId").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName(
				"shopId").item(0);
		Element stationIdNode = (Element) element.getElementsByTagName(
				"stationId").item(0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element couponIdNode = (Element) element.getElementsByTagName(
				"couponId").item(0);
		int id=-1,type=-1,state=-1,payState=-1,userId=-1,carId=-1,shopId=-1,stationId=-1,couponId=-1;
		float price=-1;
		String createTime="",finishTime="";
		
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (typeNode != null && typeNode.getFirstChild() != null) {
			type = Integer.parseInt(typeNode.getFirstChild().getNodeValue());
		}
		if (stateNode != null && stateNode.getFirstChild() != null) {
			state = Integer.parseInt(stateNode.getFirstChild().getNodeValue());
		}
		if (payStateNode != null && payStateNode.getFirstChild() != null) {
			payState = Integer.parseInt(payStateNode.getFirstChild().getNodeValue());
		}
		if (userIdNode != null && userIdNode.getFirstChild() != null) {
			userId = Integer.parseInt(userIdNode.getFirstChild().getNodeValue());
		}
		if (carIdNode != null && carIdNode.getFirstChild() != null) {
			carId = Integer.parseInt(carIdNode.getFirstChild().getNodeValue());
		}
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (stationIdNode != null && stationIdNode.getFirstChild() != null) {
			stationId = Integer.parseInt(stationIdNode.getFirstChild().getNodeValue());
		}
		if (couponIdNode != null && couponIdNode.getFirstChild() != null) {
			couponId = Integer.parseInt(couponIdNode.getFirstChild().getNodeValue());
		}
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		if (createTimeNode != null && createTimeNode.getFirstChild() != null) {
			createTime = createTimeNode.getFirstChild().getNodeValue();
		}
		if (finishTimeNode != null && finishTimeNode.getFirstChild() != null) {
			finishTime = finishTimeNode.getFirstChild().getNodeValue();
		}
		NodeList  nodeList=element.getElementsByTagName("oilOrderNumber");
		List<OilOrderNumberInfo> oilOrderNumberInfos=new ArrayList<OilOrderInfo.OilOrderNumberInfo>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element ele = (Element) (nodeList.item(i));
			OilOrderNumberInfo oilOrderNumberInfo =parseXmlToOilOrderNumberInfo(ele);
			Logger.getLogger(this.getClass()).info(
					"[OilOrderNumberInfo]  oilOrderNumberInfo = "
							+ oilOrderNumberInfo.toString());
			oilOrderNumberInfos.add(oilOrderNumberInfo);
		}
		
		OilOrderInfo oilOrderInfo=new OilOrderInfo(id, type, state, payState, userId, carId, shopId, stationId, price, couponId, createTime, finishTime, null, oilOrderNumberInfos);
		return oilOrderInfo;
	}
	private OilOrderNumberInfo parseXmlToOilOrderNumberInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element oilIdNode = (Element) element.getElementsByTagName(
				"oilId").item(0);
		Element oilOrderIdNode = (Element) element.getElementsByTagName(
				"oilOrderId").item(0);
        int id=-1,oilId=-1,oilOrderId=-1;
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (oilIdNode != null && oilIdNode.getFirstChild() != null) {
			oilId = Integer.parseInt(oilIdNode.getFirstChild().getNodeValue());
		}
		if (oilOrderIdNode != null && oilOrderIdNode.getFirstChild() != null) {
			oilOrderId= Integer.parseInt(oilOrderIdNode.getFirstChild().getNodeValue());
		}
		OilOrderNumberInfo oilOrderNumberInfo=new OilOrderNumberInfo(id, oilId, oilOrderId);
		return oilOrderNumberInfo;
	}
	public List<OilOrderInfo> getOilOrderList(int searchType,int shopId,int carId,int stationId,String startTime,int maxNum) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getOilOrderList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&userId="+ this.userMap.get("userId") 
				+"&searchType="+searchType;
		
		if(shopId>0){
			service="&shopId="+shopId;
		}
		if(carId>0){
			service="&carId="+carId;
		}
		if(stationId>0){
			service="&stationId="+stationId;
		}
		if(maxNum>0){
			service="&maxNum="+maxNum;
		}
		if(startTime!=null){
			service="&startTime="+startTime;
		}
	
		Logger.getLogger(this.getClass()).info(
				"[getOilOrderList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("oilOrderList");
				Logger.getLogger(this.getClass()).info(
						"[oilOrderList]   nodeList Size = " + nodeList.getLength());
				List<OilOrderInfo> oilOrderInfos=new ArrayList<OilOrderInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					OilOrderInfo oilOrderInfo =parseXmlToOilOrderInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[OilOrderInfo]  oilOrderInfo = "
									+ oilOrderInfo.toString());
					oilOrderInfos.add(oilOrderInfo);
				}
				return oilOrderInfos;
			}
		}
		return null;
	}
	public boolean  createOilOrderNumber(int oilOrderId,int oilId) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "createOilOrderNumber?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&oilOrderId="+ oilOrderId+"&oilId="+oilId; 
		Logger.getLogger(this.getClass()).info(
				"[createOilOrderNumber]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean createMetaOrder(MetaOrderInfo metaOrderInfo) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "createMetaOrder?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&type="+metaOrderInfo.getType()+"&state="+metaOrderInfo.getState()
				+"&payState="+metaOrderInfo.getPayState()+"&userId="+this.userMap.get("userId")+"&carId="+metaOrderInfo.getCarId()
				+"&shopId="+metaOrderInfo.getShopId()+"&stationId="+metaOrderInfo.getStationId()+"&orderTime="+metaOrderInfo.getOrderTime();
	
		if(metaOrderInfo.getPrice()>0){
			service+="&price="+metaOrderInfo.getPrice();
		}
		if(metaOrderInfo.getCouponId()>0){
			service+="&couponId="+metaOrderInfo.getCouponId();
		}
		
		Logger.getLogger(this.getClass()).info(
				"[createMetaOrder]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
		
	}
	public boolean delMetaOrder(int id) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "delMetaOrder?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&id="+id;
		Logger.getLogger(this.getClass()).info(
				"[delMetaOrder]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
	private MetaOrderInfo parseXmlToMetaOrderInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element typeNode = (Element) element.getElementsByTagName(
				"type").item(0);
		Element createTimeNode = (Element) element.getElementsByTagName(
				"createTime").item(0);
		Element finishTimeNode = (Element) element.getElementsByTagName(
				"finishTime").item(0);
		Element stateNode = (Element) element.getElementsByTagName(
				"state").item(0);
		Element payStateNode = (Element) element.getElementsByTagName(
				"payState").item(0);
		Element userIdNode = (Element) element.getElementsByTagName(
				"userId").item(0);
		Element carIdNode = (Element) element.getElementsByTagName(
				"carId").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName(
				"shopId").item(0);
		Element stationIdNode = (Element) element.getElementsByTagName(
				"stationId").item(0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element couponIdNode = (Element) element.getElementsByTagName(
				"couponId").item(0);
		int id=-1,type=-1,state=-1,payState=-1,userId=-1,carId=-1,shopId=-1,stationId=-1,couponId=-1;
		float price=-1;
		String createTime="",finishTime="";
		
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (typeNode != null && typeNode.getFirstChild() != null) {
			type = Integer.parseInt(typeNode.getFirstChild().getNodeValue());
		}
		if (stateNode != null && stateNode.getFirstChild() != null) {
			state = Integer.parseInt(stateNode.getFirstChild().getNodeValue());
		}
		if (payStateNode != null && payStateNode.getFirstChild() != null) {
			payState = Integer.parseInt(payStateNode.getFirstChild().getNodeValue());
		}
		if (userIdNode != null && userIdNode.getFirstChild() != null) {
			userId = Integer.parseInt(userIdNode.getFirstChild().getNodeValue());
		}
		if (carIdNode != null && carIdNode.getFirstChild() != null) {
			carId = Integer.parseInt(carIdNode.getFirstChild().getNodeValue());
		}
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (stationIdNode != null && stationIdNode.getFirstChild() != null) {
			stationId = Integer.parseInt(stationIdNode.getFirstChild().getNodeValue());
		}
		if (couponIdNode != null && couponIdNode.getFirstChild() != null) {
			couponId = Integer.parseInt(couponIdNode.getFirstChild().getNodeValue());
		}
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		if (createTimeNode != null && createTimeNode.getFirstChild() != null) {
			createTime = createTimeNode.getFirstChild().getNodeValue();
		}
		if (finishTimeNode != null && finishTimeNode.getFirstChild() != null) {
			finishTime = finishTimeNode.getFirstChild().getNodeValue();
		}
		NodeList  metaOrderNumberNodeList=element.getElementsByTagName("metaOrderNumber");
		NodeList  metaOrderImgNodeList=element.getElementsByTagName("metaOrderImg");
		List<MetaOrderNumber> metaOrderNumbers=new ArrayList<MetaOrderInfo.MetaOrderNumber>();
		List<MetaOrderImg> metaOrderImgs=new ArrayList<MetaOrderInfo.MetaOrderImg>();
		
		for (int i = 0; i < metaOrderNumberNodeList.getLength(); i++) {
			Element ele = (Element) (metaOrderNumberNodeList.item(i));
			MetaOrderNumber metaOrderNumber =parseXmlToMetaOrderNumber(ele);
			Logger.getLogger(this.getClass()).info(
					"[MetaOrderNumber]  metaOrderNumber = "
							+ metaOrderNumber.toString());
			metaOrderNumbers.add(metaOrderNumber);
		}
		
		for (int i = 0; i < metaOrderImgNodeList.getLength(); i++) {
			Element ele = (Element) (metaOrderImgNodeList.item(i));
			MetaOrderImg metaOrderImg =parseXmlToMetaOrderImg(ele);
			Logger.getLogger(this.getClass()).info(
					"[MetaOrderImg]  metaOrderImg = "
							+ metaOrderImg.toString());
			metaOrderImgs.add(metaOrderImg);
		}
		
		MetaOrderInfo metaOrderInfo=new MetaOrderInfo(id, type, state, payState, userId, carId, 
				shopId, stationId, price, couponId, createTime, finishTime, null, metaOrderNumbers, metaOrderImgs);
		return metaOrderInfo;
	}
	private MetaOrderNumber parseXmlToMetaOrderNumber(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element metaIdNode = (Element) element.getElementsByTagName(
				"metaId").item(0);
		Element metaOrderIdNode = (Element) element.getElementsByTagName(
				"metaOrderId").item(0);
        int id=-1,metaId=-1,metaOrderId=-1;
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (metaIdNode != null && metaIdNode.getFirstChild() != null) {
			metaId = Integer.parseInt(metaIdNode.getFirstChild().getNodeValue());
		}
		if (metaOrderIdNode != null && metaOrderIdNode.getFirstChild() != null) {
			metaOrderId= Integer.parseInt(metaOrderIdNode.getFirstChild().getNodeValue());
		}
		MetaOrderNumber metaOrderNumber=new MetaOrderNumber(id, metaId, metaOrderId);
		return metaOrderNumber;
	}
	private MetaOrderImg parseXmlToMetaOrderImg(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element imgNameNode = (Element) element.getElementsByTagName(
				"imgName").item(0);
		Element metaOrderIdNode = (Element) element.getElementsByTagName(
				"metaOrderId").item(0);
        int id=-1,metaOrderId=-1;
        String imgName="";
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (imgNameNode != null && imgNameNode.getFirstChild() != null) {
			imgName =imgNameNode.getFirstChild().getNodeValue();
		}
		if (metaOrderIdNode != null && metaOrderIdNode.getFirstChild() != null) {
			metaOrderId= Integer.parseInt(metaOrderIdNode.getFirstChild().getNodeValue());
		}
		MetaOrderImg metaOrderImg=new MetaOrderImg(id, metaOrderId, imgName);
		return metaOrderImg;
	}
	public List<MetaOrderInfo> getMetaOrderList(int searchType,int shopId,int carId,int stationId,String startTime,int maxNum) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getMetaOrderList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&userId="+ this.userMap.get("userId") 
				+"&searchType="+searchType;
		
		if(shopId>0){
			service="&shopId="+shopId;
		}
		if(carId>0){
			service="&carId="+carId;
		}
		if(stationId>0){
			service="&stationId="+stationId;
		}
		if(maxNum>0){
			service="&maxNum="+maxNum;
		}
		if(startTime!=null){
			service="&startTime="+startTime;
		}
	
		Logger.getLogger(this.getClass()).info(
				"[getMetaOrderList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("metaOrderList");
				Logger.getLogger(this.getClass()).info(
						"[metaOrderList]   nodeList Size = " + nodeList.getLength());
				List<MetaOrderInfo> metaOrderInfos=new ArrayList<MetaOrderInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					MetaOrderInfo metaOrderInfo =parseXmlToMetaOrderInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[MetaOrderInfo]  metaOrderInfo = "
									+ metaOrderInfo.toString());
					metaOrderInfos.add(metaOrderInfo);
				}
				return metaOrderInfos;
			}
		}
		return null;
	}
	public boolean createMetaOrderNumber(int metaOrderId,int metaId) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "createMetaOrderNumber?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&metaOrderId="+metaOrderId 
				+"&metaId="+metaId;
		Logger.getLogger(this.getClass()).info(
				"[createMetaOrderNumber]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
    public boolean createMetaOrderImg(int metaOrderId,String imgName) throws WSException{
    	String service = "";
		service = WSConnector.wsUrl + "createMetaOrderImg?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&metaOrderId="+metaOrderId 
				+"&imgName="+imgName;
		Logger.getLogger(this.getClass()).info(
				"[createMetaOrderImg]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
    public DecoOrderInfo createDecoOrder(DecoOrderInfo decoOrderInfo) throws WSException{
    	String service = "";
		service = WSConnector.wsUrl + "createDecoOrder?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&type="+decoOrderInfo.getType()+"&state="+decoOrderInfo.getState()
				+"&payState="+decoOrderInfo.getPayState()+"&userId="+this.userMap.get("userId")+"&carId="+decoOrderInfo.getCarId()
				+"&shopId="+decoOrderInfo.getShopId()+"&stationId="+decoOrderInfo.getStationId()+"&orderTime="+decoOrderInfo.getOrderTime();
	
		if(decoOrderInfo.getPrice()>0){
			service+="&price="+decoOrderInfo.getPrice();
		}
		if(decoOrderInfo.getCouponId()>0){
			service+="&couponId="+decoOrderInfo.getCouponId();
		}
		
		Logger.getLogger(this.getClass()).info(
				"[createDecoOrder]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element idNode = root.getElementsByTagName("id") != null ? (Element) root
				.getElementsByTagName("id").item(0) : null;
		Element createTimeNode = root.getElementsByTagName("createTime") != null ? (Element) root
				.getElementsByTagName("createTime").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				int id = Integer.parseInt(idNode.getFirstChild()
						.getNodeValue());
				String createTime = createTimeNode.getFirstChild()
						.getNodeValue();
				decoOrderInfo.setId(id);
				decoOrderInfo.setCreateTime(createTime);
				return decoOrderInfo;
			}else{
				throw new WSException(ErrorCode.get(errorCode));
			}
		}
		return null;
    }
    public boolean delDecoOrder(int id) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "delDecoOrder?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&id="+id;
		Logger.getLogger(this.getClass()).info(
				"[delDecoOrder]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			}
		}
		return false;
	}
    private DecoOrderInfo parseXmlToDecoOrderInfo(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element typeNode = (Element) element.getElementsByTagName(
				"type").item(0);
		Element createTimeNode = (Element) element.getElementsByTagName(
				"createTime").item(0);
		Element finishTimeNode = (Element) element.getElementsByTagName(
				"finishTime").item(0);
		Element stateNode = (Element) element.getElementsByTagName(
				"state").item(0);
		Element payStateNode = (Element) element.getElementsByTagName(
				"payState").item(0);
		Element userIdNode = (Element) element.getElementsByTagName(
				"userId").item(0);
		Element carIdNode = (Element) element.getElementsByTagName(
				"carId").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName(
				"shopId").item(0);
		Element stationIdNode = (Element) element.getElementsByTagName(
				"stationId").item(0);
		Element priceNode = (Element) element.getElementsByTagName(
				"price").item(0);
		Element couponIdNode = (Element) element.getElementsByTagName(
				"couponId").item(0);
		int id=-1,type=-1,state=-1,payState=-1,userId=-1,carId=-1,shopId=-1,stationId=-1,couponId=-1;
		float price=-1;
		String createTime="",finishTime="";
		
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (typeNode != null && typeNode.getFirstChild() != null) {
			type = Integer.parseInt(typeNode.getFirstChild().getNodeValue());
		}
		if (stateNode != null && stateNode.getFirstChild() != null) {
			state = Integer.parseInt(stateNode.getFirstChild().getNodeValue());
		}
		if (payStateNode != null && payStateNode.getFirstChild() != null) {
			payState = Integer.parseInt(payStateNode.getFirstChild().getNodeValue());
		}
		if (userIdNode != null && userIdNode.getFirstChild() != null) {
			userId = Integer.parseInt(userIdNode.getFirstChild().getNodeValue());
		}
		if (carIdNode != null && carIdNode.getFirstChild() != null) {
			carId = Integer.parseInt(carIdNode.getFirstChild().getNodeValue());
		}
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (stationIdNode != null && stationIdNode.getFirstChild() != null) {
			stationId = Integer.parseInt(stationIdNode.getFirstChild().getNodeValue());
		}
		if (couponIdNode != null && couponIdNode.getFirstChild() != null) {
			couponId = Integer.parseInt(couponIdNode.getFirstChild().getNodeValue());
		}
		if (priceNode != null && priceNode.getFirstChild() != null) {
			price = Float.parseFloat(priceNode.getFirstChild().getNodeValue());
		}
		if (createTimeNode != null && createTimeNode.getFirstChild() != null) {
			createTime = createTimeNode.getFirstChild().getNodeValue();
		}
		if (finishTimeNode != null && finishTimeNode.getFirstChild() != null) {
			finishTime = finishTimeNode.getFirstChild().getNodeValue();
		}
		NodeList  nodeList=element.getElementsByTagName("decoOrderNumber");
		List<DecoOrderNumber> decoOrderNumbers=new ArrayList<DecoOrderInfo.DecoOrderNumber>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element ele = (Element) (nodeList.item(i));
			DecoOrderNumber decoOrderNumber =parseXmlToDecoOrderNumber(ele);
			Logger.getLogger(this.getClass()).info(
					"[DecoOrderNumber]  decoOrderNumber = "
							+ decoOrderNumber.toString());
			decoOrderNumbers.add(decoOrderNumber);
		}
		
		DecoOrderInfo decoOrderInfo=new DecoOrderInfo(id, type, state, payState, userId, carId, shopId, stationId, price, couponId,
				            createTime, finishTime, null, decoOrderNumbers);
		return decoOrderInfo;
	}
    private DecoOrderNumber parseXmlToDecoOrderNumber(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element decoIdNode = (Element) element.getElementsByTagName(
				"decoId").item(0);
		Element decoOrderIdNode = (Element) element.getElementsByTagName(
				"decoOrderId").item(0);
        int id=-1,decoId=-1,decoOrderId=-1;
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (decoIdNode != null && decoIdNode.getFirstChild() != null) {
			decoId = Integer.parseInt(decoIdNode.getFirstChild().getNodeValue());
		}
		if (decoOrderIdNode != null && decoOrderIdNode.getFirstChild() != null) {
			decoOrderId= Integer.parseInt(decoOrderIdNode.getFirstChild().getNodeValue());
		}
		DecoOrderNumber decoOrderNumber=new DecoOrderNumber(id, decoOrderId, decoId);
		return decoOrderNumber;
	}
    public List<DecoOrderInfo> getDecoOrderList(int searchType,int shopId,int carId,int stationId,String startTime,int maxNum) throws WSException{
		String service = "";
		service = WSConnector.wsUrl + "getDecoOrderList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&userId="+ this.userMap.get("userId") 
				+"&searchType="+searchType;
		
		if(shopId>0){
			service="&shopId="+shopId;
		}
		if(carId>0){
			service="&carId="+carId;
		}
		if(stationId>0){
			service="&stationId="+stationId;
		}
		if(maxNum>0){
			service="&maxNum="+maxNum;
		}
		if(startTime!=null){
			service="&startTime="+startTime;
		}
	
		Logger.getLogger(this.getClass()).info(
				"[getDecoOrderList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("decoOrderList");
				Logger.getLogger(this.getClass()).info(
						"[decoOrderList]   nodeList Size = " + nodeList.getLength());
				List<DecoOrderInfo> decoOrderInfos=new ArrayList<DecoOrderInfo>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					DecoOrderInfo decoOrderInfo =parseXmlToDecoOrderInfo(element);
					Logger.getLogger(this.getClass()).info(
							"[DecoOrderInfo]  decoOrderInfo = "
									+ decoOrderInfo.toString());
					decoOrderInfos.add(decoOrderInfo);
				}
				return decoOrderInfos;
			}
		}
		return null;
	}
    public boolean createDecoOrderNumber(int decoId,int decoOrderId) throws WSException{
    	String service = "";
		service = WSConnector.wsUrl + "createDecoOrderNumber?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&decoId="+ decoId 
				+"&decoOrderId="+decoOrderId;
		Logger.getLogger(this.getClass()).info(
				"[createDecoOrderNumber]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				 return true;
			}
		}
		return false;
    }
    private PromotionInfoType parseXmlToBannerInfoType(Element element){
		Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element imgNameNode = (Element) element.getElementsByTagName(
				"imgName").item(0);
		Element srcNode = (Element) element.getElementsByTagName(
				"src").item(0);
        int id=-1;
        String imgName="",src="";
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (imgNameNode != null && imgNameNode.getFirstChild() != null) {
			imgName = imgNameNode.getFirstChild().getNodeValue();
		}
		if (srcNode != null && srcNode.getFirstChild() != null) {
			src = srcNode.getFirstChild().getNodeValue();
		}
		PromotionInfoType promotionInfoType=new PromotionInfoType(id, imgName, src);
		return promotionInfoType;
	}
    public List<BannerInfoType> getBannerList(int maxNum) throws WSException{
    	String service = "";
		service = WSConnector.wsUrl + "getBannerList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&maxNum="+ maxNum;
		Logger.getLogger(this.getClass()).info(
				"[getBannerList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("bannerList");
				Logger.getLogger(this.getClass()).info(
						"[getBannerList]   nodeList Size = " + nodeList.getLength());
				List<BannerInfoType> bannerInfoTypes=new ArrayList<BannerInfoType>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					BannerInfoType bannerInfoType =parseXmlToBannerInfoType(element);
					Logger.getLogger(this.getClass()).info(
							"[BannerInfoType]  bannerInfoType = "
									+ bannerInfoType.toString());
					bannerInfoTypes.add(bannerInfoType);
				}
				return bannerInfoTypes;
			}
		}
		return null;
    }
    private  AssessInfoType parseXmlToAssessInfoType(Element element){
    	Element idNode = (Element) element.getElementsByTagName(
				"id").item(0);
		Element assessNode = (Element) element.getElementsByTagName(
				"assess").item(0);
		Element shopIdNode = (Element) element.getElementsByTagName(
				"shopId").item(0);
		Element levelNode = (Element) element.getElementsByTagName(
				"level").item(0);
		Element userIdNode = (Element) element.getElementsByTagName(
				"userId").item(0);
		Element createTimeNode = (Element) element.getElementsByTagName(
				"createTime").item(0);
		Element orderIdNode = (Element) element.getElementsByTagName(
				"orderId").item(0);
        int id=-1,shopId=-1,level=-1,userId=-1,orderId=-1;
        
        String assess="",createTime="";
		if (idNode != null && idNode.getFirstChild() != null) {
			id = Integer.parseInt(idNode.getFirstChild().getNodeValue());
		}
		if (shopIdNode != null && shopIdNode.getFirstChild() != null) {
			shopId = Integer.parseInt(shopIdNode.getFirstChild().getNodeValue());
		}
		if (levelNode != null && levelNode.getFirstChild() != null) {
			level = Integer.parseInt(levelNode.getFirstChild().getNodeValue());
		}
		if (userIdNode != null && userIdNode.getFirstChild() != null) {
			userId = Integer.parseInt(userIdNode.getFirstChild().getNodeValue());
		}
		if (orderIdNode != null && orderIdNode.getFirstChild() != null) {
			orderId = Integer.parseInt(orderIdNode.getFirstChild().getNodeValue());
		}
		if (assessNode != null && assessNode.getFirstChild() != null) {
			assess = assessNode.getFirstChild().getNodeValue();
		}
		if (createTimeNode != null && createTimeNode.getFirstChild() != null) {
			createTime = createTimeNode.getFirstChild().getNodeValue();
		}
		
		AssessInfoType assessInfoType=new AssessInfoType(id, assess, shopId, level, userId, createTime, orderId);
		return assessInfoType;
    }
    public List<AssessInfoType> getAssessListByShopId(int shopId,String startTime,int maxNum) throws WSException{
    	String service = "";
		service = WSConnector.wsUrl + "getAssessListByShopId?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&shopId="+ shopId;
		if(startTime!=null){
			service+="&startTime="+startTime;
		}
		if(maxNum>0){
			service+="&maxNum="+maxNum;
		}
		
		Logger.getLogger(this.getClass()).info(
				"[getAssessListByShopId]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("assessList");
				Logger.getLogger(this.getClass()).info(
						"[getAssessListByShopId]   nodeList Size = " + nodeList.getLength());
				List<AssessInfoType> assessInfoTypes=new ArrayList<AssessInfoType>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					AssessInfoType assessInfoType =parseXmlToAssessInfoType(element);
					Logger.getLogger(this.getClass()).info(
							"[AssessInfoType]  assessInfoType = "
									+ assessInfoType.toString());
					assessInfoTypes.add(assessInfoType);
				}
				return assessInfoTypes;
			}
		}
		return null;
    }
    private OrderStateType parseXmlToOrderStateType(Element element){
    	Element orderTimeNode = (Element) element.getElementsByTagName(
				"orderTime").item(0);
		Element isFullNode = (Element) element.getElementsByTagName(
				"isFull").item(0);
		String orderTime="";
		boolean isFull=false;
		if (orderTimeNode != null && orderTimeNode.getFirstChild() != null) {
			orderTime = orderTimeNode.getFirstChild().getNodeValue();
		}
		if (isFullNode != null && isFullNode.getFirstChild() != null) {
			isFull =Boolean.parseBoolean(isFullNode.getFirstChild().getNodeValue());
		}
		OrderStateType orderStateType=new OrderStateType(orderTime, isFull);
		return orderStateType;
    }
    public List<OrderStateType> getDayOrderStateList(int searchType,int shopId,String searchTime) throws WSException, UnsupportedEncodingException {
    	String service = "";
		service = WSConnector.wsUrl + "getDayOrderStateList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken")+"&shopId="
				+ shopId+"&searchType="+searchType+"&searchTime="+URLEncoder.encode(searchTime,"UTF-8");
		Logger.getLogger(this.getClass()).info(
				"[getDayOrderStateList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("orderStateList");
				Logger.getLogger(this.getClass()).info(
						"[getDayOrderStateList]   nodeList Size = " + nodeList.getLength());
				List<OrderStateType> orderStateTypes=new ArrayList<OrderStateType>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					OrderStateType orderStateType =parseXmlToOrderStateType(element);
					Logger.getLogger(this.getClass()).info(
							"[OrderStateType]  orderStateType = "
									+ orderStateType.toString());
					orderStateTypes.add(orderStateType);
				}
				return orderStateTypes;
			}
		}
		return null;
    }

    public List<PromotionInfoType> getPromotionList(int maxNum) throws WSException{
    	String service = "";
		service = WSConnector.wsUrl + "getPromotionList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken");
		if(maxNum>0){
			service+="&maxNum="+maxNum;
		}
		Logger.getLogger(this.getClass()).info(
				"[getPromotionList]  ws query = " + service);

		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				NodeList nodeList = root.getElementsByTagName("promotionList");
				Logger.getLogger(this.getClass()).info(
						"[getPromotionList]   nodeList Size = " + nodeList.getLength());
				List<PromotionInfoType> promotionInfoTypes=new ArrayList<PromotionInfoType>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) (nodeList.item(i));
					PromotionInfoType promotionInfoType = parseXmlToBannerInfoType(element);
					Logger.getLogger(this.getClass()).info(
							"[PromotionInfoType]  promotionInfoType = "
									+ promotionInfoType.toString());
					promotionInfoTypes.add(promotionInfoType);
				}
				return promotionInfoTypes;
			}
		}
		return null;
    }
    
}
