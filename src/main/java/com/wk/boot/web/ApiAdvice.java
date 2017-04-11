package com.wk.boot.web;

import com.wk.boot.util.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by gaige on 2017/4/10.
 */
@RestControllerAdvice
public class ApiAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAdvice.class);

    @ExceptionHandler
    public Msg handleException(Exception e) {

        LOGGER.error("handleException", e);
        return Msg.error(e);
    }

    @InitBinder
    public void bind(WebDataBinder dataBinder) {

    }
}
