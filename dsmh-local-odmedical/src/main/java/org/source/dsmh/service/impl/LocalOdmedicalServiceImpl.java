package org.source.dsmh.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.source.dsmh.model.AppUser;
import org.source.dsmh.service.LocalDataService;
import org.source.dsmh.service.LocalEnterService;
import org.source.dsmh.utils.DataTemplate;
import org.source.dsmh.utils.mongodb.SpringContextUtil;
import org.source.dsmh.utils.mongodb.LogMsg;
import org.springframework.stereotype.Service;

/**
 * local-odmedical入口，只做功能分发，不处理具体业务
 * 
 * @author hzc
 *
 */
@com.alibaba.dubbo.config.annotation.Service(group = "local-odmedical")
@Service
public class LocalOdmedicalServiceImpl implements LocalEnterService {

	private static final Log log = LogFactory.getLog(LocalOdmedicalServiceImpl.class);
	private LocalDataService localDataService;

	@Override
	public DataTemplate localVisit(String paramJson, String function, String method, String appUser) {

		// 根据方法名获取实例
		try {
			localDataService = SpringContextUtil.getBean("local-" + function + "-" + method, LocalDataService.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(LogMsg.getLogMsgForError(function, method, paramJson, "local-" + function + "-" + method,
					"获取方法实例错误"), e);
			return DataTemplate.error("功能暂未实现！");
		}

		if (localDataService == null) {
			return DataTemplate.error("功能暂未实现！");
		} else {
			return localDataService.localData(paramJson, function, method, appUser);
		}
	}

}
