package com.dkm;


import com.dkm.aop.beans.Aspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@MapperScan("com.dkm.*.dao")
public class TaxServiceApplication  extends Aspect {

   public static void main(String[] args) {
      SpringApplication.run(TaxServiceApplication.class, args);
   }


   /**
    * 打包
    * @param builder
    * @return
    */
   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(TaxServiceApplication.class);
   }

}
