package com.taobao.top.autosdk.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.taobao.top.autosdk.SdkConstants;
import com.taobao.top.autosdk.SdkException;
import com.taobao.top.autosdk.domain.ApiDomain;
import com.taobao.top.autosdk.domain.ApiField;
import com.taobao.top.autosdk.domain.ApiRequest;
import com.taobao.top.autosdk.domain.ApiResponse;
import com.taobao.top.autosdk.mapper.TypeMapper;
import com.taobao.top.autosdk.util.XmlUtils;

/**
 * 元数据解释器。
 * 
 * @author fengsheng
 */
public class MetaSdkParser implements SdkParser {

	private static final String URL = "http://my.open.taobao.com/apidoc/metadata.do?rf=true";

	private Element root;
	private TypeMapper mapper;

	public MetaSdkParser(TypeMapper mapper) throws SdkException {
		this.mapper = mapper;
		this.root = XmlUtils.getRootElementFromUrl(URL);
	}

	public List<ApiDomain> getApiDomains() throws Exception {
		List<ApiDomain> domains = new ArrayList<ApiDomain>();

		Element structsE = XmlUtils.getChildElement(root, "structs");
		List<Element> structEs = XmlUtils.getChildElements(structsE, "struct");
		for (Element structE : structEs) {
			ApiDomain domain = new ApiDomain();
			domain.setName(XmlUtils.getElementValue(structE, "name"));
			domain.setDesc(XmlUtils.getElementValue(structE, "desc"));

			Element propsE = XmlUtils.getChildElement(structE, "props");
			if (propsE != null) {
				List<Element> propEs = XmlUtils.getChildElements(propsE, "prop");
				for (Element propE : propEs) {
					ApiField field = new ApiField();
					field.setName(XmlUtils.getElementValue(propE, "name"));
					field.setDesc(XmlUtils.getElementValue(propE, "desc"));
					field.setApiType(XmlUtils.getElementValue(propE, "type"));
					if (mapper != null) {
						field.setLangType(mapper.getDomainLangType(field.getApiType()));
					}
					field.setApiLevel(XmlUtils.getElementValue(propE, "level"));
					domain.addField(field);
				}
			}

			if (!domains.contains(domain)) {
				domains.add(domain);
			}
		}

		return domains;
	}

	public List<ApiRequest> getApiRequests() throws Exception {
		List<ApiRequest> requests = new ArrayList<ApiRequest>();

		Element apisE = XmlUtils.getChildElement(root, "apis");
		List<Element> apiEs = XmlUtils.getChildElements(apisE, "api");
		for (Element apiE : apiEs) {
			ApiRequest request = new ApiRequest();
			request.setName(XmlUtils.getElementValue(apiE, "name"));
			request.setDesc(XmlUtils.getElementValue(apiE, "desc"));

			Element requestE = XmlUtils.getChildElement(apiE, "request");
			if (requestE != null) {
				List<Element> paramEs = XmlUtils.getChildElements(requestE, "param");
				for (Element paramE : paramEs) {
					ApiField field = new ApiField();
					field.setName(XmlUtils.getElementValue(paramE, "name"));
					field.setDesc(XmlUtils.getElementValue(paramE, "desc"));
					String level = XmlUtils.getElementValue(paramE, "level");
					if (SdkConstants.TYPE_BASIC_ARRAY.equals(level)) { // 基本数组型入参其实也是字符串类型
						field.setApiType("String");
					} else {
						field.setApiType(XmlUtils.getElementValue(paramE, "type"));
					}
					if (mapper != null) {
						field.setLangType(mapper.getRequestLangType(field.getApiType()));
					}
					request.addField(field);
				}
			}

			if (!requests.contains(request)) {
				requests.add(request);
			}
		}

		return requests;
	}

	public List<ApiResponse> getApiResponses() throws Exception {
		List<ApiResponse> responses = new ArrayList<ApiResponse>();

		Element apisE = XmlUtils.getChildElement(root, "apis");
		List<Element> apiEs = XmlUtils.getChildElements(apisE, "api");
		for (Element apiE : apiEs) {
			ApiResponse response = new ApiResponse();
			response.setName(XmlUtils.getElementValue(apiE, "name"));
			response.setDesc(XmlUtils.getElementValue(apiE, "desc"));

			Element responseE = XmlUtils.getChildElement(apiE, "response");
			if (responseE != null) {
				List<Element> paramEs = XmlUtils.getChildElements(responseE, "param");
				for (Element paramE : paramEs) {
					ApiField field = new ApiField();
					field.setName(XmlUtils.getElementValue(paramE, "name"));
					field.setDesc(XmlUtils.getElementValue(paramE, "desc"));
					field.setApiType(XmlUtils.getElementValue(paramE, "type"));
					if (mapper != null) {
						field.setLangType(mapper.getResponseLangType(field.getApiType()));
					}
					field.setApiLevel(XmlUtils.getElementValue(paramE, "level"));
					response.addField(field);
				}
			}

			if (!responses.contains(response)) {
				responses.add(response);
			}
		}

		return responses;
	}

}
