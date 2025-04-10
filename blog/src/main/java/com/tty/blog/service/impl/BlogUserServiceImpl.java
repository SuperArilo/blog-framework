package com.tty.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.blog.dto.BlogModifyEmailDTO;
import com.tty.blog.dto.BlogUserProfilesModifyDTO;
import com.tty.blog.dto.sql.UserProfilesModifyDTO;
import com.tty.blog.entity.BlogUser;
import com.tty.blog.entity.BlogUserMeans;
import com.tty.blog.enums.MailType;
import com.tty.blog.event.UserLoginOutEvent;
import com.tty.blog.mapper.BlogUserMapper;
import com.tty.blog.mapper.BlogUserMeansMapper;
import com.tty.blog.service.BlogUserService;
import com.tty.blog.service.SendMailService;
import com.tty.blog.vo.BlogUserInfoVO;
import com.tty.blog.vo.BlogUserProfilesVO;
import com.tty.blog.vo.LoginSuccessVO;
import com.tty.common.entity.TokenUser;
import com.tty.common.enums.ImageType;
import com.tty.common.enums.InputRegex;
import com.tty.common.enums.JsonWebTokenTypeEnum;
import com.tty.common.enums.redis.FindPasswordKey;
import com.tty.common.enums.redis.UserKey;
import com.tty.common.utils.*;
import com.tty.system.service.SysManagerFileService;
import com.tty.system.utils.ImageConvertUtil;
import io.github.cooperlyt.cloud.uid.impl.DefaultUidGenerator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
public class BlogUserServiceImpl extends ServiceImpl<BlogUserMapper, BlogUser> implements BlogUserService {

    private static final Logger logger = Logger.getLogger(BlogUserServiceImpl.class);

    @Value("${jwt.token.seconds.login}")
    private int loginTokenSeconds;

    @Resource
    BlogUserMeansMapper blogUserMeansMapper;
    @Resource
    RedisUtil redisUtil;
    @Resource
    DefaultUidGenerator defaultUidGenerator;
    @Resource
    JsonWebTokenUtil jsonWebTokenUtil;
    @Resource
    FunctionTool functionTool;
    @Resource
    SendMailService sendMailService;
    @Resource
    ApplicationEventPublisher eventPublisher;
    @Resource
    ImageConvertUtil imageConvertUtil;
    @Resource
    SysManagerFileService sysManagerFileService;

    @Value("${custom.login-off-avatar}")
    private String loginOffAvatar;
    @Value("${custom.default-avatar}")
    private String defaultAvatar;
    @Value("${custom.default-background}")
    private String defaultBackground;
    @Value("${cdn.path}")
    protected String path;
    @Override
    public JsonResult registerUser(String email, String password, String nickName, String verifyCode, HttpServletResponse response) {

        //验证合法
        if(!email.trim().matches(InputRegex.Email.getRegex())) return JsonResult.ERROR(0,"邮箱不合法！");
        if(!password.trim().matches(InputRegex.Password.getRegex())) return JsonResult.ERROR(0,"密码不合法！");
        if(!nickName.trim().matches(InputRegex.NickName.getRegex())) return JsonResult.ERROR(0,"昵称不合法！");

        if (!sendMailService.verify(email, verifyCode, MailType.Register)) return JsonResult.ERROR(400, "邮箱验证码不正确");

        LambdaQueryWrapper<BlogUser> blogUserLambdaQueryWrapper = new QueryWrapper<BlogUser>().lambda();
        LambdaQueryWrapper<BlogUserMeans> blogUserMeansLambdaQueryWrapper = new QueryWrapper<BlogUserMeans>().lambda();
        blogUserLambdaQueryWrapper.eq(BlogUser::getEmail, email);


        //查询是否被注册了
        BlogUser query = this.getOne(blogUserLambdaQueryWrapper);
        if (query != null) return JsonResult.ERROR(0, "邮箱被注册！");

        //查询NickName是否被使用
        blogUserMeansLambdaQueryWrapper.eq(BlogUserMeans::getNickName, nickName);
        BlogUserMeans one = this.blogUserMeansMapper.selectOne(blogUserMeansLambdaQueryWrapper);
        if (one != null) return JsonResult.ERROR(0, "啊哦，您输入的昵称已被使用了哦");


        //写入用户注册信息
        BlogUser blogUser = new BlogUser();
        Long newUid = this.defaultUidGenerator.getUID().block();
        blogUser.setUid(newUid);
        blogUser.setEmail(email);
        blogUser.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        blogUser.setRegisterTime(new Date());

        //写入用户个人资料
        BlogUserMeans blogUserMeans = new BlogUserMeans();
        blogUserMeans.setUid(newUid);
        blogUserMeans.setAvatar(this.defaultAvatar);
        blogUserMeans.setNickName(nickName);
        blogUserMeans.setSex(null);
        blogUserMeans.setAge(null);
        blogUserMeans.setBackground(this.defaultBackground);

        this.save(blogUser);
        this.blogUserMeansMapper.insert(blogUserMeans);


        //注册后向用户发送欢迎

        return JsonResult.OK("注册成功！");
    }

