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
