package com.crazycode.mapper;

import com.crazycode.pojo.Teacher;

import java.util.List;

public interface TeacherMapper {
    public List<Teacher> findAllTeachers() throws  Exception;
}
