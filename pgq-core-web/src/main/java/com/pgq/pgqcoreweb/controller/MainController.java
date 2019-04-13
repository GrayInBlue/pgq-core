package com.pgq.pgqcoreweb.controller;

import com.pgq.common.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class MainController
{

    @RequestMapping(value = "octet-stream", method = RequestMethod.POST, headers = {"Content-Type=application/octet-stream"})
    @ResponseBody
    public String ostream(String name, String password, @RequestBody byte[] body) throws IOException
    {
        FileUtil.writeBytesToFile(body, "D:\\2.rar");
        System.out.println(name);
        System.out.println(password);
        return name + "=" + password;
    }

    @RequestMapping(value = "form-data", method = RequestMethod.POST, headers = {"Content-Type=multipart/form-data"})
    @ResponseBody
    public String formdata(String name, String password, MultipartFile[] file) throws IOException
    {
        System.out.println(name);
        System.out.println(password);
        System.out.println(file[0].getOriginalFilename());
        return name + "=" + password + "=" + file[0].getOriginalFilename();
    }

    @RequestMapping(value = "json", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public String json(String name, String password, @RequestBody(required = false) String body) throws IOException
    {
        System.out.println(name);
        System.out.println(password);
        return name + "=" + password + "=" + body;
    }
}
