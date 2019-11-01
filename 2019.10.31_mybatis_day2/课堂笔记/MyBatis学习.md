### MyBatis学习

#### 一、MyBatis介绍

##### 1.1）MyBatis简介

>   MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJOs(Plain Ordinary Java Object,普通的 Java对象)映射成数据库中的记录。 [1] 

```java
持久化的工具类(半持久化的框架)
jdbcTemplate,dbutil  
1. 预编译sql
2. 设置参数
3. 执行sql语句
4. 封装结果集

持久化框架:
除了以上功能以外,
1.事务的处理
2.插件的支持
3.拦截器
......
学了mybatis我们注重的业务是
我们只要编写sql语句和接口,让接口和sql语句完成映射即可.
```

##### 1.2)MyBatis学习地址

```java
https://mybatis.org/mybatis-3/zh/index.html
```

#### 二、MyBatis入门

##### 2.1）导入MyBatis的jar包

```xml
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.6</version>
</dependency>

```

##### 2.2) 创建mybatis的核心配置文件(mybatis-config.xml)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--配置程序的运行环境,可以1到多个-->
    <environments default="development">
        <environment id="development">
            <!--配置事务管理器来支持事务的处理,默认jdbc事务处理-->
            <transactionManager type="JDBC"/>
            <!--配置数据源连接池-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatisdb"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <!--加载sql映射文件-->
    <mappers>
        <mapper resource="StudentMapper.xml"/>
    </mappers>
</configuration>
```

##### 2.3) 配置sql映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--命名空间+ id 名字来确定唯一的sql语句-->
<mapper namespace="com.crazycode">
    <!--resultType 查询结果要封装的数据类型-->
    <select id="queryAllStudents" resultType="com.hwua.pojo.Student">
         select id,sno,sname,ssex from student
    </select>
</mapper>
```

##### 2.4)修改主配置文件去加载sql映射文件

```xml
 <!--加载sql映射文件-->
<mappers>
    <mapper resource="StudentMapper.xml"/>
</mappers>
```

##### 2.5)创建sqlSessionFactory对象来加载配置文件

```java
String resource = "mybatis-config.xml";//主配置文件的路径
InputStream inputStream = Resources.getResourceAsStream(resource);//获取类路径下的配置文件,以字节流的方式打开
//构建sqlSessionFactory对象,sqlSessionFactory可以看作是一个连接池
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

##### 2.6)从 SqlSessionFactory 中获取 SqlSession(sqlSession可以看作是一个Connection对象)

```java
try (SqlSession session = sqlSessionFactory.openSession()) {
  Blog blog = (Blog) session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 101);
}
```

##### 2.7)导入log4j日志jar包,并编写日志配置文件(不是必须的),目的在运行的时候可以查看程序加载运行的过程

```xml
<!-- https://mvnrepository.com/artifact/log4j/log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>

```

```properties
# Global logging configuration
log4j.rootLogger=debug, stdout
# 日志输出的目的地是控制台
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
#日志输出的格式为自定义格式
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
#日志具体的格式我们自定义
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

##### 2.8)编写单元测试运行

```java
package com.crazycode.test;

import com.hwua.pojo.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 编写单元测试类
 */
public class StudentTest {
    @Test
    public void test1() throws Exception {
        String resource = "mybatis-config.xml";//主配置文件的路径
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //获取类路径下的配置文件,以字节流的方式打开
        // 构建sqlSessionFactory对象,sqlSessionFactory可以看作是一个连接池
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            //sqlSession中提供了很多API来进行对数据库增删改查的操作
            List<Student> stuList = sqlSession.selectList("com.crazycode.queryAllStudents");
            System.out.println(stuList);
        }

    }
}

```

#### 三、MyBatis构建对象的作用域

