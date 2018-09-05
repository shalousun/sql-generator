package com.power.sql.generator;

import com.power.poi.excel.ExcelImportUtil;
import com.power.sql.generator.builder.CodeBuilder;
import com.power.sql.generator.builder.CodeOuter;
import com.power.sql.generator.model.DataModel;
import com.power.sql.generator.util.PropertiesUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author yu 2018/9/5.
 */
public class AppStart {

    public static void main(String[] args) throws Exception {
        Properties properties = PropertiesUtil.loadFromFile("generator.properties");
        String excelFile = properties.getProperty("generator.excel");
        String output = properties.getProperty("generator.outfile");
        createSqlFromExcel(excelFile,output);

    }
    public static void createSqlFromExcel(String excelDir, String outDir) {
        File file = new File(excelDir);
        Map<String,String> tableNames = ExcelImportUtil.readExcelIntoMap(file,0,1);
        Map<String,String> tableComments = ExcelImportUtil.readExcelIntoMap(file,1,1);
        Map<String, List<DataModel>> map = ExcelImportUtil.readExcelIntoMap(file, 3, DataModel.class);
        String sql = CodeBuilder.createSqlForMysql(map,tableNames,tableComments);
        File sqlFile = new File(outDir);
        CodeOuter.writeFile(sql, sqlFile);
    }
}
