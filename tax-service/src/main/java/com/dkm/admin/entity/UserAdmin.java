package com.dkm.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2020/4/21
 * @vesion 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_admin")
public class UserAdmin extends Model<UserAdmin> {

   private Long id;

   /**
    * 管理员名字
    */
   private String name;

   private String iphone;

   /**
    * 0-正常 1-停用
    */
   private Integer status;
}