```java
- SqlSessionFactoryBuilder
这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但是最好还是不要让其一直存在，以保证所有的 XML 解析资源可以被释放给更重要的事情。

- SqlSessionFactory
SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏味道（bad smell）”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式。

- SqlSession
每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，要考虑 SqlSession 放在一个和 HTTP 请求对象相似的作用域中。 换句话说，每次收到的 HTTP 请求，就可以打开一个 SqlSession，返回一个响应，就关闭它。 这个关闭操作是很重要的，你应该把这个关闭操作放到 finally 块中以确保每次都能执行关闭。 下面的示例就是一个确保 SqlSession 关闭的标准模式：
try (SqlSession session = sqlSessionFactory.openSession()) {
  // 你的应用逻辑代码
}
```

```JAVA
package com.crazycode.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public class SqlSessionFactoryUtil {
    private static SqlSessionFactory sessionFactory = null;

    public static SqlSessionFactory getInstance() {
        if (sessionFactory == null) {
            synchronized (SqlSessionFactoryUtil.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return sessionFactory;
    }
}

```

```JAVA
package com.crazycode.test;

import com.crazycode.util.SqlSessionFactoryUtil;
import com.hwua.pojo.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 编写单元测试类
 */
public class StudentTest {
    @Test
    public void test1() throws Exception {
        try (SqlSession sqlSession = SqlSessionFactoryUtil.getInstance().openSession()) {
            //sqlSession中提供了很多API来进行对数据库增删改查的操作
            List<Student> stuList = sqlSession.selectList("com.crazycode.queryAllStudents");
            System.out.println(stuList);
        }

    }

    @Test
    public void test2() throws Exception {
        try (SqlSession sqlSession = SqlSessionFactoryUtil.getInstance().openSession()) {
            //sqlSession中提供了很多API来进行对数据库增删改查的操作
            Student stu = sqlSession.selectOne("queryStudentById", 1);
            System.out.println(stu);
        }

    }

    @Test
    public void test3() throws Exception {
        try (SqlSession sqlSession = SqlSessionFactoryUtil.getInstance().openSession()) {
            //sqlSession中提供了很多API来进行对数据库增删改查的操作
            int res = sqlSession.delete("delStudentById", 5);
            sqlSession.commit();//提交事务
            System.out.println(res);
        }

    }

    @Test
    public void test4() throws Exception {
        try (SqlSession sqlSession = SqlSessionFactoryUtil.getInstance().openSession()) {
            //sqlSession中提供了很多API来进行对数据库增删改查的操作
            Student stu = new Student();
            stu.setSname("mary");
            stu.setSsex("女");
            stu.setSno("no005");
            int res = sqlSession.insert("saveStudent", stu);
            sqlSession.commit();
            System.out.println(res);
        }

    }
    @Test
    public void test5() throws Exception {
        try (SqlSession sqlSession =  SqlSessionFactoryUtil.getInstance().openSession()) {
            //sqlSession中提供了很多API来进行对数据库增删改查的操作
            Student stu = sqlSession.selectOne("queryStudentById", 6);
            stu.setSname("小强");
            int res = sqlSession.update("updateStudent",stu);
            sqlSession.commit();
            System.out.println(res);
        }

    }

}

```

```XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--命名空间+ id 名字来确定唯一的sql语句-->
<mapper namespace="com.crazycode">
    <!--resultType 查询结果要封装的数据类型-->
    <select id="queryAllStudents" resultType="com.hwua.pojo.Student">
         select id,sno,sname,ssex from student
    </select>
    <select id="queryStudentById" resultType="com.hwua.pojo.Student" >
         select id,sno,sname,ssex from student where id = #{id}
    </select>
    <delete id="delStudentById">
        delete from student where id=#{id}
    </delete>

    <insert id="saveStudent" parameterType="com.hwua.pojo.Student">
        insert into student values(null,#{sno},#{sname},#{ssex})
    </insert>

    <update id="updateStudent" parameterType="com.hwua.pojo.Student">
        update student set sname = #{sname} where id = #{id}
    </update>

</mapper>
```

#### 四、MyBatis入门之接口编程(掌握)

##### 4.1)编写Dao接口

