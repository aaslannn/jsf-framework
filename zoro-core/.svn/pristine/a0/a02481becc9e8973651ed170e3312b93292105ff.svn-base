package com.ozguryazilim.zoro.core.loader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Loads the applications from the any module registered
 * 
 * @author aaslannn
 * 
 */
@ApplicationScoped
@Named
public class ModuleController implements Serializable
{
	private static final long			serialVersionUID	= -1426002704487318459L;

	@Any
	@Inject
	private Instance<ApplicationLoader>	loaders;

	private List<Application>			applications;

	/**
	 * Traverses each loader and loads the applications
	 */
	@PostConstruct
	public void initialize()
	{
		applications = new ArrayList<Application>();

		for (ApplicationLoader loader : loaders)
		{
			applications.add(loader.getApplication());
		}
	}

	/**
	 * @return the applications
	 */
	public List<Application> getApplications()
	{
		return applications;
	}
}
