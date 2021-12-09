package com.xxb.mediasystem.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxb.mediasystem.mapper.PublicTagMapper;
import com.xxb.mediasystem.model.PublicTag;
import com.xxb.mediasystem.model.Type;
import com.xxb.mediasystem.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagService {
    @Resource
    private PublicTagMapper publicTagMapper;

    public JSONObject getTagList(){
        List<PublicTag> tagList = publicTagMapper.getTagList();
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(tagList));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tagList",array);
        jsonObject.put("total",array.size());
        return  jsonObject;
    }


}