```java
package com.crazycode.mapper;

import com.crazycode.pojo.Student;

import java.util.List;

public interface StudentMapper {
    public Student findStudentById(Long id) throws Exception;
    public List<Student> findAllStudents() throws Exception;
    public int saveStudent(Student stu) throws Exception;
    public int delStudentById(Long id) throws Exception;
    public int updateStudent(Student stu) throws Exception;
    //模糊查询名字所对应的学员
    public List<Student> findStudentsByNameLike(String name) throws Exception;
    public Student findStudentByName(String name,String sex) throws Exception;
}

```

##### 4.2)接口绑定sql映射文件的规则

```java
Studentdao --- > Studentdaoimpl
StudentMapper----> StudentMapper.xml
说明: MyBatis 不用去手动创建接口的实现类,底层会自动帮我们创建实现接口的一个代理类对象.
```

```java
1. sql映射文件的namespace值必须是接口的全限定名
2. 接口的方法名和配置的id名要一致
3. 接口方法的返回类型和配置的返回类型要一致
4. 接口方法的参数类型和配置的参数类型要一致(可以省略),会自动识别
```

##### 4.3)编写sql映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--命名空间+ id 名字来确定唯一的sql语句-->
<mapper namespace="com.crazycode.mapper.StudentMapper">
    <select id="findStudentById" resultType="com.crazycode.pojo.Student" parameterType="java.lang.Long">
       select id,sname,sno,ssex from student where id = #{id}
   </select>

    <select id="findAllStudents" resultType="com.crazycode.pojo.Student">
        select id,sname,sno,ssex from student
    </select>
</mapper>
```

##### 4.4)编写测试类来调用方法运行

```java
package com.crazycode.test;

import com.crazycode.mapper.StudentMapper;
import com.crazycode.pojo.Student;
import com.crazycode.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.List;

public class StudentTest {
    @Test
    public void test1() throws Exception{
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try(SqlSession sqlSession=sqlSessionFactory.openSession()){
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            //调用代理类对象中的方法
            Student student = mapper.findStudentById(1L);
            System.out.println(student);

        }
    }

    @Test
    public void test2() throws Exception{
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try(SqlSession sqlSession=sqlSessionFactory.openSession()){
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            //调用代理类对象中的方法
            List<Student> stuList = mapper.findAllStudents();
            System.out.println(stuList);

        }
    }
}

```

#### 五、MyBatis映射文件之参数处理(掌握)

##### 5.1)单个参数(掌握)

```java
语法:#{参数名}  单个参数:名字随意
 <select id="findStudentById" resultType="com.crazycode.pojo.Student" parameterType="java.lang.Long">
       select id,sname,sno,ssex from student where id = #{id}
  </select>
```

##### 5.2)多个参数(掌握)

```java
* 第一种情况不加@Param注解
  底层是是一个hashMap来计收参数的,key 是 arg0,arg1 值就是 传过去的value
  {arg0: 张三,arg1: 男,param1: 张三, param2: 男}
  缺点: 程序的可读性太差
  
* 第二种就是给每个参数加上注解@Param(key),也就是指定存放到Map中的key
 public Student findStudentByNameAndSex(@Param("name") String name, @Param("sex") String sex) throws Exception;
{"name": 张三, sex:男}
 select id,sname,sno,ssex from student where sname=#{name} and ssex=#{sex}

推荐:当参数的个数<=5个的时候推荐使用这种方式
```

##### 5.3)参数的类型为map(了解)

```java
<select id="findStudentByNameAndSex2" resultType="com.crazycode.pojo.Student">
        select id,sname,sno,ssex from student where sname=#{name} and ssex=#{sex}
</select>
```

```java
 @Test
    public void test4() throws Exception{
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try(SqlSession sqlSession=sqlSessionFactory.openSession()){
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            Map<String,Object> map = new HashMap<>();
            map.put("name","张三");
            map.put("sex","男");
            //调用代理类对象中的方法
            Student student = mapper.findStudentByNameAndSex2(map);
            System.out.println(student);

        }
    }
