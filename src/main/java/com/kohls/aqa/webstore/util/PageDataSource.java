package com.kohls.aqa.webstore.util;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kohls.aqa.webstore.elements.*;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.testng.databinding.DataSource;
import net.sf.testng.databinding.IDataSource;
import net.sf.testng.databinding.TestInput;
import net.sf.testng.databinding.TestOutput;
import net.sf.testng.databinding.core.error.ErrorCollector;
import net.sf.testng.databinding.core.error.MissingPropertiesException;
import net.sf.testng.databinding.core.error.MultipleConfigurationErrorsException;
import net.sf.testng.databinding.core.error.MultipleSourceErrorsException;
import net.sf.testng.databinding.util.MethodParameter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

import static java.util.Arrays.asList;
import static net.sf.testng.databinding.core.util.Types.*;

@DataSource(name = "PageJSON")
public class PageDataSource implements IDataSource {

	public static final String COMMON_DATA_KEY = "commonData";
	public static final String INCLUDE_KEY = "include";
	public static final String ELEMENTS_KEY = "elements";
	public static final String TYPE_KEY = "type";
	public static final String URL_KEY = "url";

	private List<Object> elements = new LinkedList<>();

	private Object params;

	private JSONObject json;

	private Properties properties;

	private List<MethodParameter> methodParameters;

	public PageDataSource(final List<MethodParameter> methodParameters, final Properties properties) {
		this.setMethodParameters(methodParameters);
		this.setProperties(properties);

		this.params = this.extractCommonParams(this.json);
		this.elements = this.extractElements(this.json);
		this.elements.addAll(this.extractIncludeElements(this.json));
	}

	@Override
	public boolean hasNext() {
		return !this.elements.isEmpty();
	}

	@Override
	public Object[] next() {
		if (this.elements.isEmpty()) {
			throw new NoSuchElementException();
		}
		return new Object[] { this.params, this.elements.remove(0) };
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("cannot remove test parameters");
	}

	private List<Object> extractIncludeElements(JSONObject data) {
		List<Object> elements = new ArrayList<>();
		if (data.has(INCLUDE_KEY)) {
			JSONArray urls = data.optJSONArray(INCLUDE_KEY);
			Map<String, Object> parameters = this.getParameters();
			for (int i = 0; i < urls.length(); i++) {
				String url = urls.optString(i);
				String jsonStr = this.getDataString(url, parameters);
				JSONObject json = new JSONObject(jsonStr);
				elements.addAll(this.extractElements(json));
			}
		}
		return elements;
	}

	private CommonParams extractCommonParams(JSONObject data) {
		if (data.has(COMMON_DATA_KEY)) {
			JSONObject commonData = data.optJSONObject(COMMON_DATA_KEY);
			return (CommonParams) this.getBean(CommonParams.class, commonData);
		} else {
			return new CommonParams();
		}
	}

	private List<Object> extractElements(JSONObject data) {
		if (data.has(ELEMENTS_KEY)) {
			List<Object> elements = new ArrayList<>();
			JSONArray elementsArray = data.optJSONArray(ELEMENTS_KEY);
			for (int i = 0; i < elementsArray.length(); i++) {
				JSONObject elementObject = elementsArray.optJSONObject(i);
				elements.add(this.getData(Element.class, elementObject));
			}
			return elements;
		} else {
			this.throwMSEException(COMMON_DATA_KEY, "has no elements");
		}
		return null;
	}

