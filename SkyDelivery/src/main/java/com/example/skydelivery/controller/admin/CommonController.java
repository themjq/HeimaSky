package com.example.skydelivery.controller.admin;

import com.example.skydelivery.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/admin/common")
@Api(tags="通用接口")
@Slf4j
public class CommonController {

    Path filePath=null;
    String name2=null;
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws FileNotFoundException {
        log.info("文件存储{}",file);
//        //创建Yaml对象
//        Yaml yaml=new Yaml();
//        InputStream inputStream=new FileInputStream("image.yml");
//        Map<String,String> map=yaml.load(inputStream);
//
//        String src=map.get("src");

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            // 读取YAML文件内容
            Map<String, String> data = objectMapper.readValue(new File("src/main/resources/image.yml"), Map.class);

            String src =data.get("src");
            String src2 =data.get("src2");
            String name=file.getOriginalFilename();
            name2= UUID.randomUUID().toString()+name.lastIndexOf(".");

            filePath = Path.of(src + name2+".jpg");
            // 将文件保存到本地
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // 输出src属性值
            //System.out.println("src: " + src);
            System.out.println(src+name2+".jpg");
            return Result.success(src2+name2+".jpg");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("文件上传失败");
        }
        return Result.error("上传失败");
    }
}
