package com.power.generator;



import com.power.poi.excel.ExcelImportUtil;
import com.power.sql.generator.builder.CodeBuilder;
import com.power.sql.generator.builder.CodeOuter;
import com.power.sql.generator.model.DataModel;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author sunyu
 */
public class CreateSqlFromExcelTest {
    public static void main(String[] args) throws Exception {

        //根据excel生成sql信息
        String excel = "e:\\福建移动.xls";
        String outputSql = "d://test.sql";
        createSqlFromExcel(excel,outputSql);


        //读取本项目下的template模板输出脚本测试
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
        Map<String, List<DataModel>> map = ExcelImportUtil.readExcelIntoMap(file, 1, DataModel.class);
        String sql = CodeBuilder.createSqlForOracle(map);
        File sqlFile = new File(outDir);
        CodeOuter.writeFile(sql, sqlFile);
        System.out.println(sql);//输出sql
    }

    /**
     * 读取excel输入流生成sql脚本，目前只支持mysql常用字段类型
     *
     * @param input
     * @param outDir
     * @throws Exception
     */
    public static void createSqlFromExcel(InputStream input, String outDir) {
        Map<String, List<DataModel>> map = ExcelImportUtil.readExcelIntoMap(input, 1, DataModel.class);
        String sql = CodeBuilder.createSqlForOracle(map);
        File sqlFile = new File(outDir);
        CodeOuter.writeFile(sql, sqlFile);
        System.out.println(sql);//输出sql
    }
}
