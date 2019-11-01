package com.crazycode.test;

import com.crazycode.mapper.StudentMapper;
import com.crazycode.mapper.TeacherMapper;
import com.crazycode.pojo.Student;
import com.crazycode.pojo.Teacher;
import com.crazycode.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.List;

public class TeacherTest {
    @Test
    public void test1() throws Exception{
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getInstance();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //得到接口的实现类对象(创建一个实现StudentMapper接口的代理类对象)
            TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
            List<Teacher> allTeachers = mapper.findAllTeachers();
            System.out.println(allTeachers);

        }
    }
}
