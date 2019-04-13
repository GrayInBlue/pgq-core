package com.pgq.pgqcoreweb;

import com.pgq.common.util.HttpRequester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;


public class PgqCoreWebApplicationTests {



    public static void main(String[] args) throws Exception
    {
        HttpRequester.postFile("http://localhost:8080/octet-stream",new File("D:\\1.rar"));
    }
}
