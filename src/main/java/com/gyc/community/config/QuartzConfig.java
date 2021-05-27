package com.gyc.community.config;

import com.gyc.community.quartz.AlphaJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

//仅仅第一次有用，第一次启动程序会把数据写入到数据库中，以后直接查询数据库
@Configuration
public class QuartzConfig {
    //FactoryBean可简化bean的实例化过程
    //1.通过factoryBean封装bean的实例化过程
    //2.将FactoryBean装配到容器里
    //3.将FactoryBean注入给其他的bean
    //4.该bean得到的是FactoryBean所管理的对象实例

    //配置jobdetail
    @Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true);//声明任务是持久保存
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }


    //配置trigger
    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());//
        return factoryBean;
    }
}
