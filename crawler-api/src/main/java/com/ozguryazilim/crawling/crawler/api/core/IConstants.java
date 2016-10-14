package com.ozguryazilim.crawling.crawler.api.core;

public interface IConstants
{
	public static final String	FIELD_TYPE_COMBO_SELECT_EACH		= "ftComboSelectEach";
	public static final String	FIELD_TYPE_COMBO_SET_VALUE			= "ftComboSetValue";
	public static final String	FIELD_TYPE_INTEGER					= "ftInt";
	public static final String	FIELD_TYPE_RADIO_BUTTON				= "ftRadioButton";
	public static final String	FIELD_TYPE_BOOLEAN					= "ftBoolean";

	public static final String	RELATION_MANY_TO_ONE				= "ManyToOne";
	public static final String	RELATION_MANY_TO_MANY				= "ManyToMany";
	public static final String	RELATION_INTEGRATED					= "Integrated";

	public static final String	DATA_CAPTURE_METHOD_SIMPLE			= "simpleInnerText";
	public static final String	DATA_CAPTURE_METHOD_EMBEDDED_SIMPLE	= "embeddedSimpleInnerText";
	public static final String	DATA_CAPTURE_METHOD_EMBEDDED_ALL	= "embeddedAllInnerText";
	public static final String	DATA_CAPTURE_METHOD_ALL				= "allInnerText";
	public static final String	DATA_CAPTURE_METHOD_NEXT_RECURSIVE	= "nextSiblingRecursively";

	public static final long	ONE_DAY								= 24 * 60 * 60 * 1000;
}
