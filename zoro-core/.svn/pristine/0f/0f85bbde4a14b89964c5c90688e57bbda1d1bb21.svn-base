package com.ozguryazilim.zoro.core.settings;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.ldap.client.api.exception.InvalidConnectionException;
import org.apache.directory.shared.ldap.model.cursor.EntryCursor;
import org.apache.directory.shared.ldap.model.exception.LdapAuthenticationException;
import org.apache.directory.shared.ldap.model.message.SearchScope;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.jboss.seam.transaction.Transactional;

import com.ozguryazilim.zoro.core.UtilityViewController;
import com.ozguryazilim.zoro.core.auth.AuthenticationService;
import com.ozguryazilim.zoro.core.db.DBEntityManager;
import com.ozguryazilim.zoro.core.util.JSFUtility;
import com.ozguryazilim.zoro.core.util.MessageUtility;

@ViewScoped
@Named
public class SettingsViewController extends UtilityViewController implements Serializable {

	private static final long	serialVersionUID	= -69123973263488285L;
	private ZoroSettingsHolder	entity;
	@Inject
	private DBEntityManager		entityManager;

	@SuppressWarnings("rawtypes")
	@PostConstruct
	@Transactional
	public void postConstruct() {
		List list = entityManager.executeParameterizedSelectQuery("SELECT zsh FROM ZoroSettingsHolder zsh", new Object[] {});
		ZoroSettingsHolder holder = ZoroSettings.settingsHolder;

		if (list.size() == 0) {
			// entityManager.save(ZoroSettings.settingsHolder);
			// entityManager.flush();
			// list =
			// entityManager.executeParameterizedSelectQuery("SELECT zsh FROM ZoroSettingsHolder zsh",
			// new Object[] {});
		}
		else {
			ZoroSettingsHolder dbHolder = (ZoroSettingsHolder) list.get(0);
			holder.setGuiListSize(dbHolder.getGuiListSize());

		}

		entity = holder;
	}

	public void save() {
		if (!isLDAPValid()) {
			return;
		}

		entityManager.save(entity);
		ZoroSettings.loadFromDB();

		JSFUtility.addInfoMessage(null, MessageUtility.get("settings", "infoSaveSettingsSuccessful"));
	}

	/**
	 * Checks if the ldap is valid
	 * 
	 * @return
	 */
	private boolean isLDAPValid() {
		try {
			LdapNetworkConnection connection = AuthenticationService.getConnection();
			connection.connect();
			connection.bind();

			EntryCursor userEntries = connection.search(ZoroSettings.settingsHolder.getLdapSearchDN(), "(&(objectclass=person)(" + ZoroSettings.settingsHolder.getLdapLoginAttr()
					+ "=" + entity.getAdminUsername() + "))", SearchScope.SUBTREE);

			Dn dnOfUser = null;
			if (userEntries.next()) {
				dnOfUser = userEntries.get().getDn();
			}

			if (dnOfUser == null) {
				JSFUtility.addErrorMessage(null, MessageUtility.get("settings", "errorCheckLdapSearchDNAndAdminDNSettings"));
				return false;
			}

			return true;
		}
		catch (InvalidConnectionException e) {
			JSFUtility.addErrorMessage(null, MessageUtility.get("settings", "errorCheckLdapHostSettings"));
		}
		catch (LdapAuthenticationException e) {
			JSFUtility.addErrorMessage(null, MessageUtility.get("settings", "errorCheckLdapAuthenticationSettings"));
		}
		catch (Exception e) {
			JSFUtility.addErrorMessage(null, MessageUtility.get("settings", "errorCheckLdapSettings"));
		}

		return false;
	}

	/**
	 * @return the entity
	 */
	public ZoroSettingsHolder getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(ZoroSettingsHolder entity) {
		this.entity = entity;
	}
}
