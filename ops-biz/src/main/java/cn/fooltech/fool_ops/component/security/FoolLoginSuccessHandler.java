package cn.fooltech.fool_ops.component.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FoolLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private String defaultTargetUrl;

    private boolean forwardToDestination = false;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private RequestCache requestCache = new HttpSessionRequestCache();

    public FoolLoginSuccessHandler(String defaultTargetUrl) {
        super();
        this.defaultTargetUrl = defaultTargetUrl;
    }

    public FoolLoginSuccessHandler(String defaultTargetUrl, boolean forwardToDestination) {
        super();
        this.defaultTargetUrl = defaultTargetUrl;
        this.forwardToDestination = forwardToDestination;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        //登录成功后记录登录信息等
//        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
//        if(savedRequest == null) {
//            if (this.forwardToDestination) {
//                request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);
//            } else {
//                this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);
//            }
//        } else {
//            String targetUrlParameter = this.getTargetUrlParameter();
//            if(!this.isAlwaysUseDefaultTargetUrl() && (targetUrlParameter == null || !StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
//                this.clearAuthenticationAttributes(request);
//                String targetUrl = savedRequest.getRedirectUrl();
//                this.logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
//                this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
//            } else {
//                if (this.forwardToDestination) {
//                    request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);
//                } else {
//                    this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);
//                }
//            }
//        }

        if (this.forwardToDestination) {
            request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);
        } else {
            this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);
        }
    }

    public void setDefaultTargetUrl(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }

    public void setForwardToDestination(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }
}
