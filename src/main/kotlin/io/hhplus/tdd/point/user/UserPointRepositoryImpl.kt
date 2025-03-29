package io.hhplus.tdd.point.user

import io.hhplus.tdd.database.UserPointTable
import org.springframework.stereotype.Repository

@Repository
class UserPointRepositoryImpl(
    private val userPointTable: UserPointTable
) : UserPointRepository {

    override fun findUserPointBy(id: Long): UserPoint {
        return userPointTable.selectById(id)
    }

    override fun upsert(userPoint: UserPoint): UserPoint {
        return userPointTable.insertOrUpdate(userPoint.id, userPoint.point)
    }
}