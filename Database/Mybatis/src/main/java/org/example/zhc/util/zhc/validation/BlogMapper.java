package org.example.zhc.util.zhc.validation;

public interface BlogMapper {
//    @Select("select * from storage_1000004_1 where primary_key = #{id}")
    User selectBlog(String owner_id);
}
