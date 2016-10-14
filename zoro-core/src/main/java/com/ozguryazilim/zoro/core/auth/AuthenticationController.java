package com.ozguryazilim.zoro.core.auth;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.Identity;
import org.picketlink.idm.api.User;
import org.picketlink.idm.impl.api.model.SimpleUser;

import com.ozguryazilim.zoro.core.settings.ZoroSettings;
import com.ozguryazilim.zoro.core.util.JSFUtility;
import com.ozguryazilim.zoro.core.util.MessageUtility;

@ViewScoped
@Named
public class AuthenticationController implements Authenticator, Serializable {
	private static final long		serialVersionUID	= -2259873047525622472L;

	@Inject
	private ZoroCredentials		zoroCredentials;

	@Inject
	private Identity				identity;

	@Inject
	private AuthenticationService	authenticationService;

	/**
	 * Logins the user
	 * 
	 * @return
	 */
	public String login() {
		if (identity.login().equals(Identity.RESPONSE_LOGIN_SUCCESS)) {
			return "success";
		}
		else {
			JSFUtility.addErrorMessage(null, MessageUtility.get("default", "errorLogin"));
		}

		return null;
	}

	/**
	 * Automatically called by identity
	 * 
	 * @return
	 */
	public void authenticate() {
		if (authenticationService.authenticate(zoroCredentials.getUsername(), zoroCredentials.getPassword())) {
			zoroCredentials.setPassword(null);
			zoroCredentials.setLoggedIn(true);
			if (zoroCredentials.getUsername().equals(ZoroSettings.settingsHolder.getAdminUsername())) {
				zoroCredentials.setAdmin(true);
			}
		}
	}

	/**
	 * Logouts the user
	 * 
	 * @return
	 */
	public String logout() {
		identity.logout();

		zoroCredentials.setUsername(null);
		zoroCredentials.setLoggedIn(false);
		zoroCredentials.setAdmin(false);

		return "logout";
	}

	@Override
	public void postAuthenticate() {

	}

	@Override
	public User getUser() {
		User user = new SimpleUser(zoroCredentials.getUsername());

		return user;
	}

	@Override
	public AuthenticationStatus getStatus() {
		return zoroCredentials.isLoggedIn() ? AuthenticationStatus.SUCCESS : AuthenticationStatus.FAILURE;
	}

}