	private Object getData(Type type, Object data) {
		if (type == Element.class) {
			JSONObject jsonData = (JSONObject) data;
			if (jsonData.has(TYPE_KEY)) {
				String elementTypeStr = jsonData.optString(TYPE_KEY);
				ElementType elementType = ElementType.valueOf(elementTypeStr);
				switch (elementType) {
				case BUTTON:
					return this.getBean(WButton.class, jsonData);
				case CHECKBOX:
					return this.getBean(WCheckbox.class, jsonData);
				case IMAGE:
					return this.getBean(WImage.class, jsonData);
				case HYPER_LINK:
					return this.getBean(WHyperLink.class, jsonData);
				case LIST:
					return this.getBean(WList.class, jsonData);
				case LIST_OF_LINKS:
					return this.getBean(WListOfLinks.class, jsonData);
				case MONTH_SELECT:
					return this.getBean(WMonthSelect.class, jsonData);
				case SELECT:
					return this.getBean(WSelect.class, jsonData);
				case TEXT:
					return this.getBean(WText.class, jsonData);
				case TEXT_INPUT:
					return this.getBean(WTextInput.class, jsonData);
				case TITLE:
					return this.getBean(WTitle.class, jsonData);
				case YEAR_DAY_SELECT:
					return this.getBean(WDayYearSelect.class, jsonData);
				default:
					throw new RuntimeException("unknown type of element");
				}
			} else {
				throw new RuntimeException("element provides no type");
			}
		} else if (isEnumType(type)) {
			return this.getEnum(type, data);
		} else if (isPrimitiveType(type)) {
			return this.getPrimitive(type, data);
		} else if (isBigDecimal(type)) {
			return BigDecimal.valueOf(Double.valueOf((String) data));
		} else if (isSingleBeanType(type)) {
			return this.getBean(type, (JSONObject) data);
		} else if (isListOfPrimitivesType(type)) {
			return this.getPrimitiveList(type, data);
		} else if (isListOfBeansType(type)) {
			return this.getBeanList(type, data);
		} else {
			throwMSEException(type.toString(), "unsupported param type");
		}
		return null;
	}

	private Map<String, Object> getParameters() {
		Map<String, Object> data = new HashMap<>();
		if (this.properties.containsKey(COMMON_DATA_KEY)) {
			Map<String, Object> commonParameters = this.getCommonParameters();
			data.putAll(commonParameters);
		}
		return data;
	}

	private Map<String, Object> getCommonParameters() {
		String url = (String) this.properties.get(COMMON_DATA_KEY);
		InputStream is = this.getClass().getResourceAsStream(url);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		Type srcType = new TypeToken<Map<String, Object>>() {
		}.getType();
		return new Gson().fromJson(br, srcType);
	}

	private boolean isBigDecimal(Type type) {
		return (type instanceof Class<?>) && (type == BigDecimal.class);
	}

	private List<Object> getBeanList(Type paramType, Object data) {
		List<Object> beans = new LinkedList<>();
		ParameterizedType parameterizedType = (ParameterizedType) paramType;
		Type[] typeArguments = parameterizedType.getActualTypeArguments();
		Type type = typeArguments[0];
		JSONArray array = (JSONArray) data;
		for (int i = 0; i < array.length(); i++) {
			beans.add(this.getBean(type, array.optJSONObject(i)));
		}
		return beans;
	}

	private Object getPrimitiveList(Type paramType, Object data) {
		List<Object> primitives = new LinkedList<>();
		Type type = ((ParameterizedType) paramType).getActualTypeArguments()[0];
		JSONArray array = (JSONArray) data;
		for (int i = 0; i < array.length(); i++) {
			primitives.add(this.getPrimitive(type, array.opt(i)));
		}
		return primitives;
	}

	@SuppressWarnings({ "unchecked" })
	private Object getBean(Type paramType, JSONObject data) {
		try {
			Class<?> clazz = (Class<?>) paramType;
			BeanInfo info = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
			Map<String, PropertyDescriptor> fields = this.getFields(descriptors);
			Object bean = clazz.newInstance();

			Iterator<String> keysIterator = data.keys();
			while (keysIterator.hasNext()) {
				String key = keysIterator.next();
				if (fields.containsKey(key)) {
					PropertyDescriptor pd = fields.get(key);
					Type type;
					if (pd.getPropertyType().getTypeParameters().length > 0) {
						type = pd.getWriteMethod().getGenericParameterTypes()[0];
					} else {
						type = pd.getPropertyType();
					}
					Object value = this.getData(type, data.opt(key));
					pd.getWriteMethod().invoke(bean, value);
				}
			}

			return bean;
		} catch (IntrospectionException | ReflectiveOperationException e) {
			String msg = "unable to create bean: " + e.getMessage();
			this.throwMSEException(paramType.toString(), msg);
		}
		return null;
	}

