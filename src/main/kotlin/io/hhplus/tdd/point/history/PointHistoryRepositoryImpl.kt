package io.hhplus.tdd.point.history

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.point.user.UserPoint
import org.springframework.stereotype.Repository

@Repository
class PointHistoryRepositoryImpl(
    private val pointHistoryTable: PointHistoryTable
) : PointHistoryRepository {

    override fun save(userPoint: UserPoint, amount: Long, transactionType: TransactionType): PointHistory {
        return pointHistoryTable.insert(userPoint.id, amount, transactionType, userPoint.updateMillis)
    }

    override fun findAllByUserId(id: Long): List<PointHistory> {
        return pointHistoryTable.selectAllByUserId(id)
    }

}