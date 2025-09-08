package org.adzc.elevenapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.adzc.elevenapi.auth.dto.RegisterRequest;
import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.domain.UserMembership;
import org.adzc.elevenapi.domain.UserProfile;
import org.adzc.elevenapi.mapper.UserMapper;
import org.adzc.elevenapi.mapper.UserMembershipMapper;
import org.adzc.elevenapi.mapper.UserProfileMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 登录鉴权服务：使用 MyBatis 查库 + BCrypt 校验
 */
@Service
public class AuthService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserMembershipMapper userMembershipMapper;


    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserMapper userMapper, UserProfileMapper userProfileMapper, UserMembershipMapper userMembershipMapper,
                       JwtUtil jwtUtil, BCryptPasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.userMembershipMapper = userMembershipMapper;

        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    /**
     * 登录结果：token + user
     */
    public static class LoginResult {
        private final String token;
        private final User user;
        private final UserProfile userProfile;

        public LoginResult(String token, User user, UserProfile userProfile) {
            this.token = token;
            this.user = user;
            this.userProfile = userProfile;
        }

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }

        public UserProfile getUserProfile() {
            return userProfile;
        }
    }

    /**
     * 登录：identity（邮箱或手机号），password（明文）
     */
    public LoginResult login(String identity, String rawPassword) {
        String norm = normalizeIdentity(identity);
        User user = userMapper.findByIdentity(norm);
        if (user == null || !encoder.matches(safe(rawPassword), user.getPasswordHash())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        UserProfile userProfile = userProfileMapper.selectByPrimaryKey(user.getId());

        if (userProfile == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("nick", userProfile.getNickname());

        String subject = user.getIdentity() != null ? user.getIdentity() : norm;
        String token = jwtUtil.generateToken(subject, claims);
        return new LoginResult(token, user, userProfile);
    }

    @Transactional
    public User register(RegisterRequest req) {
        if (userMapper.findByIdentity(req.getIdentity()) != null) {
            throw new RuntimeException("用户已存在");
        }

        //用户基本信息
        User user = new User();
        user.setIdentity(req.getIdentity());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        userMapper.insert(user);

        //用户资料信息
        UserProfile profile = new UserProfile();
        profile.setUserId(user.getId());
        profile.setNickname(req.getNickname() != null ? req.getNickname() : "新用户");
        profile.setGender("male".equalsIgnoreCase(req.getGender()) ? 1 : 0);
        profile.setHidePhotos(false);
        profile.setFirstLogin(true);
        userProfileMapper.insert(profile);

        //用户会员信息
        UserMembership userMembership = new UserMembership();
        userMembership.setUserId(user.getId());
        userMembership.setTier("normal");
        userMembership.setDmLeft(0);
        userMembership.setInviteLeft(5);
        userMembership.setStartTime(new Date());
        userMembership.setExpireTime(DateUtils.addYears(new Date(), 1));
        userMembership.setAutoRenew(false);
        userMembership.setStatus("active");
        userMembershipMapper.insert(userMembership);
        return user;
    }

    /**
     * 解析 token
     */
    public Jws<Claims> parse(String token) {
        return jwtUtil.parseAndValidate(token);
    }

    /* ===== 工具方法 ===== */
    private static String normalizeIdentity(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.contains("@")) return s.toLowerCase(Locale.ROOT);
        return s.replaceAll("\\s+", "");
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
