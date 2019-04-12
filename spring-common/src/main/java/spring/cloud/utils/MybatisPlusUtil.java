//-------------------------------------------------------------------------
// Copyright (c) 2000-2016 Digital. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Digital
//
// Original author: allnas
//
//-------------------------------------------------------------------------
// APACHE
//-------------------------------------------------------------------------
package spring.cloud.utils;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.Objects;

public class MybatisPlusUtil
{
    public static void main(String[] args)
    {
        String[] models = { "spring-controller", "spring-dao", "spring-entity", "spring-service" };
        for (String model : models)
        {
            shell(model);
        }
    }

    private static void shell(String model)
    {
        boolean flag = false;
        File file = null;
        if (Objects.equals(model, "com/hu/demo/mapper"))
        {
            file = new File("dao");
            flag = true;
        }else {
            file = new File(model);
        }
        String path = file.getAbsolutePath();
        path = path.replace("iservice\\", "");

        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        if(flag) {
            gc.setOutputDir(path + "/src/main/java");// 需要生成的resources目录
        }else {
            gc.setOutputDir(path + "/src/main/java");// 需要生成的java目录
        }
        
        gc.setFileOverride(true);// 是否覆盖
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setMapperName("%sDao");
        gc.setServiceName("I%sService");
        gc.setAuthor("huwl");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                String type=fieldType.toUpperCase();
                if (type.equals("DATETIME") || type.equals("TIMESTAMP"))
                    return DbColumnType.LOCAL_DATE_TIME;
                else if (type.equals("DATE"))
                    return DbColumnType.LOCAL_DATE;
                else if(type.equals("TIME"))
                    return DbColumnType.TIME;
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义                    返回、非如下直接返回。
               else return super.processTypeConvert(fieldType);
            }
        });

        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl(
                "jdbc:mysql://172.16.1.11:3306/shop?characterEncoding=utf8");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        //strategy.setTablePrefix(new String[] { "t_" });
        //strategy.setInclude(new String[] { "insurance_policy","medicine_policy","user","medicine","user_insurance_policy","user_medicine_policy","user_medicine" });
        strategy.setInclude(new String[] { "operation_log"});
     // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        strategy.setEntityLombokModel(true);
        strategy.setSuperMapperClass("spring.cloud.dao.CommonDao");
        strategy.setSuperServiceClass("spring.cloud.iservice.CommonService");
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(null);
        pc.setEntity("spring.cloud.entity");
        pc.setMapper("spring.cloud.dao");
        pc.setXml("spring.cloud.mapper");
        pc.setController("spring.cloud.controller");
        pc.setService("spring.cloud.iservice");
        pc.setServiceImpl("spring.cloud.iservice.impl");

        mpg.setPackageInfo(pc);

        TemplateConfig tc = new TemplateConfig();
       switch (model)
        {
        case "spring-dao":
            tc.setEntity(null);
            //tc.setXml(null);
            tc.setController(null);
            tc.setService(null);
            tc.setServiceImpl(null);
            break;
        case "spring-mapper":
            tc.setEntity(null);
            tc.setMapper(null);
            tc.setController(null);
            tc.setService(null);
            tc.setServiceImpl(null);
            break;
        case "spring-entity":
            tc.setController(null);
            tc.setService(null);
            tc.setServiceImpl(null);
            tc.setMapper(null);
            tc.setXml(null);
            break;
        case "spring-service":
            tc.setController(null);
            tc.setMapper(null);
            tc.setXml(null);
            tc.setEntity(null);
            break;
        case "spring-controller":
            tc.setMapper(null);
            tc.setXml(null);
            tc.setService(null);
            tc.setServiceImpl(null);
            tc.setEntity(null);
            break;
        default:
            break;
        }
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
    }
}
