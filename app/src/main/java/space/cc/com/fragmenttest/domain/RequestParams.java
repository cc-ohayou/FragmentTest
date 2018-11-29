package space.cc.com.fragmenttest.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: RequestParams
 * @Description: 请求参数对象
 * @author yaojian
 * @date 2015-11-6 下午1:29:02
 */
public class RequestParams {

	public final static int PARAM_TYPE_JSON = 0;

	public final static int PARAM_TYPE_FORM = 1;

	private int mParamType;
	private Map<String, Object> mParams;

	public RequestParams(int paramType) {
		this(paramType, new HashMap<String, Object>());
	}

	public RequestParams(int paramType, Map<String, Object> params) {
		setParamType(paramType);
		mParams = params;
	}

	public int getParamType() {
		return mParamType;
	}

	public JSONObject getParamsJson() {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(getParamsString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	public void setParamType(int mParamType) {
		this.mParamType = mParamType;
	}

	public void put(String key, Object value) {
		mParams.put(key, value);
	}

	public Map<String, Object> getParams() {
		return mParams;
	}

	public String getParamsString() {
		String paramsString = "";

		StringBuilder sb = new StringBuilder();
		if (mParamType == PARAM_TYPE_FORM) {
			if (mParams.size() > 0) {
				for (Map.Entry<String, Object> entry : mParams.entrySet()) {
					if (sb.length() > 0)
						sb.append("&");

					sb.append(entry.getKey());
					sb.append("=");
					sb.append(entry.getValue());
				}
				paramsString = sb.toString();
			}
		} else if (mParamType == PARAM_TYPE_JSON) {

			JSONObject jsonObject = new JSONObject(mParams);
			paramsString = jsonObject.toString();
		}
		//可以在这里添加报文加密

		return paramsString;
	}

}
