package com.dkm.qrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Component
public class QrCode {

   public void qrCode(String centext, HttpServletResponse response){
      Map<EncodeHintType, Object> hints = new HashMap<>(3);
      hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      hints.put(EncodeHintType.MARGIN,1);
      try {
         response.setContentType("image/png;charset=UTF-8");
         ServletOutputStream outputStream = response.getOutputStream();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         BitMatrix bitMatrix = new MultiFormatWriter().encode(centext, BarcodeFormat.QR_CODE, 200, 200, hints);
         MatrixToImageWriter.writeToStream(bitMatrix,"png",byteArrayOutputStream);
         //编码格式转换，由于前端用ajax设置img标签的src属性需要
         response.getOutputStream().write(byteArrayOutputStream.toByteArray());
         outputStream.flush();
         outputStream.close();
      } catch (WriterException | IOException e) {
         e.printStackTrace();
      }
   }
}
