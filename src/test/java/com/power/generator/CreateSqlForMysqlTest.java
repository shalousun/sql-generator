package com.power.generator;

import com.power.poi.excel.ExcelImportUtil;
import com.power.sql.generator.builder.CodeBuilder;
import com.power.sql.generator.builder.CodeOuter;
import com.power.sql.generator.model.DataModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 创建mysql的脚本
 * @author yu 2018/9/5.
 */
public class CreateSqlForMysqlTest {
    public static void main(String[] args) throws Exception {

//        //根据excel生成sql信息
        String excel = "e:\\template.xls";
        String outputSql = "d://test.sql";
        createSqlFromExcel(excel,outputSql);


//        //读取本项目下的template模板输出脚本测试
//        String excelInResource = "template.xls";
//        InputStream input = Thread.currentThread().getContextClassLoader()
//                .getResourceAsStream(excelInResource);
//        createSqlFromExcel(input, outputSql);

    }

    /**
     * 从excel表中读取字段信息生成sql脚本,目前只支持mysql常用字段类型
     *
     * @param excelDir excel文件路径
     * @param outDir   sql输出路径
     */
    public static void createSqlFromExcel(String excelDir, String outDir) {
        File file = new File(excelDir);
        Map<String,String> tableNames = ExcelImportUtil.readExcelIntoMap(file,0,1);
        Map<String,String> tableComments = ExcelImportUtil.readExcelIntoMap(file,1,1);
        Map<String, List<DataModel>> map = ExcelImportUtil.readExcelIntoMap(file, 3, DataModel.class);
        String sql = CodeBuilder.createSqlForMysql(map,tableNames,tableComments);
        File sqlFile = new File(outDir);
        CodeOuter.writeFile(sql, sqlFile);
        System.out.println(sql);//输出sql
    }


}
