package com.ozguryazilim.zoro.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class FileUtility
{

	public static final String	DATA_FOLDER_NAME	= "data";

	/**
	 * Writes the object to the given file
	 */
	public static void write(Object object, String fileName)
	{
		// Check if data folder exists
		File dataFolder = new File(DATA_FOLDER_NAME);
		if (!(dataFolder.exists() && dataFolder.isDirectory()))
		{
			if (!dataFolder.mkdir())
			{
				Logger.getLogger(FileUtility.class.getName()).severe("Can not create 'data' folder for " + FileUtility.class.getSimpleName());
			}
		}

		ObjectOutputStream objectStream = null;
		try
		{

			FileOutputStream fileStream = new FileOutputStream(FileUtility.DATA_FOLDER_NAME + File.separatorChar + fileName);
			objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(object);
		}
		catch (Exception e)
		{
			Logger.getLogger(FileUtility.class.getName()).severe("Can not write object to file " + fileName);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (objectStream != null)
				{
					objectStream.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads the the object from the given file
	 */
	public static Object read(String fileName)
	{
		File file = new File(FileUtility.DATA_FOLDER_NAME + File.separatorChar + fileName);
		Object readData = null;
		if (file.exists())
		{
			ObjectInputStream input = null;
			try
			{
				FileInputStream fileStream = new FileInputStream(file);
				BufferedInputStream buffer = new BufferedInputStream(fileStream);
				input = new ObjectInputStream(buffer);

				readData = input.readObject();
			}
			catch (Exception e)
			{
				Logger.getLogger(FileUtility.class.getName()).severe("Can not read object from file " + fileName);
				e.printStackTrace();
			}
			finally
			{
				if (input != null)
				{
					try
					{
						input.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return readData;
	}
}
