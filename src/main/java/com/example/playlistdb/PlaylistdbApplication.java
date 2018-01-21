package com.example.playlistdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PlaylistdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaylistdbApplication.class, args);
    }


    // new SQLiteDAO().insertWithJDBC(mp3);

    ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
    SQLiteDAO sqLiteDao = (SQLiteDAOImpl) context.getBean("sqliteDAO");

}
