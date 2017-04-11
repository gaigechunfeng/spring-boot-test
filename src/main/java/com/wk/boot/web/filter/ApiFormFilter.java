package com.wk.boot.web.filter;

import com.wk.boot.util.Msg;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by gaige on 2017/4/11.
 */
public class ApiFormFilter extends FormAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiFormFilter.class);

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {

        if (!isJsonRequest(request, response)) {
            super.onLoginSuccess(token, subject, request, response);
            return false;
        }
        return true;
    }

    private boolean isJsonRequest(ServletRequest request, ServletResponse response) {

        HttpServletRequest req = (HttpServletRequest) request;
        String s = req.getHeader("X-Requested-With");

        return "XMLHttpRequest".equalsIgnoreCase(s);
    }
}
