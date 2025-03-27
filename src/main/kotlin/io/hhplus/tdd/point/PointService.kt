package io.hhplus.tdd.point

import io.hhplus.tdd.point.history.PointHistoryRepository
import io.hhplus.tdd.point.history.TransactionType
import io.hhplus.tdd.point.history.response.PointHistoryResponse
import io.hhplus.tdd.point.user.UserPointRepository
import io.hhplus.tdd.point.user.response.UserPointResponse
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userPointRepository: UserPointRepository,
    private val pointHistoryRepository: PointHistoryRepository
) {

    fun findPoint(id: Long): UserPointResponse {
        val userPoint = userPointRepository.findUserPointBy(id)
        return UserPointResponse.of(userPoint)
    }

    fun findPointHistory(id: Long): List<PointHistoryResponse> {
        val pointHistories = pointHistoryRepository.findAllByUserId(id)
        return pointHistories.map { PointHistoryResponse.of(it) }
    }

    fun chargePoint(id: Long, amount: Long): UserPointResponse {
        val userPoint = userPointRepository.findUserPointBy(id)
        if (userPoint.isExceedMaxPoint(amount)) {
            throw IllegalArgumentException("포인트가 초과 되었습니다.")
        }
        userPoint.increasePoint(amount)

        val updatedUserPoint = userPointRepository.upsert(userPoint)
        pointHistoryRepository.save(updatedUserPoint, amount, TransactionType.CHARGE)
        return UserPointResponse.of(updatedUserPoint)
    }

    fun usePoint(id: Long, amount: Long): UserPointResponse {
        val userPoint = userPointRepository.findUserPointBy(id)
        if (userPoint.isPointLessThan(amount)) {
            throw IllegalArgumentException("포인트가 부족합니다.")
        }
        userPoint.deductPoint(amount)

        val updatedUserPoint = userPointRepository.upsert(userPoint)
        pointHistoryRepository.save(updatedUserPoint, amount, TransactionType.USE)
        return UserPointResponse.of(updatedUserPoint)
    }

}
