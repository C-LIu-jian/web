package com.crazycode.test;

import com.crazycode.mapper.StudentMapper;
import com.crazycode.pojo.Student;
import com.crazycode.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTest {
    @Test
    public void test1() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            //调用代理类对象中的方法
            Student student = mapper.findStudentById(7L);
            System.out.println(student);

        }
    }

    @Test
    public void test2() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            //调用代理类对象中的方法
            List<Student> stuList = mapper.findAllStudents();
            System.out.println(stuList);

        }
    }

    @Test
    public void test3() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            //调用代理类对象中的方法
            Student student = mapper.findStudentByNameAndSex("张三", "男");
            System.out.println(student);

        }
    }

    @Test
    public void test4() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            Map<String, Object> map = new HashMap<>();
            map.put("name", "张三");
            map.put("sex", "男");
            //调用代理类对象中的方法
            Student student = mapper.findStudentByNameAndSex2(map);
            System.out.println(student);

        }
    }

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

    @Test
    public void test9() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            List<Student> stuList = mapper.findStudentsByNameLike("%张%");
            System.out.println(stuList);

        }
    }

    @Test
    public void test10() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            Student stu = new Student();
            stu.setSname("jackwang");
            stu.setSno("no008");
            stu.setSsex("男");
            int res = mapper.saveStudent(stu);
            sqlSession.commit();
            System.out.println(res);

        }
    }
}
