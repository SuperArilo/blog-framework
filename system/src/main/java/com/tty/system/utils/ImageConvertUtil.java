package com.tty.system.utils;

import com.luciad.imageio.webp.WebPWriteParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Component
public class ImageConvertUtil {
    private static final Logger logger = Logger.getLogger(ImageConvertUtil.class);
    @Value("${file.picture.picture-form}")
    private String PictureForm;
    @Value("${file.picture.image-list}")
    private List<String> imageList;
    public MultipartFile pictureToWebP(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) return null;
        File file = File.createTempFile("prefix", "." + this.PictureForm);
        try {
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            writeParam.setAlphaFiltering(1);

            writer.setOutput(new FileImageOutputStream(file));

            writer.write(null, new IIOImage(ImageIO.read(multipartFile.getInputStream()), null, null), writeParam);

            return new MockMultipartFile("WebpFile", multipartFile.getName(), null, new FileInputStream(file));
        } catch (IOException e) {
            logger.error(e, e);
            return null;
        } finally {
            file.delete();
        }
    }

    /**
     * 返回图片后缀格式
     * @param file 文件类
     * @return 如果满足则返回，不满足则返回 null
     */
    public String imageFileCheck(MultipartFile file) {
        String orName = file.getOriginalFilename();
        if (orName == null) return null;
        String suffix = orName.substring(orName.lastIndexOf(".") + 1);
        return this.imageList.contains(suffix.toUpperCase()) ? suffix:null;
    }
}
