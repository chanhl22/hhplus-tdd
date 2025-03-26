package io.hhplus.tdd.point.user

import io.hhplus.tdd.database.UserPointTable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserPointRepositoryImplTest {

    @Autowired
    private lateinit var userPointRepository: UserPointRepository

    @Autowired
    private lateinit var userPointTable: UserPointTable

    @DisplayName("유저가 가지고 있는 포인트를 조회한다.")
    @Test
    fun findUserPointBy() {
        //given
        val userId = 1L
        val point = 10000L
        val userPoint = saveUserPoint(userId, point)

        //when
        val result = userPointRepository.findUserPointBy(userId)

        //then
        assertThat(result)
            .extracting("id", "point")
            .containsExactly(userPoint.id, userPoint.point)
    }

    @DisplayName("유저의 포인트를 업데이트 한다.")
    @Test
    fun upsert() {
        //given
        val userId = 1L
        val point = 10000L
        val userPoint = saveUserPoint(userId, point)

        val chargeAmount = 10L
        userPoint.increasePoint(chargeAmount)

        //when
        val result = userPointRepository.upsert(userPoint)

        //then
        assertThat(result)
            .extracting("id", "point")
            .containsExactly(userPoint.id, point + chargeAmount)
    }

    private fun saveUserPoint(userId: Long, point: Long) = userPointTable.insertOrUpdate(userId, point)

}