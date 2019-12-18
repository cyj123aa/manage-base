package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * EXCEL设置公式.error校验
 * @author lijunling
 *
 * @date 2019/05/29 17:51
 */
@Data
public class FormulaForExcelBO {
	private String key;
	/**
	 * excel公式字符串
	 */
	private String formula;
	private String errMsg;
	/**
	 * 级联层次
	 */
	private ExcelDropDownTypeEnum excelDropDownType;
	
	public FormulaForExcelBO(String key, String formula, String errMsg, ExcelDropDownTypeEnum excelDropDownType) {
		super();
		this.key = key;
		this.formula = formula;
		this.errMsg = errMsg;
		this.excelDropDownType = excelDropDownType;
	}
	
}
