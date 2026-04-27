package com.han.productmanagement.common.base

import org.springframework.jdbc.core.simple.JdbcClient

abstract class BaseRepository(
    protected val jdbcClient: JdbcClient,
)
