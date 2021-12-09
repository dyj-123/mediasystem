package com.xxb.mediasystem.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xxb.mediasystem.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FdfsService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public String upToFdfs(MultipartFile file) throws IOException {

        InputStream in = file.getInputStream();
        String fileName = file.getOriginalFilename();
        String extName = FileUtil.getExtensionName(fileName);
        System.out.println(extName);
        StorePath storePath = fastFileStorageClient.uploadFile(in,file.getSize(),extName,null);
        //System.out.println(storePath.getFullPath());
        return storePath.getFullPath();
    }
    public void deleteFile(String avatarPath){
        StorePath storePath = StorePath.praseFromUrl(avatarPath);
        System.out.println(storePath.getGroup()+storePath.getPath());
       fastFileStorageClient.deleteFile(storePath.getGroup(),storePath.getPath());
    }
}
