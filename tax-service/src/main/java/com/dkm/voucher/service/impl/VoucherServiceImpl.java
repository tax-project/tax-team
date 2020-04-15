package com.dkm.voucher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.count.entity.bo.CountBO;
import com.dkm.count.entity.bo.ExcelBO;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.user.dao.UserMapper;
import com.dkm.user.entity.User;
import com.dkm.user.service.IUserService;
import com.dkm.utils.ExcelUtils;
import com.dkm.utils.IdGenerator;
import com.dkm.voucher.dao.VoucherMapper;
import com.dkm.voucher.entity.Voucher;
import com.dkm.voucher.entity.bo.OptionBo;
import com.dkm.voucher.entity.vo.VoucherQrCodeVo;
import com.dkm.voucher.entity.vo.VoucherReturnQrCodeVo;
import com.dkm.voucher.service.IVoucherService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {


   private final static Integer ADMIN_NUM = 3;

   @Autowired
   private IdGenerator idGenerator;

   @Autowired
   private IUserService userService;

   @Autowired
   private LocalUser localUser;

   @Autowired
   private UserMapper userMapper;

   /**
    * 增加凭证(二维码信息)
    * @param vo
    */
   @Override
   public void insertVoucher(VoucherQrCodeVo vo) {

      //先查询该用户是否已经有二维码了
      LambdaQueryWrapper<Voucher> wrapper = new LambdaQueryWrapper<Voucher>()
            .eq(Voucher::getUserId,vo.getUserId());

      Integer count = baseMapper.selectCount(wrapper);

      if (count < 3) {
         Voucher voucher = new Voucher();

         Long id = idGenerator.getNumberId();

         voucher.setId(id);
         voucher.setQrCodeStatus(0);
         voucher.setQrCodeUrl(vo.getQrCodeUrl());
         voucher.setTypeName(vo.getTypeName());
         voucher.setTypeMoney(vo.getTypeMoney());
         voucher.setUserId(vo.getUserId());

         int insert = baseMapper.insert(voucher);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加二维码信息失败");
         }
      }

   }

   /**
    * 根据用户id返回展示所有消费者的二维码信息
    * @return
    */
   @Override
   public List<VoucherReturnQrCodeVo> listAllQrCode() {

      UserLoginQuery user = localUser.getUser("user");

      LambdaQueryWrapper<Voucher> wrapper = new LambdaQueryWrapper<Voucher>()
            .eq(Voucher::getUserId,user.getId());

      List<Voucher> list = baseMapper.selectList(wrapper);

      List<VoucherReturnQrCodeVo> result = new ArrayList<>();

      for (Voucher voucher : list) {
         VoucherReturnQrCodeVo vo = new VoucherReturnQrCodeVo();
         vo.setId(voucher.getId());
         vo.setTypeName(voucher.getTypeName());
         vo.setTypeMoney(voucher.getTypeMoney());
         vo.setQrCodeStatus(voucher.getQrCodeStatus());
         vo.setQrCodeUrl(voucher.getQrCodeUrl());
         result.add(vo);
      }
      return result;
   }


   /**
    * 操作员上传凭证
    * @param bo
    */
   @Override
   public void uploadVoucher(OptionBo bo) {

      UserLoginQuery user = localUser.getUser("user");
      //先判断该操作员有没有权限上传
      Integer status = userService.queryOptionStatus(user.getId());

      if (status == 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您今天没有上传权限,请联系管理员");
      }

      Voucher byId = baseMapper.selectById(bo.getId());

      if (byId == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"凭证id有误");
      }

      if (byId.getQrCodeStatus() == 1) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该二维码已失效");
      }

      //有权限操作
      Voucher voucher = new Voucher();
      voucher.setId(bo.getId());
      voucher.setDateTime(LocalDateTime.now());
      voucher.setTicketUrl(bo.getTicketUrl());
      voucher.setUpdateUser(bo.getOptionUser());
      voucher.setUpdateUserId(bo.getId());
      //修改二维码的状态，表示失效
      voucher.setQrCodeStatus(1);

      //修改
      int update = baseMapper.updateById(voucher);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "上传失败");
      }
   }

   /**
    * @Author: HuangJie
    * @return 获得所有的支付记录
    */
   @Override
   public List<ExcelBO> listAllVoucher() {
      UserLoginQuery localUserUser = localUser.getUser("user");
      Integer roleStatus = localUserUser.getRoleStatus();
      if (!roleStatus.equals(ADMIN_NUM)){
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您的权限不够");
      }
      List<Voucher> vouchers = baseMapper.selectList(null);
      List<User> users = userMapper.selectList(null);
      Map<Long, User> collect = users.stream().collect(Collectors.toMap(User::getId, user -> user));
      return vouchers.stream().map(voucher -> {
         ExcelBO excelBO = new ExcelBO();
         excelBO.setId(voucher.getId());
         excelBO.setUserId(voucher.getUserId());
         excelBO.setUserNickName(collect.get(voucher.getUserId()).getWxNickName());
         excelBO.setTypeName(voucher.getTypeName());
         excelBO.setTicketUrl(voucher.getTicketUrl());
         excelBO.setPayMoney(voucher.getPayMoney());
         excelBO.setPayTime(voucher.getPayTime());
         excelBO.setTaxUserId(voucher.getUpdateUserId());
         excelBO.setTaxNickName(collect.get(voucher.getUpdateUserId()).getWxNickName());
         excelBO.setTaxUserName(voucher.getUpdateUser());
         return excelBO;
      }).collect(Collectors.toList());
   }

   @Override
   public CountBO paymentOverview() {
      UserLoginQuery user = localUser.getUser("user");
      if (!user.getRoleStatus().equals(ADMIN_NUM)){
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您的权限不够");
      }
      CountBO countBO = baseMapper.paymentOverview();
      if (countBO==null){
         throw new ApplicationException(CodeType.SERVICE_ERROR, "查询异常");
      }
      countBO.setRemainMoney(100000-countBO.getIssuedMoney());
      countBO.setSupermarketMuch(baseMapper.supermarketMuch());
      countBO.setRestaurantMuch(baseMapper.restaurantMuch());
      countBO.setBuildMuch(baseMapper.buildMuch());
      return countBO;
   }

   @Override
   public HSSFWorkbook exportExcel() {
      UserLoginQuery user = localUser.getUser("user");
      if (!user.getRoleStatus().equals(ADMIN_NUM)){
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您的权限不够");
      }
      List<String> heard = new ArrayList<>();
      heard.add("支付订单编号");
      heard.add("用户的微信昵称");
      heard.add("优惠卷名称");
      heard.add("小票或者发票地址");
      heard.add("转账金额");
      heard.add("转账时间");
      heard.add("验证税务人员微信昵称");
      heard.add("验证税务人员名字");

      List<List<String>> collect = this.listAllVoucher().stream().map(excelBO -> {
         List<String> list = new ArrayList<>();
         list.add(String.valueOf(excelBO.getId()));
         list.add(String.valueOf(excelBO.getUserNickName()));
         list.add(String.valueOf(excelBO.getTypeName()));
         list.add(String.valueOf(excelBO.getTicketUrl()));
         list.add(String.valueOf(excelBO.getPayMoney()));
         list.add(String.valueOf(excelBO.getPayTime()));
         list.add(String.valueOf(excelBO.getTaxNickName()));
         list.add(String.valueOf(excelBO.getTaxUserName()));
         return list;
      }).collect(Collectors.toList());

      return ExcelUtils.expExcel(heard, collect, null);


   }
}
