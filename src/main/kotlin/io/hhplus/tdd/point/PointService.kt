package io.hhplus.tdd.point

import io.hhplus.tdd.point.history.PointHistoryRepository
import io.hhplus.tdd.point.history.TransactionType
import io.hhplus.tdd.point.history.response.PointHistoryResponse
import io.hhplus.tdd.point.user.UserPoint
import io.hhplus.tdd.point.user.UserPointRepository
import io.hhplus.tdd.point.user.response.UserPointResponse
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userPointRepository: UserPointRepository,
    private val pointHistoryRepository: PointHistoryRepository
) {

    fun findPoint(id: Long): UserPointResponse {
        val userPoint = findUserPointBy(id)
        return UserPointResponse.of(userPoint)
    }

    fun findPointHistory(id: Long): List<PointHistoryResponse> {
        val pointHistories = pointHistoryRepository.findAllByUserId(id)
        return pointHistories.map { PointHistoryResponse.of(it) }
    }

    fun chargePoint(id: Long, amount: Long): UserPointResponse {
        val userPoint = findUserPointBy(id)
        if (userPoint.isExceedMaxPoint(amount)) {
            throw IllegalArgumentException("포인트가 초과 되었습니다.")
        }
        userPoint.increasePoint(amount)

        val updatedUserPoint = processPointTransaction(userPoint, amount, TransactionType.CHARGE)
        return UserPointResponse.of(updatedUserPoint)
    }

    fun usePoint(id: Long, amount: Long): UserPointResponse {
        val userPoint = findUserPointBy(id)
        if (userPoint.isPointLessThan(amount)) {
            throw IllegalArgumentException("포인트가 부족합니다.")
        }
        userPoint.deductPoint(amount)

        val updatedUserPoint = processPointTransaction(userPoint, amount, TransactionType.USE)
        return UserPointResponse.of(updatedUserPoint)
    }

    private fun findUserPointBy(id: Long) = userPointRepository.findUserPointBy(id)

    private fun processPointTransaction(
        userPoint: UserPoint,
        amount: Long,
        transactionType: TransactionType
    ): UserPoint {
        val updatedUserPoint = userPointRepository.upsert(userPoint)
        pointHistoryRepository.save(updatedUserPoint, amount, transactionType)
        return updatedUserPoint
    }

}
