package com.filrouge.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.filrouge.bean.Utilisateur;

public class RestrictionFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		Utilisateur user = null;
		user = (Utilisateur) session.getAttribute("user");
		if (user == null || !user.getAccess().equals("admin")) {
			response.sendRedirect("index.html");
		} else {
			chain.doFilter(request, response);
		}
		
	}
	
	public void init(FilterConfig arg0) throws ServletException {
	}

}
