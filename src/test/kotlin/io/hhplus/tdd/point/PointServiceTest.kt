package io.hhplus.tdd.point

import io.hhplus.tdd.point.history.PointHistoryRepository
import io.hhplus.tdd.point.history.TransactionType
import io.hhplus.tdd.point.user.UserPoint
import io.hhplus.tdd.point.user.UserPointRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PointServiceTest {

    @Autowired
    private lateinit var pointService: PointService

    @Autowired
    private lateinit var userPointRepository: UserPointRepository

    @Autowired
    private lateinit var pointHistoryRepository: PointHistoryRepository

    @DisplayName("유저가 가지고 있는 포인트를 조회한다.")
    @Test
    fun findPoint() {
        //given
        val userId = 1L
        val point = 10000L

        val userPoint = saveUserPoint(userId, point)

        //when
        val result = pointService.findPoint(userId)

        //then
        assertThat(result)
            .extracting("id", "point")
            .containsExactly(userPoint.id, userPoint.point)
    }

    @DisplayName("유저의 포인트 충전/이용 내역을 조회한다.")
    @Test
    fun findPointHistory() {
        //given
        val userId = 2L
        val point = 10000L
        val userPoint = saveUserPoint(userId, point)

        val chargeAmount = 10000L
        val useAmount = 8000L
        savePointHistory(userPoint, chargeAmount, TransactionType.CHARGE)
        savePointHistory(userPoint, useAmount, TransactionType.USE)

        //when
        val result = pointService.findPointHistory(userId)

        //then
        assertThat(result).hasSize(2)
            .extracting("userId", "type", "amount")
            .containsExactly(
                tuple(userId, TransactionType.CHARGE, 10000L),
                tuple(userId, TransactionType.USE, 8000L)
            )
    }

    @DisplayName("유저의 포인트를 충전한다.")
    @Test
    fun chargePoint() {
        //given
        val userId = 3L
        val point = 10000L
        val userPoint = saveUserPoint(userId, point)

        val chargeAmount = 100L

        //when
        val result = pointService.chargePoint(userId, chargeAmount)

        //then
        assertThat(result)
            .extracting("id", "point")
            .containsExactly(userPoint.id, point + chargeAmount)

        val pointHistories = pointHistoryRepository.findAllByUserId(userId)
        assertThat(pointHistories).hasSize(1)
            .extracting("userId", "type", "amount")
            .containsExactly(
                tuple(userId, TransactionType.CHARGE, chargeAmount),
            )
    }

    @DisplayName("유저의 포인트를 충전할 수 없으면 예외가 발생한다.")
    @Test
    fun chargePoint2() {
        //given
        val userId = 4L
        val point = 1000000000L
        saveUserPoint(userId, point)

        val chargeAmount = 1L

        //when //then
        assertThatThrownBy { pointService.chargePoint(userId, chargeAmount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("포인트가 초과 되었습니다.")
    }

    @DisplayName("유저의 포인트를 차감한다.")
    @Test
    fun usePoint() {
        //given
        val userId = 5L
        val point = 10000L
        val userPoint = saveUserPoint(userId, point)

        val useAmount = 100L

        //when
        val result = pointService.usePoint(userId, useAmount)

        //then
        assertThat(result)
            .extracting("id", "point")
            .containsExactly(userPoint.id, point - useAmount)

        val pointHistories = pointHistoryRepository.findAllByUserId(userId)
        assertThat(pointHistories).hasSize(1)
            .extracting("userId", "type", "amount")
            .containsExactly(
                tuple(userId, TransactionType.USE, useAmount),
            )
    }

    @DisplayName("유저의 포인트를 차감할 수 없으면 예외가 발생한다.")
    @Test
    fun usePoint2() {
        //given
        val userId = 6L
        val point = 10000L
        saveUserPoint(userId, point)

        val useAmount = 10001L

        //when //then
        assertThatThrownBy { pointService.usePoint(userId, useAmount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("포인트가 부족합니다.")
    }

    private fun saveUserPoint(userId: Long, point: Long): UserPoint {
        val userPoint = UserPoint(userId, point, System.currentTimeMillis())
        return userPointRepository.upsert(userPoint)
    }

    private fun savePointHistory(userPoint: UserPoint, amountUse: Long, transactionType: TransactionType) {
        pointHistoryRepository.save(userPoint, amountUse, transactionType)
    }

}