    @Override
    public JsonResult login(String email, String password, HttpServletRequest request) {
        if(email == null || password == null) return JsonResult.ERROR(400, "请求缺少必要的参数");
        String encryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<BlogUser> queryWrapper = new QueryWrapper<BlogUser>().lambda();
        queryWrapper.eq(BlogUser::getEmail, email).eq(BlogUser::getPassword, encryptPassword);

        BlogUser queryUser = this.getOne(queryWrapper);
        if (queryUser == null || queryUser.getLoginOff()) return JsonResult.ERROR(400, "密码错误或者用户不存在！");
        if (!queryUser.getStatus()) return JsonResult.ERROR(401, "当前用户被禁用，如有疑问，请联系管理员");

        BlogUserInfoVO queryUserInfo = this.baseMapper.loginUser(email, encryptPassword);
        LoginSuccessVO successVO = new LoginSuccessVO();
        successVO.setUser(queryUserInfo);

        //查询redis里是否有上次登录未过期的token
        String key = functionTool.buildRedisKey(false, UserKey.User.getKey(), queryUserInfo.getUid());
        Object redisToken = redisUtil.get(key);

        TokenUser tokenUser = new TokenUser();
        tokenUser.setUsername(queryUserInfo.getNickName());
        tokenUser.setId(queryUserInfo.getUid());
        tokenUser.setType(JsonWebTokenTypeEnum.USER);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.loginTokenSeconds);

