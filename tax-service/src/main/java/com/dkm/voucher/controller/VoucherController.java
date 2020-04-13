package com.dkm.voucher.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.utils.StringUtils;
import com.dkm.voucher.entity.bo.OptionBo;
import com.dkm.voucher.entity.bo.ResultBo;
import com.dkm.voucher.entity.vo.VoucherReturnQrCodeVo;
import com.dkm.voucher.service.IVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Api(tags = "凭证Api")
@RestController
@RequestMapping("/v1/voucher")
public class VoucherController {

   @Autowired
   private IVoucherService voucherService;


   @ApiOperation(value = "展示消费者的二维码信息", notes = "展示消费者的二维码信息")
   @GetMapping("/listAllQrCode")
   @CrossOrigin
   @CheckToken
   public List<VoucherReturnQrCodeVo> listAllQrCode () {

      return voucherService.listAllQrCode();
   }


   @ApiOperation(value = "操作员上传凭证", notes = "操作员上传凭证")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "凭证id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "ticketUrl", value = "小票url", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "optionUser", value = "操作人名字", required = true, dataType = "String", paramType = "path"),
   })
   @PostMapping("/uploadVoucher")
   @CrossOrigin
   @CheckToken
   public ResultBo uploadVoucher (@RequestBody OptionBo bo) {

      if (bo.getId() == null || StringUtils.isBlank(bo.getOptionUser()) || StringUtils.isBlank(bo.getTicketUrl())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      voucherService.uploadVoucher(bo);

      ResultBo resultBo = new ResultBo();
      resultBo.setResult("ok");

      return resultBo;
   }
}
