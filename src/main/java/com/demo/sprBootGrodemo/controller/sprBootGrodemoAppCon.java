package com.demo.sprBootGrodemo.controller;

import java.io.File;
//import java.lang.reflect.Field;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
//import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;

//import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class sprBootGrodemoAppCon {
	/**
     * 1. 使用GroovyShell执行Groovy脚本
     * @param id
     * @return
     
	private static Logger logger = Logger.getLogger(sprBootGrodemoAppCon.class);
	// 调用GroovyShell_1_2
	@RequestMapping(value="/myTest/{id}")
    public String myTest(@PathVariable String id){
        try{
        	// 调用带参数的groovy shell时，使用Binding绑定数据
        	Binding binding = new Binding();
        	binding.setProperty("id", "1111");
        	GroovyShell groovyShell = new GroovyShell(binding);
        	Object result = groovyShell.evaluate(new File("src/main/java/com/demo/sprBootGrodemo/groovyscript/groovy_1.groovy"));
        	logger.info(result.toString());
        	System.out.println(result.toString());
        	return result.toString();
        }
        catch(Exception ex) {
        	return ex.toString();
        }
    }
	*/
	
	/**
     * 2. 使用GroovyClassLoader动态地载入Groovy脚本
     * groovy_1.groovy中定义了一个MyTest类，其中包含一个test方法
     * @param id
     * @return
     */
    @GetMapping(value = "/test1/{id}")
    public String myTest1(@PathVariable String id){
        try{
            /**
             * 通过JAVA来加载如一个groovy脚本文件，然后调用该脚本中的方法
             * 处理java调用groovy:unable to resolve class异常
             * 这么写使得groovy能调用本项目的类
             */
            //ClassLoader parent = ClassLoader.getSystemClassLoader();
            ClassLoader parent = this.getClass().getClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);
			
			//加载groovy_1.groovy   （类加载）
			//通过GroovyClassLoader类加载器加载指定路径上的groovy文件 
			Class groovyClass = loader.parseClass( 
			                    new File("src/main/java/com/demo/sprBootGrodemo/groovyscript/groovy_1.groovy"));
			//System.out.println(groovyClass);//输出结果为：com.example.demo.groovyscript.MyTest 
			//创建一个groovy_1.groovy文件中定义的MyTest类对象实例 
			GroovyObject groovyObject = (GroovyObject)groovyClass.newInstance(); 
			String result = (String) groovyObject.invokeMethod("test", id);
			
            
            /*
			 //加载groovy_2.groovy   （方法加载）
            Class groovyClass = loader.parseClass( 
       			                      new File("src/main/java/com/demo/sprBootGrodemo/groovyscript/groovy_2.groovy"));
       	    System.out.println(groovyClass);//输出结果为：com.example.demo.groovyscript.groovy_2 
       		GroovyObject groovyObject = (GroovyObject)groovyClass.newInstance(); 
       		String result = (String) groovyObject.invokeMethod("test", id);
            System.out.println(result);
            */
			/*
			 * //加载groovy_3.groovy （属性加载） Class groovyClass3 = loader.parseClass( new
			 * File("src/main/java/com/demo/sprBootGrodemo/groovyscript/groovy_3.groovy"));
			 * System.out.println(groovyClass3);//输出结果为：com.example.demo.groovyscript.
			 * groovy_3 GroovyObject groovyObject3 =
			 * (GroovyObject)groovyClass3.newInstance(); System.out.println(groovyObject3);
			 * 
			 * //Field field = (Field) groovyClass.getField("output"); //String result3 =
			 * (String) field.getName(); System.out.println(result);
			 */
       		return result;
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

    /**
     * 3. 使用GroovyScriptEngine脚本引擎加载Groovy脚本
     * groovy_2.groovy文件中只定义了一个test方法
     * @param id
     * @return
     */
    @GetMapping(value = "/test2/{id}")
    public String myTest2(@PathVariable String id){
        try{
            String path= "src/main/java/com/demo/sprBootGrodemo/groovyscript";
            GroovyScriptEngine engine = new GroovyScriptEngine(path);
            Script script = engine.createScript("groovy_2.groovy", new Binding());
            return (String) script.invokeMethod("test","test2");
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

    /**
     * 4. 使用GroovyScriptEngine脚本引擎加载Groovy脚本
     * groovy_3.groovy文件中只定义了一个output属性
     * @param id
     * @return
     */
    @GetMapping(value = "/test3/{id}")
    public String myTest3(@PathVariable String id){
        try{
            String path="src/main/java/com/demo/sprBootGrodemo/groovyscript";
            GroovyScriptEngine engine = new GroovyScriptEngine(path);

            Binding binding = new Binding();
            binding.setVariable("id","test3");
            engine.run("groovy_3.groovy",binding);
            return binding.getVariable("output").toString();
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

}