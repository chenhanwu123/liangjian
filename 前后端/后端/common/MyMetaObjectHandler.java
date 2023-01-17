package com.example.raj_liangjian.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    HttpServletRequest httpServletRequest;
    /**
     * 插入时，自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getThreadLocal());
        metaObject.setValue("updateUser", BaseContext.getThreadLocal());
    }

    /**
     * 修改时，自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        Long EmpID = (Long) httpServletRequest.getSession().getAttribute("employee");
        if (EmpID!=null){
            log.info(EmpID+" 修改人id");
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("updateUser", EmpID);
        }else {
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("updateUser", BaseContext.getThreadLocal());
        }



    }
}