```

##### 5.4)可以使用pojo对象来作为参数(掌握)

```java
@Test
    public void test5() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            Student stu = new Student();
            stu.setSname("张三");
            stu.setSsex("男");
            //调用代理类对象中的方法
            Student student = mapper.findStudent(stu);
            System.out.println(student);

        }
    }
```

```xml
  <select id="findStudent" resultType="com.crazycode.pojo.Student">
        select id,sname,sno,ssex from student where sname=#{属性名字} and ssex=#{属性名字}
    </select>
```

##### 5.5)使用数组作为参数(了解)

```java
  @Test
    public void test6() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            String [] arr={"张三","男"};
            //调用代理类对象中的方法
            Student student = mapper.findStudentByArray(arr);
            System.out.println(student);

        }
    }
```

```xml
<select id="findStudentByArray" resultType="com.crazycode.pojo.Student">
    select id,sname,sno,ssex from student where sname=#{array[0]} and ssex=#{array[1]}
</select>
```

##### 5.6)使用list和set集合作为参数(了解)

```java
@Test
public void test7() throws Exception {
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        String [] arr={"张三","男"};
        List<String> list = Arrays.asList(arr);//把数组转换成集合
        //调用代理类对象中的方法
        Student student = mapper.findStudentByList(list);
        System.out.println(student);

    }
}
```

```xml
<select id="findStudentByList" resultType="com.crazycode.pojo.Student">
    <!--select id,sname,sno,ssex from student where sname=#{list[0]} and ssex=#{list[1]}-->
    select id,sname,sno,ssex from student where sname=#{list[0]} and ssex=#{collection[1]}
</select>
```

#### 六、MyBatis映射文件之返回值(掌握)

##### 6.1)返回pojo对象(掌握)

```java
public Student findStudentById(Long id) throws Exception;
```

```xml
<select id="findStudentById" resultType="com.crazycode.pojo.Student" parameterType="java.lang.Long">
    select id,sname,sno,ssex from student where id = #{id}
</select>
```

##### 6.2)返回List集合(掌握)

```xml
<select id="findAllStudents" resultType="com.crazycode.pojo.Student">
        select id,sname,sno,ssex from student
</select>
```

```java
 public List<Student> findAllStudents() throws Exception;
```

##### 6.3)返回Map(了解)

```java
//查询所有的学生记录,一条记录封装成一个Student对象,保存到map中,key是@MapKey直接指定的字段值作为对应的key
@MapKey("id")
public Map<Long,Student> findAllStudentsMap() throws Exception;
```

```java
@Test
    public void test8() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            Map<Long, Student> map = mapper.findAllStudentsMap();
            System.out.println(map);

        }
    }
```

```xml
<select id="findAllStudentsMap" resultType="com.crazycode.pojo.Student">
    select id,sname,sno,ssex from student
</select>
```

#### 七、#{}和${}的区别

```java
区别1:
#{}它等同于?,输出的数据会自动加上单引号变字符串
${value} 原样输出,就是把取到的参数直接显式在指定的位置,不会自动加单引号转变成字符串

区别2:
当传递一个参数过来的时候
#{任意名字}
${value}
${value} 存在sql注入的风险,推荐使用#{}

List<Student> stuList = mapper.findStudentsByNameLike("%张%");
 select id,sname,sno,ssex from student where sname like #{name}

List<Student> stuList = mapper.findStudentsByNameLike("张");
select id,sname,sno,ssex from student where sname like '%${value}%'
```

#### 八、MyBatis映射文件查询之数据封装

##### 8.1)resultType

```java
功能:自动完成把查询到的结果集数据封装到指定的数据类型中,假如封装的是对象,那么必须列的名字和对象中的属性名要一致才会完成自动封装
解决查询出来的列名和属性名不一致的问题,可以给查询字段取一个和属性名一样的别名
select id,t_name as tName,sex,birthday from teacher
```

##### 8.2)resultMap(重点)-- 单表查询和多表查询进行手动封装

```xml
<select id="findAllTeachers" resultMap="teacher_map">
    select id,t_name,sex,birthday from teacher
