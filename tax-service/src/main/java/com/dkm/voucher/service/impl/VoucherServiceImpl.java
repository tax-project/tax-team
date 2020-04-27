package com.dkm.voucher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.count.entity.bo.CountBO;
import com.dkm.count.entity.bo.ExcelBO;
import com.dkm.count.entity.bo.PayPageBO;
import com.dkm.count.entity.bo.PayPageDataBO;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.user.dao.UserMapper;
import com.dkm.user.entity.User;
import com.dkm.user.service.IUserService;
import com.dkm.utils.DateUtil;
import com.dkm.utils.ExcelUtils;
import com.dkm.utils.IdGenerator;
import com.dkm.voucher.dao.VoucherMapper;
import com.dkm.voucher.entity.Voucher;
import com.dkm.voucher.entity.bo.IdVo;
import com.dkm.voucher.entity.bo.OptionBo;
import com.dkm.voucher.entity.vo.VoucherQrCodeVo;
import com.dkm.voucher.entity.vo.VoucherReturnQrCodeVo;
import com.dkm.voucher.service.IVoucherService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

   private final String SUPPER_NAME = "超市";

   private final String ZENG_NAME = "曾小厨餐厅";

   private final String YU_NAME = "渔乐圈餐厅";

   private final String CHENG_NAME = "成福记餐厅";

   private final Integer SUPPER = 600;

   private final Integer ZENG_XIAO_CHU = 30;

   private final Integer YU_LE_QUAN = 50;

   private final Integer CHENG_FU_JI = 30;

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
   public synchronized IdVo insertVoucher(VoucherQrCodeVo vo) {

      LocalDate now = LocalDate.now();
      String date = DateUtil.formatDate(now);
      String start = date + " 00:00:00";
      String end = date + " 23:59:59";
      LocalDateTime startDate = DateUtil.parseDateTime(start);
      LocalDateTime endDate = DateUtil.parseDateTime(end);
      //先判断今天金额是否超标
      if (SUPPER_NAME.equals(vo.getTypeName())) {
         //超市
         LambdaQueryWrapper<Voucher> lambdaQueryWrapper = new LambdaQueryWrapper<Voucher>()
               .ge(Voucher::getDateTime,startDate)
               .le(Voucher::getDateTime,endDate)
               .eq(Voucher::getTypeName,SUPPER_NAME);

         Integer count = baseMapper.selectCount(lambdaQueryWrapper);

         if (count >= SUPPER) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "今日人数已达上限，请明天再来");
         }
      }

      if (ZENG_NAME.equals(vo.getTypeName())) {
         //曾小厨

         LambdaQueryWrapper<Voucher> wrapper = new LambdaQueryWrapper<Voucher>()
               .ge(Voucher::getDateTime,startDate)
               .le(Voucher::getDateTime,endDate)
               .eq(Voucher::getTypeName,ZENG_NAME);

         Integer count = baseMapper.selectCount(wrapper);

         if (count >= ZENG_XIAO_CHU) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "今日人数已达上限，请明天再来");
         }
      }

      if (YU_NAME.equals(vo.getTypeName())) {
         //渔乐圈餐厅
         LambdaQueryWrapper<Voucher> lambdaQueryWrapper = new LambdaQueryWrapper<Voucher>()
               .ge(Voucher::getDateTime,startDate)
               .le(Voucher::getDateTime,endDate)
               .eq(Voucher::getTypeName,YU_NAME);
         Integer count = baseMapper.selectCount(lambdaQueryWrapper);

         if (count >= YU_LE_QUAN) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "今日人数已达上限，请明天再来");
         }
      }

      if (CHENG_NAME.equals(vo.getTypeName())) {
         //成福记餐厅
         LambdaQueryWrapper<Voucher> lambdaQueryWrapper = new LambdaQueryWrapper<Voucher>()
               .ge(Voucher::getDateTime,startDate)
               .le(Voucher::getDateTime,endDate)
               .eq(Voucher::getTypeName,CHENG_NAME);
         Integer count = baseMapper.selectCount(lambdaQueryWrapper);

         if (count >= CHENG_FU_JI) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "今日人数已达上限，请明天再来");
         }
      }

      //先查询该用户是否已经有二维码了
      LambdaQueryWrapper<Voucher> wrapper = new LambdaQueryWrapper<Voucher>()
            .eq(Voucher::getUserId,vo.getUserId());

      Integer count = baseMapper.selectCount(wrapper);

      IdVo idVo = new IdVo();

      if (count < 3) {

         LambdaQueryWrapper<Voucher> lambdaQueryWrapper = new LambdaQueryWrapper<Voucher>()
               .ge(Voucher::getDateTime,startDate)
               .le(Voucher::getDateTime,endDate)
               .eq(Voucher::getUserId,vo.getUserId());

         Voucher selectOne = baseMapper.selectOne(lambdaQueryWrapper);

         if (selectOne != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您今天已领过卷了");
         }

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

         idVo.setId(id);
         return idVo;
      }

      return null;
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
      voucher.setUpdateUserId(user.getId());
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
      return this.allGetListExcelBO();
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

      if (countBO.getIssuedMoney()==null){
         countBO.setIssuedMoney(0D);
      }

      countBO.setRemainMoney(65100-countBO.getIssuedMoney());
      //超市人数
      countBO.setSupermarketMuch(baseMapper.supermarketMuch());
      //曾小厨餐厅
      countBO.setZenRestaurant(baseMapper.restaurantMuch());
      //渔乐圈餐厅
      countBO.setYvRestaurant(baseMapper.buildMuch());
      //成福记餐厅
      countBO.setChenRestaurant(baseMapper.intoRestaurant());

      return countBO;
   }

   @Override
   public HSSFWorkbook exportExcel() {

      List<String> heard = new ArrayList<>();
      heard.add("支付订单编号");
      heard.add("用户的微信昵称");
      heard.add("优惠卷名称");
      heard.add("小票或者发票地址");
      heard.add("转账金额");
      heard.add("转账时间");
      heard.add("验证税务人员微信昵称");
      heard.add("验证税务人员名字");
      List<List<String>> collect = this.allGetListExcelBO().stream().map(excelBO -> {
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

   @Override
   public PayPageBO payPageData(Integer page, Integer pageMuch) {
      UserLoginQuery userLogin = localUser.getUser("user");
      if (!userLogin.getRoleStatus().equals(ADMIN_NUM)){
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您的权限不够");
      }

      List<User> users = userMapper.selectList(null);
      Map<Long, User> collect = users.stream().collect(Collectors.toMap(User::getId, user -> user));

      ArrayList<Voucher> vouchers = baseMapper.payPageData((page-1) * pageMuch, pageMuch);
      List<PayPageDataBO> dataBOS = vouchers.stream().map(voucher -> {
         PayPageDataBO payPageDataBO = new PayPageDataBO();
         payPageDataBO.setUserNickName(collect.get(voucher.getUserId()) == null ? "未知" : collect.get(voucher.getUserId()).getWxNickName());
         payPageDataBO.setTaxNickName(collect.get(voucher.getUpdateUserId()) == null ? "未知" : collect.get(voucher.getUpdateUserId()).getWxNickName());
         payPageDataBO.setPayMoney(voucher.getPayMoney() == null ? 0D : voucher.getPayMoney());
         payPageDataBO.setPayTime(voucher.getPayTime() == null ? "未知" : DateUtil.formatDateTime(voucher.getPayTime()));
         payPageDataBO.setImageUrl(voucher.getTicketUrl() == null ? "未知" : voucher.getTicketUrl());
         return payPageDataBO;
      }).collect(Collectors.toList());

      QueryWrapper<Voucher> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("qr_code_status",1);
      Integer selectCount = baseMapper.selectCount(queryWrapper);
      PayPageBO payPageBO = new PayPageBO();
      payPageBO.setPageMuch(selectCount);
      payPageBO.setPayPages(dataBOS);
      return payPageBO;
   }

   @Override
   public void perfectDeductionAmount(Double money, Long id) {

      Voucher voucher = new Voucher();
      voucher.setId(id);
      voucher.setPayMoney(money);
      voucher.setPayTime(LocalDateTime.now());
      int update = baseMapper.updateById(voucher);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR);
      }
   }

   private List<ExcelBO> allGetListExcelBO(){
      QueryWrapper<Voucher> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("qr_code_status","1");
      List<Voucher> vouchers = baseMapper.selectList(queryWrapper);
      List<User> users = userMapper.selectList(null);
      Map<Long, User> collect = users.stream().collect(Collectors.toMap(User::getId, user -> user));
      return vouchers.stream().map(voucher -> {
         ExcelBO excelBO = new ExcelBO();
         excelBO.setId(voucher.getId());
         excelBO.setUserId(voucher.getUserId());
         excelBO.setUserNickName(collect.get(voucher.getUserId()).getWxNickName());
         excelBO.setTypeName(voucher.getTypeName());
         excelBO.setTicketUrl(voucher.getTicketUrl());
         excelBO.setPayMoney(voucher.getPayMoney()==null?0D:voucher.getPayMoney());
         excelBO.setPayTime(voucher.getPayTime()==null?"未支付":DateUtil.formatDateTime(voucher.getPayTime()));
         excelBO.setTaxUserId(voucher.getUpdateUserId());
         excelBO.setTaxNickName(collect.get(voucher.getUpdateUserId()).getWxNickName());
         excelBO.setTaxUserName(voucher.getUpdateUser());
         return excelBO;
      }).collect(Collectors.toList());
   }
}
