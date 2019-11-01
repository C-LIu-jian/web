package com.crazycode.mapper;

import com.crazycode.pojo.Student;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface StudentMapper {
    public Student findStudentById(Long id) throws Exception;
    public List<Student> findAllStudents() throws Exception;
    public int saveStudent(Student stu) throws Exception;
    public int delStudentById(Long id) throws Exception;
    public int updateStudent(Student stu) throws Exception;
    //模糊查询名字所对应的学员
    public List<Student> findStudentsByNameLike(String name) throws Exception;
    public Student findStudentByNameAndSex(String name, String sex) throws Exception;
    public Student findStudentByNameAndSex2(Map<String,Object> map) throws Exception;
    public Student findStudent(Student stu) throws Exception;
    public Student findStudentByArray(String [] arr) throws Exception;
    public Student findStudentByList(List<String> a) throws Exception;
    //查询所有的学生记录,一条记录封装称一个Student对象,保存到map中
    @MapKey("id")
    public Map<Long,Student> findAllStudentsMap() throws Exception;
}
