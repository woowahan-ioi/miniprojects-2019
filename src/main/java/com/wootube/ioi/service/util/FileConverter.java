package com.wootube.ioi.service.util;

import com.wootube.ioi.service.exception.FileConvertException;
import com.wootube.ioi.service.exception.FileUploadException;
import com.wootube.ioi.service.exception.InvalidFileExtensionException;
import org.imgscalr.Scalr;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileConverter {
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd-HH-mm-ss-";
    private static final int THUMBNAIL_WIDTH = 200;

    public File convert(MultipartFile file) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        File convertFile = new File(LocalDateTime.now().format(dateTimeFormatter) + file.getOriginalFilename());

        try {
            if (convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                return convertFile;
            }
        } catch (IOException e) {
            convertFile.delete();
        }
        throw new FileConvertException();
    }

    public File convert(File videoFile) {
        String fileName = videoFile.getAbsolutePath();
        String baseName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.lastIndexOf("."));

        double frameNumber = getFrameNumber(videoFile);
        return getThumbNailImageFile(fileName, baseName, frameNumber);
    }

    private double getFrameNumber(File videoFile) {
        try {
            SeekableByteChannel byteChannel = NIOUtils.readableFileChannel(videoFile);
            MP4Demuxer demuxer = new MP4Demuxer(byteChannel);
            DemuxerTrack track = demuxer.getVideoTrack();
            return track.getMeta().getTotalDuration() / 5.0;
        } catch (IOException e) {
            videoFile.delete();
            throw new InvalidFileExtensionException();
        }
    }

    private File getThumbNailImageFile(String fileName, String baseName, double frameNumber) {
        try {
            Picture thumbnail = FrameGrab.getNativeFrame(new File(fileName), frameNumber);
            BufferedImage img = AWTUtil.toBufferedImage(thumbnail);

            File thumbnailFile = new File(baseName + ".png");
            if (!thumbnailFile.exists()) {
                thumbnailFile.createNewFile();
            }

            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, THUMBNAIL_WIDTH, Scalr.OP_ANTIALIAS);
            ImageIO.write(thumbImg, "png", thumbnailFile);
            return thumbnailFile;
        } catch (IOException | JCodecException e) {
            throw new FileUploadException();
        }
    }
}
