package mini.springframework.aop;

import lombok.Getter;
import mini.springframework.aop.aspect.AspectInfo;
import mini.springframework.util.ValidationUtils;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

/**
 * 向被代理对象添加横切逻辑
 *
 * @author daffupman
 * @since 2020/6/13
 */
public class AspectListExecutor implements MethodInterceptor {

    /**
     * 被代理的类
     */
    private final Class<?> targetClass;
    /**
     * 给被代理类织入的切面，已按order排好序
     */
    @Getter
    private final List<AspectInfo> sortedAspectInfoList;

    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList);
    }

    /**
     * 按照order的顺序从小到大排序
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {
        aspectInfoList.sort(Comparator.comparingInt(AspectInfo::getOrderIndex));
        return aspectInfoList;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        Object returnValue = null;
        collectAccurateMatchedAspectList(method);
        if (ValidationUtils.isEmpty(sortedAspectInfoList)) {
            return methodProxy.invokeSuper(proxy, args);
        }

        // 按照order的顺序升序执行所有before方法
        invokeBeforeAdvices(method, args);
        try {
            // 执行被代理类的方法
            returnValue = methodProxy.invokeSuper(proxy, args);
            // 如果代理类正常返回，则按order顺序倒序执行所有afterReturning方法
            returnValue = invokeAfterReturningAdvices(method, args, returnValue);
        } catch (Exception e) {
            // 如果代理类异常返回，则按order顺序倒序执行所有afterThrowing方法
            invokeAfterThrowingAdvices(method, args, e);
        }
        return returnValue;
    }

    private void collectAccurateMatchedAspectList(Method method) {
        if (ValidationUtils.isEmpty(this.sortedAspectInfoList)) { return; }
        // 移除不精确匹配的AspectInfo
        this.sortedAspectInfoList.removeIf(each -> !each.getPointcutLocator().accurateMatches(method));
    }

    // 如果代理类异常返回，则按order顺序倒序执行所有afterThrowing方法
    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {
        for (int i =  sortedAspectInfoList.size() - 1; i >=0 ; i--){
            sortedAspectInfoList.get(i).getAspectObject().afterThrowing(targetClass, method, args, e);
        }
    }

    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
        Object result = null;
        // 如果代理类正常返回，则按order顺序倒序执行所有afterReturning方法
        for (int i = this.sortedAspectInfoList.size() - 1; i >= 0; i --) {
            result = sortedAspectInfoList.get(i).getAspectObject().afterReturning(
                    targetClass, method, args, returnValue
            );
        }
        return result;
    }

    // 按照order的顺序升序执行所有before方法
    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo : this.sortedAspectInfoList) {
            aspectInfo.getAspectObject().before(targetClass, method, args);
        }
    }
}
