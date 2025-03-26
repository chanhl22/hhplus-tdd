package io.hhplus.tdd.point.user

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserPointTest {

    @DisplayName("유저가 사용한 포인트 만큼 차감할 수 있다.")
    @Test
    fun deductPoint() {
        //given
        val amount = 1000L
        val userPoint = UserPoint(1, 10000L, System.currentTimeMillis())

        //when
        userPoint.deductPoint(amount)

        //then
        assertThat(userPoint.point).isEqualTo(9000L)
    }

    @DisplayName("가진 포인트보다 높은 수량으로 차감 시도하는 경우 예외가 발생한다.")
    @Test
    fun deductPoint2() {
        //given
        val amount = 10001L
        val userPoint = UserPoint(1, 10000L, System.currentTimeMillis())

        //when //then
        assertThatThrownBy { userPoint.deductPoint(amount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("차감할 포인트가 없습니다.")
    }

    @DisplayName("유저가 충전한 포인트 만큼 증가할 수 있다.")
    @Test
    fun increasePoint() {
        //given
        val amount = 1000L
        val userPoint = UserPoint(1, 10000L, System.currentTimeMillis())

        //when
        userPoint.increasePoint(amount)

        //then
        assertThat(userPoint.point).isEqualTo(11000L)
    }

    @DisplayName("포인트를 충전할 때 1,000,000,000을 초과하는 경우 예외가 발생한다.")
    @Test
    fun increasePoint2() {
        //given
        val amount = 1L
        val userPoint = UserPoint(1, 1000000000L, System.currentTimeMillis())

        //when //then
        assertThatThrownBy { userPoint.increasePoint(amount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("포인트를 더 충전할 수 없습니다.")
    }

}