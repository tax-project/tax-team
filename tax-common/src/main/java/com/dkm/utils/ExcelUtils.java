package com.dkm.utils;

import com.dkm.entity.exl.tax.ExlTaxUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel表格导出
 * @Author qf
 * @Date 2019/8/24
 * @Version 1.0
 */
public class ExcelUtils {

    static final short borderpx = 1;

    /**
     * 导出excel表格
     * @param head
     * @param body
     * @return
     */
    public static HSSFWorkbook expExcel(List<String> head, List<List<String>> body, Map<Integer,Double> map) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell= null;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorderStyle(cellStyle, borderpx);
        cellStyle.setFont(setFontStyle(workbook, "黑体", (short) 14));
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet.createFreezePane(0,1,0,1);

        for (int i = 0; i<head.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(head.get(i));
            cell.setCellStyle(cellStyle);
        }

        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        setBorderStyle(cellStyle2, borderpx);
        cellStyle2.setFont(setFontStyle(workbook, "宋体", (short) 12));
//        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        for (int i = 0; i < body.size(); i++) {
            row = sheet.createRow(i + 1);
            List<String> paramList = body.get(i);
            for (int p = 0; p < paramList.size(); p++) {
                cell = row.createCell(p);
                cell.setCellValue(paramList.get(p));
                cell.setCellStyle(cellStyle2);
            }
        }
        for (int i = 0, isize = head.size(); i < isize; i++) {
            sheet.autoSizeColumn(i);
        }

        //判断map是否为空
        if (map != null) {
            //创建最后一行
            row = sheet.createRow(body.size() + 1);
            //遍历map
            Set<Map.Entry<Integer,Double>> entries = map.entrySet();

            for (Map.Entry<Integer,Double> entry : new HashSet<>(entries)) {
                cell = row.createCell(entry.getKey());
                cell.setCellValue(entry.getValue());
                cell.setCellStyle(cellStyle2);
            }
        }

        return workbook;
    }


    /**
     * 用模板
     * 导出excel表格
     * @param body
     * @return
     */
    public static Workbook exp2Excel(List<List<String>> body, ExlTaxUtils contentExl) {

        Workbook workbook = null;
        try {

            FileInputStream is = new FileInputStream("/java/税务局优惠卷活动数据统计.xls");
//            File directory = new File("");
//            String coursePath = directory.getCanonicalPath();
//
//            String path = coursePath + "/exl/doc/导游接待生成表.xls";
//
//            FileInputStream is = new FileInputStream(path);

            workbook = WorkbookFactory.create (is);

            Sheet sheet = workbook.getSheet("Sheet1");

            Row row;

            Cell cell= null;

            CellStyle cellStyle2 = workbook.createCellStyle();
            cellStyle2.setFont(setFont ());
            cellStyle2.setAlignment(HorizontalAlignment.CENTER_SELECTION);
//            setBorderStyle((HSSFCellStyle) cellStyle2, borderpx);
//            cellStyle2.setFont(setFontStyle((HSSFWorkbook) workbook, "宋体", (short) 12));
//            cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //开始行
            Integer start = 3;
            if (body.size() > 1) {
                sheet.shiftRows(start, sheet.getLastRowNum(), body.size() - 1, true, false);

                Row row1 = sheet.getRow(1);
                CellStyle rowStyle = row1.getRowStyle();

                for (int i = start; i < start + body.size() - 1; i++) {
                    Row row2 = sheet.createRow(i);
                    if (rowStyle != null) {
                        row2.setRowStyle(rowStyle);
                        row2.setHeight(row1.getHeight());
                    }

                    for (int col = 0; col < row1.getLastCellNum(); col++) {
                        Cell cell1 = row1.getCell(col);
                        Cell cell2 = row2.createCell(col);
                        CellStyle style = cell1.getCellStyle();
                        if (style != null) {
                            cell2.setCellStyle(cell1.getCellStyle());
                        }

                    }
                }
            }

            for (int i = 0; i < body.size(); i++) {
                row = sheet.createRow(i + 3);
                List<String> paramList = body.get(i);
                for (int p = 0; p < paramList.size(); p++) {
                    cell = row.createCell(p);
                    cell.setCellValue(paramList.get(p));
                    cell.setCellStyle(cellStyle2);
                }
            }

            Integer size = body.size();
            if (body.size() <= 1) {
                size = 1;
            }

            //超市金额
            Row sheetRow = sheet.createRow(size+3);
            Cell rowCell4 = sheetRow.createCell(0);
            rowCell4.setCellValue("发放红包金额");
            rowCell4.setCellStyle(cellStyle2);
            Cell rowCell = sheetRow.createCell(1);
            rowCell.setCellValue(contentExl.getSupperMoney());
            rowCell.setCellStyle(cellStyle2);
            //成福记餐厅
            Cell cell1 = sheetRow.createCell(2);
            cell1.setCellValue(contentExl.getCRestaurantMoney());
            cell1.setCellStyle(cellStyle2);
            //曾小厨餐厅
            Cell cell2 = sheetRow.createCell(3);
            cell2.setCellValue(contentExl.getZRestaurantMoney());
            cell2.setCellStyle(cellStyle2);
            //渔乐圈餐厅
            Cell cell3 = sheetRow.createCell(4);
            cell3.setCellValue(contentExl.getYRestaurantMoney());
            cell3.setCellStyle(cellStyle2);

            Row sheetRow1 = sheet.createRow(size+4);
            Cell rowCell5 = sheetRow1.createCell(0);
            rowCell5.setCellValue("截止数据时间");
            rowCell5.setCellStyle(cellStyle2);

            Cell rowCell1 = sheetRow1.createCell(1);
            rowCell1.setCellValue(contentExl.getEndTime());
            rowCell1.setCellStyle(cellStyle2);
            //总金额
            Cell rowCell7 = sheetRow1.createCell(3);
            rowCell7.setCellValue("合计红包总金额");
            rowCell7.setCellStyle(cellStyle2);
            Cell row2 = sheetRow1.createCell(4);
            row2.setCellValue(contentExl.getAllMoney());
            row2.setCellStyle(cellStyle2);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return workbook;
    }


    /**
     * 文件输出
     * @param workbook 填充好的workbook
     * @param path 存放的位置
     */
    public static void outFile(HSSFWorkbook workbook, String path, HttpServletResponse response) {
        SimpleDateFormat fdate=new SimpleDateFormat("yyyyMMdd-HH点mm分");
        path = path.substring(0, path.lastIndexOf(".")) + fdate.format(new Date()) + path.substring(path.lastIndexOf("."));
        OutputStream os=null;
        File file = null;
        try {
            file = new File(path);
            String filename = file.getName();
            os = new FileOutputStream(file);
            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8"));
            os= new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            workbook.write(os);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.flush();
            os.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 文件输出
     * @param workbook 填充好的workbook
     * @param path 存放的位置
     */
    public static void outFile2(Workbook workbook, String path, HttpServletResponse response) {
        SimpleDateFormat fdate=new SimpleDateFormat("yyyyMMdd-HH点mm分");
        path = path.substring(0, path.lastIndexOf(".")) + fdate.format(new Date()) + path.substring(path.lastIndexOf("."));
        OutputStream os=null;
        File file = null;
        try {
            file = new File(path);
            String filename = file.getName();
            os = new FileOutputStream(file);
            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8"));
            os= new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            workbook.write(os);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.flush();
            os.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置字体样式
     * @param workbook 工作簿
     * @param name 字体类型
     * @param height 字体大小
     * @return HSSFFont
     */
    private static HSSFFont setFontStyle(HSSFWorkbook workbook, String name, short height) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(height);
        font.setFontName(name);
        return font;
    }

    /**
     * 设置单元格样式
     * @param cellStyle 工作簿
     * @param border border样式
     */
    private static void setBorderStyle(HSSFCellStyle cellStyle, short border) {
        // 下边框
//        cellStyle.setBorderBottom(border);
//        // 左边框
//        cellStyle.setBorderLeft(border);
//        // 上边框
//        cellStyle.setBorderTop(border);
//        // 右边框
//        cellStyle.setBorderRight(border);
    }


    public static Font setFont () {
        Font font = new Font() {
            @Override
            public void setFontName(String s) {
                setFontName("宋体");
            }

            @Override
            public String getFontName() {
                return null;
            }

            @Override
            public void setFontHeight(short i) {
                setFontHeight((short) 23);
            }

            @Override
            public void setFontHeightInPoints(short i) {

            }

            @Override
            public short getFontHeight() {
                return 20;
            }

            @Override
            public short getFontHeightInPoints() {
                return 20;
            }

            @Override
            public void setItalic(boolean b) {

            }

            @Override
            public boolean getItalic() {
                return false;
            }

            @Override
            public void setStrikeout(boolean b) {

            }

            @Override
            public boolean getStrikeout() {
                return false;
            }

            @Override
            public void setColor(short i) {
                setColor((short) 23);
            }

            @Override
            public short getColor() {
                return 0;
            }

            @Override
            public void setTypeOffset(short i) {

            }

            @Override
            public short getTypeOffset() {
                return 0;
            }

            @Override
            public void setUnderline(byte b) {

            }

            @Override
            public byte getUnderline() {
                return 0;
            }

            @Override
            public int getCharSet() {
                return 0;
            }

            @Override
            public void setCharSet(byte b) {

            }

            @Override
            public void setCharSet(int i) {

            }

            @Override
            public short getIndex() {
                return 0;
            }

            @Override
            public int getIndexAsInt() {
                return 0;
            }

            @Override
            public void setBold(boolean b) {

            }

            @Override
            public boolean getBold() {
                return false;
            }
        };
        return font;
    }

}
