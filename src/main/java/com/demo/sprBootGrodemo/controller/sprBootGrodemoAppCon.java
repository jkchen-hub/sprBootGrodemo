package com.demo.sprBootGrodemo.controller;

import java.io.File;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class sprBootGrodemoAppCon {
	/**
     * 1. 使用GroovyShell执行Groovy脚本
     * @param id
     */
	private static Logger logger = Logger.getLogger(sprBootGrodemoAppCon.class);
	// 调用GroovyShell_1_2
    public String myTest(@PathVariable String id){
        try{
        	// 调用带参数的groovy shell时，使用Binding绑定数据
        	Binding binding = new Binding();
        	binding.setProperty("id", "1111");
        	GroovyShell groovyShell = new GroovyShell(binding);
        	Object result = groovyShell.evaluate(new File("src/main/java/com/demo/sprBootGrodemo/groovyscript/groovy.groovy"));
        	logger.info(result.toString());
        	return result.toString();
        }
        catch(Exception ex) {
        	return ex.toString();
        }
    }
	
	/**
     * 2. 使用GroovyClassLoader动态地载入Groovy脚本
     * @param id
     * @return
     */
    @GetMapping(value = "/test1/{id}")
    public String myTest1(@PathVariable String id){
        try{
            /**
             * 通过JAVA来加载如一个groovy脚本文件，然后调用该脚本中的方法
             * 处理java调用groovy :unable to resolve class异常
             * 这么写使得groovy能调用本项目的类
             */
            //ClassLoader parent = ClassLoader.getSystemClassLoader();
            ClassLoader parent = this.getClass().getClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);
            Class groovyClass = loader.parseClass(new File("src/main/java/com/demo/sprBootGrodemo/groovyscript/groovy_1.groovy"));
            GroovyObject groovyObject= (GroovyObject)groovyClass.newInstance();

            String result = (String) groovyObject.invokeMethod("test", id);
            return result;
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

    /**
     * 3. 使用GroovyScriptEngine脚本引擎加载Groovy脚本
     * @param id
     * @return
     */
    @GetMapping(value = "/test2/{id}")
    public String myTest2(@PathVariable String id){
        try{
            String path= "src/main/java/com/demo/sprBootGrodemo/groovyscript";
            GroovyScriptEngine engine = new GroovyScriptEngine(path);
            Script script = engine.createScript("groovy_2.groovy", new Binding());
            return (String) script.invokeMethod("test",id);
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

    /**
     * 3. 使用GroovyScriptEngine脚本引擎加载Groovy脚本
     * @param id
     * @return
     */
    @GetMapping(value = "/test3/{id}")
    public String myTest3(@PathVariable String id){
        try{
            String path="src/main/java/com/demo/sprBootGrodemo/groovyscript";
            GroovyScriptEngine engine = new GroovyScriptEngine(path);

            Binding binding = new Binding();
            binding.setVariable("id",id);
            engine.run("groovy_3.groovy",binding);
            return binding.getVariable("output").toString();
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

}