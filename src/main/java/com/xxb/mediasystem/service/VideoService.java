package com.xxb.mediasystem.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxb.mediasystem.mapper.TypeMapper;
import com.xxb.mediasystem.mapper.UserMapper;
import com.xxb.mediasystem.mapper.VideoMapper;
import com.xxb.mediasystem.model.User;
import com.xxb.mediasystem.model.Video;
import com.xxb.mediasystem.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class VideoService {
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private FdfsService fdfsService;



    /**
   *@描述 上传一个视频
   *@参数 
   *@返回值
   *@创建人 dyj
   *@创建时间 2021/11/22
   **/
    public int addVideo(Video video){
       return  videoMapper.addVideo(video);
    }
    public JSONObject getPersonalVideo(Integer curPage, Integer pageSize, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = userMapper.findUserByToken(token);
        long total;
        Page page = PageHelper.startPage(curPage,pageSize);
        List<Video> videoList = videoMapper.getPersonalVideo(user.getId());
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);
        total = pageInfo.getTotal();
        JSONArray jsonArray = new JSONArray();
        for(Video video:videoList){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",video.getId());
            jsonObject1.put("title",video.getTitle());
            jsonObject1.put("description",video.getDescription());
            jsonObject1.put("picture",video.getPicture());
            jsonObject1.put("url",video.getUrl());
            jsonObject1.put("fileName",video.getFileName());
            jsonObject1.put("author",user.getName());
            jsonObject1.put("typeId",video.getType());
            jsonObject1.put("uploadTime",video.getUploadTime());
            jsonObject1.put("publish",video.getPublish());
            jsonObject1.put("type",typeMapper.getTypeById(video.getType()));
            jsonArray.add(jsonObject1);

        }

        JSONObject  jsonObject = new JSONObject();

        jsonObject.put("videoList",jsonArray);
        jsonObject.put("total",total);
        return jsonObject;
    }

    public JSONObject getAllVideo(Integer curPage,Integer pageSize){
        long total;
        Page page = PageHelper.startPage(curPage,pageSize);
        List<Video> videoList = videoMapper.getAllVideo();
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);
        total = pageInfo.getTotal();
        JSONArray jsonArray = new JSONArray();
        for(Video video:videoList){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",video.getId());
            jsonObject1.put("title",video.getTitle());
            jsonObject1.put("description",video.getDescription());
            jsonObject1.put("picture",video.getPicture());
            jsonObject1.put("url",video.getUrl());
            jsonObject1.put("fileName",video.getFileName());
            jsonObject1.put("author",userMapper.selectByPrimaryKey(video.getAuthor()));
            jsonObject1.put("typeId",video.getType());
            jsonObject1.put("uploadTime",video.getUploadTime());
            jsonObject1.put("publish",video.getPublish());
            jsonObject1.put("type",typeMapper.getTypeById(video.getType()));
            jsonArray.add(jsonObject1);

        }


        JSONObject  jsonObject = new JSONObject();

        jsonObject.put("videoList",jsonArray);
        jsonObject.put("total",total);
        return jsonObject;
    }

    public int editVideoInfo(Video video,Integer flag){
        Video video1 = videoMapper.getVideoById(video.getId());
        String oldPicture = video1.getPicture();
        if(videoMapper.editVideoInfo(video)==1){
            if(!oldPicture.equals("")&&flag==1){//如果修改图片，需要将原先的图片删除
                System.out.println("删除");
              //  fdfsService.deleteFile(oldPicture.split("8888/")[1]);
               // System.out.println(video1.getPath()+"assets/"+video.getPicture().split("assets/")[1]);
                FileUtil.delete(video1.getPath()+"assets/"+oldPicture.split("106/")[1]);
            }
            return 1;
        }else{
            return 0;
        }
    }

    public int deleteVideo(Long videoId){
        Video video = videoMapper.getVideoById(videoId);
        String oldPicture = video.getPicture();
        //删除视频
        if(FileUtil.delete(video.getPath()+video.getFileName())){
            videoMapper.deleteVideo(videoId);
            if(!oldPicture.equals("")){//删除视频将封面图片删除
                System.out.println(video.getPath()+"assets/"+video.getPicture().split("106/")[1]);
             //   fdfsService.deleteFile(oldPicture.split("8888/")[1]);
                FileUtil.delete(video.getPath()+"assets/"+oldPicture.split("106/")[1]);
            }
            return 1;
        }else{
            return 0;
        }
    }

    public int deleteVideoLoji(Long videoId){
        return videoMapper.deleteVideoLoji(videoId);
    }

    public JSONObject getVideoByTitle(String title,Integer curPage,Integer pageSize,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = userMapper.findUserByToken(token);
        long total;
        Page page = PageHelper.startPage(curPage,pageSize);
        List<Video> videoList = videoMapper.getVideoByTitle(title);
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);
        total = pageInfo.getTotal();
        JSONArray jsonArray = new JSONArray();
        for(Video video:videoList){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",video.getId());
            jsonObject1.put("title",video.getTitle());
            jsonObject1.put("description",video.getDescription());
            jsonObject1.put("picture",video.getPicture());
            jsonObject1.put("url",video.getUrl());
            jsonObject1.put("fileName",video.getFileName());
            jsonObject1.put("author",user.getName());
            jsonObject1.put("typeId",video.getType());
            jsonObject1.put("uploadTime",video.getUploadTime());
            jsonObject1.put("type",typeMapper.getTypeById(video.getType()));
            jsonObject1.put("publish",video.getPublish());
            jsonArray.add(jsonObject1);
        }
        JSONObject  jsonObject = new JSONObject();
        jsonObject.put("videoList",jsonArray);
        jsonObject.put("total",total);
        return jsonObject;
    }

    public JSONObject getVideoByType(Integer typeId,Integer curPage,Integer pageSize,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = userMapper.findUserByToken(token);
        long total;
        Page page = PageHelper.startPage(curPage,pageSize);
        List<Video> videoList;
        if(typeId==0){
            videoList = videoMapper.getAllVideo();
        }else{
            videoList = videoMapper.getVideoByType(typeId);
        }
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);
        total = pageInfo.getTotal();
        JSONArray jsonArray = new JSONArray();
        for(Video video:videoList){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",video.getId());
            jsonObject1.put("title",video.getTitle());
            jsonObject1.put("description",video.getDescription());
            jsonObject1.put("picture",video.getPicture());
            jsonObject1.put("url",video.getUrl());
            jsonObject1.put("fileName",video.getFileName());
            jsonObject1.put("author",user.getName());
            jsonObject1.put("typeId",video.getType());
            jsonObject1.put("uploadTime",video.getUploadTime());
            jsonObject1.put("publish",video.getPublish());
            jsonObject1.put("type",typeMapper.getTypeById(video.getType()));
            jsonArray.add(jsonObject1);
        }
        JSONObject  jsonObject = new JSONObject();
        jsonObject.put("videoList",jsonArray);
        jsonObject.put("total",total);
        return jsonObject;
    }

    public JSONObject getPersonalVideoByTitle(String title, Integer curPage, Integer pageSize, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        User user = userMapper.findUserByToken(token);
        long total;
        Page page = PageHelper.startPage(curPage,pageSize);
        List<Video> videoList = videoMapper.getPesonalVideoByTitle(title,user.getId());
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);
        total = pageInfo.getTotal();
        JSONArray jsonArray = new JSONArray();
        for(Video video:videoList){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",video.getId());
            jsonObject1.put("title",video.getTitle());
            jsonObject1.put("description",video.getDescription());
            jsonObject1.put("picture",video.getPicture());
            jsonObject1.put("url",video.getUrl());
            jsonObject1.put("fileName",video.getFileName());
            jsonObject1.put("author",user.getName());
            jsonObject1.put("typeId",video.getType());
            jsonObject1.put("uploadTime",video.getUploadTime());
            jsonObject1.put("publish",video.getPublish());
            jsonObject1.put("type",typeMapper.getTypeById(video.getType()));
            jsonArray.add(jsonObject1);
        }
        JSONObject  jsonObject = new JSONObject();
        jsonObject.put("videoList",jsonArray);
        jsonObject.put("total",total);
        return jsonObject;
    }

    public int setPublish(Video video){
        return videoMapper.editVideoInfo(video);
    }
    /**
    *@描述 获取发布的视频
    *@创建人 dyj
    *@创建时间 2021/12/2
    **/
    public JSONObject getPublishedVideo(Video video,Integer curPage,Integer pageSize){
        long total;
        Page page = PageHelper.startPage(curPage,pageSize);
        List<Video> videoList = videoMapper.getPublishedVideo(video);
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);
        total = pageInfo.getTotal();
        JSONArray jsonArray = new JSONArray();
        for(Video video1:videoList){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",video1.getId());
            jsonObject1.put("title",video1.getTitle());
            jsonObject1.put("description",video1.getDescription());
            jsonObject1.put("picture",video1.getPicture());
            jsonObject1.put("url",video1.getUrl());
            jsonObject1.put("fileName",video1.getFileName());
            jsonObject1.put("author",video1.getAuthor());
            jsonObject1.put("uploadTime",video1.getUploadTime());
            jsonArray.add(jsonObject1);
        }
        JSONObject  jsonObject = new JSONObject();
        jsonObject.put("videoList",jsonArray);
        jsonObject.put("total",total);
        return jsonObject;

    }

}