	private Object getPrimitive(Type type, Object data) {
		if (type.equals(String.class)) {
			return data;
		} else if (type == Integer.class || type == int.class) {
			return ((Integer) data).intValue();
		} else if (type == Long.class || type == long.class) {
			return ((Integer) data).longValue();
		} else if (type == Float.class || type == float.class) {
			return ((Double) data).floatValue();
		} else if (type == Double.class || type == double.class) {
			return ((Double) data).doubleValue();
		} else if (type == Boolean.class || type == boolean.class) {
			return ((Boolean) data).booleanValue();
		}
		throw new RuntimeException();
	}

	private Object getEnum(Type type, Object data) {
		for (Field field : ((Class<?>) type).getFields()) {
			try {
				if (field.getName().equals(data)) {
					return field.get(null);
				}
			} catch (IllegalAccessException ignored) {
			
			}
		}
		return null;
	}

	private String extractParameterName(MethodParameter param) {
		TestInput inputAnnotation = param.getAnnotation(TestInput.class);
		if (inputAnnotation != null) {
			return inputAnnotation.name();
		}
		TestOutput outputAnnotation = param.getAnnotation(TestOutput.class);
		if (outputAnnotation != null) {
			return outputAnnotation.name();
		}
		throw new RuntimeException();
	}

	private Map<String, PropertyDescriptor> getFields(PropertyDescriptor[] descriptors) {
		Map<String, PropertyDescriptor> candidates = new HashMap<>();
		for (PropertyDescriptor descriptor : descriptors) {
			if (null != descriptor.getWriteMethod()) {
				candidates.put(descriptor.getName(), descriptor);
			}
		}
		return candidates;
	}

	private void setProperties(final Properties properties) {
		this.properties = properties;

		String url;
		if (!properties.containsKey(URL_KEY)) {
			throw new MissingPropertiesException(URL_KEY);
		} else {
			url = (String) properties.get(URL_KEY);
		}

		String jsonSource = this.getDataString(url, this.getParameters());
		try {
			this.json = new JSONObject(jsonSource);
		} catch (JSONException e) {
			this.throwMSEException(url, "Error during JSON parsing");
		}
		if (this.json.length() == 0) {
			this.throwMSEException(url, "JSON doesn't contain data");
		}
	}

	private void setMethodParameters(final List<MethodParameter> parameters)
			throws MultipleConfigurationErrorsException {
		if (parameters.isEmpty()) {
			ErrorCollector ec = new ErrorCollector("method methodParameters");
			ec.addError("no parameters with @TestInput or @TestOutput given");
			throw new MultipleConfigurationErrorsException(asList(ec));
		}
		this.methodParameters = parameters;
	}

	private String getDataString(final String url, final Map<String, Object> parameters) {
		if (parameters != null && !parameters.isEmpty()) {
			try {
				Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(this.getClass(), "/");
				configuration.setObjectWrapper(new DefaultObjectWrapper());
				configuration.setWhitespaceStripping(true);

				StringWriter writer = new StringWriter();
				Template template = configuration.getTemplate(url);
				template.process(parameters, writer);
				return writer.toString();
			} catch (IOException e) {
				this.throwMSEException(url, "Error during file reading");
			} catch (TemplateException e) {
				this.throwMSEException(url, "Error during parametrization");
			}
		} else {
			try (InputStream is = this.getClass().getResourceAsStream(url)) {
				return CharStreams.toString(new InputStreamReader(is, "UTF-8"));
			} catch (IOException e) {
				this.throwMSEException(url, "Error during file reading");
			}
		}
		return null;
	}

	private void throwMSEException(String targetName, String message) throws MultipleSourceErrorsException {
		ErrorCollector error = new ErrorCollector(targetName);
		error.addError(message);
		throw new MultipleSourceErrorsException(asList(error));
	}
}
