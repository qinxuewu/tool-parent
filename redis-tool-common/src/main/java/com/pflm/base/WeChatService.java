package com.pflm.base;
import java.io.File;
import java.util.List;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import com.pflm.utils.R;

/**
 * 微信api公共方法
 * @author qinxuewu
 * 2018年12月25日下午4:55:58
 */
@Component
public class WeChatService {
    @Autowired
    private WxMaService wxMaService;

	/**
     * 小程序 服务通知 公共方法
     * @param openid
     * @param page 跳转页面
     * @param templateId 模板ID
     * @param formid  表单id
     * @param list 数据集合
     * @return
     */
    public R sendTempMsg(String openid,String page,String templateId,String formid,List<WxMaTemplateData> list){
        try {
            WxMaTemplateMessage temp = new WxMaTemplateMessage();
            temp.setToUser(openid);
            temp.setFormId(formid);
            temp.setPage(page);
            temp.setTemplateId(templateId);
            temp.setData(list);
            //发送 服务通知
            wxMaService.getMsgService().sendTemplateMsg(temp);
            return R.ok();
        }catch (WxErrorException e){
            return R.error().put("msg", e.getError().getErrorMsg());
        }
    }
    
    /**
     * 小程序永久二维码创建
     * @return
     * @throws WxErrorException 
     */
    public File createWxaCodeUnlimit(String scene ,String page) throws WxErrorException{
    	return wxMaService.getQrcodeService().createWxaCodeUnlimit(scene,page);
    }
    



}
