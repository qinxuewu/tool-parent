package com.github.utils;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
public class FileUtil {
	
	
	
	/**
	 * 下载项目根目录的文件
	 * @param response response
	 * @param fileName 文件名
	 * @return 返回结果 成功或者文件不存在
	 */
	public static String downloadFile(HttpServletResponse response,String filePath,String fileName) {
		File path = null;
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;		
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File(path +filePath)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (FileNotFoundException e1) {
			return "系统找不到指定的文件";
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}


	/**
	 * 如果将项目部署到linux下有可能回出现，下载文件为空，或者出现下载文件时，文件出现乱码
	 * @param response response
	 * @param fileName 文件名
	 * @return 返回结果 成功或者文件不存在
	 */
	public static String downloadFileLinux(HttpServletResponse response, String fileName) {
		InputStream stream = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		try {
			String name = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLDecoder.decode(name, "ISO-8859-1") );
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			bis = new BufferedInputStream(stream);
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (FileNotFoundException e1) {
			return "系统找不到指定的文件";
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}
	

    /**
     * 获取文件创建时间
     *
     * @param srcPath 文件绝对路径
     * @return 时间
     */
    public static Date getCreateTime(String srcPath) {
        Path path = Paths.get(srcPath);
        BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class,
                LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr;
        try {
            attr = basicview.readAttributes();
            Date createDate = new Date(attr.creationTime().toMillis());
            return createDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 0, 1, 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取文件长和宽
     *
     * @param file file
     * @return String
     */
    public static String getImageWh(File file) {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(file));
            return image.getWidth() + "x" + image.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 获取原文件名 不带后缀
     * @param fileName
     * @return
     */
    public static String getFileName(MultipartFile file){
        return file.getOriginalFilename().split("[.]")[0];
    }


    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getNowFileName(String fileOriginName){
        return UUIDUtils.getUUID() + getSuffix(fileOriginName);
    }
    
    /**
     * 删除文件
     * @param path
     * @param fileName
     */
    public static void delFile(String path,String fileName){
     	try {
			File file = new File(ResourceUtils.getURL("classpath:").getPath()+path+fileName);
			if(file.exists()){
				file.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
   /**
    * 文件上传
    * @param file 文件
    * @param path 文件存放路径
    * @param fileName 源文件名
    * @return
    */
   public static boolean upload(MultipartFile file, String path, String fileName){
       //路径  +文件名
       String realPath = path + "/" + fileName;
       File dest = new File(realPath);
       //判断文件父目录是否存在
       if(!dest.getParentFile().exists()){
           dest.getParentFile().mkdir();
       }
       try {
           //保存文件
           file.transferTo(dest);
           return true;
       } catch (IllegalStateException e) {
           e.printStackTrace();
           return false;
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
   }
   
   
}
