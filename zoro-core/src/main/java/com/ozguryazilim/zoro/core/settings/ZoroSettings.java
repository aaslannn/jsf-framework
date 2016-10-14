package com.ozguryazilim.zoro.core.settings;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.transaction.Transactional;
import org.jboss.solder.servlet.WebApplication;
import org.jboss.solder.servlet.event.Initialized;

import com.ozguryazilim.zoro.core.db.DBEntityManager;

@ApplicationScoped
@Named
public class ZoroSettings {
	public static final int				LDAP_TIMEOUT	= 10000;

	public static ZoroSettingsHolder	settingsHolder;

	public static String				dbPath;

	@SuppressWarnings("unused")
	@Inject
	private DBEntityManager				entityManager;

	static {
		Properties properties = new Properties();
		try {
			properties.load(ZoroSettings.class.getResourceAsStream("/settings.properties"));

			settingsHolder = new ZoroSettingsHolder();

			settingsHolder.setGuiListSize(Integer.parseInt(properties.getProperty("gui_list_size", "10")));

			settingsHolder.setLdapHost(properties.getProperty("ldap_host", "localhost"));
			settingsHolder.setLdapPort(Integer.parseInt(properties.getProperty("ldap_port", "389")));
			settingsHolder.setLdapUser(properties.getProperty("ldap_user", "cn=admin"));
			settingsHolder.setLdapPass(properties.getProperty("ldap_pass", "admin"));
			settingsHolder.setLdapSearchDN(properties.getProperty("ldap_search_dn", "ou=Users,dc=ozguryazilim,dc=com,dc=tr"));
			settingsHolder.setLdapLoginAttr(properties.getProperty("ldap_login_attr", "cn"));
			settingsHolder.setAdminUsername(properties.getProperty("admin_username", "aaslannn"));

		}
		catch (Exception e) {
			Logger.getLogger(ZoroSettings.class.getName()).severe("Can not load settings properties, check if exists in class path");
			e.printStackTrace();
		}
	}

	@PostConstruct
	@SuppressWarnings("rawtypes")
	public void postConstruct() {

		EntityManager createEntityManager = DBEntityManager.createAEntityManager();
		Query createQuery = createEntityManager.createQuery("SELECT zsh FROM ZoroSettingsHolder zsh");
		List list = createQuery.getResultList();
		if (list.size() != 0) {
			ZoroSettingsHolder dbHolder = (ZoroSettingsHolder) list.get(0);
			settingsHolder.setGuiListSize(dbHolder.getGuiListSize());
		}

		createEntityManager.close();
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public static void loadFromDB() {

		List list = DBEntityManager.getInstance().executeParameterizedSelectQuery("SELECT zsh FROM ZoroSettingsHolder zsh", new Object[] {});

		if (list.size() == 0) {
			DBEntityManager.getInstance().save(settingsHolder);
		}
		else {
			ZoroSettingsHolder zoroSettingsHolder = (ZoroSettingsHolder) list.get(0);

			settingsHolder = zoroSettingsHolder;
		}
	}

	/**
	 * At startup of web application logs the message, it is used to initialize
	 * the bean at startup
	 * 
	 * @param webapp
	 */
	public void startup(@Observes @Initialized WebApplication webapp) {
		Logger.getLogger(ZoroSettings.class.getName()).info("ZoroSettings initialized at " + new Date(webapp.getStartTime()));
	}

	public int getGuiListSize() {
		return settingsHolder.getGuiListSize();
	}
}
