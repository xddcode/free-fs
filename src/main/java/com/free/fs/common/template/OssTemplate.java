package com.free.fs.common.template;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.utils.FileUtil;
import com.free.fs.model.FilePojo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "oss")
public class OssTemplate {

    /**
     * 每次迭代的长度限制，最大1000，推荐值 1000
     */
    private static final int LIMIT = 1000;

    /**
     * 定义每个分片的大小 默认5MB
     */
    private static final long PART_SIZE = 5 * 1024 * 1024L;

    /**
     * 分片上传进度值
     */
    private static int uploadedPart = 0;

    @Resource
    private FsServerProperties fileProperties;

    /**
     * 查询oss的资源列表
     *
     * @return
     */
    public List<OSSObjectSummary> list() {
        OSS ossClient = new OSSClientBuilder().build(
                fileProperties.getOss().getEndpoint(),
                fileProperties.getOss().getAccessKey(),
                fileProperties.getOss().getSecretKey()
        );
        // 列举文件。
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(fileProperties.getOss().getBucket()).withMaxKeys(LIMIT));
        ossClient.shutdown();
        return objectListing.getObjectSummaries();
    }

    /**
     * 上传文件-oss
     * 返回字符串
     *
     * @param file
     * @return
     */
    @SneakyThrows
    public FilePojo upload(MultipartFile file) {
        OSS ossClient = new OSSClientBuilder().build(
                fileProperties.getOss().getEndpoint(),
                fileProperties.getOss().getAccessKey(),
                fileProperties.getOss().getSecretKey()
        );
        FilePojo pojo = FileUtil.buildFilePojo(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(FileUtil.getcontentType(pojo.getFileName().substring(pojo.getFileName().lastIndexOf("."))));
        PutObjectRequest putObjectRequest = new PutObjectRequest(fileProperties.getOss().getBucket(), pojo.getFileName(),file.getInputStream());
        putObjectRequest.setMetadata(metadata);
        ossClient.putObject(putObjectRequest);
        String url = fileProperties.getOss().getPath() + CommonConstant.DIR_SPLIT + pojo.getFileName();
        pojo.setUrl(url);
        ossClient.shutdown();
        return pojo;
    }

    /**
     * 删除对象
     *
     * @param objectPath 对象路径
     */
    public void delete(String objectPath) {
        OSS ossClient = new OSSClientBuilder().build(
                fileProperties.getOss().getEndpoint(),
                fileProperties.getOss().getAccessKey(),
                fileProperties.getOss().getSecretKey()
        );
        String key = objectPath.replaceAll(fileProperties.getOss().getPath() + CommonConstant.DIR_SPLIT, "");
        ossClient.deleteObject(fileProperties.getOss().getBucket(), key);
        ossClient.shutdown();
    }

    /**
     * 下载对象
     *
     * @param objectName
     * @param response
     */
    @SneakyThrows
    public void download(String objectName,HttpServletResponse response) {
        OSS ossClient = new OSSClientBuilder().build(
                fileProperties.getOss().getEndpoint(),
                fileProperties.getOss().getAccessKey(),
                fileProperties.getOss().getSecretKey()
        );

        String fileName = objectName.replaceAll(fileProperties.getOss().getPath() + "/", "");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        OSSObject ossObject = ossClient.getObject(fileProperties.getOss().getBucket(), fileName);
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        byte[] buffer = new byte[1024];
        int lenght = 0;
        while ((lenght = in.read(buffer)) != -1) {
            out.write(buffer, 0, lenght);
        }

        if (out != null) {
            out.flush();
            out.close();
        }
        if (in != null) {
            in.close();
        }
        ossClient.shutdown();
    }

    /**
     * 分片上传
     *
     * @param file
     * @param session
     */
    @SneakyThrows
    public FilePojo uploadSharding(MultipartFile file, HttpSession session){
        String endpoint = fileProperties.getOss().getEndpoint();
        String accessKeyId = fileProperties.getOss().getAccessKey();
        String accessKeySecret = fileProperties.getOss().getSecretKey();
        String bucketName = fileProperties.getOss().getBucket();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        FilePojo pojo = FileUtil.buildFilePojo(file);

        List<PartETag> partTags = Collections.synchronizedList(new ArrayList<>());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(FileUtil.getcontentType(pojo.getFileName().substring(pojo.getFileName().lastIndexOf("."))));

        //获取分片上传id
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, pojo.getFileName());
        request.setObjectMetadata(metadata);
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
        String uploadId = result.getUploadId();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try {
            //计算要分割的部分
            long fileLength = file.getSize();
            int partCount = (int) (fileLength / PART_SIZE);
            if (fileLength % PART_SIZE != 0) {
                partCount++;
            }
            //分割的分数是有上限的
            if (partCount > 10000) {
                throw new RuntimeException("零件总数不能超过10000");
            } else {
                log.info("总分块数 " + partCount);
            }
            //将零件上传到您的存储桶
            log.info("---OSS文件分片上传开始-------" + pojo.getFileName());

            CountDownLatch countDownLatch = new CountDownLatch(partCount);
            for (int i = 0; i < partCount; i++) {
                long startPos = i * PART_SIZE;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : PART_SIZE;
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(pojo.getFileName());
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setPartSize(curPartSize);
                uploadPartRequest.setPartNumber(i + 1);
                executorService.execute(new PartUploader(ossClient,file,startPos,uploadPartRequest,partTags,partCount,session,countDownLatch));
            }
            //等待所有线程执行完毕
            countDownLatch.await();

            //验证所有零件是否均已完成
            if (partTags.size() != partCount) {
                throw new IllegalStateException("文件的某些部分上传失败！");
            } else {
                log.info("成功上传文件 " + pojo.getFileName());
            }
            //完成上传分段
            // 按升序排列零件编号
            partTags.sort(Comparator.comparingInt(PartETag::getPartNumber));
            log.info("Completing to upload multiparts");
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, pojo.getFileName(), uploadId, partTags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            String url = fileProperties.getOss().getPath() + CommonConstant.DIR_SPLIT + pojo.getFileName();
            pojo.setUrl(url);
            return pojo;
        } finally {
            executorService.shutdown();
            ossClient.shutdown();
        }
    }

    /**
     * 分片上传的静态内部类，多线程实现分片文件上传
     */
    private static class PartUploader implements Runnable {
        private OSS ossClient;
        private int partCount;
        private MultipartFile file;
        private long startPos;
        private UploadPartRequest uploadPartRequest;
        private List<PartETag> partTags;
        private HttpSession session;
        private CountDownLatch countDownLatch;

        public PartUploader(OSS ossClient,MultipartFile file,long startPos,UploadPartRequest uploadPartRequest,List<PartETag> partTags,int partCount,HttpSession session,CountDownLatch countDownLatch){
            this.ossClient = ossClient;
            this.file=file;
            this.startPos=startPos;
            this.uploadPartRequest = uploadPartRequest;
            this.partTags=partTags;
            this.partCount=partCount;
            this.session=session;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
                //跳过已经上传的分片
                inputStream.skip(startPos);
                uploadPartRequest.setInputStream(inputStream);
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                System.out.println("Part#" + uploadPartRequest.getPartNumber() + "done\n");
                uploadedPart++;
                int percent = uploadedPart * 100 / partCount;
                session.setAttribute("uploadPercent", percent);
                session.setAttribute("uploadSize", uploadedPart * 0.75 + "MB");
                //每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到PartETags中。
                synchronized (this.partTags) {
                    this.partTags.add(uploadPartResult.getPartETag());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new BusinessException("上传失败，服务异常");
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        throw new BusinessException("上传失败，流关闭异常");
                    }
                }
                countDownLatch.countDown();
            }
        }
    }
}
