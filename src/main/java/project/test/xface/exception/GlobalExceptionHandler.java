//package project.test.xface.exception;
//
//import cn.dev33.satoken.util.SaResult;
//import lombok.extern.slf4j.Slf4j;
//import project.test.xface.common.ErrorCode;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * 全局异常处理器
// */
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(BusinessException.class)
//    public SaResult businessExceptionHandler(BusinessException e) {
//        log.error("BusinessException", e);
//        return Result.error(e.getCode(), e.getMessage());
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public SaResult runtimeExceptionHandler(RuntimeException e) {
//        log.error("RuntimeException", e);
//        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
//    }
//}