package project.test.xface.aspect;


import lombok.extern.slf4j.Slf4j;
import project.test.xface.annotation.AutoFill;
import project.test.xface.mapper.LogHistoryMapper;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.pojo.LogHistory;
import project.test.xface.utils.UserHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Resource
    private LogHistoryMapper logHistoryMapper;
    /**
     * 切面记录系统日志
     */
    @Before("@annotation(autoFill)")
    public void before(JoinPoint joinPoint,AutoFill autoFill)
    {
        String value = autoFill.value();
        UserDTO userDTO = UserHolder.getUser();
        Integer userDTOId = userDTO.getId();
        LogHistory logHistory=new LogHistory();
        logHistory.setId(userDTOId);
        logHistory.setOperationType(value);
        logHistory.setUserName(userDTO.getUsername());
        logHistoryMapper.insertLog(logHistory);
    }
}