package org.joychou.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/jackson")
public class Jackson {

    static class ABean {
        public int id;
        public Object obj;
    }

    static class AnotherBean {
        @JsonTypeInfo (use = JsonTypeInfo.Id.CLASS)
        public Object obj;
    }

    static class YetAnotherBean {
        @JsonTypeInfo (use = JsonTypeInfo.Id.MINIMAL_CLASS)
        public Object obj;
    }





    @RequestMapping(value = "/deserialize0", method = {RequestMethod.POST})
    @ResponseBody
    public static String deserialize0(@RequestBody String params) throws IOException {
        System.out.println(params);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enableDefaultTyping();
            objectMapper.readValue(params, Person.class);
            return "deserialize0";
        }  catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(value = "/deserialize00", method = {RequestMethod.POST})
    @ResponseBody
    public static String deserialize00(@RequestBody String params) throws IOException {
        System.out.println(params);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            Object obj = objectMapper.readValue(params, Person.class);
            return obj.toString();
        }  catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(value = "/deserialize", method = {RequestMethod.POST})
    @ResponseBody
    public static String deserialize(@RequestBody String params) throws IOException {
        System.out.println(params);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            Object obj = objectMapper.readValue(params, Person.class);
            return obj.toString();
        }  catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }


    @RequestMapping(value = "/deserialize1", method = {RequestMethod.POST})
    @ResponseBody
    public static String deserialize1(@RequestBody String params) throws IOException {
        // 如果Content-Type不设置application/json格式，post数据会被url编码
        System.out.println(params);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enableDefaultTyping();
            objectMapper.readValue(params, ABean.class);
            return "deserialize1";
        }  catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(value = "/deserialize2", method = {RequestMethod.POST})
    @ResponseBody
    public static String deserialize2(@RequestBody String params) throws IOException {
        // 如果Content-Type不设置application/json格式，post数据会被url编码
        System.out.println(params);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
            objectMapper.readValue(params, ABean.class);
            return "deserialize2";
        }  catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    // 根据源码里的调用关系，这里的三个enableDefaultTyping方法其实是一样的！
    @RequestMapping(value = "/deserialize22", method = {RequestMethod.POST})
    @ResponseBody
    public static String deserialize22(@RequestBody String params) throws IOException {
        // 如果Content-Type不设置application/json格式，post数据会被url编码
        System.out.println(params);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS, JsonTypeInfo.As.WRAPPER_ARRAY);
            objectMapper.readValue(params, ABean.class);
            return "deserialize22";
        }  catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }


    @RequestMapping(value = "/deserialize3", method = {RequestMethod.POST})
    @ResponseBody
    public static String deserialize3(@RequestBody String params) throws IOException {
        // 如果Content-Type不设置application/json格式，post数据会被url编码
        System.out.println(params);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enableDefaultTyping();
            objectMapper.readValue(params, AnotherBean.class);
            return "deserialize3";
        }  catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    /* 以下测试defaultTyping的四种形式
    *
    * JAVA_LANG_OBJECT
    * OBJECT_AND_NON_CONCRETE
    * NON_CONCRETE_AND_ARRAYS
    * NON_FINAL
    * */


    public static void main(String[] args)throws IOException{
        Person p = new Person();
        p.age = 10;
        p.name = "Alice";
        p.object = new Dna();
        p.sexObj = new MySex();
        p.dna = new Dna();
        p.sexObj = new MySex();   //默认值是0

        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);    // 纯object
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);  // 除object以外，不具体的类（接口、抽象类）也支持
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        String json = objectMapper.writeValueAsString(p);
        System.out.println(json);
    }

    static class Person {
        public int age;
        public String name;
        public Object object;
        public Dna dna;
        public Sex sexObj;
        public final Object finalObject = new String("test final object");

        @Override
        public String toString() {
            return String.format("Person.age=%d, Person.name=%s, %s", age, name, object == null ? "null" : object);
        }
    }

    static class Dna {
        public int length = 100;
    }

    interface Sex {
        public void setSex(int sex);

        public int getSex();
    }

    static class MySex implements Sex {
        int sex;

        @Override
        public int getSex() {
            return sex;
        }

        @Override
        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
