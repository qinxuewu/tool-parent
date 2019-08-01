//package com.pflm;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import me.chanjar.weixin.common.error.WxErrorException;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.pflm.base.WeChatService;
//
//
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class WebankApiApplicationTests {
//
//	@Autowired
//	private WeChatService weChatService;
//
//    @Test
//    public void test1() throws IOException {
//		try {
//			File imgFile = weChatService.createWxaCodeUnlimit("isSign","pages/index/index");
//			//String 
//			String imgPath="C:\\Users\\admin\\Desktop";
//			String imgName="签到二维码.png";
//			File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等
//            FileOutputStream fos=new FileOutputStream(file);
//            FileInputStream fis = new FileInputStream(imgFile);
//            
//            byte[] b = new byte[1024];
//            int nRead = 0;
//            while ((nRead = fis.read(b)) != -1) {
//                fos.write(b, 0, nRead);
//            }
//            fos.flush();
//            fos.close();
//            fis.close();
//		} catch (WxErrorException e) {
//			e.printStackTrace();
//		}
//	
//    }
//
//}