</select>

<resultMap id="teacher_map" type="com.crazycode.pojo.Teacher">
    <!--手动指定封装的细节-->
    <!--主键封装-->
    <id property="id" column="id"></id>
    <!--普通属性的封装-->
    <result property="tName" column="t_name"></result>
    <result property="birthday" column="birthday"></result>
    <result property="sex" column="sex"></result>
</resultMap>
```

#### 九、MyBatis配置

##### 9.1)属性（properties）配置

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatisdb
jdbc.username=root
jdbc.password=root
```

```xml
<properties resource="db.properties"></properties><!--加载资源属性文件-->
```

```xml
<environments default="development">
    <environment id="development">
        <!--配置事务管理器来支持事务的处理,默认jdbc事务处理-->
        <transactionManager type="JDBC"/>
        <!--配置数据源连接池-->
        <dataSource type="POOLED">
            <property name="driver" value="${jdbc.driver}"/>
            <property name="url" value="${jdbc.url}"/>
            <property name="username" value="${jdbc.username}"/>
            <property name="password" value="${jdbc.password}"/>
        </dataSource>
    </environment>
</environments>
```

注意加载顺序:

```xml
1. 在 properties 元素体内指定的属性首先被读取。
2. 然后根据 properties 元素中的 resource 属性读取类路径下属性文件或根据 url 属性指定的路径读取属性文件，并覆盖已读取的同名属性。
3. 最后读取作为方法参数传递的属性，并覆盖已读取的同名属性。
```

开启默认值和修改默认分隔符(了解)

```xml
<properties resource="db.properties">
        <property name="org.apache.ibatis.parsing.PropertyParser.enable-default-value" value="true"/> <!-- 启用默认值特性 -->
        <property name="org.apache.ibatis.parsing.PropertyParser.default-value-separator" value="-"/> <!-- 修改默认值的分隔符 -->
    </properties>
```

```xml
<property name="password" value="${jdbc.password-root}"/>
```

##### 9.2)设置（settings）其它功能后续讲解

这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。 下表描述了设置中各项的意图、默认值等。

