package com.excel.test.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel数据导入，支持xls、xlsx两种格式的导入
 * 
 */
public class ImportExcelUtil {

    /**
     * 读取Office 2003 excel
     * @param file 读取文件名 文件名
     * @param lastcellnum 读取列数 当按一行行读取时，每一行读取的列数
     * @param sheetName 单元名称（工作薄的名称）
     * @return
     * @throws IOException
     */
    public static List<List<Object>> read2003Excel(File file, int lastcellnum, String sheetName) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = null;
        if (null != sheetName && sheetName.length() > 0) {
            sheet = hwb.getSheet(sheetName);// 获取指定工作表的excel内容
        } else {
            sheet = hwb.getSheetAt(0);// 默认获取第一个工作表
        }
        if (null != sheet) {
            Object value = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {//从一行遍历到物理行数（包括空行）
                row = sheet.getRow(i);
                if (row == null) {//行为空，跳过
                    continue;
                }
                List<Object> linked = new ArrayList<Object>();//记录行的内容
                for (int j = row.getFirstCellNum(); j < lastcellnum; j++) {//该行第一个单元格遍历到列数，简单来说就是行的内容遍历
                    cell = row.getCell(j);//取出当前单元格
                    if (cell == null) {
                        linked.add("");
                        continue;
                    }
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number
                                                              // String
                    // 字符
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                    DecimalFormat nf = new DecimalFormat("#");// 格式化数字
                    DecimalFormat mf = new DecimalFormat("######0.00");// 保留两位小数
                    switch (cell.getCellType()) {//判断当前单元格的格式，对value进行适当的类型转换
                    case XSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());

                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else if ("0.00_ ".equals(cell.getCellStyle().getDataFormatString())) {
                            value = mf.format(cell.getNumericCellValue());
                        } else if ("#,##0.00".equals(cell.getCellStyle().getDataFormatString())) {
                            value = mf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        }
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                    }
                    linked.add(value);
                }
                list.add(linked);
            }

        }
        return list;
    }

    /**
     * 读取Office 2007 excel
     * @param file 读取文件名 文件名
     * @param isReadColumn 是否读取列 false表示一行行读入，true表示一列列读取。
     * @param lastcellnum 读取列数 当按一行行读取时，每一行读取的列数
     * @param sheetName 单元名称（工作薄的名称）
     * @return
     * @throws IOException
     */
    public static List<List<Object>> read2007Excel(File file, boolean isReadColumn, int lastcellnum, String sheetName)
            throws IOException {

        List<List<Object>> list = new ArrayList<List<Object>>();
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));

        XSSFSheet sheet = null;
        if (null != sheetName && sheetName.length() > 0) {
            sheet = xwb.getSheet(sheetName);// 读取指定sheet
        } else {
            sheet = xwb.getSheetAt(0);// 读取第一章表格内容
        }

        if (null != sheet) {
            if (isReadColumn) {
                Object value = null;
                XSSFRow row = sheet.getRow(0);
                if (null != row) {
                    int rowNum = row.getLastCellNum();
                    XSSFCell cell = null;
                    for (int k = 0; k < rowNum; k++) {
                        List<Object> linked = new ArrayList<Object>();
                        for (int i = 0; i <= sheet.getPhysicalNumberOfRows(); i++) {
                            row = sheet.getRow(i);
                            if (row == null) {
                                continue;
                            }
                            cell = row.getCell(k);
                            if (cell == null) {
                                linked.add("");
                                continue;
                            }
                            DecimalFormat df = new DecimalFormat("0");// 格式化
                                                                      // number
                                                                      // String
                            // 字符
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                            DecimalFormat nf = new DecimalFormat("#");// 格式化数字
                            DecimalFormat mf = new DecimalFormat("######0.00");// 保留两位小数
                            switch (cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                    value = df.format(cell.getNumericCellValue());

                                } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                                    value = nf.format(cell.getNumericCellValue());
                                } else if ("0.00_ ".equals(cell.getCellStyle().getDataFormatString())) {
                                    value = mf.format(cell.getNumericCellValue());
                                } else if ("#,##0.00".equals(cell.getCellStyle().getDataFormatString())) {
                                    value = mf.format(cell.getNumericCellValue());
                                } else {
                                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                                }
                                break;
                            case XSSFCell.CELL_TYPE_BOOLEAN:
                                value = cell.getBooleanCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_BLANK:
                                value = "";
                                break;
                            default:
                                value = cell.toString();
                            }
                            linked.add(value);
                        }
                        list.add(linked);
                    }
                }
            } else {
                Object value = null;
                XSSFRow row = null;
                XSSFCell cell = null;
                for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
                    row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    List<Object> linked = new ArrayList<Object>();
                    for (int j = row.getFirstCellNum(); j < lastcellnum; j++) {
                        cell = row.getCell(j);
                        if (cell == null) {
                            linked.add("");
                            continue;
                        }
                        DecimalFormat df = new DecimalFormat("0");// 格式化 number
                                                                  // String
                        // 字符
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                        DecimalFormat nf = new DecimalFormat("#");// 格式化数字
                        DecimalFormat mf = new DecimalFormat("######0.00");// 保留两位小数

                        switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            String test = cell.getCellStyle().getDataFormatString();
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else if ("0.00_ ".equals(cell.getCellStyle().getDataFormatString())) {
                                value = mf.format(cell.getNumericCellValue());
                            } else if ("#,##0.00".equals(cell.getCellStyle().getDataFormatString())) {
                                value = mf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                        }
                        linked.add(value);
                    }
                    list.add(linked);
                }
            }
        }
        return list;
    }

    /*
     * public static void main(String[] args) { try { String excelPath =
     * "E:/efgh.xlsx"; List<List<Object>> list = new ArrayList<List<Object>>();
     * String str = excelPath.substring(excelPath.lastIndexOf(".") + 1); if
     * (str.length() > 0 && str.equals("xlsx")) list =
     * ImportExcelUtil.read2007Excel(new File(excelPath),false,8,""); else
     * if(str.length() > 0 && str.equals("xls")) list =
     * ImportExcelUtil.read2003Excel(new File(excelPath),false,8,"奖学贷"); for
     * (int i = 0; i < list.size(); i++) { System.out.println("第"+(i+1)+"行");
     * for (int j = 0; j < list.get(i).size(); j++) {
     * System.out.print(list.get(i).get(j)+","); } System.out.println(); } }
     * catch (IOException e) { _Log.error(e, e); } }
     */
}
