package com.lwl.anno;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lwl.Constant;
import io.netty.util.internal.ObjectUtil;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Aspect
@Component
@Lazy(false)
public class RabbitExecutorErrorRoutingAspect {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Pointcut("@annotation(com.lwl.anno.RabbitExecutorErrorRouting)")
    public void rabbitExecutorErrorRouting() {

    }

    @Around("rabbitExecutorErrorRouting()")
    public void around(ProceedingJoinPoint joinPoint) throws Exception {
        String methodName = joinPoint.getSignature().getName();
        RabbitExecutorErrorRouting routing = getDeclaredAnnotation(joinPoint);

        Tuple2<Throwable, Object> tuple2 = executorResult(joinPoint);
        if (null == tuple2._1) {
            return;
        }

        // 构建错误消息信息，然后发送
        JSONObject json = new JSONObject();
        json.put("methodName", methodName);
        json.put("desc", routing.desc());
        json.put("exception", getStackMsg(tuple2._1));
        json.put("params", JSON.toJSONString(tuple2._2));

        MessageProperties properties = new MessageProperties();
        properties.setContentType(Constant.APPLICATION_JSON);

        Message message = new Message(json.toJSONString().getBytes(StandardCharsets.UTF_8), properties);
        rabbitAdmin.getRabbitTemplate().send(routing.exchange(), routing.routingKey(), message);
    }
    private Tuple2<Throwable, Object> executorResult(ProceedingJoinPoint joinPoint) {

        Throwable throwable = null;
        Object[] params = null;
        try {
            params = joinPoint.getArgs();
            joinPoint.proceed();
        } catch (Throwable e) {
            throwable = e;
        }
        return new Tuple2<>(throwable,params);
    }

    public RabbitExecutorErrorRouting getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        RabbitExecutorErrorRouting annotation = method.getAnnotation(RabbitExecutorErrorRouting.class);
        return annotation;
    }

    private static JSONArray getStackMsg(Throwable e) {
        JSONArray retArray = new JSONArray();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            retArray.add(stackArray[i].toString());
        }
        return retArray;
    }
}
