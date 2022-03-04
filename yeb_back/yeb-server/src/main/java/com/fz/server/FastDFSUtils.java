package com.fz.server;

import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/24/15:05
 * @Description: fastdfs工具类
 */
public class FastDFSUtils {
    private static Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    /**
     * 初始化客户端
     * ClientGlobal.init读取配置文件，并初始化对应属性
     */
    static {
        try {
            //服务器上jar包不能使用getfile
//            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClassPathResource classPathResource = new ClassPathResource("fdfs_client.conf");
            String filePath = ResourceUtils.getURL("classpath:")+"fdfs_client.conf";
            System.out.println("==============================================");
            System.out.println("获取到的文件路径为："+filePath);
            System.out.println("==============================================");
            ClientGlobal.init(filePath);
        } catch (Exception exception) {
//            exception.printStackTrace();
            logger.error("初始化fastdfs失败",exception);
        }

    }

    /**
     * 生成tracker服务器
     * @return
     */
    private static TrackerServer get() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        return trackerServer;
    }

    /**
     * 生成storage客户端
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = get();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file){
        String filename = file.getOriginalFilename();
        logger.info("文件名：",filename);
        StorageClient storageClient = null;
        String[] uploadResults = null;
        try {
            //获取storage客户端
            storageClient = getStorageClient();
            //上传
            uploadResults = storageClient.upload_file(filename.getBytes(), filename.substring(filename.indexOf(".") + 1), null);
        } catch (Exception e) {
            logger.error("上传文件失败",e);
        }
        if(null==uploadResults){
            logger.error("上传失败",storageClient.getErrorCode());
        }
        return uploadResults;
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFileInfo(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        FileInfo file_info = null;
        try {
            storageClient = getStorageClient();
            file_info = storageClient.get_file_info(groupName, remoteFileName);
        }catch (Exception e){
            logger.error("文件信息获取失败",e.getMessage());
        }
        return file_info;
    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            byte[] fileBytes = storageClient.download_file(groupName, remoteFileName);
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            return inputStream;
        }catch (Exception e){
            logger.error("文件下载失败",e.getMessage());
        }
        return null;
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            storageClient.delete_file(groupName,remoteFileName);
        }catch (Exception e){
            logger.error("文件删除失败",e.getMessage());
        }
    }

    /**
     * 获取文件路径
     * @return
     */
    public static String getTrackerUrl(){
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        StorageServer storeStorage = null;
        try {
           trackerServer = trackerClient.getTrackerServer();
           storeStorage = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("文件路径获取失败",e.getMessage());
        }
        return "http://"+storeStorage.getInetSocketAddress().getHostString()+":8888/";
    }
}
