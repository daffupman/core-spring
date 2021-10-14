package mini.springframework.mvc.processor.imp;

import lombok.extern.slf4j.Slf4j;
import mini.springframework.core.BeanContainer;
import mini.springframework.mvc.RequestProcessorChain;
import mini.springframework.mvc.annotation.RequestMapping;
import mini.springframework.mvc.annotation.RequestParam;
import mini.springframework.mvc.annotation.ResponseBody;
import mini.springframework.mvc.processor.RequestProcessor;
import mini.springframework.mvc.render.JsonResultRender;
import mini.springframework.mvc.render.ResourceNotFoundResultRender;
import mini.springframework.mvc.render.ResultRender;
import mini.springframework.mvc.render.ViewResultRender;
import mini.springframework.mvc.type.ControllerMethod;
import mini.springframework.mvc.type.RequestPathInfo;
import mini.springframework.util.ConvertUtils;
import mini.springframework.util.ValidationUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller请求处理器
 * - 针对特定请求，选择匹配的Controller方法进行处理
 * - 解析请求里的参数及其对应的值，并赋值给Controller方法的参数
 * - 选择合适的Render，为后续的请求处理结果的渲染做准备
 *
 * @author daffupman
 * @since 2020/6/14
 */
@Slf4j
public class ControllerRequestProcessor implements RequestProcessor {

    // ioc容器
    private BeanContainer beanContainer;
    // 请求和controller方法的映射集合
    private Map<RequestPathInfo, ControllerMethod> pathControllerMethodMap = new ConcurrentHashMap<>();

    /**
     * 依然容器的能力，建立起请求路径、请求方法与Controller方法实例的映射
     */
    public ControllerRequestProcessor() {
        this.beanContainer = BeanContainer.getInstance();
        Set<Class<?>> requestMappingSet = beanContainer.getClassesByAnnotation(RequestMapping.class);
        initPathControllerMethodMap(requestMappingSet);
    }

    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
        if (ValidationUtils.isEmpty(requestMappingSet)) {
            return;
        }
        // 1、遍历所有被@RequestMapping标记的类，获取类上面该注解的属性值作为一级路径
        for (Class<?> requestMappingClass : requestMappingSet) {
            RequestMapping rm = requestMappingClass.getAnnotation(RequestMapping.class);
            String basePath = rm.value();
            if (basePath.startsWith("/")) {
                basePath = "/" + basePath;
            }
            // 2、遍历类里所有被@RequestMapping标记的方法，获取方法上面该注解的属性值，作为二级路径
            Method[] methods = requestMappingClass.getDeclaredMethods();
            if (ValidationUtils.isEmpty(methods)) {
                continue;
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    String methodPath = methodRequestMapping.value();
                    if (!methodPath.startsWith("/")) {
                        methodPath = "/" + methodPath;
                    }
                    String url = basePath + methodPath;
                    // 3、解析方法里被@RequestParam标记的参数。
                    // 获取该注解的属性值作为参数名，
                    // 获取被标记的参数的数据类型，建立参数名和参数类型的映射
                    Map<String, Class<?>> methodParams = new HashMap<>();
                    Parameter[] parameters = method.getParameters();
                    if (!ValidationUtils.isEmpty(parameters)) {
                        for (Parameter parameter : parameters) {
                            RequestParam param = parameter.getAnnotation(RequestParam.class);
                            if (param == null) {
                                throw new RuntimeException("The parameter must have @RequestParam");
                            }
                            methodParams.put(param.value(), parameter.getType());

                        }
                    }
                    // 4、将获取到的信息封装成RequestPathInfo实例和ControllerMethod实例，放置到映射表里
                    String httpMethod = methodRequestMapping.method().toString();
                    RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod, url);
                    if (this.pathControllerMethodMap.containsKey(requestPathInfo)) {
                        log.warn("duplicate url: {} registration, current class {}, method {} will override the former one",
                                requestPathInfo.getHttpPath(), requestMappingClass.getName(), method.getName());
                    }
                    ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass, method, methodParams);
                    pathControllerMethodMap.put(requestPathInfo, controllerMethod);
                }
            }
        }

    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 1、解析HttpServletRequest的请求方法，请求路径，获取对应的ControllerMethod实例
        String method = requestProcessorChain.getRequestMethod();
        String path = requestProcessorChain.getRequestPath();
        ControllerMethod controllerMethod = pathControllerMethodMap.get(new RequestPathInfo(method, path));
        if (controllerMethod == null) {
            requestProcessorChain.setResultRender(new ResourceNotFoundResultRender(method, path));
        }
        // 2、解析请求参数，并传递给获取到的ControllerMethod实例去执行
        Object result = invokeControllerMethod(controllerMethod, requestProcessorChain.getRequest());
        // 3、根据解析的结果，选择对应的render进行渲染
        setResultRender(result, controllerMethod, requestProcessorChain);
        return true;
    }

    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
        if (result == null) {
            return;
        }
        ResultRender resultRender;
        boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            resultRender = new JsonResultRender(result);
        } else {
            resultRender = new ViewResultRender(result);
        }
        requestProcessorChain.setResultRender(resultRender);
    }

    // 解析请求参数，并传递给获取到的ControllerMethod实例去执行
    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) {
        // 1、从请求里获取GET或POST的参数名及其对应的值
        Map<String, String> requestParamMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> paramEntry : parameterMap.entrySet()) {
            if (ValidationUtils.isEmpty(paramEntry.getValue())) {
                // 目前只支持一个参数对应一个值得形式
                requestParamMap.put(paramEntry.getKey(), paramEntry.getValue()[0]);
            }
        }
        // 2、根据获取到的请求参数名及其对应的值，以及controllerMethod里面的参数和类型的关系，去实例化出方法对应的参数
        List<Object> methodParams = new ArrayList<>();
        Map<String, Class<?>> methodParamMap = controllerMethod.getMethodParameters();
        for (Map.Entry<String, Class<?>> methodParamEntry : methodParamMap.entrySet()) {
            String paramName = methodParamEntry.getKey();
            Class<?> type = methodParamEntry.getValue();
            String requestValue = requestParamMap.get(paramName);
            Object value;
            // 只支持String，以及基础类型char,int byte,short,long,boolean,double,float及其包装类
            if (requestValue == null) {
                value = ConvertUtils.primitiveNull(type);
            } else {
                value = ConvertUtils.convert(type, requestValue);
            }
            methodParams.add(value);
        }
        // 3、执行Controller里面对应的方法并返回结果
        Object controller = beanContainer.getBean(controllerMethod.getControllerClass());
        Method invokeMethod = controllerMethod.getInvokeMethod();
        invokeMethod.setAccessible(true);
        Object result;
        try {
            if (methodParams.size() == 0) {
                result = invokeMethod.invoke(controller);
            } else {
                result = invokeMethod.invoke(controller, methodParams.toArray());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
        return result;
    }
}
