package com.power.sql.generator.model;


import com.power.poi.annotation.Excel;

/**
 * excel 读取数据模型
 *
 * Created by yu on 2016/11/10.
 */
public class DataModel {
	@Excel(name = "中文名", columnNum = 0)
	private String chineseName;// 中文字段名称

	@Excel(name = "英文名", columnNum = 1,width = 7000)
	private String englishName;// 英文字段名称

	@Excel(name = "类型", columnNum = 2)
	private String type;// 类型

	@Excel(name = "长度", columnNum = 3)
	private String length;// 长度

	@Excel(name = "数据类型", columnNum = 4)
	private String dataDesc;//数据类型描述

	@Excel(name = "是否为空", columnNum = 6)
	private String allowEmpty;// 是否允许为空

	@Excel(name = "是否主键", columnNum = 5)
	private String primaryKey;// 是否为主键

	@Excel(name = "标准", columnNum = 7)
	private String standard;//标准


	@Excel(name = "描述", columnNum = 8,width = 6000)
	private String desc;// 描述或说明

	public String getChineseName() {
		return chineseName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(String allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getDataDesc() {
		return dataDesc;
	}

	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}
}
