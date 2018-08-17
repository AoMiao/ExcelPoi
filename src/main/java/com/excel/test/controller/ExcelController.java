package com.excel.test.controller;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.excel.test.util.ImportExcelUtil;



/**
* excel的下载，导入<p>
* create by AooMiao on 2018-08-07
*/
@Controller
@RequestMapping("/excel/*")
public class ExcelController {
    protected Logger _Log = Logger.getLogger(this.getClass().getSimpleName());
    
    /**
     * 下载excel导入模板
     * 这里选择excel2003的模板生成(HSSFWorkbook)
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("downloadTemplate")
    public void downloadTemplate(HttpServletRequest request,HttpServletResponse response) throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        String title[] = new String[]{"字段1","字段2","字段3"};//标题

        HSSFSheet sheet = workbook.createSheet("导入模板");//工作表名称
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
        String[] status = new String[]{"启用","禁用"};//创建下拉选择框
        CellRangeAddressList regions = new CellRangeAddressList(1, 65535, 1, 1);//给excel指定区域设置下拉框，这里指定了2行到65535行（最大）,第2列
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(status);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidation);//设置下拉框
        
        
        String fileName = "excel导入模板.xls";//文件名
        response.setContentType("application/download;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=\""
                + new String(fileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }
    
    /**
     * 调整导入页面
     */
    @RequestMapping("/import")
    public String importInto(HttpServletRequest request, HttpServletResponse response) {
        return "/excel/import";
    }
    
    /**
     * 导入excel
     * @param file
     */
    @RequestMapping("doImport")
    public String doImport(@RequestParam("files[]") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        _Log.info("处理上传的文件--开始");
        byte[] bytes = file.getBytes();
        String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
        _Log.info(uploadPath);
        Path path = Paths.get(uploadPath + file.getOriginalFilename());
        path = Files.write(path, bytes);
        _Log.info("处理上传的文件--结束");
        
        /**
         * 注意这里应该判断文件的扩展名来判断是xls(2003)或xlxs(2007以上)
         * 因为xls和xlxs用的poi的api都不一样
         * xls:HSSFWorkbook
         * xlxs:XSSFworkbook
         * 由于下载模板我选用了xls,这里我就不判断了
         */
        List<List<Object>> list = ImportExcelUtil.read2003Excel(path.toFile(), 3, "");//封装excel的内容返回list列表
        //接下来就是导入的逻辑代码
        list = deleteAllEmptyElement(list);
        for (List<Object> objectList : list) {
            Object[] rows = objectList.toArray();
            _Log.info(rows[0].toString());//字段1
            _Log.info(rows[1].toString());//字段2
            _Log.info(rows[2].toString());//字段3
        }
        return "/excel/import";
    }
    
    /**
     * 清空Excel中全为空的行
     * @param list
     * @return
     */
    private <T> List<List<T>> deleteAllEmptyElement(List<List<T>> list){
        Iterator<List<T>> it = list.iterator();
        while(it.hasNext()){
            boolean isEmptyList = true;
            List<T> listElement = it.next();
            for (T element : listElement) {
                if(element != null && element.toString().length()>0 && !element.toString().equals("")){
                    isEmptyList = false;
                }
            }
            if(isEmptyList){
                it.remove();
            }
        }
        return list;
    }
}
