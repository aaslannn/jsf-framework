package com.ozguryazilim.zoro.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class ReflectionUtility
{
	public static List<Field> getAllFields(Class<?> clazz)
	{
		List<Field> allFields = new ArrayList<Field>();
		if (!clazz.equals(Object.class))
		{

			CollectionUtils.addAll(allFields, clazz.getDeclaredFields());

			allFields.addAll(getAllFields(clazz.getSuperclass()));
		}

		return allFields;
	}

	public static Class<?> getGenericType(Class<?> clazz)
	{
		try
		{
			return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
		}
		catch (Exception e)
		{
			return (Class<?>) ((ParameterizedType) clazz.getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}
}
