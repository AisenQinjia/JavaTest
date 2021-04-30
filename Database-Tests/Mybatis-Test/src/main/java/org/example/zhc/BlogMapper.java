package org.example.zhc;

import org.apache.ibatis.annotations.Select;

public interface BlogMapper {
//    @Select("select * from storage_1000004_1 where primary_key = #{id}")
    User selectBlog(String owner_id);
}
