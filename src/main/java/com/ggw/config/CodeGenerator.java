package com.ggw.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.baomidou.mybatisplus.generator.fill.Column;
import java.util.Arrays;
import java.util.Collections;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import java.util.List;

/**
 * 代码生成器 ，先修改下面的常量配置参数，然后执行 main方法
 */
public class CodeGenerator {

  public static void main(String[] args) {
    System.out.println("=====================数据库配置=======================");
    String url = "jdbc:postgresql://116.205.242.73:5432/postgres?useUnicode=true&characterEncoding=utf8";
    String username = "postgres";
    String password = "postgres";
    String author = "qizhuo";
    String parentName = "com.ggw";// 父包名
    String moduleName = "";// 父包模块名
    String tableName = "gw_project_sub_authorize_data";//表名，多个英文逗号分隔？所有输入 all
    AutoGenerator(url,
        username,
        password,
        author,
        parentName,
        moduleName,
        tableName);
  }

  /**
   * 自动生成代码调用方法
   * @param url 数据库地址
   * @param username 数据库用户名
   * @param password 数据库密码
   * @param author 作者
   * @param parentName 父包名
   * @param moduleName 父包模块名
   * @param tableName 表名，多个英文逗号分隔？所有输入 all
   */
  protected static void AutoGenerator(String url,
      String username,
      String password,
      String author,
      String parentName,
      String moduleName,
      String tableName) {
    FastAutoGenerator.create(url, username, password)
        // 全局配置
        .globalConfig(builder -> builder.author(author)// 设置作者名
                .outputDir(System.getProperty("user.dir") + "/src/main/java") //设置输出路径：项目的 java 目录下
                .commentDate("yyyy-MM-dd hh:mm:ss")//注释日期
                .dateType(DateType.TIME_PACK)//定义生成的实体类中日期的类型 TIME_PACK=LocalDateTime;ONLY_DATE=Date;
                .fileOverride()//覆盖之前的文件
//                        .enableSwagger()//开启 swagger 模式
                .disableOpenDir() //禁止打开输出目录，默认打开
        )
        // 包配置
        .packageConfig(builder -> builder.parent(parentName)// 设置父包名
            .moduleName(moduleName)//设置模块包名
            .entity("entity") //entity 实体类包名
            .service("service")//Service 包名
            .serviceImpl("service.impl")// ***ServiceImpl 包名
            .mapper("dao") //Mapper 包名
            .xml("mapper") //Mapper XML 包名
            .controller("controller") //Controller 包名
            .other("utils")//自定义文件包名
            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "/src/main/resources/mapper")) //配置 mapper.xml 路径信息：项目的 resources 目录下
        )
        // 策略配置
        .strategyConfig(builder -> {
          builder.addInclude(getTables(tableName))// 设置需要生成的数据表名
              .addTablePrefix("gw_") // 设置过滤表前缀

              // service 策略配置
              .serviceBuilder()
              .formatServiceFileName("%sService")//格式化 service 接口文件名称，%s进行匹配表名，如 UserService
              .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl

              // 实体类策略配置
              .entityBuilder()        //实体类策略配置
              .enableLombok()         //开启 Lombok
              .disableSerialVersionUID() //不实现 Serializable 接口，不生产 SerialVersionUID
              .logicDeleteColumnName("deleted")  //逻辑删除字段名
              .naming(NamingStrategy.underline_to_camel) //数据库表映射到实体的命名策略：下划线转驼峰命
              .columnNaming(NamingStrategy.underline_to_camel)  //数据库表字段映射到实体的命名策略：下划线转驼峰命
              //添加表字段填充，"create_time"字段自动填充为插入时间，"modify_time"字段自动填充为插入修改时间
              .addTableFills(
                  new Column("create_time", FieldFill.INSERT),
                  new Column("update_time", FieldFill.INSERT_UPDATE))
              .enableTableFieldAnnotation()       // 开启生成实体时生成字段注解

              // Controller策略配置
              .controllerBuilder()
              .formatFileName("%sController")//格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
              .enableRestStyle() //开启生成 @RestController 控制器

              // Mapper策略配置
              .mapperBuilder()
              .superClass(BaseMapper.class) //设置父类
              .formatMapperFileName("%sMapper")  //格式化 mapper 文件名称
              .enableMapperAnnotation()       //开启 @Mapper 注解
              .formatXmlFileName("%sMapper"); //格式化 Xml 文件名称
        })
        //5、模板
        .templateEngine(new FreemarkerTemplateEngine())
        /*
                .templateEngine(new VelocityTemplateEngine())
                .templateEngine(new FreemarkerTemplateEngine())
                .templateEngine(new BeetlTemplateEngine())
                */
        //6、执行
        .execute();
                /*
                模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                .templateEngine(new BeetlTemplateEngine())
                .templateEngine(new FreemarkerTemplateEngine())

                .execute();    */
  }

  // 处理 all 情况
  protected static List<String> getTables(String tables) {
    return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
  }

}