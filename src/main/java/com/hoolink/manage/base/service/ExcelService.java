package com.hoolink.manage.base.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.hoolink.manage.base.bo.ManagerUserPageParamBO;
import com.hoolink.manage.base.bo.UserExcelDataBO;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/29 12:58
 */
public interface ExcelService {
    /**
     * 人员导入
     * @param multipartFile
     * @param deptIdList
     * @return
     * @throws Exception
     */
    UserExcelDataBO uploadExcel(MultipartFile multipartFile, List<Long> deptIdList) throws Exception;
    
    /**
     *下载模板
     * @return
     * @throws Exception
     */
    ResponseEntity<Resource> downloadTemplate() throws Exception;
    
    
    /**
     * 用户导出
     * @param userPageParamBO
     * @return
     * @throws Exception
     */
    ResponseEntity<Resource> exportList(ManagerUserPageParamBO userPageParamBO) throws Exception;
}
