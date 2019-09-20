package com.github.modules.apidoc.controller;
import java.util.concurrent.TimeUnit;
import com.github.annotation.RedisRateLimit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import com.github.utils.JwtUtil;
import com.github.utils.R;


/**
 * 访问地址： http://localhost:8081/swagger-ui.html
 * 
 * @Api：修饰整个类，描述Controller的作用
 * @ApiOperation：描述一个类的一个方法，或者说一个接口
 * @ApiParam：单个参数描述
 * @ApiModel：用对象来接收参数
 * @ApiProperty：用对象接收参数时，描述对象的一个字段
 * @ApiResponse：HTTP响应其中1个描述
 * @ApiResponses：HTTP响应整体描述
 * @ApiIgnore：使用该注解忽略这个API
 * @ApiError ：发生错误返回的信息
 * @ApiImplicitParam：一个请求参数
 * @ApiImplicitParams：多个请求参数
 * 
 * 
 * @author qinxuewu 2018年12月27日下午6:45:33
 *
 */
@RestController
@RequestMapping("/doc")
@Api("api测试接口")
public class ApiDocController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	 @Autowired
	 private RedissonClient redissonClient;



	 @RequestMapping("redislimit")
	 @ApiOperation("redis限流接口测试")
	 @RedisRateLimit(key = "test_limit_key", time = 60, count = 1,msg="操作太频繁,请稍后再试")
	 public R redislimit(){
	 return R.ok();
	 }



	 @PostMapping("redislock")
	 @ApiOperation("RedisSession分布式锁接口测试")
	 public R redislock(){
		 RLock fairLock = redissonClient.getLock("test_lock_key");
		 //尝试加锁，最多等待20秒，上锁以后7秒自动解锁
		 try {
				 boolean res = fairLock.tryLock(20, 7, TimeUnit.SECONDS);
		 return R.ok().put("locktFalg", res);
		 } catch (Exception e) {
				 return R.error();
		 }finally{
				fairLock.unlock();
		 }
	 }

	/**
	 * 使用该注解忽略这个API
	 * 
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/hi", method = RequestMethod.GET)
	public String jsonTest() {
		return " hi you!";
	}

	/**
	 * 接口token生成
	 * @return
	 */
	@ApiOperation(value = "接口token生成", notes = "接口token生成")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "success"),
			@ApiResponse(code = 400, message = "废弃 无视它"),
			@ApiResponse(code = 401, message = "废弃 无视它"),
			@ApiResponse(code = 404, message = "废弃 无视它"),
			@ApiResponse(code = 403, message = "废弃 无视它"),
			@ApiResponse(code = 500, message = "后端统一失败提示 直接显示") })
	@PostMapping("/api/auth/getToken")
	public R getToken(
			@RequestParam(value = "openid", required = true) String openid) {
		String token = JwtUtil.generateToken(openid);
		return R.ok().put("token", token);
	}


}
