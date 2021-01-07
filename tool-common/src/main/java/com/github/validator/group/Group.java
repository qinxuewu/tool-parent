package com.github.validator.group;

import javax.validation.GroupSequence;


/*/**
* @Description:    定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
* @Author:         qinxuewu
* @CreateDate:     26/11/2018 下午 3:31
* @Email 870439570@qq.com
**/
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
