package cn.fooltech.fool_ops.config.errors;

import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.component.exception.LoginTimeOutException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@ControllerAdvice
class GlobalExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";
    public static final String LOGIN_TIMEOUT_ERROR_VIEW = "error/timeout";
    public static final String NODATA_ERROR_VIEW = "error/nodata";

    /**
     * 登录超时
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = LoginTimeOutException.class)
    public ModelAndView loginTimeOutErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName(LOGIN_TIMEOUT_ERROR_VIEW);
        return mav;
    }

    /**
     * 没数据
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = DataNotExistException.class)
    public ModelAndView nodataHandler(HttpServletRequest request, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName(NODATA_ERROR_VIEW);
        return mav;
    }
}