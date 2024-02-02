//package com.free.fs.core.config;
//
//import com.free.fs.core.IStorageFactory;
//import com.free.fs.core.IFileStorageProvider;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.AutoConfiguration;
//import org.springframework.context.annotation.Bean;
//
///**
// * 上文件传自动配置
// *
// * @Author: hao.ding@insentek.com
// * @Date: 2024/1/25 10:46
// */
//@Slf4j
//@AutoConfiguration
//public class UploaderAutoConfigure {
//
////    @Bean(destroyMethod = "shutdown")
////    @ConditionalOnProperty(prefix = "fs.uploader", name = "type", havingValue = "aliyunOss")
////    public OSS ossClient(UploaderProperties properties) {
////        if (StrUtil.isEmpty(properties.getAliyunOss().getAccessKey()) || StrUtil.isEmpty(properties.getAliyunOss().getSecretKey())
////                || StrUtil.isEmpty(properties.getAliyunOss().getBucket()) || StrUtil.isEmpty(properties.getAliyunOss().getEndPoint())) {
////            log.error("Aliyun oss配置缺失，请补充!");
////            throw new BusinessException("[Aliyun oss]Aliyun OSS配置缺失，请补充!");
////        }
////        OSS ossClient = new OSSClientBuilder().build(properties.getAliyunOss().getEndPoint(), properties.getAliyunOss().getAccessKey(),
////                properties.getAliyunOss().getSecretKey());
////        log.info("Aliyun oss配置成功，Bucket=" + properties.getAliyunOss().getBucket() + "，Endpoint=" + properties.getAliyunOss().getEndPoint());
////        return ossClient;
////    }
////
////    @Bean
////    @ConditionalOnProperty(prefix = "fs.uploader", name = "type", havingValue = "minio")
////    public MinioClient minioClient(UploaderProperties properties) {
////        if (StrUtil.isEmpty(properties.getMinio().getAccessKey()) || StrUtil.isEmpty(properties.getMinio().getSecretKey())
////                || StrUtil.isEmpty(properties.getMinio().getEndPoint())) {
////            log.error("Minio配置缺失，请补充!");
////            throw new BusinessException("[Minio] 配置缺失，请补充!");
////        }
////        MinioClient minioClient = MinioClient.builder()
////                .credentials(properties.getMinio().getAccessKey(), properties.getMinio().getSecretKey())
////                .endpoint(properties.getMinio().getEndPoint())
////                .build();
////        log.info("Minio配置成功，Bucket=" + properties.getMinio().getBucket() + "，Endpoint=" + properties.getMinio().getEndPoint());
////        return minioClient;
////    }
//
//    @Bean
//    public IFileStorageProvider iFileUploaderProvider() {
//        return new IStorageFactory();
//    }
//}
