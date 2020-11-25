package com.drsg.gochat.v1.base;

import tk.mybatis.mapper.additional.dialect.oracle.OracleMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, OracleMapper<T> {
}
