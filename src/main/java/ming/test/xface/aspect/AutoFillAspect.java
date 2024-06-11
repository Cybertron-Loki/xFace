package ming.test.xface.aspect;


import lombok.extern.slf4j.Slf4j;
import ming.test.xface.annotation.AutoFill;
import ming.test.xface.dao.LogHistoryMapper;
import ming.test.xface.dao.UserMapper;
import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.pojo.LogHistory;
import ming.test.xface.utils.UserHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.elasticsearch.common.collect.HppcMaps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


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