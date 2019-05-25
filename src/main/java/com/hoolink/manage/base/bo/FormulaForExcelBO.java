package com.hoolink.manage.base.bo;

import lombok.Data;

@Data
public class FormulaForExcelBO {
	private String key;
	//excel公式字符串
	private String formula;
	private String errMsg;
	
	public FormulaForExcelBO(String key, String formula, String errMsg) {
		super();
		this.key = key;
		this.formula = formula;
		this.errMsg = errMsg;
	}
}