        //没有则生成新的
        if (redisToken == null) {
            String newToken = jsonWebTokenUtil.createToken(tokenUser, calendar.getTime(), JsonWebTokenTypeEnum.USER);
            redisUtil.set(key, newToken, this.loginTokenSeconds, TimeUnit.SECONDS);
            successVO.setToken(newToken);
        } else {
            //不为空则验证token是否过期
            try {
                jsonWebTokenUtil.verifyToken(redisToken.toString(), JsonWebTokenTypeEnum.USER);
                successVO.setToken(redisToken.toString());
            } catch (Exception e) {
                logger.error(e, e);
                String newToken = jsonWebTokenUtil.createToken(tokenUser, calendar.getTime(), JsonWebTokenTypeEnum.USER);
                redisUtil.set(key, newToken, this.loginTokenSeconds, TimeUnit.SECONDS);
                successVO.setToken(newToken);
            }
        }
        return JsonResult.OK("登录成功！", successVO);
    }

    @Override
    public JsonResult token(HttpServletRequest request) {

        String token = request.getHeader("token");

        LoginSuccessVO vo = new LoginSuccessVO();
        vo.setToken(token);
        vo.setUser(this.baseMapper.queryByUid(jsonWebTokenUtil.getPayLoad(token, TokenUser.class).getId()));

        return JsonResult.OK("success", vo);
    }

    @Override
    public JsonResult findPasswordVerify(String email, String verifyCode) {
        if (email.isEmpty() || verifyCode.isEmpty()) return JsonResult.ERROR(400, "服务器收到的参数为空");
        StringBuilder stringBuilder = new StringBuilder();
        String fKey = this.functionTool.buildRedisKey(true, FindPasswordKey.Main.getKey(), email);
        Object codeList =  redisUtil.get(fKey + this.functionTool.buildRedisKey(false, FindPasswordKey.Code.getKey()));
        if (codeList instanceof List<?>) {
            for (Object o : (List<?>) codeList) {
                stringBuilder.append(o);
            }
            if (!stringBuilder.toString().equals(verifyCode)) return  JsonResult.ERROR(400, "服务器检查到验证码不正确");
            UUID uuid = UUID.randomUUID();
            redisUtil.set(fKey + FindPasswordKey.UUID.getKey(), uuid, 10, TimeUnit.MINUTES);
            return JsonResult.OK("授权码验证成功", uuid);
        } else {
            return JsonResult.ERROR(0, "验证码类型有误，请检查");
        }
    }

    @Override
    public JsonResult passwordModify(String verifyUUID, String email, String password, String passwordAgain) {
        String fKey = this.functionTool.buildRedisKey(true, FindPasswordKey.Main.getKey(), email);
        Object uuid = redisUtil.get(fKey + FindPasswordKey.UUID.getKey());
        if (uuid == null) return JsonResult.ERROR(403, "服务器没有查询到此请求");
        if (!verifyUUID.equals(uuid.toString())) return JsonResult.ERROR(403, "服务器验证uuid不正确");
        if (!password.equals(passwordAgain)) return JsonResult.ERROR(400, "服务器验证到两次输入的密码不一致");
        redisUtil.delete(this.functionTool.buildRedisKey(false, FindPasswordKey.Main.getKey(), email));

        if (this.update(
                new UpdateWrapper<BlogUser>().lambda()
                        .eq(BlogUser::getEmail, email)
                        .set(BlogUser::getPassword, DigestUtils.md5DigestAsHex(password.getBytes())))) {
            return JsonResult.OK("修改成功，请重新登录");
        } else {
            return JsonResult.ERROR(404, "服务器没有查询到要被修改的用户");
        }
    }

    @Override
    public JsonResult loginOutUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        this.eventPublisher.publishEvent(new UserLoginOutEvent(this, token, IpUtil.getIpAdder(request), this.jsonWebTokenUtil.getPayLoad(token, TokenUser.class).getId()));
        return JsonResult.OK("您已退出登录");
    }

    @Override
    public JsonResult userProfiles(Long viewUid, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long viewer = null;
        try {
            this.jsonWebTokenUtil.verifyToken(token, JsonWebTokenTypeEnum.USER);
            viewer = this.jsonWebTokenUtil.getPayLoad(token, TokenUser.class).getId();
        } catch (Exception ignored) {}
        BlogUser user = this.getOne(new QueryWrapper<BlogUser>().lambda().eq(BlogUser::getUid, viewUid));
        if (user == null) return JsonResult.OK("查询成功", null);
        if (user.getLoginOff()) {
            BlogUserProfilesVO vo = new BlogUserProfilesVO();
            vo.setNickName("该用户已注销");
            vo.setAvatar(this.loginOffAvatar);
            vo.setUid(viewUid);
            return JsonResult.OK("查询成功", vo);
        } else {
            BlogUserProfilesVO find = this.blogUserMeansMapper.queryUserProfiles(viewUid);
            //处理被其他用户浏览时会泄露被查询用户的email
            if (!Objects.equals(viewer, find.getUid())) {
                find.setEmail(null);
            }
            find.setViewerLike(blogUserMeansMapper.queryViewerIsLikeToTarget(viewer, viewUid));
            return JsonResult.OK("查询成功", find);
        }

    }

    @Override
    public JsonResult userProfilesModify(BlogUserProfilesModifyDTO modifyDTO, HttpServletRequest request) {
        if (Stream.of(modifyDTO.getPassword(),
                        modifyDTO.getSex(),
                        modifyDTO.getContact(),
                        modifyDTO.getAvatar(),
//                        modifyDTO.getBackground(),
                        modifyDTO.getAge(),
                        modifyDTO.getNickName(),
                        modifyDTO.getAutograph())
                .allMatch(Objects::isNull)) return JsonResult.ERROR(400, "服务器没有收到任何的参数");
        Long uploader = this.jsonWebTokenUtil.getPayLoad(request.getHeader("token"), TokenUser.class).getId();
        UserProfilesModifyDTO sqlDTO = new UserProfilesModifyDTO();

        if (modifyDTO.getAutograph() != null && !Objects.equals(modifyDTO.getAutograph(), "")) {
            sqlDTO.setAutograph(modifyDTO.getAutograph());
        }

        if (modifyDTO.getNickName() != null && !Objects.equals(modifyDTO.getNickName(), "")) {
            if (!modifyDTO.getNickName().trim().matches(InputRegex.NickName.getRegex())) return JsonResult.ERROR(400, "昵称不符合要求");
            if (this.blogUserMeansMapper.queryUserProfiles(uploader).getNickName().equals(modifyDTO.getNickName())) return JsonResult.ERROR(400, "改昵称已被占用了哦");
            sqlDTO.setNickName(modifyDTO.getNickName());
        }

        if (modifyDTO.getAge() != null) {
            sqlDTO.setAge(modifyDTO.getAge());
        }
        if (modifyDTO.getSex() != null && modifyDTO.getSex() <= 3) {
            sqlDTO.setSex(modifyDTO.getSex());
        }
        String avatarName = null;
        String avatarSuffix;
        if(modifyDTO.getAvatar() != null && !modifyDTO.getAvatar().isEmpty()) {
            avatarSuffix = this.imageConvertUtil.imageFileCheck(modifyDTO.getAvatar());
            if (avatarSuffix != null) {
                avatarName = UUID.randomUUID().toString();
            } else {
                return JsonResult.ERROR(400, "文件类型不支持");
            }
        }

        if (modifyDTO.getContact() != null && !Objects.equals(modifyDTO.getContact(), "")) {
            sqlDTO.setContact(modifyDTO.getContact());
        }
//        String backgroundName = null;
//        String backgroundSuffix = null;
//        if (modifyDTO.getBackground() != null && !modifyDTO.getBackground().isEmpty()) {
//            backgroundSuffix = this.imageConvertUtil.imageFileCheck(modifyDTO.getBackground());
//            if (backgroundSuffix != null) {
//                backgroundName = UUID.randomUUID().toString();
//            } else {
//                return JsonResult.ERROR(400, "文件类型不支持");
//            }
//        }
        if (modifyDTO.getPassword() != null && !Objects.equals(modifyDTO.getPassword(), "")) {
            if(modifyDTO.getPassword().trim().matches(InputRegex.Password.getRegex())) {
                sqlDTO.setPassword(DigestUtils.md5DigestAsHex(modifyDTO.getPassword().getBytes()));
            } else {
                return JsonResult.ERROR(400, "密码格式不正确");
            }
        }

        if (avatarName != null) {
            String a = this.sysManagerFileService.uploadImageByBunny(modifyDTO.getAvatar(), avatarName, this.path, uploader, ImageType.Normal);
            if (a == null) return JsonResult.ERROR(500, "上传图片发生错误，请联系管理员");
            sqlDTO.setAvatarUrl(a);
        }
//        if (backgroundName != null) {
//            String b = this.sysManagerFileService.uploadImageByBunny(modifyDTO.getBackground(), backgroundName, this.path, uploader, ImageType.Normal);
//            if (b == null) return JsonResult.ERROR(500, "上传图片发生错误，请联系管理员");
//            sqlDTO.setBackgroundUrl(b);
//        }

        sqlDTO.setUploader(uploader);
        if (this.blogUserMeansMapper.updateUserProfiles(sqlDTO)) {
            return JsonResult.OK("保存成功！");
        } else {
            return JsonResult.OK("服务器没有更新任何数据");
        }
    }

    @Override
    public JsonResult userProfilesModifyEmail(BlogModifyEmailDTO emailDTO, HttpServletRequest request) {
        String preToken = emailDTO.getPreviousToken();
        String verifyCode = emailDTO.getVerifyCode();
        String token = request.getHeader("token");
        if (preToken == null || verifyCode == null) return JsonResult.ERROR(400, "服务器收到的参数为空");
        if (preToken.equals(token)) {
            TokenUser verifyUser = this.jsonWebTokenUtil.getPayLoad(token, TokenUser.class);
            Map<?, ?> map = (Map<?, ?>) redisUtil.get(verifyUser.getId() + "_code_modify_email");
            if (map == null) return JsonResult.ERROR(400, "服务器没有查询到请求");
            StringBuilder code = new StringBuilder();
            Object codeList = map.get("code");
            if(codeList instanceof ArrayList<?>) {
                for (Object o : (List<?>) codeList) {
                    code.append(o);
                }
                if (!code.toString().equals(verifyCode)) {
                    return JsonResult.ERROR(400, "服务器检查到验证码不正确");
                } else {
                    this.update(new UpdateWrapper<BlogUser>().lambda().eq(BlogUser::getUid, verifyUser.getId()).set(BlogUser::getEmail, map.get("email").toString()));
                    this.redisUtil.delete(verifyUser.getId() + "_code_modify_email");
                    TokenUser tokenUser = new TokenUser();
                    tokenUser.setUsername(verifyUser.getUsername());
                    tokenUser.setId(verifyUser.getId());
                    tokenUser.setType(JsonWebTokenTypeEnum.USER);
                    return JsonResult.OK("修改成功", this.jsonWebTokenUtil.createToken(tokenUser, Calendar.getInstance().getTime(), JsonWebTokenTypeEnum.USER));
                }
            } else {
                return JsonResult.ERROR(500, "服务器验证到类型不正确，请联系管理员");
            }
        } else {
            return JsonResult.ERROR(403,"数据不一致，已终止！");
        }
    }
}
