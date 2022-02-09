package com.fz.server.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * 自定义Authority 解析器
 * 也就是反序列化 在pojo中Admin并没有反序列化  在admin中使用该类进行反序列化
 * @Author: fanbo
 * @Date: 2022/01/12/11:25
 * @Description:
 */
public class CustomAuthorityDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec mapper = jsonParser.getCodec();
        JsonNode jsonNode = (JsonNode)mapper.readTree(jsonParser);
        Iterator<JsonNode> elements = jsonNode.elements();
        List<SimpleGrantedAuthority> grantedAuthorities = new LinkedList<>();
        while (elements.hasNext()){
            JsonNode next = elements.next();
            JsonNode authority = next.get("Authority");
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
        }
        return grantedAuthorities;
    }
}
