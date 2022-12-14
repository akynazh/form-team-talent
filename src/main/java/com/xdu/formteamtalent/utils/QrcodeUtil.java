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
     * ?????? aId ???????????????????????????
     */
    public static String getQrcodeDesUrlByAId(String aId) {
        return "/pages/page_activity/detail/detail?aId=" + aId;
    }

    /**
     * ?????? aId ???????????????????????????
     */
    public static String getQrcodeRealPathByAId(String aId) {
        String aRealQrcodePath = "/qrcode/" + aId + ".jpg";
        if (!staticPath.equals("")) {
            return staticPath + aRealQrcodePath; // ????????????
        } else {
            return "static" + aRealQrcodePath; // ???????????????????????????????????????
        }
    }

    /**
     * ?????? aId ????????????????????????
     */
    public static String getQrcodeVisitPathByAId(String aId) {
        return "/qrcode/" + aId + ".jpg";
    }

    public static void generateQrcodeByAId(String aId) {
        String realPath = getQrcodeRealPathByAId(aId);
        String desUrl = getQrcodeDesUrlByAId(aId);
        FileUtil.touch(realPath);
        // FileUtil.file() ??????????????????????????????????????????ClassPath!
        QrCodeUtil.generate(desUrl, 300, 300, new File(realPath));
    }

    public static String uploadToTCByAId(String aId) {
        COSClient cosClient = null;
        try {
            // 1 ??????????????????????????????secretId, secretKey??????
            String secretId = tcSecretId;
            String secretKey = tcSecretKey;
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2 ?????? bucket ?????????, COS ???????????????????????? https://cloud.tencent.com/document/product/436/6224
            // clientConfig ?????????????????? region, https(?????? http), ??????, ????????? set ??????, ??????????????????????????????????????? Java SDK ?????????
            Region region = new Region(tcRegion);
            ClientConfig clientConfig = new ClientConfig(region);
            // ???????????????????????? https ??????
            // ??? 5.6.54 ?????????????????????????????? https
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 3 ?????? cos ????????????
            cosClient = new COSClient(cred, clientConfig);
            // ????????????????????????
            String realPath = QrcodeUtil.getQrcodeRealPathByAId(aId);
            File localFile = new File(realPath);
            // ????????????????????????????????????
            String bucketName = tcBucketName;
            // ????????????????????? COS ????????????????????????????????????????????????folder/picture.jpg????????????????????? picture.jpg ????????? folder ?????????
            String fileName = aId + ".jpg";
            String key = "qrcode/" + fileName;
            String visitUrl = "https://" + bucketName + ".cos." + tcRegion + ".myqcloud.com/" + key;
            com.qcloud.cos.model.PutObjectRequest putObjectRequest = new com.qcloud.cos.model.PutObjectRequest(bucketName, key, localFile);
            cosClient.putObject(putObjectRequest);
            log.info("upload to tencent successfully, visit: " + visitUrl);
            // ??????????????????
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
        // Endpoint?????????1???????????????????????????Region???????????????????????????
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // ??????Object??????????????????????????????????????????Bucket???????????????exampledir/exampleobject.txt???
        String fileName = aId + ".jpg";
        String objectName = "qrcode/" + fileName;
        String visitUrl = "https://" + aliBucketName + ".oss-cn-hangzhou.aliyuncs.com/" + objectName;
        String realPath = QrcodeUtil.getQrcodeRealPathByAId(aId);
        // ??????OSSClient?????????
        OSS ossClient = new OSSClientBuilder().build(endpoint, aliAccessKeyId, aliAccessKeySecret);
        try {
            // ??????PutObjectRequest?????????
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliBucketName, objectName,
                    new File(realPath));
            // ???????????????????????????????????????????????????????????????????????????????????????
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // ???????????????
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