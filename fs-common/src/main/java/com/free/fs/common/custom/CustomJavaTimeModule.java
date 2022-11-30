package com.free.fs.common.custom;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.free.fs.common.custom.serializer.LocalDateTimeToTimestampSerializer;
import com.free.fs.common.custom.serializer.TimestampToLocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 自定义序列化配置
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022-07-13 17:12:10
 */
public class CustomJavaTimeModule extends SimpleModule {

    /**
     * java 8 时间默认序列化
     */
    public CustomJavaTimeModule() {
        super(PackageVersion.VERSION);
        this.addSerializer(LocalDateTime.class,
                new LocalDateTimeToTimestampSerializer());
        this.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        this.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        this.addDeserializer(LocalDateTime.class,
                new TimestampToLocalDateTimeSerializer());
        this.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        this.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
    }
}
