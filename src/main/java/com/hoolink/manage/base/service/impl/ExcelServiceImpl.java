package com.hoolink.manage.base.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.DictPairBO;
import com.hoolink.manage.base.bo.DictPairForExcelBO;
import com.hoolink.manage.base.bo.FormulaForExcelBO;
import com.hoolink.manage.base.bo.ManageDepartmentBO;
import com.hoolink.manage.base.bo.ManageRoleBO;
import com.hoolink.manage.base.bo.ManagerUserPageParamBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.UserDeptPairParamBO;
import com.hoolink.manage.base.bo.UserExcelDataBO;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.service.ExcelService;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.FileUtil;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO.UserDepartmentBO;
import com.hoolink.sdk.enums.EncryLevelEnum;
import com.hoolink.sdk.enums.ExcelDropDownTypeEnum;
import com.hoolink.sdk.enums.ManagerUserSexEnum;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.DateUtil;
import com.hoolink.sdk.utils.ExcelUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/29 12:58
 */
@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService{

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DepartmentService departmentService;
    
	@Override
	@Transactional(rollbackFor = Exception.class)
	public UserExcelDataBO uploadExcel(MultipartFile multipartFile, List<Long> deptIdList) throws Exception{
		UserExcelDataBO userExcelData = new UserExcelDataBO();
        //校验入参
        if(multipartFile==null || CollectionUtils.isEmpty(deptIdList)){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        File file = FileUtil.multipartFileToFile(multipartFile);
        if(file != null) {
        	List<ManagerUserParamBO> userExcelList = dataAnalysis(file);
            file.delete();
            if(CollectionUtils.isEmpty(userExcelList)) {
            	throw new BusinessException(HoolinkExceptionMassageEnum.EXCEL_DATA_FORMAT_ERROR);
            }
        	userExcelList.stream().forEach(ue -> 
    			ue.getUserDeptPairParamList().stream().forEach(udp -> udp.setDeptIdList(deptIdList))
        	);
            for(ManagerUserParamBO userParam : userExcelList) {
            	try {
            		userService.createUser(userParam);
            	}catch(Exception e) {
            		throw new BusinessException(HoolinkExceptionMassageEnum.EXCEL_IMPORTED_FAILED);
            	}
            }
            userExcelData.setTotal(userExcelList.size());
            return userExcelData;
        }
		return null;
	}

    /**
     * cell的类型获取数据
     *
     * @param cell
     * @return
     */
    private String getValue(Cell cell) {
        //判断单元格的内容类型（目前的只有String或者数字）
        DecimalFormat decimalFormat = new DecimalFormat("#");
        SimpleDateFormat sFormat = new SimpleDateFormat("MM/dd/yyyy");
        String value = "";
        //目前只是支持业务判断了string类型和布尔类型以及错误和为空的值，
        switch (cell.getCellTypeEnum()) {
            case STRING:
                value = String.valueOf(cell.getStringCellValue());
                break;
            case NUMERIC:
                if(HSSFDateUtil.isCellDateFormatted(cell)) {
                    double d = cell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(d);
                   value = sFormat.format(date);
                }
                else {
                    value = decimalFormat.format((cell.getNumericCellValue()));
                }
                break;
            case FORMULA:
            	value = decimalFormat.format((cell.getNumericCellValue()));
                break;
            case ERROR:
            case BLANK:
                break;
            case BOOLEAN:
                break;
            default:
                break;
        }
        return value;
    }
    
	private List<ManagerUserParamBO> dataAnalysis(File file) throws Exception{
		List<ManagerUserParamBO> userExcelList = new ArrayList<>();
        FileInputStream inp = new FileInputStream(file);
        Workbook wb = new HSSFWorkbook(inp);
        Sheet sheet = wb.getSheet(Constant.EXCEL_SHEET1);
        
        boolean flag = false;
        for (int rowNum = 1; rowNum < sheet.getLastRowNum(); rowNum++) {
        	Row row = sheet.getRow(rowNum);
        	ManagerUserParamBO managerUserParam = new ManagerUserParamBO();
            //列 获取所有单元格的数据
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell rowCell = row.getCell(j);
                
                if(!sheet.isColumnHidden(j) && rowCell == null) {
                	flag = true;
                	break;
                };
                
                if (rowCell != null) {
                    String value = getValue(rowCell);
                    if(StringUtils.isBlank(value)) {
                    	throw new BusinessException(HoolinkExceptionMassageEnum.EXCEL_DATA_FORMAT_ERROR);
                    }
                    buildManagerUserParam(managerUserParam, j, value);
                }
            }
            
            if(flag) {
            	break;
            }else {
            	userExcelList.add(managerUserParam);	
            }
        }
        return userExcelList;
	}
	
	/**
	 * 赋值
	 * @param managerUserParam
	 * @param j
	 * @param value
	 */
	private void buildManagerUserParam(ManagerUserParamBO managerUserParam, int j, String value) {
        switch (j) {
	        case 0:
	        	//用户编号
	        	managerUserParam.setUserNo(value);
	            break;
	        case 1:
	        	//用户姓名
	        	managerUserParam.setName(value);
	            break;
	        case 2:
	        	//用户性别
	            break;
	        case 3:
	        	//所属角色
	            break;
	        case 4:
	        	//部门密保等级
	            break;
	        case 5:
	        	//职位
	        	managerUserParam.setPosition(value);
	            break;
	        case 6:
	        	//资源库密保等级
	            break;
	        case 7:
	        	//登录账号
	        	managerUserParam.setUserAccount(value);
	            break;
	        case 8:
	        	//用户性别id
	        	managerUserParam.setSex("1".equals(value) ? true:false);
	        	break;
	        case 9:
	        	//所属角色id
	        	managerUserParam.setRoleId(Long.parseLong(value));
	        	break;
	        case 10:
	        	//部门密保等级id
	        	List<UserDeptPairParamBO> userDeptPairParamList = new ArrayList<>();
	        	UserDeptPairParamBO userDeptPairParam = new UserDeptPairParamBO();
	        	userDeptPairParam.setEncryLevelDept(Integer.parseInt(value));
	        	userDeptPairParam.setDeptIdList(null);
	        	userDeptPairParamList.add(userDeptPairParam);
	        	managerUserParam.setUserDeptPairParamList(userDeptPairParamList);
	        	break;
	        case 11:
	        	//资源库密保等级id
	        	managerUserParam.setEncryLevelCompany(Integer.parseInt(value));
	        	break;
	        default:
	            break;
	    }
	}
	
	@Override
	public ResponseEntity<org.springframework.core.io.Resource> downloadTemplate() throws Exception {
		Workbook workbook = buildUserWorkbook();
		return ExcelUtil.export(workbook, Constant.HOOLINK_USER_EXPORT_EXCEL_TITLE);
	}
	
	private Workbook buildUserWorkbook() {
		Workbook wb = new HSSFWorkbook();
		setHidenSheet(wb);
		
		//设置表头
		//用户编号、用户姓名、用户性别、所属角色、部门密保等级、部门职位、资源库密保等级、账号
		String[] headerArray = {Constant.EXCEL_USER_NO, Constant.EXCEL_USER_NAME, Constant.EXCEL_USER_SEX, Constant.EXCEL_USER_ROLENAME, 
				Constant.EXCEL_USER_ENCRY_LEVEL_DEPT, Constant.EXCEL_USER_POSITION, Constant.EXCEL_USER_ENCRY_LEVEL_COMPANY, Constant.EXCEL_USER_ACCOUNT};
		
		Sheet sheet1 = wb.createSheet(Constant.EXCEL_SHEET1);
		Row row0 = sheet1.createRow(0);
		for(int i=0; i<headerArray.length; i++) {
			row0.createCell(i).setCellValue(headerArray[i]);
		}
		
		
		//设置公式
		List<FormulaForExcelBO> formulaForExcelList = new ArrayList<>();
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_SEX, Constant.EXCEL_SEX_LIST, HoolinkExceptionMassageEnum.EXCEL_SEX_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_ONE));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_ROLENAME, Constant.EXCEL_ROLE_LIST, HoolinkExceptionMassageEnum.EXCEL_ROLE_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_ONE));
		//formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_COMPANY, Constant.EXCEL_COMPANY_LIST, HoolinkExceptionMassageEnum.EXCEL_COMPANY_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_ONE));
		//formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_DEPT, Constant.EXCEL_DEPT_FORMULA, HoolinkExceptionMassageEnum.EXCEL_DEPT_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_TWO_MORE));
		//formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_TEAM, Constant.EXCEL_TEAM_FORMULA, HoolinkExceptionMassageEnum.EXCEL_TEAM_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_TWO_MORE));
		
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_ENCRY_LEVEL_DEPT, Constant.EXCEL_ENCRY_LEVEL_LIST, HoolinkExceptionMassageEnum.EXCEL_ENCRY_LEVEL_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_ONE));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_ENCRY_LEVEL_COMPANY, Constant.EXCEL_ENCRY_LEVEL_LIST, HoolinkExceptionMassageEnum.EXCEL_ENCRY_LEVEL_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_ONE));
		//formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_VIEW_ENCRY_PERMITTED, Constant.EXCEL_VIEW_ENCRY_PERMITTED_LIST, HoolinkExceptionMassageEnum.EXCEL_VIEW_ENCRY_PERMITTED_ERROR.getMassage(), ExcelDropDownTypeEnum.LEVEL_ONE));
		
		for(FormulaForExcelBO formulaForExcel : formulaForExcelList) {
			List<String> headerList = Arrays.asList(headerArray);
			if(headerList.contains(formulaForExcel.getKey())) {
				int index = headerList.indexOf(formulaForExcel.getKey());
				DVConstraint formula = DVConstraint.createFormulaListConstraint(formulaForExcel.getFormula());
				CellRangeAddressList rangeAddressList = new CellRangeAddressList(1, 10000, index, index);
				DataValidation cacse = new HSSFDataValidation(rangeAddressList, formula);
		        //处理Excel兼容性问题
		        if(cacse instanceof XSSFDataValidation) {
		        	cacse.setSuppressDropDownArrow(true);
		            cacse.createErrorBox(Constant.ERROR, formulaForExcel.getErrMsg());
		        }else {
		        	cacse.setSuppressDropDownArrow(false);
		        }
				
				sheet1.addValidationData(cacse);					
			}
		}
		
		//隐藏的id域
		String idWithUnderline = Constant.UNDERLINE + Constant.EXCEL_ID;
		int len = headerArray.length;
		boolean flag = false;
		for(FormulaForExcelBO formulaForExcel : formulaForExcelList) {
			List<String> headerList = Arrays.asList(headerArray);
			if(headerList.contains(formulaForExcel.getKey())) {
				int index = headerList.indexOf(formulaForExcel.getKey());
				Cell cell = row0.createCell(len);
				cell.setCellValue(formulaForExcel.getKey() + idWithUnderline);
				sheet1.setColumnHidden(len, true);
				
				int tenThousand = 10000;
				for(int i=1; i<tenThousand; i++) {
					Row row;
					if(!flag) {
						row = sheet1.createRow(i);	
					}else {
						row = sheet1.getRow(i);
					}
					
					Cell cell1 = row.createCell(len);
					String formula = "INDEX(INDIRECT(\"" + formulaForExcel.getFormula() + Constant.UNDERLINE + Constant.EXCEL_ID + "\"),,MATCH(INDIRECT(ADDRESS(ROW(), COLUMN(" + getExcelColumn(index)+ "1))), INDIRECT(\"" + formulaForExcel.getFormula() + "\"), 0 ))";
					cell1.setCellFormula("IF(ISERROR("+ formula + "), \"\", " + formula + ")");
					
				}
				len++;
				flag = true;
			}
		}
		sheet1.setForceFormulaRecalculation(true);
		return wb;
	}
	
	/**
	 * 在隐藏的sheet上面赋值下拉框参数
	 * @param wb
	 */
	private void setHidenSheet(Workbook wb) {
		Sheet hideSheet = wb.createSheet(Constant.EXCEL_HIDE_SHEET);
		wb.setSheetHidden(wb.getSheetIndex(Constant.EXCEL_HIDE_SHEET), true);
		
		//获取组织架构字典
		List<DictPairForExcelBO> deptPairListForExcel = new ArrayList<>();
		//获取角色字典
		deptPairListForExcel.add(getRolePairForExcel());
		//获取加密等级字典值
		deptPairListForExcel.add(getEncryLevelPairForExcel());
		//获取性别字典值
		deptPairListForExcel.add(getSexPairForExcel());
		//插入组织架构属性到隐藏的sheet
		int rowId = 0;
		for(DictPairForExcelBO deptPairForExcel : deptPairListForExcel) {
			Row row = hideSheet.createRow(rowId++);
			//插入对应id属性
			Row idrow = hideSheet.createRow(rowId++);
			DictPairBO<Long, String> parentDeptPair = deptPairForExcel.getParentDictPair();
			row.createCell(0).setCellValue(parentDeptPair.getValue());
			idrow.createCell(0).setCellValue(parentDeptPair.getKey());
			
			List<DictPairBO<Long, String>> childrenDeptPairList = deptPairForExcel.getChildrenDictPairList();
			for(int j=0; j<childrenDeptPairList.size(); j++) {
				row.createCell(j + 1).setCellValue(childrenDeptPairList.get(j).getValue());
				idrow.createCell(j + 1).setCellValue(childrenDeptPairList.get(j).getKey());
			}
			
			// 添加名称管理器
			String range = getRange(1, rowId-1, childrenDeptPairList.size());
			Name name = wb.createName();
			name.setNameName(parentDeptPair.getValue());
			String formula = Constant.EXCEL_HIDE_SHEET + Constant.EXCLAMATION_MARK + range;
			name.setRefersToFormula(formula);
			
			// 添加对应ID管理器
			range = getRange(1, rowId, childrenDeptPairList.size());
			name = wb.createName();
			name.setNameName(parentDeptPair.getValue() + Constant.UNDERLINE + Constant.EXCEL_ID);
			formula = Constant.EXCEL_HIDE_SHEET + Constant.EXCLAMATION_MARK + range;
			name.setRefersToFormula(formula);
		}
	}
	
	/**
	 * 列换算成excel的字母标识
	 * @param col
	 * @return
	 */
	private String getExcelColumn(int col) {
		char start = 'A';
		int end = (int) (start + col);
		int b =(end - 91) / 26;
		int c =(end - 91) % 26;
		
		int twentyFive = 25;
		if(col <= twentyFive) {
			return (char)(start + col)+"";
		}else {
			return (char)(start + b) +""+ (char)(start + c) ;	
		}
	}
	/**
	 * 获取角色字典值
	 * @return
	 */
	private DictPairForExcelBO getRolePairForExcel(){
		DictPairForExcelBO rolePairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentRolePair = new DictPairBO<>();
		parentRolePair.setKey(-1L);
		parentRolePair.setValue(Constant.EXCEL_ROLE_LIST);
		rolePairForExcel.setParentDictPair(parentRolePair);
		
		List<DictPairBO<Long, String>> childrenRolePairList = new ArrayList<>();
		rolePairForExcel.setChildrenDictPairList(childrenRolePairList);
		List<ManageRoleBO> roleList = roleService.list();
		roleList.stream().forEach(r -> {
			DictPairBO<Long, String> childRolePair = new DictPairBO<>();
			childRolePair.setKey(r.getId());
			childRolePair.setValue(r.getRoleName());
			childrenRolePairList.add(childRolePair);
		});
		return rolePairForExcel;
	}
	
	/**
	 * 获取加密等级字典值
	 * @return
	 */
	private DictPairForExcelBO getEncryLevelPairForExcel(){
		DictPairForExcelBO encryLevelPairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentEncryLevelPair = new DictPairBO<>();
		parentEncryLevelPair.setKey(-1L);
		parentEncryLevelPair.setValue(Constant.EXCEL_ENCRY_LEVEL_LIST);
		encryLevelPairForExcel.setParentDictPair(parentEncryLevelPair);
		
		List<DictPairBO<Long, String>> childrenEncryLevelPairList = new ArrayList<>();
		encryLevelPairForExcel.setChildrenDictPairList(childrenEncryLevelPairList);
		for(EncryLevelEnum encryLevelEnum : EncryLevelEnum.values()) {
			DictPairBO<Long, String> childEncryLevelPair = new DictPairBO<>();
			childEncryLevelPair.setKey(encryLevelEnum.getKey().longValue());
			childEncryLevelPair.setValue(encryLevelEnum.getValue());
			childrenEncryLevelPairList.add(childEncryLevelPair);
		}
		return encryLevelPairForExcel;
	}
	
	/**
	 * 获取性别字典值
	 * @return
	 */
	private DictPairForExcelBO getSexPairForExcel(){
		DictPairForExcelBO sexPairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentSexPair = new DictPairBO<>();
		parentSexPair.setKey(-1L);
		parentSexPair.setValue(Constant.EXCEL_SEX_LIST);
		sexPairForExcel.setParentDictPair(parentSexPair);
		
		List<DictPairBO<Long, String>> childrenSexPairList = new ArrayList<>();
		sexPairForExcel.setChildrenDictPairList(childrenSexPairList);
		for(ManagerUserSexEnum sexEnum : ManagerUserSexEnum.values()) {
			DictPairBO<Long, String> childSexPair = new DictPairBO<>();
			childSexPair.setKey(sexEnum.getKey()==true?1L:0L);
			childSexPair.setValue(sexEnum.getValue());
			childrenSexPairList.add(childSexPair);
		}
		return sexPairForExcel;
	}
	
	/**
	 * 获取excel别名引用地址
	 * @param offset
	 * @param rowId
	 * @param colCount
	 * @return
	 */
	private String getRange(int offset, int rowId, int colCount) {
		char start = (char)('A' + offset);
		int twentyFive = 25;
		int twentySix = 26;
		int fiftyOne = 51;
		if (colCount <= twentyFive) {
			char end = (char)(start + colCount - 1);
			return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
		} else {
			char endPrefix = 'A';
			char endSuffix = 'A';
			// 26-51之间，包括边界（仅两次字母表计算）
			if ((colCount - twentyFive) / twentySix == 0 || colCount == fiftyOne) {
				// 边界值
				if ((colCount - twentyFive) % twentySix == 0) {
					endSuffix = (char)('A' + twentyFive);
				} else {
					endSuffix = (char)('A' + (colCount - twentyFive) % twentySix - 1);
				}
			} else {
				// 51以上
				if ((colCount - twentyFive) % twentySix == 0) {
					endSuffix = (char)('A' + twentyFive);
					endPrefix = (char)(endPrefix + (colCount - twentyFive) / twentySix - 1);
				} else {
					endSuffix = (char)('A' + (colCount - twentyFive) % twentySix - 1);
					endPrefix = (char)(endPrefix + (colCount - twentyFive) / twentySix);
				}
			}
			return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
		}
	}
	
	@Override
	public ResponseEntity<org.springframework.core.io.Resource> exportList(ManagerUserPageParamBO userPageParamBO)
			throws Exception {
		PageInfo<ManagerUserBO> pageInfo = userService.list(userPageParamBO);
		List<ManagerUserBO> userList = pageInfo.getList();
		//表头
        List<String> head = new ArrayList<>();
        head.add(Constant.EXCEL_USER_NO);
        head.add(Constant.EXCEL_USER_NAME);
        head.add(Constant.EXCEL_USER_POSITION);
        head.add(Constant.EXCEL_USER_DEPT_PAIR);
        head.add(Constant.EXCEL_USER_ROLENAME);
        head.add(Constant.EXCEL_USER_COMPANY);
        head.add(Constant.EXCEL_USER_PHONE);
        head.add(Constant.EXCEL_USER_ACCOUNT);
        head.add(Constant.EXCEL_USER_ENCRY_LEVEL_COMPANY);
        head.add(Constant.EXCEL_USER_STATUS);
        head.add(Constant.EXCEL_USER_LAST_TIME);
        head.add(Constant.EXCEL_USER_CREATED);
        
        //表体
        List<List<String>> contents = new ArrayList<>();
        userList.stream().forEach(user -> {
        	List<String> content = new ArrayList<>();
            content.add(user.getUserNo());
            content.add(user.getName());
            content.add(user.getPosition());
            List<UserDepartmentBO> userDeptPairList = user.getUserDeptPairList();
            StringBuilder sb = new StringBuilder();
            userDeptPairList.stream().forEach(udp -> {
            	sb.append(udp.getDeptName()).append(Constant.BACKSLASH).append(udp.getEncryLevelDeptName()).append(Constant.SEMICOLON);
            });
            if(sb.length() > 0) {
            	sb.deleteCharAt(sb.lastIndexOf(Constant.SEMICOLON));
            }
            content.add(sb.toString());
            content.add(user.getRoleName());
            content.add(user.getCompany());
            content.add(user.getPhone());
            content.add(user.getUserAccount());
            content.add(user.getEncryLevelCompanyName());
            content.add(user.getStatusDesc());
            if (user.getLastTime() == null) {
                content.add(Constant.NO_DATA);
            }else {
            	content.add(DateUtil.getStringByTimeStamp(user.getLastTime()));
            }
            if (user.getCreated() != null) {
                content.add(DateUtil.getStringByTimeStamp(user.getCreated()));
            }
            contents.add(content);
        });
		return ExcelUtil.getResponseEntity(head, contents, Constant.USER_EXPORT_EXCEL_TITLE);
	}

	/**
	 * 得到组织架构的excel属性
	 * @return
	 */
	private List<DictPairForExcelBO> listDeptPairForExcel(){
		List<DictPairForExcelBO> deptPairForExcelList = new ArrayList<>();
		List<ManageDepartmentBO> deptList = departmentService.listAll();
		List<ManageDepartmentBO> parentDeptList = deptList.stream().filter(d -> d.getParentId()==null).collect(Collectors.toList());
		
		//加入顶级组织
		DictPairForExcelBO deptPairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentDeptPair = new DictPairBO<>();
		parentDeptPair.setKey(-1L);
		parentDeptPair.setValue(Constant.EXCEL_COMPANY_LIST);
		deptPairForExcel.setParentDictPair(parentDeptPair);
		List<DictPairBO<Long, String>> childrenDeptPairList = new ArrayList<>();
		deptPairForExcel.setChildrenDictPairList(childrenDeptPairList);
		deptPairForExcelList.add(deptPairForExcel);
		
		//遍历查找到每个组织的直接子级
		parentDeptList.stream().forEach(pd -> {
			DictPairBO<Long, String> childDeptPair = new DictPairBO<>();
			childDeptPair.setKey(pd.getId());
			childDeptPair.setValue(pd.getName());
			childrenDeptPairList.add(childDeptPair);
			
			buildDeptPairForExcel(deptPairForExcelList, deptList, pd);
		});
		return deptPairForExcelList;
	}
	
	/**
	 * 遍历查找到每个组织的直接子级
	 * @param deptPairForExcelList
	 * @param deptList
	 * @param parentDept
	 */
	private void buildDeptPairForExcel(List<DictPairForExcelBO> deptPairForExcelList, List<ManageDepartmentBO> deptList, ManageDepartmentBO parentDept) {
		DictPairForExcelBO deptPairForExcel = new DictPairForExcelBO();
		
		DictPairBO<Long, String> parentDeptPair = new DictPairBO<>();
		parentDeptPair.setKey(parentDept.getId());
		parentDeptPair.setValue(parentDept.getName());
		deptPairForExcel.setParentDictPair(parentDeptPair);
		
		//查找直接子级
		List<DictPairBO<Long, String>> childrenDeptPairList = new ArrayList<>();
		List<ManageDepartmentBO> childrenDeptList = deptList.stream().filter(d -> parentDept.getId().equals(d.getParentId())).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(childrenDeptList)) {
			//存在子级
			deptPairForExcelList.add(deptPairForExcel);
			childrenDeptList.stream().forEach(cd -> {
				DictPairBO<Long, String> childDeptPair = new DictPairBO<>();
				childDeptPair.setKey(cd.getId());
				childDeptPair.setValue(cd.getName());
				childrenDeptPairList.add(childDeptPair);
				buildDeptPairForExcel(deptPairForExcelList, deptList, cd);
			});
		}
		deptPairForExcel.setChildrenDictPairList(childrenDeptPairList);
	}
}
