package com.newegg.ec.redis.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.newegg.ec.redis.entity.RDBAnalyzeResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Kyle.K.Zhao
 * @date 1/8/2020 16:27
 */
public interface IRdbAnalyzeResult extends BaseMapper<RDBAnalyzeResult> {

    @Delete("delete from rdb_analyze_result where schedule_id IN (select min(schedule_id) from rdb_analyze_result)")
    void deleteOld();

    /**
     * query info from db by id and max schedule_id
     * @param redisInfoId queryId
     * @return RDBAnalyzeResult
     */
    @Select("select * from rdb_analyze_result where schedule_id=(select max(schedule_id) from rdb_analyze_result where redis_info_id = #{redisInfoId})")
    @Results({ @Result(id = true, column = "id", property = "id"),
            @Result(column = "schedule_id", property = "scheduleId"),
            @Result(column = "redis_info_id", property = "redisInfoId"),
            @Result(column = "result", property = "result")})
    RDBAnalyzeResult selectLatestResultByRedisInfoId(Long redisInfoId);

    /**
     * query all result by redis_info_id
     * @param redisInfoId queryId
     * @return List<RDBAnalyzeResult>
     */
    @Select("select * from rdb_analyze_result where redis_info_id= #{redisInfoId}")
    List<RDBAnalyzeResult> selectAllResultById(Long redisInfoId);

    /**
     * query all result by redis_info_id
     * @param redisInfoId queryId
     * @return List<RDBAnalyzeResult>
     */
    @Select("select * from rdb_analyze_result where schedule_id != (select max(schedule_id) from rdb_analyze_result where redis_info_id = #{redisInfoId}) and redis_info_id = #{redisInfoId}")
    @Results({ @Result(id = true, column = "id", property = "id"),
            @Result(column = "schedule_id", property = "scheduleId"),
            @Result(column = "redis_info_id", property = "redisInfoId"),
            @Result(column = "result", property = "result")})
    List<RDBAnalyzeResult> selectAllResultByIdExceptLatest(Long redisInfoId);


    @Select("create TABLE IF NOT EXISTS `rdb_analyze_result`( " +
            "id integer AUTO_INCREMENT, " +
            "schedule_id integer NOT NULL, " +
            "redis_info_id integer, " +
            "result varchar(1024), " +
            "PRIMARY KEY (id) " +
            ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;")
    void createRdbAnalyzeResult();


    @Select("select * from rdb_analyze_result where schedule_id= #{scheduleId} and redis_info_id = #{redisInfoId}")
    RDBAnalyzeResult selectByRedisIdAndSId(Long redisInfoId, Long scheduleId);
}
