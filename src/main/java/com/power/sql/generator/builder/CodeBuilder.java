package com.power.sql.generator.builder;


import com.boco.common.util.StringUtil;
import com.boco.common.util.ValidateUtil;
import com.power.sql.generator.model.DataModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeBuilder {

    private static final String EXCEPTION_MSG = " is unmarked by Table annotation";

    private static final String DATA_ERROR = "可能不符合模板规范，请检查！";

    //mysql数据据库的常用类型
    public static final List<String> mysqlType = new ArrayList<>(10);// 数据类型

    /**
     * 针对oracle的数据类型
     */
    public static final List<String> oracleType = new ArrayList<>();

    static {
        mysqlType.add("int");
        mysqlType.add("tinyint");
        mysqlType.add("bigint");
        mysqlType.add("datetime");
        mysqlType.add("char");
        mysqlType.add("varchar");
        mysqlType.add("date");
        mysqlType.add("text");
        mysqlType.add("longtext");
        mysqlType.add("decimal");

        oracleType.add("int");
        oracleType.add("smallint");
        oracleType.add("integer");
        oracleType.add("long");
        oracleType.add("float");
        oracleType.add("numeric");
        oracleType.add("number");
        oracleType.add("char");
        oracleType.add("varchar");
        oracleType.add("varchar2");
        oracleType.add("nvarchar2");
        oracleType.add("decimal");
        oracleType.add("datetime");
        oracleType.add("date");


    }


    public static String createSqlForOracle(Map<String, List<DataModel>> excelData) {
        StringBuilder builder = new StringBuilder();
        List<DataModel> listOfData;
        String tableName = null;

        for (Map.Entry<String, List<DataModel>> entry : excelData.entrySet()) {
            listOfData = entry.getValue();
            if (listOfData.size() < 1) {
                throw new RuntimeException("无法读取数据，工作表[" + tableName + "]" + DATA_ERROR);
            }

            StringBuilder comments = new StringBuilder();
            tableName = entry.getKey();
            builder.append("/*============================================================*/\n");
            builder.append("/* Table:").append(tableName).append("   */\n");
            builder.append("/*============================================================*/\n");
            builder.append("create table ").append(tableName).append("(\n");
            for (DataModel dataModel : listOfData) {

                String length = null;
                String precision = null;//decimal数据类型精度值设置
                // toLowerCase
                String type = dataModel.getType().trim().toLowerCase();
                if(type.contains("(")&& type.contains(")")){
                    int index = type.indexOf("(");
                    int endIndex = type.indexOf(")");
                    length = type.substring(index+1,endIndex);
                    precision = type.substring(index+1,endIndex);
                    type = type.substring(0,index);
                }else{
                    //检测长度和精度是否合法
                    Map<String,String> lengthEndPrecision = checkLengthAndPrecision(dataModel, oracleType, tableName);
                    length = lengthEndPrecision.get("length");
                    precision = lengthEndPrecision.get("precision");//decimal数据类型精度值设置
                }

                boolean isAllowEmpty = processAllowEmpty(dataModel, tableName);


                String fieldName = dataModel.getEnglishName().toLowerCase();
                String desc = dataModel.getDesc();// 获取说明
                String comment = null;// 注释
                if (StringUtil.isEmpty(desc)) {
                    comment = dataModel.getChineseName();
                } else {
                    // 注释= 中文字段名+说明信息
                    comment = dataModel.getChineseName() + ":" + desc;
                }

                //拼装注释
                comments.append("comment on column ").append(tableName).append(".").append(fieldName);
                comments.append(" is '").append(comment).append("';\n");

                boolean typeIsNotEmpty = StringUtil.isNotEmpty(type);
                if (typeIsNotEmpty && ("char".equals(type))){
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" char(").append(length).append(")");
                        builder.append(",\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" char(").append(length).append(")");
                        builder.append(" not null,\n");
                    }
                }else if (typeIsNotEmpty && ("varchar".equals(type))) {
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" varchar(").append(length).append(")");
                        builder.append(",\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" varchar(").append(length).append(")");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty && ("varchar2".equals(type))) {
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" varchar2(").append(length).append(")");
                        builder.append(",\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" varchar2(").append(length).append(")");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty&& "nvarchar2".equals(type)){
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" nvarchar2(").append(length).append(")");
                        builder.append(",\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" nvarchar2(").append(length).append(")");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty && "int".equals(type)) {
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" int(").append(length).append(")");
                        builder.append("',\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" int(").append(length).append(")");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty && "long".equals(type)) {
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" long(").append(length).append(")");
                        builder.append(",\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" long(").append(length).append(")");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty && "number".equals(type)) {
                    //精度未作处理
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" number(").append(length).append(")");
                        builder.append(",\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" number(").append(length).append(")");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty && "date".equals(type)) {
                    //可以使用系统时间如：hire_date      DATE  DEFAULT SYSDATE
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" date");
                        builder.append(" ,\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" date");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty && "datetime".equals(type)) {
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" datetime");
                        builder.append(" ,\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" datetime");
                        builder.append(" not null,\n");
                    }
                } else if (typeIsNotEmpty && "decimal".equals(type)) {
                    if (isAllowEmpty) {
                        builder.append("  ").append(fieldName).append(" decimal(").append(length).append(",");
                        builder.append(precision).append(")").append(",\n");
                    } else {
                        builder.append("  ").append(fieldName).append(" decimal(").append(length).append(",");
                        builder.append(precision).append(")").append(" not null ,\n");
                    }
                }

            }
            builder.deleteCharAt(builder.lastIndexOf(","));
            builder.append(");\n");
            builder.append(comments);
            builder.append("\n");
        }

        return builder.toString();

    }


    /**
     * 创建mysql数据的建库脚本
     *
     * @param excelData
     * @return
     */
    public static String createSqlForMysql(Map<String, List<DataModel>> excelData) {

        StringBuilder builder = new StringBuilder();
        List<DataModel> listOfData;
        String tableName = null;
        for (Map.Entry<String, List<DataModel>> entry : excelData.entrySet()) {

            listOfData = entry.getValue();
            if (listOfData.size() < 1) {
                throw new RuntimeException("无法读取数据，工作表[" + tableName + "]" + DATA_ERROR);
            }
            tableName = entry.getKey().toLowerCase();
            builder.append("/*============================================================*/\n");
            builder.append("/* Table:").append(tableName).append("   */\n");
            builder.append("/*============================================================*/\n");
            builder.append("create table ").append(tableName).append("(\n");
            builder.append("  id int auto_increment,\n");//强制加主键
            for (DataModel dataModel : listOfData) {
                // toLowerCase
                String type = dataModel.getType().trim().toLowerCase();

                //检测长度和精度是否合法
                Map<String,String> lengthEndPrecision = checkLengthAndPrecision(dataModel, mysqlType, tableName);
                String length = lengthEndPrecision.get("length");
                String precision = lengthEndPrecision.get("precision");//decimal数据类型精度值设置

                boolean primaryKey = StringUtil.strToBoolean("false");// 强制加主键
                boolean isAllowEmpty = processAllowEmpty(dataModel, tableName);

                if (primaryKey) {
                    builder.append("  id int auto_increment,\n");
                } else {
                    String fieldName = dataModel.getEnglishName().toLowerCase();
                    String desc = dataModel.getDesc();// 获取说明
                    String comment = null;// 注释
                    if (StringUtil.isEmpty(desc)) {
                        comment = dataModel.getChineseName();
                    } else {
                        // 注释= 中文字段名+说明信息
                        comment = dataModel.getChineseName() + ":" + desc;
                    }
                    boolean typeIsNotEmpty = StringUtil.isNotEmpty(type);
                    if (typeIsNotEmpty && ("varchar".equals(type) || "string".equals(type))) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" varchar(").append(length).append(")");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" varchar(").append(length).append(")");
                            builder.append(" not null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && "int".equals(type)) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" int (").append(length).append(")");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" int(").append(length).append(")");
                            builder.append(" not null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && "bigint".equals(type)) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" bigint (").append(length).append(")");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" bigint (").append(length).append(")");
                            builder.append(" not null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && "text".equals(type)) {
                        if (typeIsNotEmpty) {
                            builder.append("  ").append(fieldName).append(" text");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" text");
                            builder.append(" not null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && "longtext".equals(type)) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" longtext");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" longtext");
                            builder.append(" not null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && ("char".equals(type))) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" char(").append(length).append(")");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" char(").append(length).append(")");
                            builder.append(" not null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && "date".equals(type)) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" date");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" date");
                            builder.append(" not null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && "datetime".equals(type)) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" datetime");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" datetime");
                            builder.append(" default null comment '").append(comment).append("',\n");
                        }
                    } else if (typeIsNotEmpty && "decimal".equals(type)) {
                        if (isAllowEmpty) {
                            builder.append("  ").append(fieldName).append(" decimal(").append(length).append(",");
                            builder.append(precision).append(")").append(" default null comment '").append(comment)
                                    .append("',\n");
                        } else {
                            builder.append("  ").append(fieldName).append(" decimal(").append(length).append(",");
                            builder.append(precision).append(")").append(" default null comment '").append(comment)
                                    .append("',\n");
                        }
                    }
                }
            }
            builder.append("  primary key (id)\n");
            builder.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n");
        }
        return builder.toString();
    }


    /**
     * 处理字段设置是否为空
     *
     * @param dataModel
     * @param tableName
     * @return
     */
    private static boolean processAllowEmpty(DataModel dataModel, String tableName) {
        boolean isAllowEmpty;
        if (StringUtil.isEmpty(dataModel.getAllowEmpty())) {
            isAllowEmpty = true;// 如果查询到空则默认允许为空
        } else {
            String emptyText = dataModel.getAllowEmpty().trim().toLowerCase();// 获取填报要求
            if ("必填".equals(emptyText)) {
                isAllowEmpty = false;
            } else if ("可选".equals(emptyText)) {
                isAllowEmpty = true;
            } else if ("true".equals(emptyText)) {
                isAllowEmpty = false;
            } else if ("false".equals(emptyText)) {
                isAllowEmpty = true;
            } else {
                throw new RuntimeException("表[" + tableName + "][" + dataModel.getChineseName()
                        + "]字段填报要求填写错误,该项只能填写：必须、可选、true、false！");
            }
        }
        return isAllowEmpty;
    }

    /**
     * 检查字段长度和精度设置
     */
    private static Map<String,String> checkLengthAndPrecision(DataModel dataModel, List<String> typeLis, String tableName) {
        String type = dataModel.getType().trim().toLowerCase();
        String lengthStr = (StringUtil.isEmpty(dataModel.getLength())) ? "0" : dataModel.getLength().trim();
        String length = null;
        String precision = null;// decimal数据类型精度值设置
        if (typeLis.contains(type)) {
            if ((!"decimal".equals(type))) {
                if (ValidateUtil.isNonnegativeInteger(lengthStr)) {
                    length = lengthStr;
                } else {
                    throw new RuntimeException(
                            "表[" + tableName + "]中[" + dataModel.getChineseName() + "]字段长度只能大于或等于0的数字！");
                }
            } else {
                String pattern = "^[1-9]\\d*,\\d+$";// 匹配精度设置格式10,1前一位表示长度，后面表示精度
                if (ValidateUtil.validate(lengthStr, pattern)) {
                    String[] strArr = lengthStr.split(",");
                    length = strArr[0];// 第一个作为长度
                    precision = strArr[1];// 精度值
                } else {
                    throw new RuntimeException("表[" + tableName + "]中[" + dataModel.getChineseName()
                            + "]字段类型为decimal或者是数字,长度列设置参考格式：10,1(注意:10代表长度，英文逗号后表示小数点精确位)！");
                }
            }
        } else {
            throw new RuntimeException("表[" + tableName + "]中[" + dataModel.getChineseName()
                    + "]字段类型不符合模板,字段类型只能为:" + mysqlType.toString());
        }
        Map<String,String> map = new HashMap<>();
        map.put("length",length);
        map.put("precision",precision);
        return map;
    }
}
