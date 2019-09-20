package com.github.config;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 公众号,小程序信息配置
 */
@Component
public class WeChatConfig {

 
    @Value("${xcx.appid}")
    protected String xcxAppid;
    
    @Value("${xcx.appsecret}")
    protected String xcxAppSecret;
    

    /**
     * 小程序信息
     * @return
     */
    @Bean
    public WxMaConfig wxMaConfig(){
        WxMaInMemoryConfig wxMaInMemoryConfig = new WxMaInMemoryConfig();
        wxMaInMemoryConfig.setAppid(xcxAppid);
        wxMaInMemoryConfig.setSecret(xcxAppSecret);
        wxMaInMemoryConfig.setMsgDataFormat("JSON");
        return wxMaInMemoryConfig;
    }

    /**
     * 小程序信息
     * @return
     */
    @Bean
    public WxMaService wxMaService(){
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaConfig());
        return  wxMaService;
    }


 
}
