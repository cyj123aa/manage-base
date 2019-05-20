package com.hoolink.manage.base.util;

import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.vo.BackVO;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: xuli
 * @Date: 2019/4/18 16:36
 */
public class ValidatorUtil {

    private static final String ERROR_MESSAGE_PREFIX = "参数错误：";

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static final <T> BackVO<T> validateParameter(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> validate = VALIDATOR.validate(object, groups);
        if (CollectionUtils.isEmpty(validate)) {
            return null;
        }
        return BackVOUtil.operateError(ERROR_MESSAGE_PREFIX +
                validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
    }
}
