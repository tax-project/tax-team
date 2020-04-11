package com.dkm.handle;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author qf
 * @Date 2019/8/31
 * @Version 1.0
 */
@RestControllerAdvice
public class ApplicationAdviceHandle extends ResponseEntityExceptionHandler {


    /**
     * 自定义返回异常
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(value = ApplicationException.class)
    public ApplicationException applicationExceptionHandle (final ApplicationException e, HttpServletResponse response) {
//        response.setStatus(e.getHttpStatus());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-CSRF-TOKEN, X-Requested-With, Content-Type, Accept");
        return e;
    }

    /**
     * 其他异常
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ApplicationException exceptionHandle (final ApplicationException e, HttpServletResponse response) {
        response.setStatus(CodeType.UNKNOWN_ERROR.getHttpStatus());
        e.printStackTrace();
        return new ApplicationException(CodeType.UNKNOWN_ERROR);
    }
}
