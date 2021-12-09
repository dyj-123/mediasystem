package com.xxb.mediasystem.mapper;




import com.xxb.mediasystem.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectByPrimaryKey(int userid);

    int addUser(User user);

    int editUser(String newtoken,int id);
    User findUserByToken(String token);
}