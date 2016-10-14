package com.ozguryazilim.zoro.core.auth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.shared.ldap.model.cursor.EntryCursor;
import org.apache.directory.shared.ldap.model.entry.Entry;
import org.apache.directory.shared.ldap.model.message.SearchScope;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.ozguryazilim.zoro.core.settings.ZoroSettings;

public class LDAPLazyDataModel extends LazyDataModel<String> {
	private static final long	serialVersionUID	= -5123118752115649795L;

	@Override
	public List<String> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, String> filters) {
		List<String> userList = new ArrayList<String>();
		LdapNetworkConnection connection = AuthenticationService.getConnection();
		try {
			connection.bind();

			String query = "(&(objectclass=person))";
			String username = filters.get("entity");
			if (username != null && !username.equals("")) {
				query = "(&(objectclass=person)(" + ZoroSettings.settingsHolder.getLdapLoginAttr() + "=*" + username + "*))";
			}
			EntryCursor userEntries = connection.search(new Dn(ZoroSettings.settingsHolder.getLdapSearchDN()), query, SearchScope.SUBTREE,
					ZoroSettings.settingsHolder.getLdapLoginAttr());

			Iterator<Entry> iterator = userEntries.iterator();
			int current = 0;
			while (iterator.hasNext()) {
				Entry next = iterator.next();
				if (current >= first && current < first + pageSize) {
					userList.add(next.get(ZoroSettings.settingsHolder.getLdapLoginAttr()).getString());
				}

				current++;
			}
			userEntries.close();

			connection.close();

			setRowCount(current);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return userList;
	}

	@Override
	public List<String> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		List<SortMeta> multiSortMeta = new ArrayList<SortMeta>();
		if (!sortOrder.equals(SortOrder.UNSORTED) && sortField != null) {
			multiSortMeta.add(new SortMeta(null, sortField, sortOrder, null));
		}

		return load(first, pageSize, multiSortMeta, filters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getRowData(String rowKey) {
		List<String> wrappedData = (List<String>) getWrappedData();
		for (String entity : wrappedData) {
			if (entity.equals(rowKey)) {
				return entity;
			}
		}

		return null;
	}

	public Object getRowKey(String object) {
		return object;
	}
}
