package com.example.demo.groovyscript

// 带参数的groovy方法
def sayHello(name) {
	println "Hello " + name + "."
	
	return "GroovyShell_1中的sayHello(name)方法的返回值"
}

// 运行groovy方法
sayHello(name)