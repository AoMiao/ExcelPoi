package com.excel.test.controller;

import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
* <p>
* create by AooMiao on 2018-08-07
*/
@Controller
@RequestMapping("/excel/*")
public class ExcelController {
    protected Logger _Log = Logger.getLogger(this.getClass().getSimpleName());
    
    @RequestMapping("downloadTemplate")
    public void downloadTemplate(HttpServletRequest request,HttpServletResponse response) throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        String title[] = new String[]{"字段1","字段2","字段3"};

        HSSFSheet sheet = workbook.createSheet("导入模板");
        sheet.setDefaultColumnWidth(18);

        // 樣式
        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP); // 垂直居中
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        HSSFDataFormat format = workbook.createDataFormat();  
        headStyle.setDataFormat(format.getFormat("@"));  
        
        HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
        columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        columnHeadStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        columnHeadStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        columnHeadStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        columnHeadStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

        int rowNO = 0;
        HSSFRow row = null;
        HSSFCell cell = null;

        row = sheet.createRow(rowNO++);
        row.setHeight((short) 400);
        //两个for循环，生成execl的标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(headStyle);
        }
        String[] status = new String[]{"启用","禁用"};//给某一列创建下拉选择框
        CellRangeAddressList regions = new CellRangeAddressList(1, 65535, 1, 1);//第八列
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(status);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidation);
        
        
        String fileName = "excel导入模板.xls";
        response.setContentType("application/download;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=\""
                + new String(fileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }

}
