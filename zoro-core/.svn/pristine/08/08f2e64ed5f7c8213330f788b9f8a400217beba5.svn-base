package com.ozguryazilim.zoro.core;

import java.io.IOException;

import javax.faces.application.ViewExpiredException;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.security.NotLoggedInException;
import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;

@HandlesExceptions
public class ExceptionHandlers {
	/**
	 * Handles the view expired exception
	 * 
	 * @param caught
	 * @param response
	 */
	public void handleViewExpired(@Handles CaughtException<ViewExpiredException> caught, HttpServletResponse response) {
		redirectPage(response);
	}

	/**
	 * 
	 * @param caught
	 * @param response
	 */
	public void handleIllegalArguement(@Handles CaughtException<IllegalArgumentException> caught, HttpServletResponse response) {
		redirectPage(response);

	}

	/**
	 * 
	 * @param caught
	 * @param response
	 */
	public void handleNotLoggedIn(@Handles CaughtException<NotLoggedInException> caught, HttpServletResponse response) {
		try {
			response.sendRedirect("login.xhtml");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Redirects the page
	 * 
	 * @param response
	 */
	private void redirectPage(HttpServletResponse response) {
		try {
			response.sendRedirect("index.xhtml");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
