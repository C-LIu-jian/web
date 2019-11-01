package com.crazycode.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.Properties;

public class SqlSessionFactoryUtil {
    private static SqlSessionFactory sessionFactory = null;

    public static SqlSessionFactory getInstance() {
        if (sessionFactory == null) {
            synchronized (SqlSessionFactoryUtil.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new SqlSessionFactoryBuilder()
                                .build(Resources.getResourceAsStream("mybatis-config.xml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return sessionFactory;
    }
}
