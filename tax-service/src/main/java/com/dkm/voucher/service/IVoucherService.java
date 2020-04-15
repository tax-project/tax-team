package com.dkm.voucher.service;

import com.dkm.count.entity.bo.CountBO;
import com.dkm.count.entity.bo.ExcelBO;
import com.dkm.voucher.entity.bo.OptionBo;
import com.dkm.voucher.entity.vo.VoucherQrCodeVo;
import com.dkm.voucher.entity.vo.VoucherReturnQrCodeVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.dkm.voucher.entity.bo.IdVo;
import java.util.List;

/**
 * 凭证
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
public interface IVoucherService {

   /**
    * 增加二维码信息
    * @param vo
    * @return
    */
   IdVo insertVoucher (VoucherQrCodeVo vo);

   /**
    * 展示消费者的二维码信息
    * @return
    */
   List<VoucherReturnQrCodeVo> listAllQrCode ();

   /**
    * 操作员上传凭证
    * @param bo
    */
   void uploadVoucher (OptionBo bo);

   /**
    * 支付报表
    * @Author: HuangJie
    * @return 获得所有的支付记录
    */
   List<ExcelBO> listAllVoucher();

   /**
    * 获取消费总览
    * @Author: HuangJie
    * @return 消费总览数据
    */
   CountBO paymentOverview();

   /**
    * 导出支付记录的Excel表格
    * @return
    */
   HSSFWorkbook exportExcel();
}
