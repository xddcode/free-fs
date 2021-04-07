package com.free.fs.common.utils;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;

/**
 * oss上传进度监听
 *
 * @author dinghao
 * @date 2021/3/23
 */
@Slf4j
public class PutObjectProgressListener implements ProgressListener {

    private HttpSession session;
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    private int percent = 0;

    public PutObjectProgressListener() {

    }

    public PutObjectProgressListener(HttpSession mSession, long size) {
        this.session = mSession;
        this.totalBytes= size;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                log.info("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                log.info(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                    //将进度存入session
                    session.setAttribute("upload_oss_percent", percent);
                    log.info(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                } else {
                    log.info(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                log.info("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                log.info("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }

    public boolean isSucceed() {
        return succeed;
    }
}

