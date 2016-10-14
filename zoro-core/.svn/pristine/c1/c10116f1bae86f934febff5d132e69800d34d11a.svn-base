package com.ozguryazilim.zoro.core.auth;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/*" })
public class LoginFilter implements Filter
{
	@Inject
	private ZoroCredentials		credentials;

	@Inject
	private AuthorizationService	authorizationService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String contentType = httpRequest.getRequestURI();
		if (!(contentType.contains(".png") || contentType.contains(".jpg") || contentType.contains(".gif") || contentType.contains(".js") || contentType.contains(".css") || contentType.endsWith("login.xhtml") || contentType.endsWith("error403.xhtml")))
		{
			if (!credentials.isLoggedIn())
			{
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
				return;
			}
			else if (!credentials.isAdmin())
			{
				if (!authorizationService.isAuthorizedFor(credentials.getUsername(), httpRequest.getServletPath()))
				{
					httpResponse.sendError(403);
				}
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{

	}

}
