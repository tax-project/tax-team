package com.dkm.aop.beans;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * @author qf
 * @date 2020/3/6
 * @vesion 1.0
 **/
public abstract class Aspect extends SpringBootServletInitializer {


   /**
    * 多表分页的bean
    * @return
    */
   @Bean
   public PaginationInterceptor paginationInterceptor() {
      return new PaginationInterceptor();
   }


}
