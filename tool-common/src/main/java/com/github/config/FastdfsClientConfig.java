package com.github.config;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;


/**
 * FastDFS分布式文件服务器
 * @author qxw
 * @data 2018年7月27日下午6:52:28
 */
@Component
public class FastdfsClientConfig {
	private  final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${fastdfs.server.open: false}")
    private boolean open;
	/***
	 * 文件服务器fastDFS配置 多个地址;分开
	 */
	@Value("${fastdfs.server.host}")
	private String server;


	/**
	 * 文件服务器前端访问地址
	 */
	@Value("${fastdfs.server.view}")
	private String serverView;

	@PostConstruct
	public  void  init(){
		 if(open){
				String[] server_s=server.split(";");
				InetSocketAddress[] tracker_servers = new InetSocketAddress[server_s.length];
				for (int i = 0; i < server_s.length; i++) {
					String[] parts = server_s[i].split("\\:", 2);
					tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
				}
				ClientGlobal.g_tracker_group = new TrackerGroup(tracker_servers);
				ClientGlobal.g_connect_timeout=30001;
				ClientGlobal.g_connect_timeout=60001;
				logger.debug("*************初始好 文件服务器配置**"+ClientGlobal.configInfo());
		 }

	}


	/**
	 * 上传文件方法
	 * @param file
	 * @return
	 */

	public  String[] uploadFile(File file) throws IOException, MyException{
        String[] files = null;
        // 建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient client = new StorageClient(trackerServer, storageServer);
        files = client.upload_file(file.getAbsolutePath(), null, null);
        trackerServer.close();
        return files;
    }
	/**
	 * 上传文件方法
	 * @param fileConten
	 * @param name
	 * @return
	 */
	public String[] uploadFile(byte[] fileConten,String name) throws IOException, MyException{
        String[] files = null;
        // 建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient client = new StorageClient(trackerServer, storageServer);
        files = client.upload_file(name, null, null);
        trackerServer.close();
        return files;
    }



	/**
	 * 上传文件方法-返回文件路径
	 * @param file
	 * @return
	 */
	public String uploadFileReturnPath(File file) throws IOException, MyException{
		String[] files = null;
		// 建立连接
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient client = new StorageClient(trackerServer, storageServer);
		files = client.upload_file(file.getAbsolutePath(), null, null);
		trackerServer.close();
		String fileId = "";
		if(files.length >= 2){
			fileId = "/" + files[0] + "/" + files[1];
			return serverView + fileId;
		}else{
			logger.debug("上传错误,结果:{}", Arrays.asList(files));
			return "";
		}
	}


	/**
	 * 上传文件方法-返回文件路径
	 * @param fileConten
	 * @param name
	 * @return
	 */
	public  String uploadFileReturnPath(byte[] fileConten,String name) throws IOException, MyException{
		String[] files = null;
		// 建立连接
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient client = new StorageClient(trackerServer, storageServer);
		files = client.upload_file(name, null, null);
		trackerServer.close();

		String fileId = "";
		if(files.length >= 2){
			fileId = "/" + files[0] + "/" + files[1];
			return serverView + fileId;
		}else{
			logger.debug("上传错误,结果:{}", Arrays.asList(files));
			return "";
		}
	}


	/**
	 *
	 * @param groupName  存储文件服务器的组名
	 * @param name		  存储服务器上的文件名
	 * @return           0成功 非0失败
	 */
	public   int deleteFile(String groupName,String name) throws IOException, MyException{
        // 建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient client = new StorageClient(trackerServer, storageServer);
        return  client.delete_file(groupName, name);
    }



}
