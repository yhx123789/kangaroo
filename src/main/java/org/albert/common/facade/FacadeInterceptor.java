package org.albert.common.facade;

import com.alibaba.fastjson.JSONObject;
import org.albert.common.constant.string.ConstPunctuation;
import org.albert.common.constant.string.ConstString;
import org.albert.common.domain.RequestInRPC;
import org.albert.common.domain.Response;
import org.albert.common.domain.annotation.HandleStringParameter;
import org.albert.common.util.RunningEnvironment;
import org.albert.common.util.StringUtils;
import org.albert.common.util.ThreadLocalUtil;
import org.albert.common.util.ValidationUtil;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.kangaroo.common.ThreadContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;

/**
 * Facade拦截器
 * Created by Albert on 2017/03/30.
 */
public class FacadeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FacadeInterceptor.class);

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    	logger.info("开始aop切面增强方法");
        Object[] requestParameters = pjp.getArgs();
        Object object = pjp.getTarget();
        Signature signature = pjp.getSignature();
        Method method = getCurrentMethodInInterface(object, signature);

        Object response = new Response<Boolean>();
        try {
            validateParams(object, method, requestParameters);
            setAppKeyToThreadLocal(requestParameters);
            response = doBasicProfiling(pjp, object, requestParameters, method);
        } catch (Throwable throwable) {
            response = new Response(throwable, requestParameters);
        }
        return response;
    }


    /**
     * 检验方法的入参
     *
     * @param object
     * @param method
     * @param requestParameters
     */
    private void validateParams(Object object, Method method, Object[] requestParameters) {
        ValidationUtil.getInstance().validateParams(object, method, requestParameters);
    }


    /**
     * 获取具体的实现类的方法
     *
     * @param object
     * @param signature
     * @return
     */
    private Method getCurrentMethodInImplClass(Object object, Signature signature) throws NoSuchMethodException {
        MethodSignature msig = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) signature;
        Method currentMethod = object.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        return currentMethod;
    }


    /**
     * 获取对应接口的方法
     *
     * @param object
     * @param signature
     * @return
     */
    private Method getCurrentMethodInInterface(Object object, Signature signature) throws NoSuchMethodException {
        MethodSignature ms = (MethodSignature) signature;
        Method currentMethod = ms.getMethod();
        return currentMethod;
    }

    /**
     * 把AppKey设置到ThreadLocal中,使用ThreadLocal是为了屏蔽不同线程之间对变量值的干扰
     *
     * @param requestParameters
     */
    private void setAppKeyToThreadLocal(Object[] requestParameters) {
        String traceId = getTraceId(requestParameters);
        MDC.put(ConstString.TRACE_ID, traceId);
        if (traceId != null) {
            //设置traceID到线程变量中，用于下层隐式传递
            ThreadLocalUtil.setTraceId(traceId);
            //编码规范规定traceId用"&"进行拼接
            String[] strArry = traceId.split(ConstPunctuation.LINK);
            if (strArry != null && strArry.length > 0) {
                String appKey = strArry[0];
                logger.info("ThreadContextHolder.set(appKey = {});",appKey);
                ThreadContextHolder.set(appKey);
                ThreadLocalUtil.setAppKey(appKey);
            }
        }
    }


    private String getTraceId(Object[] args) {
        String traceId = "";
        if (args != null && args.length > 0) {
            if (args.length == 1 && args[0] instanceof RequestInRPC) {
                RequestInRPC requestInRPC = (RequestInRPC) args[0];
                traceId = requestInRPC.getTraceId();
            } else {
                //编码规范规定,traceId为最后一个参数
                Object arg = args[args.length - 1];
                if (arg instanceof String) {
                    traceId = (String) arg;
                }
            }
        }
        return traceId;
    }


    public Object doBasicProfiling(ProceedingJoinPoint pjp, Object object, Object[] parameterValues, Method method)
            throws Throwable {
        Long startTime = System.currentTimeMillis();
        String methodName = this.getMethodName(pjp);
        HandleStringParameter annotation = method.getAnnotation(HandleStringParameter.class);
        if (annotation != null) {
            handleInParam(pjp);
        }
        //开始执行真正要被调用的方法
        Object returnDate = pjp.proceed(pjp.getArgs());
        //打印日志
        this.printLog(methodName, parameterValues, returnDate, startTime);
        return returnDate;
    }

    /**
     * 处理入参,去空格
     *
     * @param pjp 切面对象
     */
    private void handleInParam(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            args[i] = StringUtils.removeEmpty(args[i]);
        }
    }


    /**
     * 打印日志
     *
     * @param returnDate
     * @param startTime
     */
    private void printLog(String methodName, Object[] requestParameters, Object returnDate, Long startTime) {
        Long endTime = System.currentTimeMillis();
        String requestParametersString = this.getParams(requestParameters);
        String returnStr = returnDate != null ? JSONObject.toJSONString(returnDate) : "没有返回值";
        int threshold = 1500;
        if (RunningEnvironment.isProd() || RunningEnvironment.isBeta()) {
            //太长的请求进行截断,否则日志会打的太多
            if (requestParametersString.length() > threshold) {
                requestParametersString = requestParametersString.substring(0, threshold);
            }
            //太长的返回值进行截断,否则日志会打的太多
            if (returnStr.length() > threshold) {
                returnStr = returnStr.substring(0, threshold);
            }
        }
        String time = "" + (endTime - startTime) + "ms";
        if (logger.isInfoEnabled()) {
            logger.info("\n===Method:{}============\nRequest:{}.\nResponse:{}.\nElapsed:{}" +
                            "\n==================================================================",
                    methodName,
                    requestParametersString, returnStr, time);
        }
    }


    private String getMethodName(ProceedingJoinPoint pjp) {
        String method = pjp.getStaticPart().getSignature().getDeclaringType().getName();
        String name = pjp.getStaticPart().getSignature().getName();
        return method + "." + name;
    }

    private String getParams(Object[] args) {
        StringBuffer parm = new StringBuffer("(");
        if (ArrayUtils.isEmpty(args)) {
            parm.append("无参数");
            return parm.toString();
        }
        for (Object arg : args) {
            parm.append(arg).append(",");
        }
        parm.append(")");
        return parm.toString();
    }


}