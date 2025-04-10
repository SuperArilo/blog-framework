package com.tty.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.enums.ImageType;
import com.tty.system.entity.SysImageRecord;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysManagerFileService extends IService<SysImageRecord> {
    /**
     * @param file 上传的文件对象
     * @param fileName 文件名称，不能出现重复名称
     * @param path 上传到Bunny的绝对路径
     * @param uploader 上传者，UID
     * @param type 上传文件的类型 ImageType
     * @return 返回上传成功后的完整URL，如果上传失败返回 null
     */
    String uploadImageByBunny(MultipartFile file, String fileName, String path, Long uploader, ImageType type);
    /**
     * @param lists 需要删除的文件对象id列表
     * @param targetUid 之前上传者的UID
     * @return 返回删除的个数
     */
    boolean deleteImageByBunny(List<Long> lists, Long targetUid);
    /**
     * @param file 上传的文件对象
     * @param fileName 文件名称，不能出现重复名称
     * @param path 上传到Bunny的绝对路径
     * @param uploader 上传者，UID
     * @return 返回上传成功后的完整URL，如果上传失败返回 null
     */
    String uploadFileByBunny(MultipartFile file, String fileName, String path, Long uploader);



    ResponseEntity<Object> resourceFile(HttpServletRequest request);
}
