package spring.cloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Date: 2019/6/18 9:22
 * @Author: huwl
 * @Description:excel导出工具类
 */
@Slf4j
public class CommonExcelUtil {
    /**
     * @param sheetName, headers, titles, formats, data, request, response,dateFormat]
     * @Description:导出excel
     * @Return: void
     * @Author: huwl
     * @Date: 2019/6/18
     */
    public static void export(String excelName, String sheetName, String[][] headers, String[] titles, int[] formats, List<Map<String, Object>> data, HttpServletRequest request, HttpServletResponse response, String tempPath) {
        //自定义单元格样式，不传则默认1
        if (Objects.isNull(formats)) {
            int titleLength = titles.length;
            formats = new int[titleLength];
            for (int i = 0; i < titleLength; i++) {
                formats[i] = 1;
            }
        }
        //创建一个工作簿
        Workbook wb = null;
        try {
            //2007版本，扩展名是.xlsx
            wb = new SXSSFWorkbook(1000);
        } catch (Exception e) {
            //2003及以前版本，扩展名是.xls
            wb = new HSSFWorkbook();
        }
        //创建一个sheet,名称不能有：等特殊字符
        SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(sheetName.replaceAll(":", "-").replaceAll("：", ":"));
        sheet.trackAllColumnsForAutoSizing();
        //创建表头，如果没有跳过
        int headRow = 0;
        //设置表头样式
        CellStyle titleStyle = wb.createCellStyle();
        if (Objects.nonNull(headers)) {
            //字体样式
            Font font = wb.createFont();
            font.setBold(true);
            //表头加粗
            titleStyle.setFont(font);
            //水平居中
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            //垂直居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //合并数
            int mergeNum = 0;
            int headersLength = headers.length;
            for (int i = 0; i < headersLength; i++) {
                Row row = sheet.createRow(i);
                row.setHeight((short) 700);
                for (int j = 0; j < headers[i].length; j++) {
                    //创建表头单元格
                    Cell cell = row.createCell(j);
                    cell.setCellValue(headers[i][j]);
                    cell.setCellStyle(titleStyle);
                }
                headRow++;
            }
            //合并行时要跳过的行列
            Map<Integer, List<Integer>> map = new HashMap<>();
            //合并列
            for (int i = 0; i < headers[headersLength - 1].length; i++) {
                if ("".equals(headers[headersLength - 1][i])) {
                    for (int j = headersLength - 2; j >= 0; j--) {
                        if (!"".equals(headers[j][i])) {
                            sheet.addMergedRegion(new CellRangeAddress(j, headersLength - 1, i, i));
                            break;

                        } else {
                            if (map.containsKey(j)) {
                                List<Integer> list = map.get(j);
                                list.add(j);
                                map.put(j, list);
                            } else {
                                List<Integer> list = new ArrayList<>();
                                list.add(i);
                                map.put(j, list);
                            }
                        }
                    }
                }
            }
            //合并行
            for (int i = 0; i < headersLength - 1; i++) {
                for (int j = 0; j < headers[i].length; j++) {
                    List<Integer> list = map.get(i);
                    if (list == null || (list != null && !list.contains(j))) {
                        if ("".equals(headers[i][j])) {
                            mergeNum++;
                            if (mergeNum != 0 && j == (headers[i].length - 1)) {
                                //合并单元格
                                sheet.addMergedRegion(new CellRangeAddress(i, i, j - mergeNum, j));
                                mergeNum = 0;
                            }
                        } else {
                            if (mergeNum != 0) {
                                //合并单元格
                                sheet.addMergedRegion(new CellRangeAddress(i, i, j - mergeNum - 1, j - 1));
                                mergeNum = 0;
                            }
                        }
                    }
                }
            }
        }
        //设置数据单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        //表格主体解析list
        if (Objects.nonNull(data)) {
            //行数
            for (int i = 0; i < data.size(); i++) {
                //创建一行
                Row row = sheet.createRow(headRow);
                Map<String, Object> map = data.get(i);
                //列数
                for (int j = 0; j < titles.length; j++) {
                    sheet.autoSizeColumn(j);
                    sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 17 / 5);
                    setCellValue(map.get(titles[j]), formats[j], sheet, cellStyle, row.createCell(j), j);
                }
                headRow++;
            }
        }
        if (Objects.nonNull(request)) {
            try {
                download(request, response, wb, excelName, "xlsx");
            } catch (IOException e) {
                log.error("excel导出异常===[{}]", e.getMessage());
            }
        } else {
            try {
                File f = new File(tempPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                excelName = excelName.replaceAll(":", "").replaceAll("：", "");
                OutputStream outputStream = new FileOutputStream(tempPath + File.separator + excelName + ".xlsx");
                wb.write(outputStream);
                outputStream.flush();
                wb.close();
                outputStream.close();
            } catch (FileNotFoundException e) {
                log.error("文件不存在");
            } catch (IOException e) {
                log.error("本地生成文件失败");
            }
        }
    }

    private static void setCellValue(Object o, int format, Sheet sheet, CellStyle cellStyle, Cell cell1, int j) {
        Cell cell = cell1;
        cell.setCellStyle(cellStyle);
        if (Objects.isNull(o) || Objects.equals("", o)) {
            cell.setCellValue("");
        } else if(format==1){
            cell.setCellValue(o.toString());
        }else if (format == 2) {
            //int
            cell.setCellValue(Long.valueOf(o + ""));
        } else if (format == 3) {
            //float
            cell.setCellValue(Double.valueOf(o + ""));
        } else if (format == 4) {
            //日期
            cell.setCellValue(DateFormatUtils.format((Date) o, "yyyy-MM-dd"));
        } else if (format == 5) {
            //日期时间
            cell.setCellValue(DateFormatUtils.format((Date) o, "yyyy-MM-dd HH:mm"));
        } else {
            cell.setCellValue(new XSSFRichTextString((String) o));
        }
    }

    /**
     * @param response, wb, excelName]
     * @Description:下载excel
     * @Return: void
     * @Author: huwl
     * @Date: 2019/6/18
     */
    private static void download(HttpServletRequest request, HttpServletResponse response, Workbook wb, String excelName, String suffix) throws IOException {
        if (StringUtils.isEmpty(suffix)) {
            excelName = URLEncoder.encode(excelName, "UTF-8") + ".xls";
        } else {
            excelName = URLEncoder.encode(excelName, "UTF-8") + "." + suffix;
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //提示浏览器以下载形式
        response.setHeader("Content-disposition", "attachment;filename=" + excelName);
        //浏览器不缓存
        response.setHeader("Pragma", "No-cache");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public static void exportZip(HttpServletRequest request, HttpServletResponse response, String zipName, String tempPath) {
        ZipOutputStream zipOutputStream = null;
        try {
            zipName = URLEncoder.encode(zipName, "UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + zipName);
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
            File f = new File(tempPath);
            File[] files = f.listFiles();
            if (Objects.nonNull(files)) {
                for (int i = 0; i < files.length; i++) {
                    doCompress(files[i], zipOutputStream);
                }
            }
            zipOutputStream.close();
            f.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void doCompress(File file, ZipOutputStream zipOutputStream) {
        String entryName = file.getName();
        ZipEntry entry = new ZipEntry(entryName);
        try {
            zipOutputStream.putNextEntry(entry);
            int len = 0;
            byte[] buffer = new byte[1024];
            FileInputStream inputStream = new FileInputStream(file);
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, len);
            }
            zipOutputStream.flush();
            inputStream.close();
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
