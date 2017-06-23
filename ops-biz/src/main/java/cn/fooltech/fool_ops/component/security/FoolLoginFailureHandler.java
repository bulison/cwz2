package cn.fooltech.fool_ops.component.security;

import cn.fooltech.fool_ops.config.Constants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FoolLoginFailureHandler implements AuthenticationFailureHandler {

    private String defaultFailureUrl;

    private boolean forwardToDestination = false;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public FoolLoginFailureHandler(String defaultFailureUrl) {
        super();
        this.defaultFailureUrl = defaultFailureUrl;
    }

    public FoolLoginFailureHandler(String defaultFailureUrl, boolean forwardToDestination) {
        super();
        this.defaultFailureUrl = defaultFailureUrl;
        this.forwardToDestination = forwardToDestination;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        //把异常放置入session中
        HttpSession session = request.getSession();
        session.setAttribute(Constants.DEFAULT_SECURITY_EXCEPTION_KEY, exception.getMessage());

        //登录失败后记录登录信息等

        if (this.forwardToDestination) {
            request.getRequestDispatcher(this.defaultFailureUrl).forward(request, response);
        } else {
            this.redirectStrategy.sendRedirect(request, response, this.defaultFailureUrl);
        }
    }

    public void setForwardToDestination(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
