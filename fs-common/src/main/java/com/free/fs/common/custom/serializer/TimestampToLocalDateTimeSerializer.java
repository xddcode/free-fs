package com.free.fs.common.custom.serializer;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * 时间戳反序列化LocalDateTime
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022-07-13 17:12:10
 */
public class TimestampToLocalDateTimeSerializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final Long timestamp = p.getValueAsLong();
        if (ObjectUtil.isNotNull(timestamp)) {
            final ZoneOffset offset = OffsetDateTime.now(ZoneId.systemDefault()).getOffset();
            return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, offset);
        }
        return null;
    }
}
