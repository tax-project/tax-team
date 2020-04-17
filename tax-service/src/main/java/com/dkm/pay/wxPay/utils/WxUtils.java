package com.dkm.pay.wxPay.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * @author qf
 * @date 2020/3/19
 * @vesion 1.0
 **/
public class WxUtils {

   /**
    * 流转String
    * @param tInputStream
    * @return
    */
   public static String getStreamToStr(InputStream tInputStream) {
      if (tInputStream != null) {
         try {
            BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
            StringBuffer tStringBuffer = new StringBuffer();
            String sTempOneLine;
            while ((sTempOneLine = tBufferedReader.readLine()) != null) {
               tStringBuffer.append(sTempOneLine);
            }
            return tStringBuffer.toString();
         } catch (Exception ex) {
            ex.printStackTrace();
         }
      }
      return null;
   }

   /**
    * 元转分
    * @param price
    * @return
    */
   public static Integer changeY2F(double price) {
      DecimalFormat df = new DecimalFormat("#.00");
      price = Double.parseDouble(df.format(price));
      Integer money = (int) (price * 100);
      return money;
   }






}
