package spring.cloud.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class ExcelUtils
{
    /**
     * 数据样式 String left
     *
     * @author pengqiang
     * @date 2019/3/11
     * @param
     * @return
     */
    public static final int DS_FORMAT_STRING_LEFT = 1;

    /**
     * 数据样式 String center
     *
     * @author pengqiang
     * @date 2019/3/11
     * @param
     * @return
     */
    public static final int DS_FORMAT_STRING_CENTER = 2;

    /**
     * 数据样式 String right
     *
     * @author pengqiang
     * @date 2019/3/11
     * @param
     * @return
     */
    public static final int DS_FORMAT_STRING_RIGHT = 3;

    /**
     * 数据样式 int right
     *
     * @author pengqiang
     * @date 2019/3/11
     * @param
     * @return
     */
    public static final int DS_FORMAT_INT_RIGHT = 4;

    /**
     * 数据样式 float ###,###.## right
     *
     * @author pengqiang
     * @date 2019/3/11
     * @param
     * @return
     */
    public static final int DS_FORMAT_FLOAT_RIGHT = 5;

    /**
     * 数据样式 number: #.00% 百分比 right
     *
     * @author pengqiang
     * @date 2019/3/11
     * @param
     * @return
     */
    public static final int DS_FORMAT_NUMBER_RIGHT = 6;


    // excel默认宽度；
    // private static int width = 256*14;
    // 默认字体
    // private static String excelfont = "微软雅黑";

    /**
     * @param excelName 导出的EXCEL名字
     * @param sheetName 导出的SHEET名字 当前sheet数目只为1
     * @param headers   导出的表格的表头
     * @param ds_titles 导出的数据 map.get(key) 对应的 key
     * @param ds_format 导出数据的样式 1:String left; 2:String center 3:String right 4 int right
     *                  5:float ###,###.## right 6:number: #.00% 百分比 right
     * @param widths    表格的列宽度 默认为 256*14
     * @param data      数据集 List<Map>
     * @param response
     * @throws IOException
     */
    public static void export(String excelName, String sheetName, String[] headers, String[] ds_titles, int[] ds_format,
                              int[] widths, List<Map<String, Object>> data, HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        if (ds_format == null)
        {
            ds_format = new int[ds_titles.length];
            for (int i = 0; i < ds_titles.length; i++)
            {
                ds_format[i] = 1;
            }
        }
        // 设置文件名
        String fileName = "";
        if (StringUtils.isNotEmpty(excelName))
        {
            fileName = excelName;
        }
        // 创建一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建一个sheet
        HSSFSheet sheet = wb.createSheet(StringUtils.isNotEmpty(sheetName) ? sheetName : "excel");
        sheet.setDefaultColumnWidth(20);
        // 创建表头，如果没有跳过
        int headerrow = 0;
        // 表头样式
        // 单元格样式
        CellStyle titleStyle = wb.createCellStyle();
        CellStyle cellStyle = wb.createCellStyle();
        if (headers != null)
        {
            HSSFRow row = sheet.createRow(headerrow);
            // 字体样式
            Font font = wb.createFont();
            font.setBold(true);
            titleStyle.setFont(font);
            // 水平居中
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            // 垂直居中
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            // 水平居中
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            // 垂直居中
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            for (int i = 0; i < headers.length; i++)
            {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(titleStyle);
            }
            headerrow++;
        }
        // 表格主体 解析list
        if (data != null)
        {
            for (int i = 0; i < data.size(); i++)
            { // 行数
                HSSFRow row = sheet.createRow(headerrow);
                Map map = data.get(i);
                for (int j = 0; j < ds_titles.length; j++)
                { // 列数
                    HSSFCell cell = row.createCell(j);
                    Object o = map.get(ds_titles[j]);
                    if (o == null || "".equals(o))
                    {
                        cell.setCellValue("");
                    } else if (ds_format[j] == 4)
                    {
                        // int
                        cell.setCellValue((Long.valueOf((map.get(ds_titles[j])) + "")).longValue());
                    } else if (ds_format[j] == 5 || ds_format[j] == 6)
                    {
                        // float
                        cell.setCellValue((Double.valueOf((map.get(ds_titles[j])) + "")).doubleValue());
                    } else
                    {
                        cell.setCellValue(map.get(ds_titles[j]) + "");
                    }

                    cell.setCellStyle(cellStyle);
                }
                headerrow++;
            }
        }

        fileName = URLEncoder.encode(fileName, "UTF-8") + ".xls";
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", fileName);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
        session.setAttribute("state", "open");

    }

    /**
     * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
     *
     * @throws UnsupportedEncodingException
     */
    public static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) throws Exception
    {

        String filename = null;
        String agent = request.getHeader("USER-AGENT");
        if (null != agent)
        {
            if (-1 != agent.indexOf("Firefox"))
            {// Firefox
                filename = "=?UTF-8?B?"
                        + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
                        + "?=";
            } else if (-1 != agent.indexOf("Chrome"))
            {// Chrome
                filename = new String(pFileName.getBytes(), "ISO8859-1");
            } else
            {// IE7+
                filename = URLEncoder.encode(pFileName, "UTF-8");
                filename = filename.replace("+", "%20");
            }
        } else
        {
            filename = pFileName;
        }
        return filename;
    }
}
