package io.hhplus.tdd.point.history

import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.point.user.UserPoint
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PointHistoryRepositoryImplTest {

    @Autowired
    private lateinit var pointHistoryRepository: PointHistoryRepository

    @Autowired
    private lateinit var userPointTable: UserPointTable

    @DisplayName("유저의 포인트 충전/이용 내역을 저장한다.")
    @Test
    fun save() {
        //given
        val userId = 1L
        val point = 10000L
        val userPoint = saveUserPoint(userId, point)

        val chargeAmount = 1000L

        //when
        val result = pointHistoryRepository.save(userPoint, chargeAmount, TransactionType.CHARGE)

        //then
        assertThat(result)
            .extracting("userId", "type", "amount")
            .containsExactly(userPoint.id, TransactionType.CHARGE, chargeAmount)
    }

    @DisplayName("유저의 포인트 충전/이용 내역을 조회한다.")
    @Test
    fun findAllByUserId() {
        //given
        val userId = 1L
        val point = 10000L
        val userPoint = saveUserPoint(userId, point)

        val chargeAmount = 10000L
        val useAmount = 8000L
        savePointHistory(userPoint, chargeAmount, TransactionType.CHARGE)
        savePointHistory(userPoint, useAmount, TransactionType.USE)

        //when
        val result = pointHistoryRepository.findAllByUserId(userId)

        //then
        assertThat(result).hasSize(2)
            .extracting("userId", "type", "amount")
            .containsExactly(
                tuple(userId, TransactionType.CHARGE, 10000L),
                tuple(userId, TransactionType.USE, 8000L)
            )
    }

    private fun saveUserPoint(userId: Long, point: Long) = userPointTable.insertOrUpdate(userId, point)

    private fun savePointHistory(userPoint: UserPoint, amountUse: Long, transactionType: TransactionType) {
        pointHistoryRepository.save(userPoint, amountUse, transactionType)
    }

}