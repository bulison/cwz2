package cn.fooltech.fool_ops.utils;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.SpringBeanUtils;
import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelMapService;
import cn.fooltech.fool_ops.domain.excelmap.vo.ExcelMapVo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * excel导出类
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月30日
 */
public class ExcelUtils {

    public static final String EXCEL_PATH = "uploadxls";
    protected static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    protected static final String IMPROT_DATE = "yyyy-MM-dd";
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 导出到Excel到response
     *
     * @param clazz    :vo的类全路径
     * @param dataList ：vo数据集
     * @param fileName ：包含后缀的文件名
     * @param response ：http响应
     */
    public static void exportExcel(Class clazz, List dataList, String fileName,
                                   HttpServletResponse response) throws Exception {
        exportExcel(clazz, null, dataList, fileName, response);
    }

    //TODO aaaa
    /**
     * 导出到Excel到response
     *
     * @param clazz    :vo的类全路径
     * @param type     :类型标识
     * @param dataList ：vo数据集
     * @param fileName ：包含后缀的文件名
     * @param response ：http响应
     */
    public static void exportExcel(Class clazz, Integer type, List dataList,
                                   String fileName, HttpServletResponse response) throws Exception {

        if (StringUtils.isBlank(fileName)) {
            fileName = "export.xls";
        }
        fileName = URLEncoder.encode(fileName, "UTF-8");

        // 清空response
        //response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename="
                + fileName);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        exportExcel(clazz, type, dataList, response.getOutputStream());
    }

    /**
     * 导出Excel输出流
     *
     * @param clazz    :vo的类全路径
     * @param dataList ：vo数据集
     * @param os       ：输出流
     */
    public static void exportExcel(Class clazz, List dataList, OutputStream os)
            throws Exception {
        exportExcel(clazz, null, dataList, os);
    }

    /**
     * 导出Excel输出流
     *
     * @param clazz    :vo的类全路径
     * @param dataList ：vo数据集
     * @param os       ：输出流
     */
    public static void exportExcel(Class clazz, Integer type, List dataList,
                                   OutputStream os) throws Exception {
        ExcelMapService excelMapService = (ExcelMapService) SpringBeanUtils.getBean(ExcelMapService.class);
        List<ExcelMapVo> mapList = excelMapService.findByClazz(clazz, type, true, false);
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("sheet1");

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置单元格格式为文本格式
        HSSFDataFormat format = workbook.createDataFormat();
        style2.setDataFormat(format.getFormat("@"));
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 产生表格标题行
        HSSFRow title = sheet.createRow(0);
        for (int i = 0; i < mapList.size(); i++) {
            ExcelMapVo excelMap = mapList.get(i);
            HSSFCell cell = title.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(
                    excelMap.getCnName());
            cell.setCellValue(text);
        }

        int rowNum = 1;
        for (Object data : dataList) {

            HSSFRow row = sheet.createRow(rowNum);

            rowNum++;
            int colNum = 0;
            for (ExcelMapVo map : mapList) {
                HSSFCell cell = row.createCell(colNum);
                cell.setCellStyle(style2);

                colNum++;

                Field field = ReflectionUtils.getDeclaredField(clazz, map.getField());
                field.setAccessible(true);
                Object value = field.get(data);

                fillCellValue(cell, value, map.getProcessor());
            }
        }

		/*for (int i = 0; i < mapList.size(); i++) {
			sheet.autoSizeColumn((short)i);
		}*/

        workbook.write(os);
        os.flush();

    }

    /**
     * 获取处理器处理后的值
     *
     * @param processorStr
     * @param value
     * @return
     * @throws Exception
     */
    private static Object getProcessVal(String processorStr, Object value) throws Exception {
        if (StringUtils.isNotBlank(processorStr) && value != null) {
            Class processorClazz = Class.forName(processorStr);
            Processor processor = (Processor) processorClazz.newInstance();
            return processor.process(value);
        } else {
            return null;
        }
    }

    /**
     * 填写Cell的值
     *
     * @param cell
     * @param value
     * @param processor
     */
    private static void fillCellValue(HSSFCell cell, Object value, String processor) throws Exception {

        if (value != null && cell != null) {
            Object objp = getProcessVal(processor, value);
            boolean isNumber = objp == null ? false : NumberUtil.isNumber(objp.toString());
            if (value instanceof Date) {
                if (objp == null) {
                    Date date = (Date) value;
                    cell.setCellValue(date);
                } else {
                    cell.setCellValue(objp.toString());
                }
            } else if (value instanceof Integer) {
                if (objp == null) {
                    Integer obj = (Integer) value;
                    cell.setCellValue(obj);
                } else {
                    if (objp instanceof Integer) {
                        cell.setCellValue((Integer) objp);
                    } else if (isNumber) {
                        cell.setCellValue(Integer.parseInt(objp.toString()));
                    } else {
                        cell.setCellValue(objp.toString());
                    }
                }
            } else if (value instanceof Short) {
                if (objp == null) {
                    Short obj = (Short) value;
                    cell.setCellValue(obj);
                } else {
                    if (objp instanceof Short) {
                        cell.setCellValue((Short) objp);
                    } else if (isNumber) {
                        cell.setCellValue(Short.parseShort(objp.toString()));
                    } else {
                        cell.setCellValue(objp.toString());
                    }
                }
            } else if (value instanceof BigDecimal) {
                if (objp == null) {
                    BigDecimal obj = (BigDecimal) value;
                    cell.setCellValue(obj.doubleValue());
                } else {
                    if (objp instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) objp).doubleValue());
                    } else if (isNumber) {
                        cell.setCellValue(new BigDecimal(objp.toString()).doubleValue());
                    } else {
                        cell.setCellValue(objp.toString());
                    }
                }
            } else if (value instanceof Long) {
                if (objp == null) {
                    Long obj = (Long) value;
                    cell.setCellValue(obj);
                } else {
                    if (objp instanceof Long) {
                        cell.setCellValue((Long) objp);
                    } else if (isNumber) {
                        cell.setCellValue(Long.parseLong(objp.toString()));
                    } else {
                        cell.setCellValue(objp.toString());
                    }
                }
            } else if (value instanceof Double) {
                if (objp == null) {
                    Double obj = (Double) value;
                    cell.setCellValue(obj);
                } else {
                    if (objp instanceof Double) {
                        cell.setCellValue((Double) objp);
                    } else if (isNumber) {
                        cell.setCellValue(Double.parseDouble(objp.toString()));
                    } else {
                        cell.setCellValue(objp.toString());
                    }
                }
            } else if (value instanceof Float) {
                if (objp == null) {
                    Float obj = (Float) value;
                    cell.setCellValue(obj);
                } else {
                    if (objp instanceof Float) {
                        cell.setCellValue((Float) objp);
                    } else if (isNumber) {
                        cell.setCellValue(Float.parseFloat(objp.toString()));
                    } else {
                        cell.setCellValue(objp.toString());
                    }
                }
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    /**
     * 从Excel转换Vo
     *
     * @param clazz     vo全路径
     * @param tag       1:通过顺序号设置值;2:通过表头中文设置值
     * @param request
     * @param voResults 辅助列表存放转换结果
     */
    public static RequestResult importExcel(Class clazz, HttpServletRequest request, ImportType tag,
                                            final List<ImportVoBean> voResults) {
        return importExcelByType(clazz, request, tag, voResults, null);
    }

    /**
     * 从Excel转换Vo
     *
     * @param clazz     vo全路径
     * @param tag       1:通过顺序号设置值;2:通过表头中文设置值
     * @param request
     * @param voResults 辅助列表存放转换结果
     * @param type      类型标识
     */
    public static RequestResult importExcelByType(Class clazz, HttpServletRequest request, ImportType tag,
                                                  final List<ImportVoBean> voResults, Integer type) {
        try {
            Map<String, MultipartFile> files = getFiles(request);
            for (Entry<String, MultipartFile> entry : files.entrySet()) {
                MultipartFile file = entry.getValue();
                if (file != null) {
                    String fileFix = entry.getKey();
                    String fileType = fileFix.substring(fileFix.indexOf(".") + 1);
                    return importExcelByType(clazz, file.getInputStream(), tag, voResults, fileType, type);
                }
            }
            return new RequestResult(RequestResult.RETURN_SUCCESS, "找不到上传的附件");
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestResult(RequestResult.RETURN_SUCCESS, "导入异常");
        }
    }

    /**
     * 从HttpServletRequest获取文件
     *
     * @throws IOException
     */
    private static Map<String, MultipartFile> getFiles(HttpServletRequest request) throws IOException {
        Map<String, MultipartFile> files = Maps.newHashMap();

        HttpSession session = request.getSession();
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(session.getServletContext());
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iterator = multiRequest.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                files.put(file.getOriginalFilename(), file);

            }
        }

        return files;
    }


    /**
     * 从Excel转换Vo
     *
     * @tag 1:通过顺序号设置值;2:通过表头中文设置值
     */
    public static RequestResult importExcel(Class clazz, InputStream is, ImportType tag,
                                            final List<ImportVoBean> voResults, final String fileType) {
        RequestResult response = baseImportExcel(clazz, is, tag, voResults, fileType, null);
        return response;
    }

    /**
     * 根据类型从Excel转换Vo
     *
     * @param clazz     转换vo
     * @param is        文件流
     * @param tag       1:通过顺序号设置值;2:通过表头中文设置值
     * @param voResults 转换好的实力对象集合
     * @param fileType  文件类型
     * @param type      类型标识
     * @return
     */
    public static RequestResult importExcelByType(Class clazz, InputStream is, ImportType tag,
                                                  final List<ImportVoBean> voResults, final String fileType, Integer type) {
        RequestResult response = baseImportExcel(clazz, is, tag, voResults, fileType, type);
        return response;
    }

    /**
     * 从Excel转换Vo
     *
     * @tag 1:通过顺序号设置值;2:通过表头中文设置值
     * type  类型标识
     */
    public static RequestResult baseImportExcel(Class clazz, InputStream is, ImportType tag,
                                                final List<ImportVoBean> voResults, final String fileType, Integer type) {
        Workbook workbook = null;
        try {

            if (fileType.equals(XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (fileType.equals(XLSX)) {
                workbook = new XSSFWorkbook(is);
            } else {
                return new RequestResult(RequestResult.RETURN_FAILURE, "导入文件格式不支持");
            }

            ExcelMapService excelMapService = (ExcelMapService) SpringBeanUtils.getBean(ExcelMapService.class);
            List<ExcelMapVo> mapList = excelMapService.findByClazz(clazz, type, false, true);

            // 循环工作表Sheet
            // for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets();
            // numSheet++) {

            // 目前只取sheet1
            Sheet sheet = workbook.getSheetAt(0);

            //获取表头
            Row title = sheet.getRow(0);

            // 1--验证sheet表有效性
            // 2--验证表头有效性
            if (!checkSheet(sheet) || !checkTitle(title, mapList)) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "表头错误");
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

                int excelNum = rowNum + 1;
                Row row = sheet.getRow(rowNum);
                //如果一整行都为空，则跳过
                if (checkRowBlank(row)) {
                    continue;
                }

                Object data = clazz.newInstance();

                // 循环列Cell
                ExcelMapVo finded = null;

                boolean flag = true;

                for (int cellNum = 0; cellNum < mapList.size(); cellNum++) {
                    Cell cellVal = row.getCell(cellNum);
                    if (cellVal != null) {
                        cellVal.setCellType(Cell.CELL_TYPE_STRING);
                    }

                    if (tag == ImportType.SEQUNENCE) {
                        //判断Tag是通过顺序号设置值
                        finded = mapList.get(cellNum);
                    } else if (tag == ImportType.NAME) {
                        //判断Tag是通过表头中文设置值
                        finded = findExcelMapByName(mapList, title.getCell(cellNum).getStringCellValue());
                    }

                    if (finded == null) {
                        ImportVoBean err = new ImportVoBean(excelNum, "数据库未找到对应Excel表的匹配属性");
                        voResults.add(err);
                        flag = false;
                        break;
                    }

                    // 3--验证该行数据有效性
                    if (!checkNeedCell(cellVal, finded)) {
                        ImportVoBean err = new ImportVoBean(excelNum, finded.getCnName() + "|属性必填");
                        voResults.add(err);
                        flag = false;
                        break;
                    }

                    try {
                        Field field = clazz.getDeclaredField(finded.getField());
                        field.setAccessible(true);
                        if (StringUtils.isNotBlank(finded.getProcessor())) {
                            Class processorClazz = Class.forName(finded.getProcessor());
                            Processor processor = (Processor) processorClazz
                                    .newInstance();
                            String cellValStr = "";
                            if (cellVal != null) {
                                cellValStr = cellVal.getStringCellValue();
                            }
                            Object result = processor.reprocess(cellValStr);

                            field.set(data, result);
                        } else {

                            if (cellVal == null) continue;
                            String cellValue = cellVal.getStringCellValue();
                            if (cellValue == null) continue;

                            if (StringUtils.isBlank(cellValue)) continue;
                            //设置值
                            if (field.getType().equals(Integer.class)) {
                                field.set(data, Integer.valueOf(cellValue));
                            } else if (field.getType().equals(Long.class)) {
                                field.set(data, Long.valueOf(cellValue));
                            } else if (field.getType().equals(Short.class)) {
                                field.set(data, Short.valueOf(cellValue));
                            } else if (field.getType().equals(String.class)) {
                                field.set(data, cellValue);
                            } else if (field.getType().equals(BigDecimal.class)) {
                                field.set(data, new BigDecimal(cellValue));
                            } else if (field.getType().equals(Double.class)) {
                                field.set(data, Double.valueOf(cellValue));
                            } else if (field.getType().equals(Float.class)) {
                                field.set(data, Float.valueOf(cellValue));
                            } else if (field.getType().equals(Boolean.class)) {
                                field.set(data, Boolean.valueOf(cellValue));
                            } else if (field.getType().equals(Date.class)) {
                                field.set(data, DateUtilTools.string2Date(cellValue, IMPROT_DATE));
                            }
                        }
                    } catch (Exception e) {
                        ImportVoBean err = new ImportVoBean(excelNum, finded.getCnName() + "属性在数据转换时发生异常");
                        voResults.add(err);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    ImportVoBean bean = new ImportVoBean(excelNum, data);
                    voResults.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestResult(RequestResult.RETURN_FAILURE, "导入过程异常");
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put(Constants.WORK_BOOK, workbook);

        RequestResult result = new RequestResult(RequestResult.RETURN_SUCCESS,"操作成功!",workbook);
//        RequestResult result = new RequestResult();
//        result.setData(workbook);
        return result;
    }


    /**
     * 判断一整行是否都为空
     */
    private static boolean checkRowBlank(Row row) {
        if (row == null || row.getLastCellNum() == -1) return true;
        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            Cell cellVal = row.getCell(cellNum);
            if (cellVal == null) continue;
            cellVal.setCellType(Cell.CELL_TYPE_STRING);
            if (StringUtils.isNotBlank(cellVal.getStringCellValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据表头名称查找匹配的ExcelMap配置
     */
    private static ExcelMapVo findExcelMapByName(List<ExcelMapVo> list, String titleName) {
        if (StringUtils.isBlank(titleName)) return null;
        for (ExcelMapVo map : list) {
            if (map.getCnName().trim().equals(titleName.trim())) {
                return map;
            }
        }
        return null;
    }

    /**
     * 验证表格
     */
    private static boolean checkSheet(Sheet sheet) {
        if (sheet == null) {
            return false;
        }
        return true;
    }

    /**
     * 验证表头
     */
    private static boolean checkTitle(Row title, List<ExcelMapVo> mapList) {
        if (title == null) {
            return false;
        }
        if (title.getLastCellNum() < mapList.size()) {
            return false;
        }
        return true;
    }

    /**
     * 验证单元格
     */
    private static boolean checkNeedCell(Cell cellVal, ExcelMapVo finded) {
        if ((cellVal == null || StringUtils.isBlank(cellVal.getStringCellValue()))
                && finded.getNeed() != null && finded.getNeed() == 1) {
            return false;
        }
        return true;
    }

    /**
     * 生成excel文件
     *
     * @param voBeans
     * @param workbook
     * @param code
     */
    public static void processResultVos(final List<ImportVoBean> voBeans, Workbook workbook, String code) {

        try {
            // 目前只取sheet1
            Sheet sheet = workbook.getSheetAt(0);
            //获取表头
            Row title = sheet.getRow(0);

            FileSystem fileSystem = (FileSystem) SpringBeanUtils.getBean(FileSystem.class);

            for (int i = voBeans.size() - 1; i >= 0; i--) {
                ImportVoBean voBean = voBeans.get(i);

                Row row = sheet.getRow(voBean.getRow() - 1);
                if (voBean.getVaild()) {
                    sheet.removeRow(row);
                } else {
                    Cell cell = row.createCell(title.getLastCellNum(), Cell.CELL_TYPE_STRING);
                    cell.setCellValue(voBean.getMsg());
                }
            }
            File dir = new File(fileSystem.getRoot() + File.separator + EXCEL_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String finalPath = fileSystem.getRoot() + File.separator + EXCEL_PATH +
                    File.separator + code + ".xls";
            File outFile = new File(finalPath);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(finalPath);
            workbook.write(fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出到response
     *
     * @param titles   excel表头
     * @param datas    excel数据
     * @param response 返回体
     * @throws Exception
     */
    public static void exportExcel(final String[] titles, final List<Object[]> datas,
                                   HttpServletResponse response, String fileName) throws Exception {
        Assert.notNull(titles);
        Assert.notNull(datas);

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("sheet1");

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置单元格格式为文本格式
        HSSFDataFormat format = workbook.createDataFormat();
        style2.setDataFormat(format.getFormat("@"));
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 产生表格标题行
        HSSFRow title = sheet.createRow(0);
      
    	//判断标题数组是否包含[]子列，例如：期初[借方;贷方]
    	String sb = new String();
    	for(int i = 0; i < titles.length; i++){
    	 sb +=titles[i];
    	}
    	//若包含，则重新封装标题集合
		List<String> afterTitles=Lists.newArrayList();
    	int indexOf = sb.indexOf(";");
    	//若标题数组包含子列则从新构建标题集合，否则循环输出
    	if(indexOf!=-1){
    			for (int i = 0; i < titles.length; i++) {
    				String aa=titles[i];
    				if(aa.indexOf(";")!=-1){
    					String[] split = aa.split(";");
    					for (int j = 0; j < split.length; j++) {
    						String substring = aa.substring(0, aa.indexOf("["));
    						String name=split[j];
    						if(j==0){
    							afterTitles.add(name+"]");
    						}
    						else if(j+1==split.length){
    							afterTitles.add(substring+"["+name);
    						}
    						else if(j<split.length){
    						    afterTitles.add(substring+"["+name+"]");
    						}
    					}
    				}
    				else{
    					afterTitles.add(aa);
    				}
    			}
    			for (int i = 0; i < afterTitles.size(); i++) {
                    String titleStr = afterTitles.get(i);
                    HSSFCell cell = title.createCell(i);
                    cell.setCellStyle(style);
                    HSSFRichTextString text = new HSSFRichTextString(titleStr);
                    cell.setCellValue(text);
				}

    	}else{
            for (int k = 0; k < titles.length; k++) {
                String titleStr = titles[k];
                HSSFCell cell = title.createCell(k);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(titleStr);
                cell.setCellValue(text);
            }
    	}


        //填充数据
        for (int i = 0; i < datas.size(); i++) {
            Object[] rowData = datas.get(i);
            HSSFRow row = sheet.createRow(i + 1);//产生行
            //如果表头存在子列则导出添加子列后的长度
            if(afterTitles.size()>0){
                for (int j = 0; j < rowData.length && j < afterTitles.size(); j++) {
                    HSSFCell cell = row.createCell(j);//产生单元格
                    cell.setCellStyle(style2);

                    Object value = rowData[j];
                    fillCellValue(cell, value, null);
                }
            }else{
                for (int j = 0; j < rowData.length && j < titles.length; j++) {
                    HSSFCell cell = row.createCell(j);//产生单元格
                    cell.setCellStyle(style2);

                    Object value = rowData[j];
                    fillCellValue(cell, value, null);
                }
            }

        }

        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(fileName, "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.flush();
    }

    public enum ImportType {
        SEQUNENCE, NAME;
    }
}
