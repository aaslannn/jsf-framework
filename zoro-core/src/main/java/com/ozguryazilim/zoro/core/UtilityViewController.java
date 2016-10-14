package com.ozguryazilim.zoro.core;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import com.ozguryazilim.zoro.core.db.DBEntityManager;
import com.ozguryazilim.zoro.core.util.MessageUtility;

public class UtilityViewController {

	public static final String	SENTIMENT_TYPE_STR	= "sentiment_type_";

	public static final String	DEFAULT_BUNDLE		= "default";

	@Inject
	protected DBEntityManager	entityManager;

	/**
	 * Returns comma separated objects in the list
	 * 
	 * @param list
	 * @return
	 */
	public String getListStr(List<?> list) {
		if (list == null)
			return "";

		return StringUtils.join(list, ", ");
	}

	/**
	 * Returns comma separated objects in the list
	 * 
	 * @param list
	 * @return
	 */
	public String getArrayStr(Object[] list) {
		if (list == null)
			return "";

		if (list.length > 3) {
			return list[0] + ", " + list[1] + ", " + list[2] + "...";
		}
		return StringUtils.join(list, ", ");
	}

	/**
	 * Returns the localized string for the state from default bundle
	 * 
	 * @param state
	 * @return
	 */
	public String getEnumStr(Enum<?> state) {
		if (state == null) {
			return "";
		}

		return MessageUtility.get("default", state.toString().toLowerCase());
	}

	/**
	 * Returns the boolean localized value
	 * 
	 * @param value
	 * @return
	 */
	public String getBooleanStr(boolean value) {
		return MessageUtility.get("default", value + "");
	}

	/**
	 * Returns the localized string for the state from given bundle
	 * 
	 * @param bundle
	 * @param state
	 * @return
	 */
	public String getEnumStr(String bundle, Enum<?> state) {
		if (state == null) {
			return "";
		}

		return MessageUtility.get(bundle, state.toString().toLowerCase());
	}

	/**
	 * @return the dbEntityManager
	 */
	public DBEntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @param dbEntityManager
	 *            the dbEntityManager to set
	 */
	public void setEntityManager(DBEntityManager dbEntityManager) {
		this.entityManager = dbEntityManager;
	}
}
