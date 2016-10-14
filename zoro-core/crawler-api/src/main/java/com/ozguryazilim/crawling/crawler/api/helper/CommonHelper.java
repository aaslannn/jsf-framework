package com.ozguryazilim.crawling.crawler.api.helper;

import java.util.ArrayList;
import java.util.List;

public class CommonHelper
{
	public static List<Integer[]> getPartialIndexes(int maxIndex, int partSize)
	{
		int endIndex;
		int startIndex;
		int increaseAmount;
		List<Integer[]> startEndIndexArrayList = new ArrayList<Integer[]>();
		
		increaseAmount = maxIndex / partSize;
		for (int i = 0; i < partSize; i++)
		{
			startIndex = i * increaseAmount;
			endIndex = (i + 1) * increaseAmount < maxIndex ? (i + 1) * increaseAmount : maxIndex;
			if (i == partSize - 1)
			{
				endIndex = maxIndex;
			}
			
			startEndIndexArrayList.add( new Integer[]{startIndex, endIndex});
		}
		
		return startEndIndexArrayList;
	}
}
