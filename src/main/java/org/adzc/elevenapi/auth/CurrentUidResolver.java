package org.adzc.elevenapi.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUidResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUid.class)
                && (parameter.getParameterType() == Long.class || parameter.getParameterType() == long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        Object v = req.getAttribute("auth.uid");
        if (v == null) throw new RuntimeException("Unauthorized: uid not found");
        if (v instanceof Long) return v;
        if (v instanceof Integer) return ((Integer) v).longValue();
        if (v instanceof String) return Long.parseLong((String) v);
        throw new RuntimeException("Unauthorized: invalid uid type");
    }
}
