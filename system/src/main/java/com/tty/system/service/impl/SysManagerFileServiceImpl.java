package com.tty.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.common.enums.ImageType;
import com.tty.common.enums.TypeContentEnum;
import com.tty.common.utils.JsonResult;
import com.tty.common.enums.ResourcePath;
import com.tty.system.utils.BunnyUtil;
import com.tty.system.utils.ImageConvertUtil;
import com.tty.system.entity.SysImageRecord;
import com.tty.system.mapper.SysImageRecordMapper;
import com.tty.system.service.SysManagerFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Service
public class SysManagerFileServiceImpl extends ServiceImpl<SysImageRecordMapper, SysImageRecord> implements SysManagerFileService {

    private static final Logger logger = Logger.getLogger(SysManagerFileServiceImpl.class);
    @Resource
    ImageConvertUtil imageConvertUtil;
    @Resource
    BunnyUtil bunnyUtil;

    @Value("${file.picture.picture-form}")
    private  String PictureForm;
    @Value("${file.picture.no-need-to-convert}")
    private List<String> NoNeedToConvert;
    @Value("${cdn.url}")
    private String url;

    @Override
    public String uploadImageByBunny(MultipartFile file, String fileName, String path, Long uploader, ImageType type) {
        //获取上传文件的后缀
        String suffix = imageConvertUtil.imageFileCheck(file);
        //判断后缀是否满足图片列表
        boolean status = NoNeedToConvert.contains(suffix.toUpperCase());
        if (!status) {
            try {
                file = imageConvertUtil.pictureToWebP(file);
            } catch (IOException e) {
                logger.error(e, e);
                return null;
            }
        }
        if (bunnyUtil.upload(file, fileName + "." + (status ? suffix:PictureForm), path)) {
            SysImageRecord record = new SysImageRecord();
            record.setImageName(fileName);
            record.setSuffix(status ? suffix:PictureForm);
            record.setUploader(uploader);
            record.setCreateTime(new Date());
            record.setPath(path);
            record.setType(type.getType());
            this.save(record);
            return this.url + (path.isEmpty() ? "/":(path + "/")) + fileName + "." + (status ? suffix:PictureForm);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteImageByBunny(List<Long> lists, Long targetUid) {
        LambdaQueryWrapper<SysImageRecord> queryWrapper = new QueryWrapper<SysImageRecord>().lambda();
        queryWrapper.in(SysImageRecord::getImageId, lists).or().eq(SysImageRecord::getUploader, targetUid);
        List<SysImageRecord> recordList = this.list(queryWrapper);
        //判断数据库返回的条数和前台提交的条数进行数量匹配
        if (recordList.isEmpty() || lists.size() != recordList.size()) return false;

        queryWrapper.clear();
        queryWrapper.in(SysImageRecord::getImageId, lists).or().eq(SysImageRecord::getUploader, targetUid);
        for (SysImageRecord record : recordList) {
            bunnyUtil.delete(record.getImageName() + "." + record.getSuffix(), record.getPath());
        }

        return this.remove(queryWrapper);
    }

    @Override
    public String uploadFileByBunny(MultipartFile file, String fileName, String path, Long uploader) {
        String orName = file.getOriginalFilename();
        //文件后缀为空的时候
        if (orName == null) return null;
        String suffix = orName.substring(orName.lastIndexOf(".") + 1);
        if(this.bunnyUtil.upload(file, fileName + "." + suffix, path)) {
            return this.url + (path.isEmpty() ? "/":(path + "/")) + fileName + "." + suffix;
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<Object> resourceFile(HttpServletRequest request) {
        String uri = request.getRequestURI();

        //获取后缀
        int suffixIndex = uri.lastIndexOf(".");
        if (suffixIndex == -1) return  ResponseEntity.notFound().build();
        TypeContentEnum type;
        try {
            type = TypeContentEnum.valueOf(uri.substring(suffixIndex + 1).toUpperCase());
        } catch (Exception e) {
            return new ResponseEntity<>(JsonResult.ERROR(404, "无法找到资源"), HttpStatus.NOT_FOUND);
        }

        File file = new File((System.getProperty("os.name").toLowerCase().startsWith("windows") ? ResourcePath.WINDOWS:ResourcePath.LINUX).getPath() + uri.replace("/file", ""));

        try {
            return file.isFile() ? ResponseEntity.ok().header("Content-type", type.getValue()).body(this.asyncLoadFile(file).get()):new ResponseEntity<>(JsonResult.ERROR(404, "无法找到资源"), HttpStatus.NOT_FOUND);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e, e);
            return new ResponseEntity<>(JsonResult.ERROR(500, "ServerError"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Async
    public CompletableFuture<byte[]> asyncLoadFile(File file) {
        byte[] bytes = new byte[(int) file.length()];
        try {
            InputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);
            inputStream.close();
        } catch (Exception e) {
            logger.error(e, e);
        }
        return CompletableFuture.completedFuture(bytes);
    }
}
