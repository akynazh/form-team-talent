package com.xdu.formteamtalent.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class QrcodeUtil {
    @Value("${staticPath}")
    public void setStaticPath(String staticPath) {
        QrcodeUtil.staticPath = staticPath;
    }

    @Value("${aliAccessKeyId}")
    public void setAliAccessKeyId(String aliAccessKeyId) {
        QrcodeUtil.aliAccessKeyId = aliAccessKeyId;
    }

    @Value("${aliAccessKeySecret}")
    public void setAliAccessKeySecret(String aliAccessKeySecret) {
        QrcodeUtil.aliAccessKeySecret = aliAccessKeySecret;
    }

    @Value("${aliBucketName}")
    public void setAliBucketName(String aliBucketName) {
        QrcodeUtil.aliBucketName = aliBucketName;
    }

    @Value("${tcSecretId}")
    public void setTcSecretId(String tcSecretId) {
        QrcodeUtil.tcSecretId = tcSecretId;
    }

    @Value("${tcSecretKey}")
    public void setTcSecretKey(String tcSecretKey) {
        QrcodeUtil.tcSecretKey = tcSecretKey;
    }

    @Value("${tcRegion}")
    public void setTcRegion(String tcRegion) {
        QrcodeUtil.tcRegion = tcRegion;
    }

    @Value("${tcBucketName}")
    public void setTcBucketName(String tcBucketName) {
        QrcodeUtil.tcBucketName = tcBucketName;
    }

    private static String staticPath;
    private static String aliAccessKeyId;
    private static String aliAccessKeySecret;
    private static String aliBucketName;

    private static String tcSecretId;
    private static String tcSecretKey;
    private static String tcRegion;
    private static String tcBucketName;

    /**
     * 根据 aId 获取二维码目标地址
     */
    public static String getQrcodeDesUrlByAId(String aId) {
        return "/pages/page_activity/detail/detail?aId=" + aId;
    }

    /**
     * 根据 aId 获取二维码本地地址
     */
    public static String getQrcodeRealPathByAId(String aId) {
        String aRealQrcodePath = "/qrcode/" + aId + ".jpg";
        if (!staticPath.equals("")) {
            return staticPath + aRealQrcodePath; // 绝对路径
        } else {
            return "static" + aRealQrcodePath; // 相对路径，默认相对于类路径
        }
    }

    /**
     * 根据 aId 获取前端访问地址
     */
    public static String getQrcodeVisitPathByAId(String aId) {
        return "/qrcode/" + aId + ".jpg";
    }

    public static void generateQrcodeByAId(String aId) {
        String realPath = getQrcodeRealPathByAId(aId);
        String desUrl = getQrcodeDesUrlByAId(aId);
        FileUtil.touch(realPath);
        // FileUtil.file() 当地址为相对地址时默认相对与ClassPath!
        QrCodeUtil.generate(desUrl, 300, 300, new File(realPath));
    }

    public static String uploadToTCByAId(String aId) {
        COSClient cosClient = null;
        try {
            // 1 初始化用户身份信息（secretId, secretKey）。
            String secretId = tcSecretId;
            String secretKey = tcSecretKey;
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
            Region region = new Region(tcRegion);
            ClientConfig clientConfig = new ClientConfig(region);
            // 这里建议设置使用 https 协议
            // 从 5.6.54 版本开始，默认使用了 https
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 3 生成 cos 客户端。
            cosClient = new COSClient(cred, clientConfig);
            // 指定要上传的文件
            String realPath = QrcodeUtil.getQrcodeRealPathByAId(aId);
            File localFile = new File(realPath);
            // 指定文件将要存放的存储桶
            String bucketName = tcBucketName;
            // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
            String fileName = aId + ".jpg";
            String key = "qrcode/" + fileName;
            String visitUrl = "https://" + bucketName + ".cos." + tcRegion + ".myqcloud.com/" + key;
            com.qcloud.cos.model.PutObjectRequest putObjectRequest = new com.qcloud.cos.model.PutObjectRequest(bucketName, key, localFile);
            cosClient.putObject(putObjectRequest);
            log.info("upload to tencent successfully, visit: " + visitUrl);
            // 删除本地文件
            FileUtil.del(realPath);
            if (!FileUtil.exist(realPath)) {
                log.info("remove file: " + realPath + " successfully");
            } else {
                log.info("fail to remove file: " + realPath);
            }
            return visitUrl;
        } catch (Exception e) {
            log.info(e.getMessage());
            return "";
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    public static String uploadToAliByAId(String aId) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String fileName = aId + ".jpg";
        String objectName = "qrcode/" + fileName;
        String visitUrl = "https://" + aliBucketName + ".oss-cn-hangzhou.aliyuncs.com/" + objectName;
        String realPath = QrcodeUtil.getQrcodeRealPathByAId(aId);
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, aliAccessKeyId, aliAccessKeySecret);
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliBucketName, objectName,
                    new File(realPath));
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            ossClient.putObject(putObjectRequest);
            log.info("upload to ali successfully, visit: " + visitUrl);

            FileUtil.del(realPath);
            if (!FileUtil.exist(realPath)) {
                log.info("remove file: " + realPath + " successfully");
            } else {
                log.info("fail to remove file: " + realPath);
            }
            return visitUrl;
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
            return "";
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
            return "";
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}