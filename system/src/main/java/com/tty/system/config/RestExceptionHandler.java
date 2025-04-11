package com.tty.system.config;


import com.tty.common.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public JsonResult methodError(){
        return JsonResult.ERROR(405, "请求方法错误");
    }
    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult resourceIsNull(){
        return JsonResult.ERROR(404, "路径不存在");
    }
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult notFound(){
        return JsonResult.ERROR(404, "路径不存在");
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult authentication(Exception e){
        log.error("error!", e);
        return JsonResult.ERROR(500, "服务器繁忙");
    }
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult parameterTypeError(){
        return JsonResult.ERROR(400, "参数类型错误！");
    }
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult notReadable(){
        return JsonResult.ERROR(400, "服务器没有接收到任何的对象参数");
    }
    @ExceptionHandler(value = MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult noMultipartFile(){
        return JsonResult.ERROR(400, "服务器没有接收到文件");
    }
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult mediaTypeNotSupport() {
        return JsonResult.ERROR(400, "参数类型错误！");
    }
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult bindException() {
        return JsonResult.ERROR(400, "参数类型错误！");
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult parameterIsNull(){
        return JsonResult.ERROR(400, "无效的参数请求");
    }
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult missingParameter(){
        return JsonResult.ERROR(400, "请求缺少必要参数");
    }
    @ExceptionHandler(value = MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult missingHeader(){
        return JsonResult.ERROR(400, "请求缺少必要Header参数");
    }
    @ExceptionHandler(value = MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult missingPatch(){
        return JsonResult.ERROR(400, "请求缺少必要path参数");
    }
    @ExceptionHandler(value = MyBatisSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult myBatisSystemException(Exception e) {
        return JsonResult.ERROR(500, e.getMessage());
    }
}