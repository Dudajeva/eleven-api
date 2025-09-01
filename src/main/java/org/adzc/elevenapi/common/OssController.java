package org.adzc.elevenapi.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("/api/oss")
public class OssController {

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.bucket}")
    private String bucket;
    @Value("${oss.host}")
    private String host;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.sts.roleArn}")
    private String roleArn;
    @Value("${aliyun.sts.roleSessionName}")
    private String roleSessionName;
    @Value("${aliyun.sts.durationSeconds}")
    private long durationSeconds;

    // 生成当天前缀：user/{uid}/yyyyMMdd/
    private String buildDir(long uid) {
        String day = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return "user/" + uid + "/" + day + "/";
    }

    @GetMapping("/policy")
    public OssPolicyResp getPolicy(@RequestHeader(value = "X-UID", required = false) Long uidHeader,
                                   @RequestParam(value = "uid", required = false) Long uidParam) throws Exception {
        long uid = uidParam != null ? uidParam : (uidHeader != null ? uidHeader : 0L);
        if (uid <= 0) { uid = 0L; } // 游客可写公共前缀，或者直接拒绝

        // 1) 申请 STS 临时凭证
        AssumeRoleResponse.Credentials cred = assumeRole();

        // 2) 组策略：仅允许上传到指定前缀
        long expireTime = System.currentTimeMillis() / 1000 + durationSeconds;
        String dir = buildDir(uid);

        JSONObject policyConds = new JSONObject();
        // 条件数组形式：匹配前缀
        List<Object> conditions = new ArrayList<>();
        conditions.add(Arrays.asList("starts-with", "$key", dir));
        // 限制文件大小（0~20MB）
        conditions.add(Arrays.asList("content-length-range", 0, 20 * 1024 * 1024));
        policyConds.put("expiration", iso8601Date(expireTime * 1000));
        policyConds.put("conditions", conditions);

        String policy = Base64.getEncoder().encodeToString(policyConds.toJSONString().getBytes(StandardCharsets.UTF_8));
        String signature = sign(policy, cred.getAccessKeySecret());

        OssPolicyResp resp = new OssPolicyResp();
        resp.setHost(host);                 // e.g. https://bucket.oss-cn-xxx.aliyuncs.com
        resp.setDir(dir);
        resp.setAccessId(cred.getAccessKeyId());
        resp.setSecurityToken(cred.getSecurityToken());
        resp.setPolicy(policy);
        resp.setSignature(signature);
        resp.setExpire(expireTime);
        return resp;
    }

    private AssumeRoleResponse.Credentials assumeRole() throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);

        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysRegionId("cn-hangzhou");
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setDurationSeconds(durationSeconds);
        // 也可以设置权限最小化的 policy JSON（可选）

        AssumeRoleResponse response = client.getAcsResponse(request);
        return response.getCredentials();
    }

    private static String iso8601Date(long millis) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date(millis));
    }

    private static String sign(String policyBase64, String accessKeySecret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(accessKeySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        byte[] signData = mac.doFinal(policyBase64.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signData);
    }
}