```xml
 <settings>
     <!--开启驼峰命名规则-->
     <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

##### 9.3)类型别名（typeAliases）

类型别名是为 Java 类型设置一个短的名字。 它只和 XML 配置有关，存在的意义仅在于用来减少类完全限定名的冗余

mybatis提供了基本数据类型和对应包装类的相关别名

| 别名       | 映射的类型 |
| :--------- | :--------- |
| _byte      | byte       |
| _long      | long       |
| _short     | short      |
| _int       | int        |
| _integer   | int        |
| _double    | double     |
| _float     | float      |
| _boolean   | boolean    |
| string     | String     |
| byte       | Byte       |
| long       | Long       |
| short      | Short      |
| int        | Integer    |
| integer    | Integer    |
| double     | Double     |
| float      | Float      |
| boolean    | Boolean    |
| date       | Date       |
| decimal    | BigDecimal |
| bigdecimal | BigDecimal |
| object     | Object     |
| map        | Map        |
| hashmap    | HashMap    |
| list       | List       |
| arraylist  | ArrayList  |
| collection | Collection |
| iterator   | Iterator   |

*   给自定义类设置设置别名的方式

    *   单独一个个设置(了解)

        ```xml
        <typeAliases>
            <typeAlias type="com.crazycode.pojo.Teacher" alias="teacher"></typeAlias>
        </typeAliases>
        ```

    *   批量设置别名(别名包扫描)掌握

        ```xml
        <typeAliases>
             <!--扫描指定包中或其子包下的所类,给这些类设置别名,别名就是类名(首字母小写),别名是不区分大小写的-->
              <package name="com.crazycode.pojo"/>
        </typeAliases>
        ```

    *   当扫描到不同包中同名的类的时候,会产生别名冲突,如何解决,可以使用注解来解决

        ```java
        @Alias("author")
        public class Author {
            ...
        }
        ```

        

#### 十、类型处理器（typeHandlers）了解

>无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，还是从结果集中取出一个值时， 都会用类型处理器将获取的值以合适的方式转换成 Java 类型。作用就是jdbcType<-->javaType类型转换的一个桥梁.

| 类型处理器                   | Java 类型                       | JDBC 类型                                                    |
| :--------------------------- | :------------------------------ | :----------------------------------------------------------- |
| `BooleanTypeHandler`         | `java.lang.Boolean`, `boolean`  | 数据库兼容的 `BOOLEAN`                                       |
| `ByteTypeHandler`            | `java.lang.Byte`, `byte`        | 数据库兼容的 `NUMERIC` 或 `BYTE`                             |
| `ShortTypeHandler`           | `java.lang.Short`, `short`      | 数据库兼容的 `NUMERIC` 或 `SMALLINT`                         |
| `IntegerTypeHandler`         | `java.lang.Integer`, `int`      | 数据库兼容的 `NUMERIC` 或 `INTEGER`                          |
| `LongTypeHandler`            | `java.lang.Long`, `long`        | 数据库兼容的 `NUMERIC` 或 `BIGINT`                           |
| `FloatTypeHandler`           | `java.lang.Float`, `float`      | 数据库兼容的 `NUMERIC` 或 `FLOAT`                            |
| `DoubleTypeHandler`          | `java.lang.Double`, `double`    | 数据库兼容的 `NUMERIC` 或 `DOUBLE`                           |
| `BigDecimalTypeHandler`      | `java.math.BigDecimal`          | 数据库兼容的 `NUMERIC` 或 `DECIMAL`                          |
| `StringTypeHandler`          | `java.lang.String`              | `CHAR`, `VARCHAR`                                            |
| `ClobReaderTypeHandler`      | `java.io.Reader`                | -                                                            |
| `ClobTypeHandler`            | `java.lang.String`              | `CLOB`, `LONGVARCHAR`                                        |
| `NStringTypeHandler`         | `java.lang.String`              | `NVARCHAR`, `NCHAR`                                          |
| `NClobTypeHandler`           | `java.lang.String`              | `NCLOB`                                                      |
| `BlobInputStreamTypeHandler` | `java.io.InputStream`           | -                                                            |
| `ByteArrayTypeHandler`       | `byte[]`                        | 数据库兼容的字节流类型                                       |
| `BlobTypeHandler`            | `byte[]`                        | `BLOB`, `LONGVARBINARY`                                      |
| `DateTypeHandler`            | `java.util.Date`                | `TIMESTAMP`                                                  |
| `DateOnlyTypeHandler`        | `java.util.Date`                | `DATE`                                                       |
| `TimeOnlyTypeHandler`        | `java.util.Date`                | `TIME`                                                       |
| `SqlTimestampTypeHandler`    | `java.sql.Timestamp`            | `TIMESTAMP`                                                  |
| `SqlDateTypeHandler`         | `java.sql.Date`                 | `DATE`                                                       |
| `SqlTimeTypeHandler`         | `java.sql.Time`                 | `TIME`                                                       |
| `ObjectTypeHandler`          | Any                             | `OTHER` 或未指定类型                                         |
| `EnumTypeHandler`            | Enumeration Type                | VARCHAR 或任何兼容的字符串类型，用以存储枚举的名称（而不是索引值） |
| `EnumOrdinalTypeHandler`     | Enumeration Type                | 任何兼容的 `NUMERIC` 或 `DOUBLE` 类型，存储枚举的序数值（而不是名称）。 |
| `SqlxmlTypeHandler`          | `java.lang.String`              | `SQLXML`                                                     |
| `InstantTypeHandler`         | `java.time.Instant`             | `TIMESTAMP`                                                  |
| `LocalDateTimeTypeHandler`   | `java.time.LocalDateTime`       | `TIMESTAMP`                                                  |
| `LocalDateTypeHandler`       | `java.time.LocalDate`           | `DATE`                                                       |
| `LocalTimeTypeHandler`       | `java.time.LocalTime`           | `TIME`                                                       |
| `OffsetDateTimeTypeHandler`  | `java.time.OffsetDateTime`      | `TIMESTAMP`                                                  |
| `OffsetTimeTypeHandler`      | `java.time.OffsetTime`          | `TIME`                                                       |
| `ZonedDateTimeTypeHandler`   | `java.time.ZonedDateTime`       | `TIMESTAMP`                                                  |
| `YearTypeHandler`            | `java.time.Year`                | `INTEGER`                                                    |
| `MonthTypeHandler`           | `java.time.Month`               | `INTEGER`                                                    |
| `YearMonthTypeHandler`       | `java.time.YearMonth`           | `VARCHAR` 或 `LONGVARCHAR`                                   |
| `JapaneseDateTypeHandler`    | `java.time.chrono.JapaneseDate` | `DATE`                                                       |

总结:以上提供的类型转换器能解决大多数的应用场景,特殊的应用场景我们可以自定义一个类型转换器

比如: 插入一个学生,java类型:"男" ,插入到数据保存为1,读取出来是1就显示"男"

##### 10.1)自定义类型转换器的步骤

*   创建一个类去实现`org.apache.ibatis.type.TypeHandler` 接口或者继承org.apache.ibatis.type.BaseTypeHandler

    ```java
    package com.crazycode.typehandler;
    
    import org.apache.ibatis.type.JdbcType;
    import org.apache.ibatis.type.TypeHandler;
    
    import java.sql.CallableStatement;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    
    public class StringToIntHandler implements TypeHandler<String> {
    
        //java类型转换成jdbc类型
        @Override
        public void setParameter(PreparedStatement ps, int i, String s, JdbcType jdbcType) throws SQLException {
            if(s.equals("男")){
                ps.setInt(i,1);//把占位符替换成1
            }else if(s.equals("女")){
                ps.setInt(i,2);//把占位符替换成1
            }else{
                ps.setInt(i,3);//把占位符替换成1
            }
        }
    
        //jdbc类型转换成java类型
        @Override
        public String getResult(ResultSet resultSet, String columnName) throws SQLException {
            int res = resultSet.getInt(columnName);
            if(res==1){
                return "男";
            }else if(res==2){
                return "女";
            }else {
                return "不确定";
            }
        }
    
        @Override
        public String getResult(ResultSet resultSet, int i) throws SQLException {
            int res = resultSet.getInt(i);
            if(res==1){
                return "男";
            }else if(res==2){
                return "女";
            }else {
                return "不确定";
            }
        }
    
        @Override
        public String getResult(CallableStatement callableStatement, int i) throws SQLException {
            int res = callableStatement.getInt(i);
            if(res==1){
                return "男";
            }else if(res==2){
                return "女";
            }else {
                return "不确定";
            }
        }
    }
    
    ```

*   注册类型转换器

    ```xml
    <typeHandlers>
            <typeHandler handler="com.crazycode.typehandler.StringToIntHandler" javaType="string" jdbcType="int"/>
    </typeHandlers>
    ```

*   使用自定义类型转换器

    ```xml
     <insert id="saveStudent">
            insert into Student values(null,#{sno},#{sname},#{ssex,typeHandler=com.crazycode.typehandler.StringToIntHandler})
        </insert>
    ```

    ```xml
     <select id="findStudentById" resultMap="stu_map">
           select id,sname,sno,ssex from student where id = #{id}
       </select>
    
        <resultMap id="stu_map" type="student">
            <result column="ssex" property="ssex" typeHandler="com.crazycode.typehandler.StringToIntHandler"></result>
        </resultMap>
    ```

    //作业: 把短消息项目的dao层改为mybatis