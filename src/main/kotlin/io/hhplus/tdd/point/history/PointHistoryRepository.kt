package io.hhplus.tdd.point.history

import io.hhplus.tdd.point.user.UserPoint

interface PointHistoryRepository {

    fun save(userPoint: UserPoint, amount: Long, transactionType: TransactionType): PointHistory

    fun findAllByUserId(id: Long): List<PointHistory>

}