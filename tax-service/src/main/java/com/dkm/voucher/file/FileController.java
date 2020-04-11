package com.dkm.voucher.file;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.utils.IdGenerator;
import com.dkm.voucher.file.utils.FileUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Api(tags = "上传文件api")
@Slf4j
@RestController
@RequestMapping("/v1/file")
public class FileController {


   @Autowired
   private IdGenerator idGenerator;

   @Autowired
   private FileUtils fileUtils;

   @Value("${file.url}")
   private String fileUrl;

   private final Object createFileLock = new Object();

   /**
    * 上传文件图片到本地工程
    * @param file
    * @return
    * @throws Exception
    */
   @PostMapping("/storeFile")
   @CrossOrigin
   public String storeFile(@RequestBody MultipartFile file) throws Exception{

      if (file == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "未上传文件");
      }

      String name = file.getOriginalFilename();
      String extraName = name.substring(name.lastIndexOf("."));

      synchronized (createFileLock) {
         //文件保存路径
         Path path;
         //图片名称
         String fileName;
         do {
            //图片名称赋值
            fileName = idGenerator.getOrderCode();
            //保存路径赋值
            path = fileUtils.name2Path(fileName);

            //循环条件就是 如果文件地址不存在的情况下
         } while (fileUtils.checkPathConflict(path));
         try {
            //创建文件夹
            Files.createDirectories(path.getParent());
            //复制文件到指定文件夹下面
            String newPath = path.toString() + extraName;
            File toFile = fileUtils.multipartFileToFile(file,newPath);
            System.out.println(extraName);
            return  fileUrl + "/" + fileName.substring(0, 8) + "/"  + fileName + extraName;
         } catch (IOException e) {
            log.info("io err", e);
            throw new IllegalArgumentException("文件上传失败");
         }
      }
   }

}
