package com.free.fs.common.custom.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * LocalDateTime序列化转时间戳
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022-07-13 17:11:27
 */
public class LocalDateTimeToTimestampSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        final ZoneOffset offset = OffsetDateTime.now(ZoneId.systemDefault()).getOffset();
        gen.writeNumber(value.toInstant(offset).toEpochMilli());
    }
}
